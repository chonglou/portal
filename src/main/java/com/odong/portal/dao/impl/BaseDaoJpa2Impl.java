package com.odong.portal.dao.impl;

import com.odong.portal.dao.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-23
 * Time: 下午9:27
 */
public class BaseDaoJpa2Impl<T extends Serializable, PK extends Serializable> implements BaseDao<T, PK> {
    @Override
    public T select(PK id) {
        return entityManager.find(clazz, id);  //
    }

    @Override
    public void insert(T t) {
        entityManager.persist(t);  //
    }

    @Override
    public void delete(PK id) {
        entityManager.createQuery("DELETE FROM " + tablename() + " AS i WHERE i." + pkName + "=:id").setParameter("id", id).executeUpdate();
    }

    @Override
    public List<T> list() {
        return entityManager.createQuery("FROM " + tablename() + " as i ORDER BY i." + pkName + " DESC", clazz).getResultList();  //
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(*) FROM " + tablename(), Long.class).getSingleResult();  //
    }

    @Override
    public void update(T t) {
        entityManager.merge(t);
    }

    protected void remove(T t) {
        entityManager.remove(t);
    }

    @SuppressWarnings("unchecked")
    public BaseDaoJpa2Impl() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Field[] fields = this.clazz.getDeclaredFields();
        String pk = null;
        for (Field f : fields) {
            if (f.isAnnotationPresent(Id.class)) {
                pk = f.getName();
            }
        }
        if (pk == null) {
            throw new IllegalArgumentException("类[" + clazz.getSimpleName() + "]没有定义主键"
            );
        }
        this.pkName = pk;

    }


    /*
    public BaseDaoJpa2Impl(Class<T> clazz, String pkName){
        this.clazz = clazz;
        this.pkName = pkName;
    }
    */


    protected String tablename() {
        return clazz.getSimpleName();
    }

    @PersistenceContext
    protected EntityManager entityManager;
    protected final Class<T> clazz;
    protected final String pkName;
    private final static Logger logger = LoggerFactory.getLogger(BaseDaoJpa2Impl.class);

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}

