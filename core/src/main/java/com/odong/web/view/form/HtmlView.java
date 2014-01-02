package com.odong.web.view.form;

import com.odong.web.model.form.Form;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by flamen on 14-1-1下午4:19.
 */
public class HtmlView extends AbstractView {

    public HtmlView() {
        super();
        setContentType(CONTENT_TYPE);

    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE);
        //FIXME
        response.setHeader("Cache-Control", "no-cache");
        Form fm = (Form) map.get("form");
        PrintWriter writer = response.getWriter();
        //FIXME
        writer.write("FORM VIEW PAGE");
        writer.close();
    }

    @Override
    public void setContentType(String contentType) {
        super.setContentType(contentType);
    }

    private String url;
    private final static String CONTENT_TYPE = "text/html";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
