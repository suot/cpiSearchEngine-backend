package com.uwindsor.cpisearch.Controller;

import com.uwindsor.cpisearch.Entity.Webpage;
import com.uwindsor.cpisearch.Service.CPIStartupService;
import com.uwindsor.cpisearch.Service.HeapSortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


    @RequestMapping("/startup")
    public HashMap<String, HashMap<Integer, Integer>> cpiStartup(@RequestParam String domain, @RequestParam int maximumAmount, @RequestParam int maximumDepth) throws IOException {
        cpiStartupService.cpiStartup(domain, maximumAmount, maximumDepth);
        return CPIStartupService.getInvertedIndex().getmHash();
    }

    @RequestMapping("/ranktest")
    public List<Webpage> ranktest(@RequestParam String domain, @RequestParam int maximumAmount, @RequestParam int maximumDepth, @RequestParam String word) throws IOException {
        cpiStartupService.cpiStartup(domain, maximumAmount, maximumDepth);

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


}
