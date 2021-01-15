package com.data_base.repositories;

import com.data_base.entities.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long>,
        JpaSpecificationExecutor<Speciality> {
    long count();

    List<Speciality> findAllByOrderByIdAsc();

    Speciality findOneByName(String name);

    List<Speciality> findAllByNameContainingIgnoreCase(String name);

    Page<Speciality> findByNameContainingIgnoreCaseOrderByIdAsc(Pageable pageSettings, String specialityName);

    Page<Speciality> findAllByOrderByIdAsc(Pageable pageSettings);

    long countByNameContainingIgnoreCase(String specialityName);
}
