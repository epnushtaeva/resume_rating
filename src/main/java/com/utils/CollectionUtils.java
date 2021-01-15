package com.utils;

import java.util.Map;

public class CollectionUtils {

    public static void addOrIncreaseCount(Map<String, Integer> wordsToCountInFiles, String word, int countInFiles){
        if(wordsToCountInFiles.containsKey(word)){
            wordsToCountInFiles.put(word, wordsToCountInFiles.get(word) + countInFiles);
        } else {
            wordsToCountInFiles.put(word, countInFiles);
        }
    }
}
