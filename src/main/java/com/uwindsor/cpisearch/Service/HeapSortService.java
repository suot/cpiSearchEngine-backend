package com.uwindsor.cpisearch.Service;

import com.uwindsor.cpisearch.Util.BinaryHeap;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class HeapSortService {
    private static int PAGINATION_OFFSET_LIMIT=5; // default
    private HashMap<Integer, Integer> pageHash;
    private BinaryHeap<Integer> heap;
    private HashMap<Integer, ArrayList<Integer>> frequency_mapper_to_page; // to track duplicate frequencies


    public HeapSortService(){
    }

    public HeapSortService(HashMap<Integer, Integer> pH){
        pageHash = pH;
        frequency_mapper_to_page = new HashMap<Integer, ArrayList<Integer>>();
    }

    public HeapSortService(HashMap<Integer, Integer> pH, int offset_limit){
        pageHash = pH;
        frequency_mapper_to_page = new HashMap<Integer, ArrayList<Integer>>();
        PAGINATION_OFFSET_LIMIT = offset_limit;
    }


    public ArrayList<Integer> getRankedPageIndexes(LinkedHashMap<Integer, Integer> cached_pages, int page_offset){
        int i=0, page_no, freq=0;

        heap = new BinaryHeap<Integer>();
        Iterator it = (Iterator)pageHash.entrySet().iterator();

        Map.Entry pair=null;

        int InitialHeapSize = (PAGINATION_OFFSET_LIMIT * page_offset) - cached_pages.size();

        if ((PAGINATION_OFFSET_LIMIT * page_offset) <= cached_pages.size() )
            InitialHeapSize = InitialHeapSize + PAGINATION_OFFSET_LIMIT;

        while(it.hasNext()) {
            pair = (Map.Entry)it.next();

            page_no = (int)pair.getKey();
            freq = (int)pair.getValue();

            if (i < InitialHeapSize && !cached_pages.containsKey(page_no)) {
                heap.insert(freq);

                insert_frequency_mapper(freq, page_no);

                i++;

            } else if(!cached_pages.containsKey(page_no)) {

                if (freq > heap.findMin()) {
                    delete_frequency_mapper(heap.deleteMin());

                    heap.insert(freq);
                    insert_frequency_mapper(freq, page_no);
                }
            } else{} // not required as of yet

        }

        return collect_paginated_sorted_pages();
    }

    public ArrayList<Integer> getRankedPageIndexes(){
            int i=0, page_no, freq=0;

            heap = new BinaryHeap<Integer>();
            Iterator it = (Iterator)pageHash.entrySet().iterator();

            Map.Entry pair=null;

            while(it.hasNext()) {
                pair = (Map.Entry)it.next();

                page_no = (int)pair.getKey();
                freq = (int)pair.getValue();

                if (i < PAGINATION_OFFSET_LIMIT) {
                    heap.insert(freq);
                    insert_frequency_mapper(freq, page_no);

                } else {
                    if (freq > heap.findMin()) {
                        delete_frequency_mapper(heap.deleteMin());

                        heap.insert(freq);
                        insert_frequency_mapper(freq, page_no);
                    }
                }
                i++;
            }

        return collect_sorted_pages();
    }

    private void insert_frequency_mapper(int freq, int page_i){
        if(frequency_mapper_to_page.containsKey(freq)){
            frequency_mapper_to_page.get(freq).add(page_i);
        }
        else{
            frequency_mapper_to_page.put(freq, new ArrayList<Integer>());
            frequency_mapper_to_page.get(freq).add(page_i);
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

    private ArrayList<Integer> collect_sorted_pages(){
        Stack pages = new Stack();
        while(heap.findMin()!=null){

            pages.push(
                    frequency_mapper_to_page.get(heap.deleteMin()).remove(0)
            );
        }

        ArrayList<Integer> sorted_pages = new ArrayList<Integer>();
        while(!pages.empty())
            sorted_pages.add((int)pages.pop());

        return sorted_pages;
    }


    private ArrayList<Integer> collect_paginated_sorted_pages(){
        Stack pages = new Stack();

        for (int i=0; i < PAGINATION_OFFSET_LIMIT; i++){
            if(heap.findMin()!=null) {
                pages.push(
                        frequency_mapper_to_page.get(heap.deleteMin()).remove(0)
                );
            }
        }

        heap = new BinaryHeap<Integer>();

        ArrayList<Integer> sorted_pages = new ArrayList<Integer>();
        while(!pages.empty())
            sorted_pages.add((int)pages.pop());

        return sorted_pages;
    }
}
