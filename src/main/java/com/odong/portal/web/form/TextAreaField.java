package com.odong.portal.web.form;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:40
 */
public class TextAreaField extends Field<String> {
    private static final long serialVersionUID = 2811328119954932042L;

    public TextAreaField(String id, String label, String value) {
        super(id, label, "textarea", value);
    }

    private boolean html;

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }
}
