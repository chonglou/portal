package com.odong.portal.dao.impl;

import com.odong.portal.dao.BaseDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-22
 * Time: 下午2:31
 */

public class BaseHibernate4DaoImpl<V extends Serializable, K extends Serializable> implements BaseDao<V,K> {
    @Override
    @SuppressWarnings("unchecked")
    public V select(K k) {
        return (V)getSession().get(clazz, k);
        //String hql = " FROM "+clazz.getSimpleName()+" AS i WHERE i."+pkName+"=:id";
        //logger.debug(hql);
        //return (V)getSession().createQuery(hql).setParameter("id", k).uniqueResult();
    }

    @Override
    @SuppressWarnings("unckecked")
    public K insert(V v) {
        return (K)getSession().save(v);
    }

    @Override
    public void delete(K k) {
        getSession().createQuery("DELETE FROM "+clazz.getName()+" WHERE "+pkName + "=:id").setParameter("id",k).executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<V> list() {
        return getSession().createQuery("FROM "+clazz.getName()+" ORDER BY "+pkName+" DESC ").list();  //
    }

    @Override
    public int count() {
        return (int)getSession().createQuery("SELECT COUNT(*) FROM " + clazz.getName()).uniqueResult();  //
    }

    @SuppressWarnings("unchecked")
    public BaseHibernate4DaoImpl(){
        this.clazz = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Field[] fields = this.clazz.getDeclaredFields();
        String pk = null;
        for(Field f : fields) {
            if(f.isAnnotationPresent(Id.class)) {
                pk = f.getName();
            }
        }
        if (pk == null){
            throw new IllegalArgumentException("类["+clazz.getSimpleName()+"]没有定义主键");
        }
        this.pkName = pk;

    }

    @Override
    public void update(V v){
        getSession().update(v);
    }
    @Override
    public void saveOrUpdate(V v){
        getSession().saveOrUpdate(v);
    }

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }
    @Resource
    private SessionFactory sessionFactory;
    protected final Class<V> clazz;
    protected final String pkName;
    private final static Logger logger = LoggerFactory.getLogger(BaseHibernate4DaoImpl.class);


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
