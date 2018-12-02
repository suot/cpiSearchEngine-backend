package com.uwindsor.cpisearch.Controller;

import com.uwindsor.cpisearch.Entity.Result;
import com.uwindsor.cpisearch.Entity.Webpage;
import com.uwindsor.cpisearch.Entity.Webpage2;
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
 * Modified by Ittsel Ali on 2018/12/01
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
    public Result ranktest(@RequestParam String word, @RequestParam String stringPageOffset) throws IOException {

        System.out.println("word: " + word + " pageOffset: "+ stringPageOffset);
        Integer pageOffset = Integer.parseInt(stringPageOffset);

        double startTime = 0;
        double endTime = 0;

        if (searched_pages_cache.containsKey(word) && page_offset_trackor.containsKey(word) && page_offset_trackor.get(word).contains(pageOffset)) {
//        if (pageOffset != null && searched_pages_cache.containsKey(word) && page_offset_trackor.containsKey(word) && page_offset_trackor.get(word).contains(pageOffset)) {

            int start = page_offset_trackor.get(word).indexOf(pageOffset) * PAGE_OFFSET_LIMIT;
            int end = start + PAGE_OFFSET_LIMIT;

            ArrayList<Integer> pages = new ArrayList<Integer>(searched_pages_cache.get(word).keySet());
            List<Webpage2> webpageList = new ArrayList<Webpage2>();

            if (pages.size() < end)
                end = pages.size();

            startTime = System.nanoTime();

            for (int i = start; i < end; i++) {
                Webpage page = CPIStartupService.getWebpageList().get(pages.get(i));

                Webpage2 result_page = new Webpage2(page.getUrl(), page.getTitle(), page.getText(), CPIStartupService.getInvertedIndex().get_all(word).get(pages.get(i)));

                webpageList.add(result_page);
            }

            endTime = System.nanoTime();

            for (Webpage2 page : webpageList) {
                String text = BruteForceMatch.offset_search(word, page.getText());

                page.setText(text);
            }


            double totalSearchTime = endTime - startTime;

            Result result = new Result(webpageList, CPIStartupService.getInvertedIndex().get_all(word).size(), totalSearchTime, null);

            return result;

        } else{

            /* test search word */
            if (CPIStartupService.getInvertedIndex().getmHash().containsKey(word) && CPIStartupService.getInvertedIndex().get_all(word) != null) {
                HeapSortService hs = new HeapSortService(CPIStartupService.getInvertedIndex().get_all(word), PAGE_OFFSET_LIMIT);

                List<Webpage2> webpageList = new ArrayList<Webpage2>();


                if (!searched_pages_cache.containsKey(word)) {
                    searched_pages_cache.put(word, new LinkedHashMap<Integer, Integer>());
                    page_offset_trackor.put(word, new ArrayList<Integer>());

                    page_offset_trackor.get(word).add(pageOffset);

                } else{
                    page_offset_trackor.get(word).add(pageOffset);
                }

                startTime = System.nanoTime();

                ArrayList<Integer> pageIndexes = hs.getRankedPageIndexes(searched_pages_cache.get(word), pageOffset);

                endTime = System.nanoTime();

                double totalSearchTime = endTime - startTime;

                for (int pi : pageIndexes) {
                    Webpage page = CPIStartupService.getWebpageList().get(pi);
                    String text = BruteForceMatch.offset_search(word, page.getText());

                    Webpage2 result_page = new Webpage2(page.getUrl(), page.getTitle(), text, CPIStartupService.getInvertedIndex().get_all(word).get(pi));

                    webpageList.add(result_page);

                    searched_pages_cache.get(word).put(pi, pi);
                }

                Result result = new Result(webpageList, CPIStartupService.getInvertedIndex().get_all(word).size(), totalSearchTime, null);

                return result;

            } else {
                // return words from EDIT DISTANCE

                startTime = System.nanoTime();

                Stack<String> words =  EditDistanceService.getRecommendedWordsByEditDistance(word);

                endTime = System.nanoTime();

                double totalSearchTime = endTime - startTime;

                Result result = new Result(null, 0, totalSearchTime, words);

                return result;
            }
        }
    }


    @RequestMapping("/suggestion")
    public Stack<String> suggestion(@RequestParam String word){

        logger.info("Find prefix matchinig for: " + word);
        /* test suggestion of the input word */
        return TSTPrefixService.getPrefixes(word);

    }




    private static HashMap<String, List<Webpage>> organiseResults(List<Webpage> pages, String name, String time){
        HashMap<String, List<Webpage>> result = new HashMap<String, List<Webpage>>();
        result.put(name, pages);
        result.put((new Double(time).toString()), new ArrayList<Webpage>());

        return result;
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
            ArrayList<Webpage> suggestions = new ArrayList<Webpage>();

            return suggestions;
        }
    }

    @RequestMapping("/editDistance")
    public Stack<String> recommend(@RequestParam String word){
        logger.info("Find recommendations for: " + word + " with the edit distance equals to 1");
        return EditDistanceService.getRecommendedWordsByEditDistance(word);
    }

}
