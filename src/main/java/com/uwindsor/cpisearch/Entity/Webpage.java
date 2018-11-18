package com.uwindsor.cpisearch.Entity;

import com.uwindsor.cpisearch.Util.TST;

import java.util.HashMap;

public class Webpage {
    private String url;
    private String title;
    private String text;
    private TST<Integer> tst;


    public Webpage(String url, String title, String text) {
        this.url = url;
        this.title = title;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TST<Integer> getTst() {
        return tst;
    }

    public void setTst(TST<Integer> tst) {
        this.tst = tst;
    }
}
