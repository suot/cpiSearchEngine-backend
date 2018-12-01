package com.uwindsor.cpisearch.Controller;

import com.uwindsor.cpisearch.Entity.Webpage;
import com.uwindsor.cpisearch.Service.CPIStartupService;
import com.uwindsor.cpisearch.Service.EditDistanceService;
import com.uwindsor.cpisearch.Service.HeapSortService;
import com.uwindsor.cpisearch.Service.TSTPrefixService;

import com.uwindsor.cpisearch.Util.BruteForceMatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;


/**
 * Created by Suo Tian on 2018/10/30.
 */

@CrossOrigin
@RestController
@RequestMapping("/cpi")
public class MainController {
    @Autowired
    private CPIStartupService cpiStartupService;


    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    private static Webpage[] webpages;
    private final int PAGE_OFFSET_LIMIT=5;

public static HashMap<String, LinkedHashMap<Integer, Integer>> searched_pages_cache = new HashMap<String, LinkedHashMap<Integer, Integer>>();
public static HashMap<String, ArrayList<Integer>> page_offset_trackor = new HashMap<String, ArrayList<Integer>>();

    @RequestMapping("/startup")
    public HashMap<String, HashMap<Integer, Integer>> cpiStartup(@RequestParam String domain, @RequestParam int maximumAmount, @RequestParam int maximumDepth) throws IOException {
        cpiStartupService.cpiStartup(domain, maximumAmount, maximumDepth);
        return CPIStartupService.getInvertedIndex().getmHash();
    }

    @RequestMapping("/ranktest")
    public List<Webpage> ranktest(@RequestParam String domain, @RequestParam int maximumAmount, @RequestParam int maximumDepth, @RequestParam String word, @RequestParam Integer pageOffset) throws IOException {

        if (pageOffset != null && searched_pages_cache.containsKey(word) && page_offset_trackor.containsKey(word) && page_offset_trackor.get(word).contains(pageOffset)) {

            int start = page_offset_trackor.get(word).indexOf(pageOffset) * PAGE_OFFSET_LIMIT;
            int end = start + PAGE_OFFSET_LIMIT;

            ArrayList<Integer> pages = new ArrayList<Integer>(searched_pages_cache.get(word).keySet());
            List<Webpage> webpageList = new ArrayList<Webpage>();

            if (pages.size() < end)
                end = pages.size();

            for (int i = start; i < end; i++) {
                Webpage page = CPIStartupService.getWebpageList().get(pages.get(i));
                int position = BruteForceMatch.search1(word, page.getText());
                page.setText(page.getText().substring(position, position+100));
                webpageList.add(page);
            }
            return webpageList;

        } else{

            /* test search word */
            if (CPIStartupService.getInvertedIndex().getmHash().containsKey(word) && CPIStartupService.getInvertedIndex().get_all(word) != null) {
                HeapSortService hs = new HeapSortService(CPIStartupService.getInvertedIndex().get_all(word), PAGE_OFFSET_LIMIT);

                List<Webpage> webpageList = new ArrayList<Webpage>();


                if (!searched_pages_cache.containsKey(word)) {
                    searched_pages_cache.put(word, new LinkedHashMap<Integer, Integer>());
                    page_offset_trackor.put(word, new ArrayList<Integer>());

                    page_offset_trackor.get(word).add(pageOffset);

                } else{
                    page_offset_trackor.get(word).add(pageOffset);
                }

                for (int pi : hs.getRankedPageIndexes(searched_pages_cache.get(word), pageOffset)) {
                    Webpage page = CPIStartupService.getWebpageList().get(pi);
                    int position = BruteForceMatch.search1(word, page.getText());
                    page.setText(page.getText().substring(position, position+100));
                    webpageList.add(page);

                    searched_pages_cache.get(word).put(pi, pi);
                }
                //NOTE: text search not applied yet
                return webpageList;

            } else {
                // return words from EDIT DISTANCE
                return new ArrayList<>();
            }
        }
    }

    @RequestMapping("/rank")
    public List<Webpage> rank(@RequestParam String word) throws IOException {

        System.out.println("Search query: " + word);
        /* test search word */
        if(CPIStartupService.getInvertedIndex().getmHash().containsKey(word) && CPIStartupService.getInvertedIndex().get_all(word) != null){
            HeapSortService hs = new HeapSortService(CPIStartupService.getInvertedIndex().get_all(word));

            List<Webpage> webpageList = new ArrayList<Webpage>();
            for(int pi : hs.getRankedPageIndexes())
                webpageList.add(CPIStartupService.getWebpageList().get(pi));

            //NOTE: text search not applied yet
            return webpageList;
        }
        else{
            // return words from EDIT DISTANCE
            return new ArrayList<>();
        }
    }


    @RequestMapping("/suggestion")
    public Stack<String> suggestion(@RequestParam String word){

        System.out.println("Find prefix matchinig for: " + word);
        /* test suggestion of the input word */
        return TSTPrefixService.getPrefixes(word);

    }

    @RequestMapping("/editDistance")
    public Stack<String> recommend(@RequestParam String word){
        System.out.println("Find recommendations for: " + word + " with the edit distance equals to 1");
        return EditDistanceService.getRecommendedWordsByEditDistance(word);
    }

}
