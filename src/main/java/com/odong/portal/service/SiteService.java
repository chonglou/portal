package com.odong.portal.service;

import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:28
 */
@TransactionConfiguration( defaultRollback = true )
public interface SiteService {

    @Transactional( propagation = Propagation.REQUIRED )
    void set(String key, Object value);

    @Transactional( propagation = Propagation.SUPPORTS,readOnly = true )
    <T> T get(String key, Class<T> clazz);
}
