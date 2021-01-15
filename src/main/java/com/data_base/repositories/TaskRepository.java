package com.data_base.repositories;

import com.data_base.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {
    Page<Task> findAllByOrderByIdDesc(Pageable pageSettings);

    Page<Task> findAllByTaskDateGreaterThanEqualAndTaskDateLessThanEqualOrderByIdDesc(Date taskDateFrom, Date taskDateTo, Pageable pageSettings);

    Page<Task> findAllByTaskDateGreaterThanEqualOrderByIdDesc(Date taskDateFrom, Pageable pageSettings);

    Page<Task> findAllByTaskDateLessThanEqualOrderByIdDesc(Date taskDateTo, Pageable pageSettings);

    long countByTaskDateGreaterThanEqualAndTaskDateLessThanEqual(Date taskDateFrom, Date taskDateTo);

    long countByTaskDateGreaterThanEqual(Date taskDateFrom);

    long countByTaskDateLessThanEqual(Date taskDateTo);

    List<Task> findAllByTaskDateAndStartTimeBetweenAndStatusIdOrderById(Date taskDate, Time startTimeTo, Time startTimeFrom, long statusId);

    void deleteAllBySpecialityId(long specialityId);
}
