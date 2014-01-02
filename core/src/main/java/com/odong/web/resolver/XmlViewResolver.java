package com.odong.web.resolver;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * Created by flamen on 14-1-1下午6:37.
 */
public class XmlViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return view;
    }
    private View view;

    public void setView(View view) {
        this.view = view;
    }
}
