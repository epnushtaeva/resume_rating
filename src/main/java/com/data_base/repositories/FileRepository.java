package com.data_base.repositories;

import com.data_base.entities.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long>,
        JpaSpecificationExecutor<File> {
    List<File> findByIdInOrderByIdAsc(List<Long> ids);

    List<File> findAllByLearnExampleTrue();

    List<File> findAllByLearnExampleFalse();

    List<File> findAllByHrManagerId(long hrManagerId);

    Page<File> findAllByNameContainingIgnoreCaseAndLearnExampleFalse(String fileName, Pageable pageSettings);

    Page<File> findAllByNameContainingIgnoreCaseAndLearnExampleTrue(String fileName, Pageable pageSettings);

    Page<File> findAllByLearnExampleFalse(Pageable pageSettings);

    Page<File> findAllByLearnExampleTrue(Pageable pageSettings);

    List<File> findAllByLearnExampleId(long learnExampleId);

    long countByLearnExampleFalse();

    long countByLearnExampleTrue();

    long countByNameContainingIgnoreCaseAndLearnExampleFalse(String fileName);

    long countByNameContainingIgnoreCaseAndLearnExampleTrue(String fileName);

    long countByNameContainingIgnoreCaseAndIdIn(String fileName,  List<Long> ids);

    long countByIdIn(List<Long> ids);

    Page<File> findAllByNameContainingIgnoreCaseAndIdIn(String fileName, List<Long> ids, Pageable pageSettings);

    Page<File> findAllByIdIn(List<Long> ids, Pageable pageSettings);
}
