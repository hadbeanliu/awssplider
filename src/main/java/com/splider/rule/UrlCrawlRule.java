package com.splider.rule;

import java.util.List;
import java.util.Map;

public class UrlCrawlRule {

    private String url;
    private CrawlType crawlType;
    private String name;
    private List<Entity> entities;
    private Map<String,String> extend;
    private Map<String,String> values;
    private List<UrlCrawlRule> children;
    private Map<String,List<String>> imgUrl;
    private int status=0;


    public Map<String, List<String>> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(Map<String, List<String>> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UrlCrawlRule(String url, CrawlType crawlType) {
        this.url = url;
        this.crawlType = crawlType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CrawlType getCrawlType() {
        return crawlType;
    }

    public void setCrawlType(CrawlType crawlType) {
        this.crawlType = crawlType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public Map<String, String> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, String> extend) {
        this.extend = extend;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public List<UrlCrawlRule> getChildren() {
        return children;
    }

    public void setChildren(List<UrlCrawlRule> children) {
        this.children = children;
    }
}
