package com.splider.rule;

import java.util.List;

public class MainPage extends PageRule{

    private String url;
    private List<String> urls;
    private String starCycleStr;
    private String endCycleStr;

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
}
