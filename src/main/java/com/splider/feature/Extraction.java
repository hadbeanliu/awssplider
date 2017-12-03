package com.splider.feature;

abstract class Extraction {

    public abstract void setBefore(Extraction e);

    public abstract String extract(String str);

    public abstract void setAfter(Extraction e);
}
