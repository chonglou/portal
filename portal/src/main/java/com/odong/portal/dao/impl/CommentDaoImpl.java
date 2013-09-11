package com.odong.portal.dao.impl;

import com.odong.portal.dao.CommentDao;
import com.odong.portal.entity.Comment;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:34
 */
@Repository("commentDao")
public class CommentDaoImpl extends BaseJpa2DaoImpl<Comment, Long> implements CommentDao {
}
