package com.services;

import java.util.List;
import java.util.Set;

public interface Porter {

    List<String> portWords(List<String> words);

    String portWord(String word);
}
