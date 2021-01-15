package com.services.impl;

import com.dictionaries.RussianWordsDictionary;
import com.services.NGramService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.NumberUtils;

import javax.print.attribute.standard.NumberUp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnigrammService implements NGramService {

    public List<String> getNgrams(String text) {
        if (text == null || text == "") {
            text = "";
        }

        String[] words = text
                .toLowerCase()
                .replaceAll("[\"'<>]","")
                .split("[ \\pP\n\t\r\\v+=,!.?:;-\\\\*#^\\d/]");

        List<String> result = new ArrayList<>();

        for(String wordTemp: words){
            String word = wordTemp.replace(" ","");

            if(!word.equals("") && !RussianWordsDictionary.INTERJECTIONS.contains(word) && !RussianWordsDictionary.PREPOSITIONS.contains(word) &&
            word.length() > 3 && !word.contains("жен") && !word.contains("муж") && !word.contains("жела") && !word.contains("дата") &&
            !word.contains("рож") && !word.contains("оплаты") && !StringUtils.isNumeric(word.replace(".", "")) &&
            !word.contains("фамил") && !word.contains("имя") && !word.contains("отчест") && !word.contains("ф.и") && !word.contains("и.о") && !word.contains("фио") &&
            !word.contains("график") && !word.contains("город") && !word.contains("рублей") && !word.contains("контактная") && !word.contains("телефон") &&
            !word.contains("почта") && !word.contains("дохода") && !word.equals("укажите")  && !word.equals("свои") && !word.equals("указать") &&
                    !word.contains("mail") && !word.equals("года") && !word.contains("гражд") && !word.contains("росси")  && !word.contains("федер")|| word.equals("1C")) {
                result.add(word);
            }
        }

        List<String> realResult = new ArrayList<>();

        int i = 0;

        while (i < result.size()){
            String currentWord = result.get(i);
            i++;

            if(i < result.size()) {
                currentWord += " " + result.get(i);
            }

            if(!realResult.contains(currentWord)) {
                realResult.add(currentWord);
            }
        }

        result = realResult;

        return result;
    }
}
