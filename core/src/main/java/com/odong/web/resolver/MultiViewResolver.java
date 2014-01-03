package com.odong.web.resolver;

import com.odong.web.template.TemplateHelper;
import com.odong.web.view.HtmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import java.util.Locale;

/**
 * Created by flamen on 14-1-1下午4:26.
 */
public class MultiViewResolver extends AbstractCachingViewResolver implements Ordered {

    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        //logger.debug("VIEW NAME:{}", viewName);
        String ext = StringUtils.getFilenameExtension(viewName);
        if (ext == null) {
            return null;
        }
        switch (ext) {
            case "httl":
                return new HtmlView(viewName, templateHelper);
            case "json":
                return jsonView;
            case "xml":
                return xmlView;
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }


    private View jsonView;
    private View xmlView;
    private TemplateHelper templateHelper;
    private final static Logger logger = LoggerFactory.getLogger(MultiViewResolver.class);

    public void setTemplateHelper(TemplateHelper templateHelper) {
        this.templateHelper = templateHelper;
    }

    public void setJsonView(View jsonView) {
        this.jsonView = jsonView;
    }

    public void setXmlView(View xmlView) {
        this.xmlView = xmlView;
    }
}
