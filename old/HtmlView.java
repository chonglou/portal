package com.odong.web.view;

import com.odong.web.model.Page;
import com.odong.web.template.TemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by flamen on 14-1-1下午4:19.
 */
public class HtmlView extends AbstractUrlBasedView {

    public HtmlView(String url, TemplateHelper templateHelper) {
        super(url);
        setContentType(CONTENT_TYPE);
        this.templateHelper = templateHelper;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE);
        logger.debug("VIEW URL:{}", getUrl());
        templateHelper.render(getUrl(), map, response.getOutputStream());
    }

    @Override
    public void setContentType(String contentType) {
        super.setContentType(contentType);
    }

    private TemplateHelper templateHelper;
    private final static String CONTENT_TYPE = "text/html";
    private final static Logger logger = LoggerFactory.getLogger(HtmlView.class);

}
