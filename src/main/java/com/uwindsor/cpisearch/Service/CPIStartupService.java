package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Entity.Webpage;
import com.uwindsor.cpisearch.Util.InvertedIndex;
import com.uwindsor.cpisearch.Util.TST;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by Suo Tian on 2018/10/30.
 *
 */

@Service
public class CPIStartupService {
    private static Logger logger = LoggerFactory.getLogger(CPIStartupService.class);
    private static final int TIME_OUT = 3000;
    private static String domainUrl;
    private static HashSet<String> urlHashSet;
    private static List<Webpage> webpageList;
    private static HashSet<String> wordBlackList;
    //pairs of <word, <indexOfWebPage, frequencyInEveryWebPage>>
    private static TST<HashMap<Integer, Integer>> tst;
    private static InvertedIndex<String, Integer, Integer> invertedIndex;


    public static HashSet<String> getUrlHashSet() {
        return urlHashSet;
    }

    public static List<Webpage> getWebpageList() {
        return webpageList;
    }

    public static InvertedIndex<String, Integer, Integer> getInvertedIndex() {
        return invertedIndex;
    }

    public static TST<HashMap<Integer, Integer>> getTst() {
        return tst;
    }

    /**
     * Generate a global list containing Webpage objects. Each Webpage object has title, text extracted from url html, and TST extracted from its text and containing the <word, frequency> pairs for further sorting or ranking use.
     * @param domain: input value from frontend, like .uwindsor.ca, or .utoronto.ca
     * @param maximumAmount: a reference maximum value to the amount of web pages.
     * @param maximumDepth: an indicator to stop search recursively among the entry url and its descendants.
     * @throws IOException
     */
    public void cpiStartup(String domain, int maximumAmount, int maximumDepth) throws IOException {
        //Step 0: Initialize all global variables
        initializeGlobalVariables();

        //Step 1: Generate domainUrlPattern from domain
        String domainUrlPattern = generateDomainUrlPattern(domain);

        //Step 2: Generate a global hash set filled with unique link urls recursively.
        generateUrlHashSet(domain, domainUrlPattern, maximumAmount, maximumDepth);

        //Step 3: Generate webpageList globally, composed of Webpage objects. Each Webpage object has value of url, title, text by default.
        generateWebpageList();

        //Step 4: Generate TST for every object in webpageList
        generateTST();

        //Step 5: Generate invertedIndex
        generateInvertedIndex();
    }

    /**
     * Initialize all global variables
     */
    private void initializeGlobalVariables(){
        logger.info("Step 0: Initialize all global variables");
        domainUrl = null;
        urlHashSet = null;
        webpageList = null;
        invertedIndex = null;

        wordBlackList = new HashSet<>();
        wordBlackList.addAll(Arrays.asList("a", "an", "the", "this", "that", "it", "they", "i", "these", "those", "am", "is", "are"));
    }


    /**
     * Generate domainUrlPattern from domain
     * @param domain
     * @return
     */
    private String generateDomainUrlPattern(String domain){
        logger.info("Step 1: Get regex of domainUrlPattern from input.");
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

        return domainUrlPattern;
    }

    /**
     * Generate a global variable urlHashSet, with all unique url links.
     * @param domain
     * @param domainUrlPattern
     * @param maximumAmount
     * @param maximumDepth
     * @throws IOException
     */
    private void generateUrlHashSet(String domain, String domainUrlPattern, int maximumAmount, int maximumDepth){
        logger.info("Step 2: Generate a global hash set and fill it with unique link urls recursively...");
        domainUrl = "http://www" + domain;
        urlHashSet = new HashSet<>();

        logger.info("Recurse starts, adding embedded urls from the entry: " + domainUrl + ". Recurse will end when the amount of links exceeds " + maximumAmount + " or when the depth is " + maximumDepth);
        long start_hashSet = System.currentTimeMillis();

        //Add url links into urlHashSet recursively
        addUrlToHashSet(domainUrl, domainUrlPattern, maximumAmount, 0, maximumDepth);

        long end_hashSet = System.currentTimeMillis();
        logger.info("Recurse ends in "+ (end_hashSet - start_hashSet) +" milliseconds. HashSet has been generated with " + urlHashSet.size() + " unique urls.");
    }

