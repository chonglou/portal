package com.odong.portal.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:30
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article {
}
