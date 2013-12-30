package com.odong.portal.util;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by flamen on 13-12-30上午1:04.
 */
@Component
public class FtpServer {
    @PostConstruct
    void init() throws FtpException{
        if(enable){
            FtpServerFactory serverFactory = new FtpServerFactory();
            ListenerFactory listenerFactory = new ListenerFactory();
            listenerFactory.setPort(port);
            serverFactory.addListener("default", listenerFactory.createListener());
            org.apache.ftpserver.FtpServer server = serverFactory.createServer();
            server.start();
        }
    }
    @PreDestroy
    void destroy(){
    }
    @Value("${ftpd.enable}")
    private boolean enable;
    @Value("${ftpd.port}")
    private int port;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
