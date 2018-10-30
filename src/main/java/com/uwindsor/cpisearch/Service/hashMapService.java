package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Entity.Webpage;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class hashMapService {

    /**
     * Construct a hash map for one single input file.
     * @param filePath
     * @return A hashMap with <word, frequency> pairs.
     */
    public HashMap<String, Integer> constructWordFrequencyPairs(String filePath){
        //read the file

        //construct hash map for every word: key=word, value=frequency.

        //close the file and return back the hash map.
        return null;
    }


    /**
     * Construct a web page array used for further algorithms globally.
     * @param folderPath
     * @return Webpage[]
     */
    public Webpage[] getAllWebpages(String folderPath){
        //traverse all files in the folder


        //revoke the constructWordFrequencyPairs function.


        //store all webpage objects into the returned array

        return null;
    }


}
