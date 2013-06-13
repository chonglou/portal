package com.odong.portal.service;

import com.odong.portal.entity.rbac.Resource;
import com.odong.portal.entity.rbac.Role;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-5
 * Time: 上午10:34
 */
public interface RbacService {

    List<Role> listRole(long user);
    List<Resource> listResource(long role);

    void setAdmin(long user);
    boolean authAdmin(long user);

    String getArticleResourceName(long article);
    String getSiteResourceName();
    public enum OperationType{
        MANAGER
    }
}
