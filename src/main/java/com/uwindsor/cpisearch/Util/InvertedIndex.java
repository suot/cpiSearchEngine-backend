package com.uwindsor.cpisearch.Util;

import java.util.HashMap;


public class InvertedIndex<K1, K2, V> {
    private final HashMap<K1, HashMap<K2, V>> mHash;

    public InvertedIndex() {
        mHash = new HashMap<K1, HashMap<K2, V>>();
    }

    public HashMap<K1, HashMap<K2, V>> getmHash() {
        return mHash;
    }

    public V put(K1 key1, K2 key2, V value) {
        HashMap<K2, V> cell;

        if(mHash.containsKey(key1)) {
            cell = mHash.get(key1);
        }
        else {
            cell = new HashMap<K2, V>();
            mHash.put(key1, cell);
        }

        return cell.put(key2, value);
    }

    public V get(K1 key1, K2 key2) {
        if(mHash.containsKey(key1)) {
            return mHash.get(key1).get(key2);
        }
        else {
            return null;
        }
    }

    public HashMap<K2, V> get_all(K1 key1) {
        if(mHash.containsKey(key1)) {
            return mHash.get(key1);
        }
        else {
            return null;
        }
    }

    public Integer getFrequency(K1 key1){
        if(mHash.containsKey(key1)) {
            int frequency = 0;
            //get pairs of <Index, Frequency> for string key1. Index refers to the index of webpage in the webpageList
            HashMap<K2, V> hashMap = mHash.get(key1);
            for(K2 index : hashMap.keySet()){
                frequency = frequency + (Integer) hashMap.get(index);
            }
            return frequency;
        }else{
            return null;
        }
    }

    public boolean containsKey(K1 key1, K2 key2) {
        if(mHash.containsKey(key1)) {
            if(mHash.get(key1).containsKey(key2)) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

}

