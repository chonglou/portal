package com.odong.web.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import java.util.Locale;
import java.util.Map;

/**
 * Created by flamen on 14-1-1下午4:26.
 */
public class MultiViewResolver extends AbstractCachingViewResolver implements Ordered {

    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        //logger.debug("VIEW NAME:{}", viewName);
        String ext = StringUtils.getFilenameExtension(viewName);
        return ext == null ? null : resolvers.get(ext);
    }
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    private Map<String,View> resolvers;
    private final static Logger logger = LoggerFactory.getLogger(MultiViewResolver.class);


    public void setResolvers(Map<String, View> resolvers) {
        this.resolvers = resolvers;
    }
}
