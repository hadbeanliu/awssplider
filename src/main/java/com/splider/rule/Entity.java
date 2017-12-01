package com.splider.rule;

public class Entity {

    private String key;
    private String query;
    private boolean downloadable=false;

    public Entity(String key, String query) {
        this.key = key;
        this.query = query;
    }

    public Entity(String key, String query, boolean downloadable) {
        this.key = key;
        this.query = query;
        this.downloadable = downloadable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }
}
