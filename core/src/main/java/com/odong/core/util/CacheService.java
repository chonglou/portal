package com.odong.core.util;

import com.odong.core.model.SmtpProfile;
import com.odong.web.model.Page;

/**
 * Created by flamen on 13-12-31下午2:33.
 */
public interface CacheService {
    Page getPage();

    void popPage();

    SmtpProfile getSmtp();

    void popSmtp();

}
