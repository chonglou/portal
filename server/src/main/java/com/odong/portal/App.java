package com.odong.portal;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.PropertiesConfigurationManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ResourceBundle;

public class App {
    public static void main(String[] args) {
        final App app = new App();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    app.destroy();
                    app.stop();
                } catch (Exception e) {
                    logger.error("停止服务出错", e);
                }
            }
        });
        try {
            app.init();
            app.start();
        } catch (Exception e) {
            logger.debug("启动失败", e);
        }
    }

    void init() throws Exception {
        logger.info("初始化");
        ResourceBundle bundle = ResourceBundle.getBundle("config");

        int httpPort = getInteger("httpPort", bundle);
        int httpsPort = getInteger("httpsPort", bundle);
        String store = getString("store", bundle);

        logger.info("使用端口[{},{}]，数据目录[{}]", httpPort, httpsPort, store);
        for (String s : new String[]{"apps", "log"}) {
            File f = new File(store + "/" + s);
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    throw new IOException("数据目录[" + f.getAbsolutePath() + "]不存在且创建失败");
                }
            }
        }

        //线程池设置
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(getInteger("threadPool", bundle));
        server = new Server(threadPool);
        server.setStopAtShutdown(true);

        //网络设置
        HttpConfiguration config = new HttpConfiguration();
        config.setSecureScheme("https");
        config.setSecurePort(httpsPort);
        config.setOutputBufferSize(32768);
        config.setRequestHeaderSize(8192);
        config.setResponseHeaderSize(8192);
        config.setSendServerVersion(true);
        config.setSendDateHeader(false);
        HandlerCollection handlers = new HandlerCollection();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        handlers.setHandlers(new Handler[]{contexts, new DefaultHandler()});
        server.setHandler(handlers);
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);
        server.setStopAtShutdown(true);
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(config));
        http.setPort(httpPort);
        http.setIdleTimeout(Integer.parseInt(bundle.getString("app.timeout")));
        server.addConnector(http);

        initWebApp(store);


    }

    void start() throws Exception {
        logger.info("启动");
        server.start();
        server.join();
    }

    void stop() throws Exception {
        logger.info("停止");
        server.stop();
    }

    void destroy() {
        logger.info("销毁");
    }


    private void initWebApp(String store) {
        WebAppContext app = new WebAppContext();
        app.setContextPath("/");
        //app.setWar(store + "/apps/ROOT.war");
        app.setWar("ROOT.war");
        server.setHandler(app);

    }

    private void initWebAppProvider(ContextHandlerCollection contexts, String store) {
        DeploymentManager deployer = new DeploymentManager();
        deployer.setContexts(contexts);
        deployer.setContextAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/servlet-api-[^/]*\\.jar$");
        WebAppProvider provider = new WebAppProvider();
        provider.setMonitoredDirName(store + "/apps");
        //provider.setDefaultsDescriptor("etc/web.xml");
        provider.setScanInterval(1);
        provider.setExtractWars(true);
        provider.setConfigurationManager(new PropertiesConfigurationManager());
        deployer.addAppProvider(provider);
        server.addBean(deployer);

    }

    private int getInteger(String key, ResourceBundle bundle) {
        return Integer.parseInt(getString(key, bundle));
    }

    private String getString(String key, ResourceBundle bundle) {
        return bundle.getString("app." + key);
    }

    private Server server;
    private final static Logger logger = LoggerFactory.getLogger(App.class);
}
