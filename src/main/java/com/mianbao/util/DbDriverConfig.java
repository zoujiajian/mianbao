package com.mianbao.util;

/**
 * Created by zoujiajian on 2017-3-14.
 */
public class DbDriverConfig {

    /**
     * 初始化连接池大小
     */
    private int initialSize;

    /**
     * 最小活跃连接数
     */
    private int minIdle;

    /**
     * 最大活跃连接数
     */
    private int maxActive;

    private String url;

    private String username;

    private String password;

    private String driver;

    public int getInitialSize() {
        return initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}
