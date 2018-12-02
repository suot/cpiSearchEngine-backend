package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Util.BinaryHeap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HeapSortService2 {
    private final int SUGGESTION_LIMIT =5; // set number of results
    private HashMap<String, Integer> pageHash;
    private BinaryHeap<Integer> heap;
    private HashMap<Integer, ArrayList<String>> frequency_mapper_to_page; // to track duplicate frequencies
    private static Logger logger = LoggerFactory.getLogger(HeapSortService2.class);


    public HeapSortService2(HashMap<String, Integer> unsortedPrefixMatches){
        pageHash = unsortedPrefixMatches;
        frequency_mapper_to_page = new HashMap<Integer, ArrayList<String>>();
    }

    public Stack<String> getRankedPageIndexes(){
            int i = 0;
            int freq;
            String word;

            heap = new BinaryHeap<>();

            Iterator it = pageHash.entrySet().iterator();

            Map.Entry pair;

            while(it.hasNext()) {
                pair = (Map.Entry)it.next();

                word = (String)pair.getKey();
                freq = (int)pair.getValue();

                if (i < SUGGESTION_LIMIT) {
                    heap.insert(freq);
                    insert_frequency_mapper(freq, word);

                } else {
                    if (freq > heap.findMin()) {
                        delete_frequency_mapper(heap.deleteMin());

                        heap.insert(freq);
                        insert_frequency_mapper(freq, word);
                    }
                }
                i++;
            }

        return collect_sorted_pages();
    }

    private void insert_frequency_mapper(int freq, String word){
        if(frequency_mapper_to_page.containsKey(freq)){
            frequency_mapper_to_page.get(freq).add(word);
        }
        else{
            frequency_mapper_to_page.put(freq, new ArrayList<>());
            frequency_mapper_to_page.get(freq).add(word);
        }
    }

    private void delete_frequency_mapper(int freq){
        if(frequency_mapper_to_page.containsKey(freq)){
            if (frequency_mapper_to_page.get(freq).size() > 1){
                frequency_mapper_to_page.get(freq).remove(0);
            }
            else{
                frequency_mapper_to_page.remove(freq);
            }
        }
        else{
            //TBD
        }
    }

    private Stack<String> collect_sorted_pages(){
        Stack<String> suggestions;
        if(!heap.isEmpty()){
            suggestions = new Stack<>();
            while(heap.findMin()!= null){
                int frequency = heap.deleteMin();
                String word = frequency_mapper_to_page.get(frequency).remove(0);
                suggestions.push(word);
                //logger.info("Suggestion word: " + word + ". Frequency: " + frequency);
            }
            return invertStack(suggestions);
        }else{
            return null;
        }
    }

    private Stack<String> invertStack(Stack<String> stack){
        Stack<String> invertedStack = new Stack<>();
        int size = stack.size();
        for(int i=0;i<size;i++)
        invertedStack.push(stack.pop());
        return invertedStack;
    }
}
