package com.odong.portal.web.form;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:47
 */
public final class Button implements Serializable {
    public Button(String id, String label) {
        this.id = id;
        this.label = label;
    }

    private static final long serialVersionUID = 5406159836300711112L;
    private String id;
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
