package com.example.quoteservice.workflow.entity;

import com.example.quoteservice.workflow.model.WorkflowTaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowTaskEntity {

    @Id
    private String id;

    @Column(name = "business_id", nullable = false)
    private String businessId;

    @Column(name = "business_type", nullable = false)
    private String businessType;

    @Column(name = "task_code", nullable = false)
    private String taskCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WorkflowTaskStatus status;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
