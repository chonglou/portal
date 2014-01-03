package com.odong.platform.aop;

import com.odong.web.model.ResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 13-12-31下午10:36.
 */
public class ExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        logger.error("servlet异常", ex);
        ResponseItem ri = new ResponseItem(ResponseItem.Type.message);
        ri.addData(ex.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("message", ri);
        return new ModelAndView("/core/message.httl", map);
    }

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
}
