package com.uwindsor.cpisearch.Entity;

import java.util.HashMap;

public class Webpage {
    //private String id;
    private String name;
    private String time;
    private String path;
    private HashMap<String, Integer> wordFrequency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, Integer> getWordFrequency() {
        return wordFrequency;
    }

    public void setWordFrequency(HashMap<String, Integer> wordFrequency) {
        this.wordFrequency = wordFrequency;
    }
}
