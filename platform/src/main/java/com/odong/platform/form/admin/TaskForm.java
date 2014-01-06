package com.odong.platform.form.admin;

import java.io.Serializable;

/**
 * Created by flamen on 14-1-5下午10:47.
 */
public class TaskForm implements Serializable {
    private static final long serialVersionUID = -5842992364254634119L;
     private int gc;
    private int rss;
    private int backup;
    private int sitemap;

    public int getGc() {
        return gc;
    }

    public void setGc(int gc) {
        this.gc = gc;
    }

    public int getRss() {
        return rss;
    }

    public void setRss(int rss) {
        this.rss = rss;
    }

    public int getBackup() {
        return backup;
    }

    public void setBackup(int backup) {
        this.backup = backup;
    }

    public int getSitemap() {
        return sitemap;
    }

    public void setSitemap(int sitemap) {
        this.sitemap = sitemap;
    }
}
