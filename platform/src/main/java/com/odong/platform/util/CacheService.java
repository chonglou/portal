package com.odong.platform.util;

import com.odong.core.model.GoogleAuthProfile;
import com.odong.core.model.QqAuthProfile;

/**
 * Created by flamen on 13-12-31下午10:46.
 */
public interface CacheService {
    GoogleAuthProfile getGoogleAuthProfile();
    void popGoogleAuthProfile();
    QqAuthProfile getQqAuthProfile();
    void popQqAuthProfile();
    String getGoogleValidCode();

    void popGoogleValidCode();
}

