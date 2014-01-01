package com.odong.cms.service.impl;

import com.odong.cms.service.ContentService;
import com.odong.core.store.JdbcHelper;
import org.springframework.stereotype.Service;

/**
 * Created by flamen on 13-12-31下午6:08.
 */
@Service("cms.contentService")
public class ContentServiceImpl extends JdbcHelper implements ContentService {
}
