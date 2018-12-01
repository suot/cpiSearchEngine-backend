package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Entity.Webpage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class EditDistanceService {

    public List<String> getEditDistance(String word){
        HashMap<String, HashMap<Integer, Integer>> mHash = CPIStartupService.getInvertedIndex().getmHash();

        /*
        //if there is at least one key in the global invertedIndex whose edit distance value toward the input word is 1
        List<String> distanceOne = xxx.xxx(word);
        if(distanceOne==null || distanceOne.isEmpty()){
            return null;
        }else{
            //quick sort
            List<String> sortedDistanceOne = quickSort(distanceOne);
            if(sortedDistanceOne.size()>5){
                return sortedDistanceOne.subList(0, 4);
            }else{
                return sortedDistanceOne;
            }
        }

        */
        return null;
    }
}
