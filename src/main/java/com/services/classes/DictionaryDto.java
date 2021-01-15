package com.services.classes;

public class DictionaryDto {
    private String word;
    private String codedWord;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCodedWord() {
        return codedWord;
    }

    public void setCodedWord(String codedWord) {
        this.codedWord = codedWord;
    }

    @Override
    public boolean equals(Object dictionaryDto){
        return this.word.equals(((DictionaryDto)dictionaryDto).getWord());
    }

    @Override
    public int hashCode(){
        return this.word.hashCode();
    }
}
