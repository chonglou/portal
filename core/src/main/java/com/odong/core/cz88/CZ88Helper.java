package com.odong.core.cz88;

import com.odong.core.cz88.CZ88;

/**
 * Created by flamen on 14-1-3下午1:38.
 */

public interface CZ88Helper {
    public int count();
    public void load(String file);
    public CZ88 search(String ip);
    public void clear();
}
