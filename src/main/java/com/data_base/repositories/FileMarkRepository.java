package com.data_base.repositories;

import com.data_base.entities.FileMarkForSpeciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMarkRepository extends JpaRepository<FileMarkForSpeciality, Long>,
        JpaSpecificationExecutor<FileMarkForSpeciality> {
     void deleteAllByFileId(long fileId);

     List<FileMarkForSpeciality> findBySpecialityIdOrderByFileIdAsc(Long specialityId);

     List<FileMarkForSpeciality> findByFileIdInAndSpecialityIdOrderByFileIdAsc(List<Long> fileIds, Long specialityId);

     Page<FileMarkForSpeciality> findByFileIdAndSpecialityIdInOrderByFileIdAsc(Pageable pageSettings, long fileId, List<Long> specialitiesIds);
     Page<FileMarkForSpeciality> findByFileIdAndSpecialityIdInAndMarkOrderByFileIdAsc(Pageable pageSettings, long fileId, List<Long> specialitiesIds, double mark);
     Page<FileMarkForSpeciality> findByFileIdAndSpecialityIdInAndMarkGreaterThanOrderByFileIdAsc(Pageable pageSettings, long fileId, List<Long> specialitiesIds, double mark);
     Page<FileMarkForSpeciality> findByFileIdAndSpecialityIdInAndMarkGreaterThanEqualOrderByFileIdAsc(Pageable pageSettings, long fileId, List<Long> specialitiesIds, double mark);
     Page<FileMarkForSpeciality> findByFileIdAndSpecialityIdInAndMarkLessThanOrderByFileIdAsc(Pageable pageSettings, long fileId, List<Long> specialitiesIds, double mark);
     Page<FileMarkForSpeciality> findByFileIdAndSpecialityIdInAndMarkLessThanEqualOrderByFileIdAsc(Pageable pageSettings, long fileId, List<Long> specialitiesIds, double mark);

     Page<FileMarkForSpeciality> findByFileIdOrderByFileIdAsc(Pageable pageSettings, long fileId);
     Page<FileMarkForSpeciality> findByFileIdAndMarkOrderByFileIdAsc(Pageable pageSettings, long fileId, double mark);
     Page<FileMarkForSpeciality> findByFileIdAndMarkGreaterThanOrderByFileIdAsc(Pageable pageSettings, long fileId, double mark);
     Page<FileMarkForSpeciality> findByFileIdAndMarkGreaterThanEqualOrderByFileIdAsc(Pageable pageSettings, long fileId, double mark);
     Page<FileMarkForSpeciality> findByFileIdAndMarkLessThanOrderByFileIdAsc(Pageable pageSettings, long fileId, double mark);
     Page<FileMarkForSpeciality> findByFileIdAndMarkLessThanEqualOrderByFileIdAsc(Pageable pageSettings, long fileId, double mark);

     long countByFileIdAndSpecialityId(long fileId, long specialityId);
     long countByFileIdAndSpecialityIdAndMark(long fileId, long specialityId, double mark);
     long countByFileIdAndSpecialityIdAndMarkGreaterThan(long fileId, long specialityId, double mark);
     long countByFileIdAndSpecialityIdAndMarkGreaterThanEqual(long fileId, long specialityIde, double mark);
     long countByFileIdAndSpecialityIdAndMarkLessThan(long fileId, long specialityIde, double mark);
     long countByFileIdAndSpecialityIdAndMarkLessThanEqual(long fileId, long specialityId, double mark);

     long countByFileId(long fileId);
     long countByFileIdAndMark(long fileId, double mark);
     long countByFileIdAndMarkGreaterThan(long fileId, double mark);
     long countByFileIdAndMarkGreaterThanEqual(long fileId, double mark);
     long countByFileIdAndMarkLessThan(long fileId, double mark);
     long countByFileIdAndMarkLessThanEqual(long fileId, double mark);

     void deleteAllBySpecialityId(long specialityId);

     @Query("SELECT DISTINCT fileId FROM  FileMarkForSpeciality u WHERE u.specialityId = ?1 AND u.mark>0.8 AND u.file.learnExample=true ORDER BY fileId DESC")
     List<Long> findAllLearnExampleBySpeacialityIdAndMaxMark(Pageable pageable, long specialityId);

     @Query("SELECT DISTINCT fileId FROM  FileMarkForSpeciality u WHERE u.specialityId = ?1 AND u.mark>0.8 AND u.file.learnExample=false ORDER BY fileId DESC")
     List<Long> findAllRealResumeBySpeacialityIdAndMaxMark(Pageable pageable, long specialityId);
}
