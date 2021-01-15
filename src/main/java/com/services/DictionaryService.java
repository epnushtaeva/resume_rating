package com.services;

import com.services.classes.DictionaryDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface DictionaryService {

    @Transactional
    void rebuildDictionaryForSpeciality(long specialityId);

    @Transactional
    void rebuildDictionaryForSpeciality(List<Long> fileIds, long specialityId);

    @Transactional
    Set<DictionaryDto> getDictionary(long specialityId);

    @Transactional
    void clearDictionary(long specialityId);

    void saveAllDictionaryWordsToDataBase(List<String> dictionaryWords, long specialityId);
}
