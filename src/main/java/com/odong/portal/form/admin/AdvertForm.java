package com.odong.portal.form.admin;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-8-14
 * Time: 下午4:01
 */
public class AdvertForm implements Serializable {
    private static final long serialVersionUID = 6214354911516285796L;
    @NotNull
    private String id;
    private String script;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
