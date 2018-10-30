package com.uwindsor.cpisearch.Controller;

import com.uwindsor.cpisearch.Entity.Webpage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Suo Tian on 2018/10/30.
 */

@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    private static Webpage[] webpages;


    @RequestMapping(value = {"/"})
    public String searchInput() {
        return "/searchInput";
    }

    @RequestMapping("/search")
    @ResponseBody
    public void getSearchResult() {

    }


}