    /**
     * Find url link matching the domain pattern, and recursively add it into global hash map.
     * @param url: entry url
     * @param pattern: to filter those url links inside the domain's website
     * @param maximumAmount: the minimum amount of links, telling recurse to stop moving deep, add current url, and move backward to parents who revokes it.
     * @param depth: added from 0 to the maximumDepth.
     * @param maximumDepth: depth of the url to stop the recurse
     * @return
     * @throws IOException
     */
    private void addUrlToHashSet(String url, String pattern, int maximumAmount, int depth, int maximumDepth) {
        try {
            Document doc = Jsoup.connect(url).timeout(TIME_OUT).get();
            //Filter out links matching the pattern.
            Elements links = doc.select(pattern);

            if(links != null) {
                int linkAmount = links.size();

                //End recurse when amount of unique links reaches amountLimit, when depth exceeds depthLimit, or when there is no links in the url.
                if (depth <= maximumDepth && urlHashSet.size() < maximumAmount) {
                    urlHashSet.add(url);
                    logger.info("Try to add url to urlHashSet: " + url);
                    logger.info("Index: " + urlHashSet.size() + ". Depth: " + depth);

                    if (linkAmount > 0) {
                        depth++;
                        int i = 0;
                        while (i < linkAmount && depth <= maximumDepth && urlHashSet.size() < maximumAmount){
                            //recursively find all links matching the regex pattern.
                            String linkUrl = links.get(i).attr("abs:href");
                            addUrlToHashSet(linkUrl, pattern, maximumAmount, depth, maximumDepth);
                            i++;
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("Error happens when accessing url: " + url + " Error: " + e);
        }
    }


    /**
     * Generate a global variable webpageList
     * @throws IOException
     */
    private void generateWebpageList(){
        logger.info("Step 3: Generate webpageList globally using Jsoup. Each Webpage object has value of url, title, text by default.");
        if(urlHashSet != null){
            if(!urlHashSet.isEmpty()){
                //Traverse the urlHashSet
                webpageList = new ArrayList<>();
                Webpage webpage;

                for(String url : urlHashSet){
                    try {
                        Document doc = Jsoup.connect(url).timeout(TIME_OUT).get();
                        if(doc != null) {
                            if (doc.title() != null && doc.body() != null) {
                                //text does not contain title, meaning that occurrence of word in title will not be counted.
                                webpage = new Webpage(url, doc.title(), doc.body().text());
                                webpageList.add(webpage);
                            }
                        }
                    }catch(Exception e){
                        logger.error("Error occurs when adding url content to webpageList. Url: " + url + " Error: " + e);
                        continue;
                    }
                }
                logger.info("Step 3: webpageList is generated successfully with size of " + webpageList.size());
            }
        }
    }

    /**
     * Generate a global TST to store all the words and their frequency occurred in every web page, identified by the index of webpage in the webpageList.
     */
    private void generateTST() {
        logger.info("Step 4: Generate a global TST with pairs of <word, <index, frequency>> using StringTokenizer.");
        if(webpageList != null){
            if(!webpageList.isEmpty()){
                //Define delimiters and initialize a StringTokenizer object. Delimiter does not include '-', meaning that a-b is a different word from a or b.
                logger.info("Define delimiters and initialize a StringTokenizer object. Delimiter does not include '-'.");
                String delimiter = " `~!@#$%^&*()_+=[]{}|\\;\':\",.?/<>\n\r\t\b\f";
                StringTokenizer tokenizer;

                //Traverse every webpage
                logger.info("Traverse webpageList and construct TST. Two words in different case, upper or lower, are treated as one tokenizer.");
                tst = new TST<>();
                Webpage webpage;
                for(int i = 0; i< webpageList.size(); i++){
                    webpage = webpageList.get(i);

                    tokenizer = new StringTokenizer(webpage.getText(), delimiter);
                    String word;
                    HashMap<Integer, Integer> hashMap;

                    while (tokenizer.hasMoreTokens()) {
                        //two words in different case, upper or lower, are treated as one tokenizer.
                        word = tokenizer.nextToken().toLowerCase();
                        //get rid of those unnecessary words, like: a, an, the, am, is, are...
                        if(!wordBlackList.contains(word)) {
                            if (tst.contains(word)) {
                                hashMap = tst.get(word);

                                if(hashMap.containsKey(i)){
                                    hashMap.put(i, hashMap.get(i) + 1);
                                }else{
                                    hashMap.put(i, 1);
                                }
                                tst.put(word, hashMap);

                            } else {
                                tst.put(word, new HashMap<>());
                                tst.get(word).put(i, 1);
                            }
                        }
                    }
                }
                logger.info("The global tst has been generated with the size of " + tst.size());
            }
        }
    }

    /**
     * Generate invertedIndex, which contains pairs of <word, hashMap<index, frequency>>
     */
    private void generateInvertedIndex(){
        logger.info("Step 5: Generate invertedIndex with the type of <String, Integer, Integer>, denoting <word, <index, frequency>>.");
        if(tst != null) {
            invertedIndex = new InvertedIndex<>();
            for(String word : tst.keys()) {
                invertedIndex.getmHash().put(word, tst.get(word));
            }
        }
        logger.info("The invertedIndex has been generated successfully.");
    }
}
