package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Entity.Webpage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class hashMapService {
    private static Logger logger = LoggerFactory.getLogger(hashMapService.class);
    private static HashSet<String> urlHashSet;
    private static List<Webpage> webpageList;
    private static final int TIME_OUT = 3000;

    public static List<Webpage> getWebpageList() {
        return webpageList;
    }


    /**
     * Generate a global list containing Webpage objects. Each Webpage object has title, text extracted from url html, and hashMap extracted from its text and containing the <word, frequency> pairs for further sorting or ranking use.
     * @param domain: input value from frontend, like .uwindsor.ca, or .utoronto.ca
     * @param minimumAmount: a reference minimum value to the amount of web pages.
     * @param maximumDepth: an indicator to stop search recursively among the entry url and its descendants.
     * @throws IOException
     */
    public List<Webpage> generateWebpageList(String domain, int minimumAmount, int maximumDepth) throws IOException {
        //Step 1: Get domainUrlPattern from domain
        logger.info("Step 1: Get domainUrlPattern from domain...");
        logger.info("Extract domain-url pattern from the input");
        String domainString = "";
        String[] domainParts = domain.split("\\.");

        if(domainParts.length>0)
        for(int i=0; i<domainParts.length; i++){
            if(!domainParts[i].isEmpty()) {
                domainString = domainString +"\\."+ domainParts[i];
            }
        }

        String domainUrlPattern = "a[href~=(https?:(\\/){2})?www(\\.[a-zA-Z0-9][a-zA-Z0-9-_]{0,61}[a-zA-Z0-9]?)*" + domainString +"(\\/[^\\s\\/]+)*(\\/)?]";
        logger.info("Domain-url pattern is: " + domainUrlPattern);


        //Step 2: Generate a global hash set and fill it with unique link urls recursively.
        logger.info("Step 2: Generate a global hash set and fill it with unique link urls recursively...");
        String entryUrl = "www" + domain;
        urlHashSet = new HashSet<>();

        logger.info("Recurse starts, adding embedded urls from the entry: " + entryUrl + ". Recurse will end when the amount of links exceeds " + minimumAmount + " or when the depth is " + maximumDepth);
        long start_hashSet = System.currentTimeMillis();

        //Add url links into urlHashSet recursively
        addUrlToHashSet(entryUrl, domainUrlPattern, minimumAmount, 0, maximumDepth);

        long end_hashSet = System.currentTimeMillis();
        logger.info("Recurse ends in "+ (end_hashSet - start_hashSet) +" milliseconds. HashSet has been generated with " + urlHashSet.size() + " unique urls.");


        //Step 3: Extract tile and text from each url, and generate its hashMap with the <word, frequency> as the hashMap's <key, value> pair.
        logger.info("Step 3: Extract text from each url and generate its hashMap with the <word, frequency> as the hashMap's <key, value> pair...");





        return webpageList;
    }


    /**
     * Find url link matching the domain pattern, and recursively add it into global hash map.
     * @param url: entry url
     * @param pattern: to filter those url links inside the domain's website
     * @param minimumAmount: the minimum amount of links, telling recurse to stop moving deep, add current url, and move backward to parents who revokes it.
     *                     The final amount of links will exceed this minimum value too much maybe.
     *                     It is a way to diversify links, without clustering one child link and its descendants into the hash map.
     * @param depth: added from 0 to the maximumDepth.
     * @param maximumDepth: depth of the url to stop the recurse
     * @return the entry url called by
     * @throws IOException
     */
    private String addUrlToHashSet(String url, String pattern, int minimumAmount, int depth, int maximumDepth) throws IOException {
        //Filter out links matching the pattern.
        Document doc = Jsoup.connect(url).timeout(TIME_OUT).get();
        Elements links = doc.select(pattern);

        //End recurse when amount of unique links reaches amountLimit, when depth reaches depthLimit, or when there is no links in the url.
        if(links.size() == 0 || depth==maximumDepth || urlHashSet.size() >= minimumAmount){
            return url;
        }
        else{
            //recursively find all links matching the regex pattern.
            depth++;
            for(Element link : links) {
                String linkUrl = link.attr("abs:href");
                urlHashSet.add(addUrlToHashSet(linkUrl, pattern, minimumAmount, depth, maximumDepth));
            }
            return url;
        }
    }


    private void generateHashMap() throws IOException {
        if(urlHashSet != null){
            if(!urlHashSet.isEmpty()){
                //Traverse the urlHashSet
                Webpage webpage;
                HashMap<String, Integer> wordFrequency;
                for(String url : urlHashSet){
                    Document doc = Jsoup.connect(url).timeout(TIME_OUT).get();
                    String text = doc.text();
                    wordFrequency = getWordFrequencyFromText(text);
                    webpage = new Webpage(url, doc.title(), text, wordFrequency);
                    webpageList.add(webpage);
                }
            }
        }


    }


    private HashMap<String, Integer> getWordFrequencyFromText(String text){
        if(text == null|| text.isEmpty()){
            return null;
        }else{
            HashMap<String, Integer> wordFrequency = new HashMap<>();





            return wordFrequency;
        }
    }


}
