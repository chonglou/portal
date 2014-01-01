package com.odong.core.service.impl;

import com.odong.core.entity.rbac.Operation;
import com.odong.core.entity.rbac.Permission;
import com.odong.core.entity.rbac.Role;
import com.odong.core.service.RbacService;
import com.odong.core.store.JdbcHelper;
import com.odong.core.util.TimeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Date;

/**
 * Created by flamen on 13-12-31下午6:13.
 */
@Service("core.rbacService")
public class RbacServiceImpl extends JdbcHelper implements RbacService {

    @Override
    public void bind(String role, String operation, String resource, Date startup, Date shutdown, boolean bind) {
        long roleId = getRole(role);
        long operationId = getOperation(operation);
        long resourceId = getResource(resource);
        if (bind) {
            Permission p = getPermission(roleId, operationId, resourceId);
            if (p == null) {
                execute("INSERT INTO permissions(role_, operation_, resource_, startup_, shutdown_, created_) VALUES(?,?,?,?,?,?)",
                        roleId, operationId, resourceId, startup, shutdown, new Date());
            } else {
                execute("UPDATE permissions SET startup_=?, shutdown_=?,version=version+1 WHERE id=?",
                        startup, shutdown, p.getId());
            }
        } else {
            execute("DELETE FROM permissions WHERE role_=? AND operation_=? AND resource_=?",
                    roleId, operationId, resourceId);
        }

    }

    @Override
    public boolean auth(String role, String operation, String resource) {
        Permission p = getPermission(getRole(role), getOperation(operation), getResource(resource));
        Date now = new Date();
        return p != null && now.compareTo(p.getShutDown()) < 0 && now.compareTo(p.getStartup()) > 0;
    }

    private Permission getPermission(long role, long operation, long resource) {
        return select("SELECT * FROM permissions WHERE role_=? AND operation_=? AND resource_=?", new Object[]{role, operation, resource}, mapperPermission());
    }

    private long getRole(String name) {
        Long id = select("SELECT id FROM roles WHERE name_=?", new Object[]{name}, Long.class);
        if (id == null) {
            return insert("INSERT INTO roles(name_,created_) VALUES(?,?)", new Object[]{name, new Date()}, "id", Long.class);
        }
        return id;
    }

    private long getOperation(String name) {
        Long id = select("SELECT id FROM operations WHERE name_=?", new Object[]{name}, Long.class);
        if (id == null) {
            return insert("INSERT INTO operations(name_,created_) VALUES(?,?)", new Object[]{name, new Date()}, "id", Long.class);
        }
        return id;
    }

    private long getResource(String name) {
        Long id = select("SELECT id FROM resources WHERE name_=?", new Object[]{name}, Long.class);
        if (id == null) {
            return insert("INSERT INTO resources(name_,created_) VALUES(?,?)", new Object[]{name, new Date()}, "id", Long.class);
        }
        return id;
    }

    @PostConstruct
    void init() {
        install("roles",
                longIdColumn(),
                stringColumn("name_", 255, true, true),
                dateColumn("created_", true));
        install("operations",
                longIdColumn(),
                stringColumn("name_", 255, true, true),
                dateColumn("created_", true));
        install("resources",
                longIdColumn(),
                stringColumn("name_", 255, true, true),
                dateColumn("created_", true));
        install("permissions",
                longIdColumn(),
                longColumn("role_", true),
                longColumn("resource_", true),
                longColumn("operation_", true),
                dateColumn("startup_", true),
                dateColumn("shutdown_", true),
                dateColumn("created_", true),
                versionColumn()
        );
    }

    private RowMapper<Role> mapperRole() {
        return (ResultSet rs, int i) -> {
            Role r = new Role();
            r.setId(rs.getLong("id"));
            r.setName(rs.getString("name_"));
            r.setCreated(rs.getTimestamp("created_"));
            return r;
        };
    }

    private RowMapper<Operation> mapperOperation() {
        return (ResultSet rs, int i) -> {
            Operation o = new Operation();
            o.setId(rs.getLong("id"));
            o.setName(rs.getString("name_"));
            o.setCreated(rs.getTimestamp("created_"));
            return o;
        };
    }

    private RowMapper<com.odong.core.entity.rbac.Resource> mapperResource() {
        return (ResultSet rs, int i) -> {
            com.odong.core.entity.rbac.Resource r = new com.odong.core.entity.rbac.Resource();
            r.setId(rs.getLong("id"));
            r.setName(rs.getString("name_"));
            r.setCreated(rs.getTimestamp("created_"));
            return r;
        };
    }

    private RowMapper<Permission> mapperPermission() {
        return (ResultSet rs, int i) -> {
            Permission p = new Permission();
            p.setId(rs.getLong("id"));
            p.setRole(rs.getLong("role_"));
            p.setResource(rs.getLong("resource_"));
            p.setOperation(rs.getLong("operation_"));
            p.setShutDown(rs.getTimestamp("shutdown_"));
            p.setStartup(rs.getTimestamp("startup_"));
            p.setCreated(rs.getTimestamp("created_"));
            p.setVersion(rs.getInt("version"));
            return p;
        };
    }

    @Resource
    private TimeHelper timeHelper;

    @Value("${jdbc.driver}")
    public void setDriver(String driver) {
        jdbcDriver = driver;
    }

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

}
