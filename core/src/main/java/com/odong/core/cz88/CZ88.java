package com.odong.core.cz88;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-3下午1:36.
 */
public class CZ88 implements Serializable {
    private static final long serialVersionUID = 41122664325886317L;
    private long id;
    private String ipStart;
    private String ipEnd;
    private String ext1;
    private String ext2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIpStart() {
        return ipStart;
    }

    public void setIpStart(String ipStart) {
        this.ipStart = ipStart;
    }

    public String getIpEnd() {
        return ipEnd;
    }

    public void setIpEnd(String ipEnd) {
        this.ipEnd = ipEnd;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }
}
