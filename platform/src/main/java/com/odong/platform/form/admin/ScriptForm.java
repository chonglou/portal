package com.odong.platform.form.admin;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午9:07.
 */
public class ScriptForm implements Serializable {
    private static final long serialVersionUID = -6756757202422754362L;
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
