package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Util.InvertedIndex;
import com.uwindsor.cpisearch.Util.TST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Stack;

@Service
public class TSTPrefixService {
    private static Logger logger = LoggerFactory.getLogger(CPIStartupService.class);
    private static final int limit = 5;


    public static Stack<String> getPrefixes(String prefix) {
        InvertedIndex<String, Integer, Integer> integerInvertedIndex = CPIStartupService.getInvertedIndex();
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
            logger.info("String is:" + s);
            serviceHashMap.put(s, integerInvertedIndex.getFrequency(s));
        }
        logger.info("Unsorted hash map:" + serviceHashMap);
        HeapSortService2 sortPrefix = new HeapSortService2(serviceHashMap);
        Stack<String> rankedPages = sortPrefix.getRankedPageIndexes();
        logger.info("Sorted hash map:" + rankedPages);
        //rankedWordStack = getRankedPrefixStack(rankedPages);
        return rankedPages;
    }

}