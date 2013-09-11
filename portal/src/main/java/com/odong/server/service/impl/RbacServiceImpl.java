package com.odong.server.service.impl;

import com.odong.server.dao.rbac.OperationDao;
import com.odong.server.dao.rbac.PermissionDao;
import com.odong.server.dao.rbac.ResourceDao;
import com.odong.server.dao.rbac.RoleDao;
import com.odong.server.entity.rbac.Operation;
import com.odong.server.entity.rbac.Permission;
import com.odong.server.entity.rbac.Resource;
import com.odong.server.entity.rbac.Role;
import com.odong.server.service.RbacService;
import com.odong.server.util.TimeHelper;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-6-5
 * Time: 上午10:34
 */
@Service("rbacService")
public class RbacServiceImpl implements RbacService {
    @Override
    public List<Long> listAdmin() {
        List<Long> list = new ArrayList<>();
        for(Role r : listRole(getOperation(OperationType.MANAGER), getResource(getSiteResourceName()))){
            list.add(role2user(r.getName()));
        }
        return list;  //
    }

    @Override
    public void bindAdmin(long user, boolean bind) {
        bindPermission(getRole(user), getOperation(OperationType.MANAGER), getResource(getSiteResourceName()), bind);
    }

    @Override
    public boolean authAdmin(long user) {
        return checkPermission(getRole(user), getOperation(OperationType.MANAGER), getResource(getSiteResourceName()));  //
    }


    private String getArticleResourceName(long article) {
        return "rbac://resource/article/" + article;  //
    }


    private String getSiteResourceName() {
        return "rbac://resource/site";
    }

    private List<Role> listRole(long operation, long resource){
        Map<String,Object> map = new HashMap<>();
        map.put("operation", operation);
        map.put("resource", resource);
        return roleDao.list("FROM Role r WHERE r.id IN (FROM Permission p WHERE p.operation=:operation AND p.resource=:resource)", map);
    }

    private Permission getPermission(long role, long operation, long resource) {
        Map<String, Object> map = new HashMap<>();
        map.put("role", role);
        map.put("operation", operation);
        map.put("resource", resource);
        return permissionDao.select("FROM Permission i WHERE i.role=:role AND i.operation=:operation AND i.resource=:resource", map);

    }

    private boolean checkPermission(long role, long operation, long resource) {
        Permission p = getPermission(role, operation, resource);
        return p != null;
    }

    private void bindPermission(long role, long operation, long resource, boolean bind) {
        Permission p = getPermission(role, operation, resource);
        if (bind) {
            if (p != null) {
                throw new IllegalArgumentException("权限[" + role + "," + operation + "," + resource + "]已存在");
            }
            p = new Permission();
            p.setRole(role);
            p.setOperation(operation);
            p.setResource(resource);
            p.setStartUp(new Date());
            p.setShutDown(timeHelper.max());
            p.setCreated(new Date());
            permissionDao.insert(p);

        } else {
            if (p == null) {
                throw new IllegalArgumentException("权限[" + role + "," + operation + "," + resource + "]不存在");
            }
            permissionDao.delete(p.getId());
        }
    }


    private long getResource(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        Resource r = resourceDao.select("FROM Resource i WHERE i.name=:name", map);
        if (r == null) {
            r = new Resource();
            r.setName(name);
            r.setCreated(new Date());
            resourceDao.insert(r);
        }
        return r.getId();
    }

    private long getOperation(OperationType type) {
        String key = "rbac://operation/" + type;
        Map<String, Object> map = new HashMap<>();
        map.put("name", key);
        Operation o = operationDao.select("FROM Operation i WHERE i.name=:name", map);
        if (o == null) {
            o = new Operation();
            o.setName(key);
            o.setCreated(new Date());
            operationDao.insert(o);
        }
        return o.getId();
    }

    private long role2user(String key){
        return Long.parseLong(key.split("/")[3]);
    }
    private long getRole(long user) {
        String key = "rbac://role/" + user;
        Map<String, Object> map = new HashMap<>();
        map.put("name", key);
        Role r = roleDao.select("FROM Role i WHERE i.name=:name", map);
        if (r == null) {
            r = new Role();
            r.setName(key);
            r.setCreated(new Date());
            roleDao.insert(r);
        }
        return r.getId();

    }


    @javax.annotation.Resource
    private TimeHelper timeHelper;

    @javax.annotation.Resource
    private RoleDao roleDao;
    @javax.annotation.Resource
    private PermissionDao permissionDao;
    @javax.annotation.Resource
    private OperationDao operationDao;
    @javax.annotation.Resource
    private ResourceDao resourceDao;

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    public void setOperationDao(OperationDao operationDao) {
        this.operationDao = operationDao;
    }

    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

}
