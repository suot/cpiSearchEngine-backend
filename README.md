# cpisearch
It is a domain search engine, developped using TST, invertedIndex, regex, recursion, heapSelect, quickSort, and editDistance.
Step 1: administrator generates a global TST and invertedIndex, based on the input value of domain, depth limit, and url amount.
Step 2: user searches one word and after 3 characters' input, at most 5 suggestion words will be shown in the dropdown list.
Step 3: when the word is found, the top 20 webpage links will be shown in the first page ranked by the word's frequency. When the word is not found, edit distance will work. If there are some words in tst that has the distance of 1, system will show at most 5 recommended words ranked by their total frequency. If every word's distance is more than 1 comparing with the search query, no words will be recommended.
