package com.services.classes;

import com.data_base.entities.File;

import java.util.ArrayList;
import java.util.List;

public class DictionaryWord {
    private String word;
    private String codedWord;
    private long filesCount = 0;
    private long maxInOneFileCount = 0;
    private List<File> files = new ArrayList<>();

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(long filesCount) {
        this.filesCount = filesCount;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getCodedWord() {
        return codedWord;
    }

    public void setCodedWord(String codedWord) {
        this.codedWord = codedWord;
    }

    public void addFile(File file){
        this.filesCount++;
        this.files.add(file);
    }

    public long getMaxInOneFileCount() {
        return maxInOneFileCount;
    }

    public void setMaxInOneFileCount(long maxInOneFileCount) {
        this.maxInOneFileCount = maxInOneFileCount;
    }
}
