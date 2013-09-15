package com.odong.portal.form.admin;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-9-14
 * Time: 下午5:41
 */
public class TaskForm implements Serializable {
    private static final long serialVersionUID = -6246884512272568065L;
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
