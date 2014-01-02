package com.odong.web.model.form;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-1上午4:58.
 */
public abstract class Field implements Serializable {
    protected Field(String label, String type){
        this.label = label;
        this.type =type;
    }
    private static final long serialVersionUID = 2379566022203187803L;
    private String label;
    private String type;

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
}
