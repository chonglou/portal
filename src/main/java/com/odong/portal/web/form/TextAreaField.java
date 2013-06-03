package com.odong.portal.web.form;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:40
 */
public final class TextAreaField extends Field<String> {
    private static final long serialVersionUID = 2811328119954932042L;

    public TextAreaField(String id, String label,  String value) {
        this(id, label, value,  null);
    }
    public TextAreaField(String id, String label, String value, String tooltip) {
        super(id, label, "textarea", value, false, tooltip);
        this.rows = 5;
        this.cols=80;
    }

    private boolean html;
    private int rows;
    private int cols;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }
}
