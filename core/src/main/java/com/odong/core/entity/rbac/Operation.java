package com.odong.core.entity.rbac;

import com.odong.core.entity.IdEntity;

/**
 * Created by flamen on 13-12-31下午6:12.
 */
public class Operation extends IdEntity {
    private static final long serialVersionUID = 8844166055882930930L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
