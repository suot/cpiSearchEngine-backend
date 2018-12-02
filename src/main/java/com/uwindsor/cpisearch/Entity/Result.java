package com.uwindsor.cpisearch.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Result {
    private List<Webpage2> pages;
    private int n = 0;
    private double time = 0;
    private Stack<String> suggestions;

    public Result(List<Webpage2> pages, int n, double time, Stack<String> suggestions){
        this.pages = pages;
        this.n = n;
        this.time = time;
        this.suggestions = suggestions;
    }

    public List<Webpage2> getPages(){ return pages; }
    public void setPages(List<Webpage2> pages){ this.pages = pages; }
    public int getN(){ return n; }
    public void setN(int n){ this.n = n; }
    public double getTime(){ return time; }
    public void setTime(double time){ this.time = time; }
    public Stack<String> getSuggestions(){ return suggestions; }
    public void setSuggestions(Stack<String> suggestions){ this.suggestions = suggestions; }

}
