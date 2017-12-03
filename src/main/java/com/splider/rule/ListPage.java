package com.splider.rule;

import java.util.List;

public class ListPage extends PageRule{

    private String url;
    private List<String> urls;
    private String starCycleStr;
    private String endCycleStr;
    private boolean moreDeep;
    private int maxDeep=1;
    private List<ContentPage> childPage;
    private String nextPageUrl;
    private String prefix;


    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getStarCycleStr() {
        return starCycleStr;
    }

    public void setStarCycleStr(String starCycleStr) {
        this.starCycleStr = starCycleStr;
    }

    public String getEndCycleStr() {
        return endCycleStr;
    }

    public void setEndCycleStr(String endCycleStr) {
        this.endCycleStr = endCycleStr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public boolean isMoreDeep() {
        return moreDeep;
    }

    public void setMoreDeep(boolean moreDeep) {
        this.moreDeep = moreDeep;
    }

    public List<ContentPage> getChildPage() {
        return childPage;
    }

    public void setChildPage(List<ContentPage> childPage) {
        this.childPage = childPage;
    }
}
