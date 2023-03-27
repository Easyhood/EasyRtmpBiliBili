package com.easyhood.easyrtmpbilibili;

/**
 * 功能：RTMP所需数据包
 * 详细描述：
 * 作者：guan_qi
 * 创建日期：2023-03-27
 */
public class RTMPPackage {

    // 帧数据
    private byte[] buffer;
    // 时间戳
    private long tms;

    /**
     * 构造方法
     * @param buffer 帧数据
     * @param tms 时间戳
     */
    public RTMPPackage(byte[] buffer, long tms) {
        this.buffer = buffer;
        this.tms = tms;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public long getTms() {
        return tms;
    }

    public void setTms(long tms) {
        this.tms = tms;
    }
}
