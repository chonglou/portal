package com.odong.daemon;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by flamen on 13-12-30上午3:09.
 */
public class App {
    public static void main(String[] args) throws Exception{
        Server server = new Server(8080);
        server.setHandler(new Handler());

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try{
                logger.info("停止服务");
                server.stop();
            }
            catch (Exception e){
                logger.error("出错", e);
            }
        }));
        logger.info("启动服务");
        server.start();
        server.join();
    }
    private final static Logger logger = LoggerFactory.getLogger(App.class);
}
