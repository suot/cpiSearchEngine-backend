package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Util.InvertedIndex;
import com.uwindsor.cpisearch.Util.TST;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

@Service
public class TSTPrefixService {
    private static Logger logger = LoggerFactory.getLogger(CPIStartupService.class);
    private static final int limit = 5;


    public static Stack<String> getPrefixes(String prefix) {
        InvertedIndex<String,Integer,Integer> integerInvertedIndex = CPIStartupService.getInvertedIndex();
        //HashMap<String, Integer> tstPrefixHashMap = CPIStartupService.getTstPrefixHashMap();
        //Stack<String> rankedWordStack = null;
        logger.info("Searching prefixes in the TST");
        TST<HashMap<Integer, Integer>> tst = CPIStartupService.getTst();
        Iterable<String> tstPrefixes = null;
        String searchPrefix = prefix.toLowerCase().trim();
        try {
            if (searchPrefix != null && searchPrefix != "") {
                tstPrefixes = tst.prefixMatch(searchPrefix);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, Integer> serviceHashMap = new HashMap<>();
        for (String s : tstPrefixes) {
            logger.info("String is:"+s);
            serviceHashMap.put(s, integerInvertedIndex.getFrequency(s));
            //logger.info("Prefix:"+s);
        }
        logger.info("Unsorted hash map:"+serviceHashMap);
        HeapSortService2 sortPrefix = new HeapSortService2(serviceHashMap);
        Stack<String> rankedPages = sortPrefix.getRankedPageIndexes();
        logger.info("Sorted hash map:"+rankedPages);
        //rankedWordStack = getRankedPrefixStack(rankedPages);
        return rankedPages;
}

    /*protected static HashMap<Integer, Integer> generateHeapMap(HashMap<Integer, Integer> heapMap, HashMap<String, Integer> tstPrefixHashMap, BidiMap lookupTable, Iterable<String> tstPrefixes, int limit) {
        int frequencyHeapMap = 0;
        for (String str : tstPrefixes) {
            //get value from lookuptable using word
            if (frequencyHeapMap < limit) {
                //get the index value corresponding to the word from lookupTable
                int wordIndex = Integer.valueOf(lookupTable.get(str).toString());
                //get the frequency value corresponding to the word from the lookup table
                int frequency = Integer.valueOf(tstPrefixHashMap.get(str).toString());
                heapMap.put(wordIndex, frequency);
                frequencyHeapMap++;
            }
        }
        return heapMap;
    }*/

    /*private static Stack<String> getRankedPrefixStack(ArrayList<Integer> rankedWord, Stack<String> rankedWordStack) {

        for (int j = rankedWord.size() - 1; j >= 0; j--) {
            int u = rankedWord.get(j);
            //String pushItem = rankedWord.get(j);
            //
            //String s = (String) lookupTable.getKey(Integer.valueOf(u));
            //logger.info("lookup table contains "+s+" ?"+lookupTable.containsValue(Integer.valueOf(u)));
            //logger.info("Pushing word:" + s);
            //rankedWordStack.push(pushItem);
        }
        return rankedWordStack;
    }*/

}
