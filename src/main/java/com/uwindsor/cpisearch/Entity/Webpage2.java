package com.uwindsor.cpisearch.Entity;

public class Webpage2 {
    private String url;
    private String title;
    private String text;
    private int freq;

    public Webpage2(String url, String title, String text, int freq) {
        this.url = url;
        this.title = title;
        this.text = text;
        this.freq = freq;
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

    public void setFreq(int freq){ this.freq = freq; }

    public int getFreq(){ return freq; }
}
