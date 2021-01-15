package com.data_base.repositories;

import com.data_base.entities.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long>,
        JpaSpecificationExecutor<TaskType> {
}
