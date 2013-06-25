package com.odong.portal.util;

import java.util.Comparator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-25
 * Time: 上午12:59
 */
public class MapLongValueComparator<K> implements Comparator<K> {

    public MapLongValueComparator(Map<K, Long> base) {
        this.base = base;
    }

    private Map<K, Long> base;

    @Override
    public int compare(K o1, K o2) {

        return base.get(o1) >= base.get(o2) ? 1 : -1;  //
    }
}
