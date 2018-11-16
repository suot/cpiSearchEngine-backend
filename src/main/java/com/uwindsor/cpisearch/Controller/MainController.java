package com.uwindsor.cpisearch.Controller;

import com.uwindsor.cpisearch.Entity.Webpage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Suo Tian on 2018/10/30.
 */

@CrossOrigin
@RestController
@RequestMapping("/cpi")
public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    private static Webpage[] webpages;

    @RequestMapping("/access_url")
    public String searchInput(@RequestParam String url) {
        //print out updated url
        logger.info("Request to update access url is received. New access url is: " + url);

        //

        return "access url is updated";
    }

    @RequestMapping("/search")
    @ResponseBody
    public void getSearchResult() {

    }


}
