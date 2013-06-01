package com.odong.portal.web.form;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:42
 */
public class PasswordField extends Field<String> {
    private static final long serialVersionUID = 4601649521987736108L;

    public PasswordField(String id, String label, String value) {
        this(id, label,  value,  null);
    }
    public PasswordField(String id, String label, String value, String tooltip) {
        super(id, label, "password", value, true, tooltip);
    }
}
