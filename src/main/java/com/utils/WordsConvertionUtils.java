package com.utils;

import java.util.List;

public class WordsConvertionUtils {

    public static String getCodedWord(String word, List<String> dictionary){
        StringBuilder result = new StringBuilder();

        for(int index = 0; index < dictionary.size(); index++){
            if(index == dictionary.indexOf(word)) {
                result.append('1');
            } else {
                result.append('0');
            }
        }

        return  result.toString();
    }
}
