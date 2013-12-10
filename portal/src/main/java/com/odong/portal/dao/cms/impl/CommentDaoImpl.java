package com.odong.portal.dao.cms.impl;

import com.odong.portal.dao.cms.CommentDao;
import com.odong.portal.dao.impl.BaseJpa2DaoImpl;
import com.odong.portal.entity.cms.Comment;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-24
 * Time: 下午11:34
 */
@Repository("cms.commentDao")
public class CommentDaoImpl extends BaseJpa2DaoImpl<Comment, Long> implements CommentDao {
}
