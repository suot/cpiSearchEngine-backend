package com.uwindsor.cpisearch.Entity;

import java.util.HashMap;

public class Webpage {
    private String url;
    private String title;
    private String text;
    private HashMap<String, Integer> wordFrequency;


    public Webpage(String url, String title, String text, HashMap<String, Integer> wordFrequency) {
        this.url = url;
        this.title = title;
        this.text = text;
        this.wordFrequency = wordFrequency;
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

    public HashMap<String, Integer> getWordFrequency() {
        return wordFrequency;
    }

    public void setWordFrequency(HashMap<String, Integer> wordFrequency) {
        this.wordFrequency = wordFrequency;
    }
}
