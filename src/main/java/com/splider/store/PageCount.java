package com.splider.store;

import java.util.List;

public class PageCount {


    private String url;
    private List<PageCount> listPage;
    private boolean hasSuccess;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<PageCount> getListPage() {
        return listPage;
    }

    public void setListPage(List<PageCount> listPage) {
        this.listPage = listPage;
    }

    public boolean isHasSuccess() {
        return hasSuccess;
    }

    public void setHasSuccess(boolean hasSuccess) {
        this.hasSuccess = hasSuccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageCount pageCount = (PageCount) o;

        return url.equals(pageCount.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
