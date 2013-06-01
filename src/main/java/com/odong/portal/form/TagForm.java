package com.odong.portal.form;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-1
 * Time: 上午1:10
 */
public class TagForm implements Serializable {
    private static final long serialVersionUID = -4882036809006597626L;
    private Long id;
    @NotEmpty
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
