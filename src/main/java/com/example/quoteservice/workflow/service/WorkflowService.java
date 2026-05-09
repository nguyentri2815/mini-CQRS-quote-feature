package com.example.quoteservice.workflow.service;

import com.example.quoteservice.common.exception.BusinessException;
import com.example.quoteservice.workflow.entity.WorkflowTaskEntity;
import com.example.quoteservice.workflow.model.BusinessType;
import com.example.quoteservice.workflow.model.WorkflowTaskCode;
import com.example.quoteservice.workflow.model.WorkflowTaskStatus;
import com.example.quoteservice.workflow.repository.WorkflowTaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WorkflowService {

    private final WorkflowTaskRepository workflowTaskRepository;

    public WorkflowService(WorkflowTaskRepository workflowTaskRepository) {
        this.workflowTaskRepository = workflowTaskRepository;
    }

    public void createApproveQuoteTask(String quoteId) {
        boolean hasOpenTask = workflowTaskRepository
                .existsByBusinessIdAndBusinessTypeAndTaskCodeAndStatus(
                        quoteId,
                        BusinessType.QUOTE,
                        WorkflowTaskCode.APPROVE_QUOTE,
                        WorkflowTaskStatus.OPEN
                );

        if (hasOpenTask) {
            throw new BusinessException("Approve task already exists for quote: " + quoteId);
        }

        WorkflowTaskEntity task = WorkflowTaskEntity.builder()
                .id(UUID.randomUUID().toString())
                .businessId(quoteId)
                .businessType(BusinessType.QUOTE)
                .taskCode(WorkflowTaskCode.APPROVE_QUOTE)
                .status(WorkflowTaskStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        workflowTaskRepository.save(task);
    }

    public void completeApproveQuoteTask(String quoteId, String approver) {
        WorkflowTaskEntity task = workflowTaskRepository
                .findFirstByBusinessIdAndBusinessTypeAndTaskCodeAndStatus(
                        quoteId,
                        BusinessType.QUOTE,
                        WorkflowTaskCode.APPROVE_QUOTE,
                        WorkflowTaskStatus.OPEN
                )
                .orElseThrow(() -> new BusinessException(
                        "No open approve task for quote: " + quoteId
                ));

        task.setStatus(WorkflowTaskStatus.COMPLETED);
        task.setAssignee(approver);
        task.setCompletedAt(LocalDateTime.now());

        workflowTaskRepository.save(task);
    }

    public boolean hasOpenApproveQuoteTask(String quoteId) {
        return workflowTaskRepository
                .existsByBusinessIdAndBusinessTypeAndTaskCodeAndStatus(
                        quoteId,
                        BusinessType.QUOTE,
                        WorkflowTaskCode.APPROVE_QUOTE,
                        WorkflowTaskStatus.OPEN
                );
    }
}
