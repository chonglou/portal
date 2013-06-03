package com.odong.portal.web.form;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:38
 */
public final class TextField<T> extends Field<T> {
    private static final long serialVersionUID = 5436830171467555708L;

    public TextField(String id, String label, T value) {
        this(id, label, value,  null);
    }

    public TextField(String id, String label, T value, String tooltip) {
        super(id, label, "text", value, true, tooltip);
    }

}
