package com.sunquan.chimingfazhou.download.module;

/**
 * 作    者: sunquan
 * 文件描述:代理信息封装类
 */
public class ProxyInfo {
    /**
     * 代理host
     */
    private String proxyHost;
    /**
     * 代理端口
     */
    private int proxyPort = -1;

    /**
     * 获取代理host
     *
     * @return 代理host
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * 设置代理host
     *
     * @param proxyHost 代理host
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * 获取代理端口号
     *
     * @return 代理端口号
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * 设置代理端口号
     *
     * @param proxyPort 代理端口号
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

}
