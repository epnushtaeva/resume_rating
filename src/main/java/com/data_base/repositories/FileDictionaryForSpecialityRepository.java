package com.data_base.repositories;

import com.data_base.entities.FileDictionaryForSpeciality;
import com.data_base.entities.NeuralNetworkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDictionaryForSpecialityRepository extends JpaRepository<FileDictionaryForSpeciality, Long>,
        JpaSpecificationExecutor<FileDictionaryForSpeciality> {

    Long countByDictionaryIdAndSpecialityIdNot(long dictionaryId, long specialityId);

    FileDictionaryForSpeciality findFirstBySpecialityIdAndDictionaryId(long specialityId, long dictionaryId);

    @Modifying
    @Query("DELETE FROM FileDictionaryForSpeciality WHERE specialityId = ?1")
    void deleteBySpecialityId(long specialityId);

    Long countBySpecialityId(long specialityId);

    Page<FileDictionaryForSpeciality> findAllBySpecialityId(Pageable pageSettings, long specialityId);

    List<FileDictionaryForSpeciality> findAllBySpecialityId(long specialityId);

    List<FileDictionaryForSpeciality> findAllBySpecialityIdOrderByFileIdAsc(long specialityId);

    Page<FileDictionaryForSpeciality> findAllBySpecialityIdOrderByFileIdAsc(Pageable pageSettings, long specialityId);

    @Query("SELECT DISTINCT dictionaryId FROM  FileDictionaryForSpeciality u WHERE u.specialityId = ?1 ORDER BY dictionaryId")
    List<Long> getAllDictionaries(long specialityId);

    @Query("SELECT DISTINCT fileId FROM  FileDictionaryForSpeciality u WHERE u.specialityId = ?1 AND u.file.learnExample=true")
    List<Long> findAllFileIdsBySpecialityIdOrderByFileIdAsc(long specialityId);
}
