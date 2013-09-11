package com.odong.portal.config;

import org.hibernate.dialect.DerbyTenSevenDialect;

import java.sql.Types;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-10
 * Time: 下午11:46
 */
public class DerbyDialect extends DerbyTenSevenDialect {
    public DerbyDialect() {
        super();
        registerColumnType(Types.CLOB, "clob");
    }
}
