package com.odong.web.form;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:29
 */
public abstract class AField<T> extends Field {
    protected AField(String id, String label, String type, T value, boolean required, String tooltip) {
        super(label, type);
        this.id = id;
        this.value = value;
        this.tooltip = tooltip;
        this.required = required;

    }

    private static final long serialVersionUID = 5161896645906420869L;
    private String id;
    private boolean readonly;
    private boolean required;
    private T value;
    private String tooltip;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
