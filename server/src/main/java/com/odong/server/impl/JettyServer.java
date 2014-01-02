package com.odong.server.impl;

import com.odong.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by flamen on 14-1-2下午12:33.
 */
public class JettyServer extends Server {

    public JettyServer(int port, String webapps) {
        super(port, webapps);
    }

    @Override
    public void start() throws Exception{
        server = new org.eclipse.jetty.server.Server(port);
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setWar(war);
        server.setHandler(context);

        server.start();
        server.join();
    }

    @Override
    public void stop() throws Exception{
        server.stop();
    }

    private org.eclipse.jetty.server.Server server;
}
