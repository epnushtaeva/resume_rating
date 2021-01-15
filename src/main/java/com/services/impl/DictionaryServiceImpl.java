package com.services.impl;

import com.data_base.entities.Dictionary;
import com.data_base.entities.File;
import com.data_base.entities.FileDictionaryForSpeciality;
import com.data_base.entities.Speciality;
import com.data_base.repositories.DictionaryRepository;
import com.data_base.repositories.FileDictionaryForSpecialityRepository;
import com.services.*;
import com.services.classes.DictionaryDto;
import com.services.classes.DictionaryWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private FileService fileService;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private DictionaryRepository dictionaryRepository;

    @Autowired
    private FileDictionaryForSpecialityRepository fileDictionaryForSpecialityRepository;

    private Porter porter = new StemmingPorter();
    private NGramService nGramService = new UnigrammService();

    @Override
    @Transactional
    public void rebuildDictionaryForSpeciality(long specialityId) {
        this.clearDictionary(specialityId);
        List<File> filesForDictionary = this.fileService.getFilesForDictionary(specialityId);
        List<DictionaryWord> dictionaryWords = this.getDictionaryWords(filesForDictionary);

        for(DictionaryWord dictionaryWord: dictionaryWords){
            this.saveDictionaryWordInDataBase(dictionaryWord, specialityId);
        }
    }

    @Override
    @Transactional
    public void rebuildDictionaryForSpeciality(List<Long> fileIds, long specialityId) {
        this.clearDictionary(specialityId);
        List<File> filesForDictionary = this.fileService.getFilesForDictionary(fileIds);
        List<DictionaryWord> dictionaryWords = this.getDictionaryWords(filesForDictionary);

        for(DictionaryWord dictionaryWord: dictionaryWords){
            this.saveDictionaryWordInDataBase(dictionaryWord, specialityId);
        }
    }

    @Override
    @Transactional
    public Set<DictionaryDto> getDictionary(long specialityId){
        List<Long> dictionaryIds = this.fileDictionaryForSpecialityRepository.getAllDictionaries(specialityId);
        Set<DictionaryDto> result = new LinkedHashSet<>();

        for(long dictionaryId: dictionaryIds){
            Dictionary dictionary = this.dictionaryRepository.getOne(dictionaryId);
            DictionaryDto dictionaryDto = new DictionaryDto();
            dictionaryDto.setWord(dictionary.getWord());
            dictionaryDto.setCodedWord(this.fileDictionaryForSpecialityRepository.findFirstBySpecialityIdAndDictionaryId(specialityId, dictionaryId).getCodedWord());
            result.add(dictionaryDto);
        }

        return result;
    }

    @Override
    @Transactional
    public void clearDictionary(long specialityId){
        int currentPageNumber = 1;

        List<Long> dictionaryIds = this.fileDictionaryForSpecialityRepository.getAllDictionaries(specialityId);

        for(long id:dictionaryIds){
            if(this.fileDictionaryForSpecialityRepository.countByDictionaryIdAndSpecialityIdNot(id, specialityId) == 0 &&
                    this.dictionaryRepository.existsById(id)){
                this.dictionaryRepository.deleteById(id);
            }
        }

        this.fileDictionaryForSpecialityRepository.deleteBySpecialityId(specialityId);
    }

    @Override
    public void saveAllDictionaryWordsToDataBase(List<String> dictionaryWords, long specialityId){
        Speciality speciality = this.specialityService.getSpecialityById(specialityId);

        dictionaryWords.stream().forEach(dictionaryWord -> {
            Dictionary dictionary = new Dictionary();

            if(!this.dictionaryRepository.existsByWord(dictionaryWord)){
                dictionary = this.dictionaryRepository.findTopByWord(dictionaryWord);
            } else {
                dictionary.setWord(dictionaryWord);
                dictionary = this.dictionaryRepository.saveAndFlush(dictionary);
            }

            FileDictionaryForSpeciality fileDictionaryForSpeciality = new FileDictionaryForSpeciality();
            fileDictionaryForSpeciality.setDictionary(dictionary);
            fileDictionaryForSpeciality.setSpeciality(speciality);
            fileDictionaryForSpeciality.setCodedWord(this.convertDictionaryWordStringToCodedWord(dictionaryWord, dictionaryWords));
        });
    }

    private List<DictionaryWord> getDictionaryWords(List<File> files){
        List <DictionaryWord> result = new ArrayList<>();

        for(File file: files){
            String fileText = this.fileService.readTextFromFile(file.getPath());
           List<String> filesWords = this.porter.portWords(this.nGramService.getNgrams(fileText));

            for(String fileWord: filesWords){
                DictionaryWord dictionaryWord = new DictionaryWord();

                if(this.isFileWordInDictionaryList(result, fileWord)){
                    dictionaryWord = this.getDictionaryWordFromList(result, fileWord);
                } else {
                    dictionaryWord.setWord(fileWord);
                    result.add(dictionaryWord);
                }

                int wordInfileCount = this.getCountOfWordInFile(filesWords, fileWord);

                if(wordInfileCount > dictionaryWord.getMaxInOneFileCount()){
                    dictionaryWord.setMaxInOneFileCount(wordInfileCount);
                }

                dictionaryWord.addFile(file);
            }
        }

        result = result.stream().sorted(Comparator.comparing(DictionaryWord::getFilesCount).reversed()).collect(Collectors.toList());

        int targetWordsCount = result.size()/6;

        List<DictionaryWord> tempResult = new ArrayList<>();

        for(int i = 0; i < targetWordsCount; i++){
                tempResult.add(result.get(i));
        }

        result = tempResult;

        for(DictionaryWord dictionaryWord: result){
            dictionaryWord.setCodedWord(this.convertDictionaryWordToCodedWord(dictionaryWord, result));
        }

        return result;
    }

    private int getCountOfWordInFile(List<String> filesWords, String fileWord) {
        return (int) filesWords.stream().filter(word -> word.equals(fileWord)).count();
    }


    @Transactional
    private void saveDictionaryWordInDataBase(DictionaryWord dictionaryWord, long specialityId) {
        Dictionary dictionary;

        if(this.dictionaryRepository.existsByWord(dictionaryWord.getWord())){
            dictionary = this.dictionaryRepository.findTopByWord(dictionaryWord.getWord());
        } else {
            dictionary = new Dictionary();
            dictionary.setWord(dictionaryWord.getWord());
            this.dictionaryRepository.save(dictionary);
        }

        for(File file: dictionaryWord.getFiles()){
            FileDictionaryForSpeciality fileDictionaryForSpeciality = new FileDictionaryForSpeciality();
            fileDictionaryForSpeciality.setDictionary(dictionary);
            fileDictionaryForSpeciality.setFile(file);
            fileDictionaryForSpeciality.setSpeciality(this.specialityService.getSpecialityById(specialityId));
            fileDictionaryForSpeciality.setCodedWord(dictionaryWord.getCodedWord());
            this.fileDictionaryForSpecialityRepository.save(fileDictionaryForSpeciality);
        }
    }

    private boolean isFileWordInDictionaryList(List<DictionaryWord> dictionaryWords, String word){
        for(DictionaryWord dictionaryWord: dictionaryWords){
            if(dictionaryWord.getWord().equals(word)){
                return true;
            }
        }

        return false;
    }

    private DictionaryWord getDictionaryWordFromList(List<DictionaryWord> dictionaryWords, String word){
        for(DictionaryWord dictionaryWord: dictionaryWords){
            if(dictionaryWord.getWord().equals(word)){
                return dictionaryWord;
            }
        }

        return new DictionaryWord();
    }

    private void removeNotOftenWords(List<DictionaryWord> words, int minFilesCount, int maxFilesCount){
        for(int wordIndex = 0; wordIndex < words.size(); wordIndex++){
            long currentWordFilesCount = words.get(wordIndex).getFilesCount();

            if(currentWordFilesCount < minFilesCount || currentWordFilesCount > maxFilesCount){
                words.remove(wordIndex);
            }
        }
    }

    private String convertDictionaryWordToCodedWord(DictionaryWord dictionaryWord, List<DictionaryWord> words){
        StringBuilder result = new StringBuilder();
        int wordsCount = words.size();

        for(int dictionaryIndex = 0; dictionaryIndex < wordsCount; dictionaryIndex++){
            if(words.get(dictionaryIndex).getWord().equals(dictionaryWord.getWord())){
                result.append('1');
            } else {
                result.append('0');
            }
        }

        return  result.toString();
    }

    private String convertDictionaryWordStringToCodedWord(String dictionaryWord, List<String> words){
        StringBuilder result = new StringBuilder();
        int wordsCount = words.size();

        for(int dictionaryIndex = 0; dictionaryIndex < wordsCount; dictionaryIndex++){
            if(words.get(dictionaryIndex).equals(dictionaryWord)){
                result.append('1');
            } else {
                result.append('0');
            }
        }

        return  result.toString();
    }
}
