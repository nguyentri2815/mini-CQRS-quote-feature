package com.example.quoteservice.workflow.repository;

import com.example.quoteservice.workflow.entity.WorkflowTaskEntity;
import com.example.quoteservice.workflow.model.WorkflowTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkflowTaskRepository
        extends JpaRepository<WorkflowTaskEntity, String> {

    Optional<WorkflowTaskEntity> findFirstByBusinessIdAndBusinessTypeAndTaskCodeAndStatus(
            String businessId,
            String businessType,
            String taskCode,
            WorkflowTaskStatus status
    );

    boolean existsByBusinessIdAndBusinessTypeAndTaskCodeAndStatus(
            String businessId,
            String businessType,
            String taskCode,
            WorkflowTaskStatus status
    );
}
