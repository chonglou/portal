package com.odong.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 13-12-30上午2:21.
 */
public class IdEntity implements Serializable {
    private static final long serialVersionUID = 1510153990285593443L;
    private long id;
    private Date created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
