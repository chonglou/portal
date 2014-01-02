package com.odong.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by flamen on 14-1-2下午12:33.
 */
public abstract class Server {
    public abstract void start() throws Exception;
    public abstract void stop()throws Exception;

    protected String getBaseUrl(String app){
        URL url = this.getClass().getClassLoader().getResource(app);
        if(url == null){
            throw new RuntimeException("查找WEB目录失败:"+app);
        }
        return url.toExternalForm();
    }
    protected Server(int port, String app) {
        this.port = port;
        this.war = getBaseUrl(app);
    }
    protected final int port;
    protected final String war;
    private final static Logger logger = LoggerFactory.getLogger(Server.class);
}
