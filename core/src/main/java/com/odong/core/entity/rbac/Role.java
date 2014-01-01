package com.odong.core.entity.rbac;

import com.odong.core.entity.IdEntity;

/**
 * Created by flamen on 13-12-31下午6:10.
 */
public class Role extends IdEntity {
    private static final long serialVersionUID = -7297215650474893319L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
