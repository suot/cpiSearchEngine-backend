package com.uwindsor.cpisearch.Controller;

import com.uwindsor.cpisearch.Entity.Webpage;
import com.uwindsor.cpisearch.Service.CPIStartupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

//    @RequestMapping("/startup")
//    public String cpiStartup(@RequestParam String domain, @RequestParam int maximumAmount, @RequestParam int maximumDepth) throws IOException {
//        cpiStartupService.cpiStartup(domain, maximumAmount, maximumDepth);
////        return CPIStartupService.getInvertedIndexHashMap();
//        return domain;
//    }

    @RequestMapping("/startup")
    public HashMap<String, HashMap<Integer, Integer>> cpiStartup(@RequestParam String domain, @RequestParam int maximumAmount, @RequestParam int maximumDepth) throws IOException {
        cpiStartupService.cpiStartup(domain, maximumAmount, maximumDepth);
        return CPIStartupService.getInvertedIndex().getmHash();
    }

    @RequestMapping("/search")
    @ResponseBody
    public void getSearchResult() {

    }


}
