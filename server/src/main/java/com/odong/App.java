package com.odong;

import com.odong.server.Server;
import com.odong.server.impl.JettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        SLF4JBridgeHandler.install();

        Properties props = new Properties();
        try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "/etc/config.properties"))) {
            props.load(is);
        } catch (IOException e) {
            logger.error("server ./bin/server");
            return;
        }

        Boolean debug = Boolean.parseBoolean(props.getProperty("debug", "false"));
        int port = Integer.parseInt(props.getProperty("port", "8080"));
        String mode = props.getProperty("mode", "jetty");
        String webapps = props.getProperty("webapps", "ROOT");
        logger.info("DEBUG={} PORT={} MODE={} ", debug, port, mode);
        Server server = getServer(mode, port, webapps);
        if (server != null) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logger.info("停止服务");
                    server.stop();
                } catch (Exception e) {
                    logger.error("出错", e);
                }
            }));
            logger.info("启动服务");
            try {
                server.start();
            } catch (Exception e) {
                logger.error("服务出错", e);
            }
        }
    }

    private static Server getServer(String mode, int port, String webapps) {

        switch (mode) {
            case "jetty":
                return new JettyServer(port, webapps);
            default:
                logger.error("不支持的模式{}", mode);
                break;
        }
        return null;
    }

    private final static Logger logger = LoggerFactory.getLogger(App.class);
}
