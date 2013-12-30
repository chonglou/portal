package com.odong.daemon;

import com.alibaba.fastjson.JSON;
import com.odong.daemon.util.CommandHelper;
import com.odong.daemon.util.EncryptHelper;
import com.odong.daemon.util.FileHelper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by flamen on 13-12-30上午3:11.
 */
public class Handler extends AbstractHandler {
    public Handler(boolean debug, String key) {
        super();
        this.debug = debug;
        this.encryptHelper = new EncryptHelper(key);
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        logger.debug("REQUEST: {} {} {}", method, uri);

        baseRequest.setHandled(true);

        Map<String, Object> map = new HashMap<>();
        switch (request.getMethod()) {
            case "GET":
                switch (uri) {
                    case "/command":
                        command(request, map);
                        break;
                    case "/file":
                        file(request, map);
                        break;
                    case "/status":
                        status(request, map);
                        break;
                }
                break;
        }
        if (map.isEmpty()) {
            error(response, HttpServletResponse.SC_NOT_FOUND);
        } else {
            json(response, map);
        }

    }

    private void finish(boolean ok, Map<String,Object>map){
        map.put("ok", ok);
        map.put("created", new Date());
    }
    private void command(HttpServletRequest request, Map<String, Object> map) {
        List<String> lines = new ArrayList<>();
        for(String line : request.getParameterValues("line")){
            lines.add(encryptHelper.decode(line));
        }

        if(debug){
            logger.debug("收到命令{}", lines);
        }
        else {
            map.put("data", CommandHelper.execute(lines.toArray(new String[1])));
        }
        finish(true, map);
    }
    private void file(HttpServletRequest request, Map<String, Object> map) {
        String name = encryptHelper.decode(request.getParameter("name"));

        if(debug){
            name = "/tmp"+name;
        }
        List<String> lines = new ArrayList<>();
        for(String line : request.getParameterValues("line")){
            lines.add(encryptHelper.decode(line));
        }
        logger.debug("写入文件{}", name);
        FileHelper.write(name, lines.toArray(new String[1]));
        finish(true, map);

    }
    private void status(HttpServletRequest request, Map<String, Object> map) {
        map.put("date", new Date());
    }

    private void html(HttpServletResponse response, String html) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(html);
    }

    private void json(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(JSON.toJSONString(object));
    }

    private void error(HttpServletResponse response, int id) throws IOException {
        response.sendError(id);
    }

    private final static Logger logger = LoggerFactory.getLogger(Handler.class);
    private final boolean debug;
    private final EncryptHelper encryptHelper;

}
