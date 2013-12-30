package com.odong.daemon;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by flamen on 13-12-30上午3:09.
 */
public class App {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "/etc/config.properties"))) {
            props.load(is);
        } catch (IOException e) {
            logger.error("请到daemon目录下运行 ./bin/daemon");
            return;
        }

        Boolean debug = Boolean.parseBoolean(props.getProperty("debug", "false"));
        int port = Integer.parseInt(props.getProperty("port", "8080"));

        logger.info("DEBUG={} PORT={}", debug, port);

        Server server = new Server(port);
        server.setHandler(new Handler(debug, props.getProperty("key")));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("停止服务");
                server.stop();
            } catch (Exception e) {
                logger.error("出错", e);
            }
        }));
        logger.info("启动服务");
        server.start();
        server.join();
    }


    private final static Logger logger = LoggerFactory.getLogger(App.class);
}
