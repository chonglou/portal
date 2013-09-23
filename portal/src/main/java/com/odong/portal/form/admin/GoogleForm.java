package com.odong.portal.form.admin;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-23
 * Time: 上午11:11
 */
public class GoogleForm implements Serializable {
    private static final long serialVersionUID = -1962194717189752239L;
    @NotNull
    private String valid;

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
