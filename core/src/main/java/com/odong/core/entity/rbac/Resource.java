package com.odong.core.entity.rbac;

import com.odong.core.entity.IdEntity;

/**
 * Created by flamen on 13-12-31下午6:10.
 */
public class Resource extends IdEntity {
    private static final long serialVersionUID = -4215180786691502389L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
