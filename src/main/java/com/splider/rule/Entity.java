package com.splider.rule;

public class Entity {

    public static enum ValueType{
        LIST,STRING
    }

    private String key;
    private String query;
    private String attr;
    private String defaultValue;
    private boolean downloadable=false;
    private ValueType valueType;
    private CrawlType type;
    private String separator=" ";
    public Entity(String key, String query,String attr,CrawlType type) {
        this(key,query,attr,null,false,ValueType.STRING,type," ");
    }

    public Entity(String key, String query, String attr, String defaultValue, boolean downloadable, ValueType valueType, CrawlType type, String separator) {
        this.key = key;
        this.query = query;
        this.attr = attr;
        this.defaultValue = defaultValue;
        this.downloadable = downloadable;
        this.valueType = valueType;
        this.type = type;
        this.separator = separator;
    }

    public CrawlType getType() {
        return type;
    }

    public void setType(CrawlType type) {
        this.type = type;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
