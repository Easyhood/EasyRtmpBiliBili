package com.easyhood.easyrtmpbilibili;

import android.media.projection.MediaProjection;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 功能：直播管理类
 * 详细描述：
 * 作者：guan_qi
 * 创建日期：2023-03-25
 */
public class ScreenLive extends Thread{

    private static final String TAG = "ScreenLive";
    private String url;
    private MediaProjection mediaProjection;

    static {
        System.loadLibrary("native-lib");
    }

    // 队列
    private LinkedBlockingQueue<RTMPPackage> queue = new LinkedBlockingQueue<>();

    // 正在执行 isLive 关闭
    private boolean isLiving;

    /**
     * 生产者入口
     * @param rtmpPackage RTMPPackage
     */
    public void addPackage(RTMPPackage rtmpPackage) {
        if (!isLiving) {
            return;
        }
        queue.add(rtmpPackage);
    }

    /**
     * 开启推送模式
     * @param url 链接
     * @param mediaProjection MediaProjection
     */
    public void startLive(String url, MediaProjection mediaProjection) {
        this.url = url;
        this.mediaProjection = mediaProjection;
        start();
    }

    @Override
    public void run() {
        //1推送到
        if (!connect(url)) {
            Log.i(TAG, "run: ----------->推送失败");
            return;
        }
//        开启线程
//
        VideoCodec videoCodec = new VideoCodec(this);
        videoCodec.startLive(mediaProjection);
        isLiving = true;
        while (isLiving) {
            RTMPPackage rtmpPackage = null;
            try {
                rtmpPackage = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (rtmpPackage.getBuffer() != null && rtmpPackage.getBuffer().length != 0) {
                Log.i(TAG, "run: ----------->推送 "+ rtmpPackage.getBuffer().length);

                sendData(rtmpPackage.getBuffer(), rtmpPackage.getBuffer()
                        .length , rtmpPackage.getTms());
            }
        }
    }

    /**
     * 发送数据
     * @param data 数据
     * @param len 长度
     * @param tms 时间戳
     * @return boolean
     */

    private native boolean sendData(byte[] data, int len, long tms);


    /**
     * 保持连接
     * @param url URL
     * @return boolean
     */
    private native boolean connect(String url);
}
