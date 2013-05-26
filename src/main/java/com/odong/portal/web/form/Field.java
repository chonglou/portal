package com.odong.portal.web.form;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:29
 */
public abstract class Field<T> implements Serializable {
    protected Field(String id, String label, String type, T value) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.value = value;

    }

    private static final long serialVersionUID = 5161896645906420869L;
    private String id;
    private String type;
    private String label;
    private T value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
