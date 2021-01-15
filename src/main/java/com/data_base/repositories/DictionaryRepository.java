package com.data_base.repositories;

import com.data_base.entities.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long>,
        JpaSpecificationExecutor<Dictionary> {
    boolean existsByWord(String word);

    Dictionary findTopByWord(String word);
}
