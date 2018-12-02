package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Entity.Webpage;
import com.uwindsor.cpisearch.Util.InvertedIndex;
import com.uwindsor.cpisearch.Util.Sequences;
import com.uwindsor.cpisearch.Util.TST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EditDistanceService {
    private static Logger logger = LoggerFactory.getLogger(HeapSortService2.class);
    private static final int EDIT_DISTANCE_LIMIT = 2;

    public static Stack<String> getRecommendedWordsByEditDistance(String word){
        InvertedIndex<String, Integer, Integer> integerInvertedIndex = CPIStartupService.getInvertedIndex();
        TST<HashMap<Integer, Integer>> tst = CPIStartupService.getTst();

        List<String> unsortedRecommendedString = new ArrayList<>();
        String wordInLowerCase = word.toLowerCase().trim();

        if (wordInLowerCase != null && wordInLowerCase != ""){
            logger.info("Start to calculate the edit distance between " + word + " and all keys in invertedIndex...");
            long startTime = System.nanoTime();

            for(String key : integerInvertedIndex.getmHash().keySet()){
                if(Sequences.editDistance(key, wordInLowerCase) <= EDIT_DISTANCE_LIMIT){
                    unsortedRecommendedString.add(key);
                }else{
                    continue;
                }
            }

            long endTime = System.nanoTime();
            logger.info("All words with the edit distance 1 or 2 from " + word + " are found, costing " + (endTime - startTime)+" nanoseconds");
        }

        if(unsortedRecommendedString.isEmpty()){
            logger.info("Edit distance function ends up. No recommendation found.");
            return null;
        }else {
            HashMap<String, Integer> serviceHashMap = new HashMap<>();
            int f;
            for (String s : unsortedRecommendedString) {
                f = integerInvertedIndex.getFrequency(s);
                serviceHashMap.put(s, f);
                //logger.info("String: " + s + ". Total frequency: " + f);
            }

            logger.info("Unsorted hash map: " + serviceHashMap);
            HeapSortService2 sortPrefix = new HeapSortService2(serviceHashMap);
            Stack<String> rankedRecommendations = sortPrefix.getRankedPageIndexes();
            logger.info("Edit distance function ends up. Ranked hash map(size <= 5) is returned as recommended string stack: " + rankedRecommendations);
            return rankedRecommendations;
        }
    }
}
