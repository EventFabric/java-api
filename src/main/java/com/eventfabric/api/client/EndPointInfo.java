package com.eventfabric.api.client;

public class EndPointInfo {
    public static String DEFAULT_API_HOST = "event-fabric.com";
    public static int DEFAULT_API_PORT = 80;
    public static int DEFAULT_SSL_API_PORT = 443;
    public static boolean DEFAULT_API_SECURE = true;
    public static String DEFAULT_API_ENDPOINT_EVENT = "/api/event";
    public static String DEFAULT_API_ENDPOINT_SESSION = "/api/session";

    private final String host;
    private final String path;
    private final String url;
    private final int port;
    private final boolean secure;

    public EndPointInfo() {
        this(DEFAULT_API_HOST, DEFAULT_API_ENDPOINT_EVENT, DEFAULT_SSL_API_PORT, DEFAULT_API_SECURE);
    }

    public EndPointInfo(String path) {
        this(DEFAULT_API_HOST, path, DEFAULT_SSL_API_PORT, DEFAULT_API_SECURE);
    }

    public EndPointInfo(String path, int port) {
        this(DEFAULT_API_HOST, path, port, DEFAULT_API_SECURE);
    }

    public EndPointInfo(String host, String path, int port) {
        this(host, path, port, DEFAULT_API_SECURE);
    }

    public EndPointInfo(String host, String path, int port, boolean secure) {
        this.host = host;
        this.path = path;
        this.port = port;
        this.secure = secure;
        this.url = this.buildURL();
    }

    public String toString() {
        return this.getURL();
    }

    private String buildURL() {
        String url = "";

        if (secure) {
            url = "https://";
        } else {
            url = "http://";
        }

        if (port == 80) {
            url += host + path;
        } else {
            url += host + ":" + port + path;
        }

        return url;
    }

    public String getURL() {
        return url;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public int getPort() {
        return port;
    }

    public boolean isSecure() {
        return secure;
    }
}
