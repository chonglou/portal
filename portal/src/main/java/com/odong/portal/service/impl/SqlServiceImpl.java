package com.odong.portal.service.impl;

import com.odong.portal.service.SqlService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-10
 * Time: 下午10:51
 */
@Service("sqlService")
public class SqlServiceImpl implements SqlService {
    private JdbcTemplate jdbcTemplate;

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
