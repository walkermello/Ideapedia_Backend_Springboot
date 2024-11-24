package com.tugasakhir.ideapedia.dto.validasi;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Date;

public class ValDetailIdeaDTO {

    private Long ideaId;

    private Long approvedById;

    private String status;

    private LocalDateTime approvalDate;

    private LocalDateTime rejectedDate;

    public LocalDateTime getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(LocalDateTime rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    private String comments;

    // Konstruktor tanpa parameter (untuk framework seperti Jackson)
    public ValDetailIdeaDTO() {}

    // Konstruktor yang hanya menerima ideaId
    public ValDetailIdeaDTO(Long ideaId) {
        this.ideaId = ideaId;
    }

    // Getters and Setters
    public Long getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(Long ideaId) {
        this.ideaId = ideaId;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long approvedById) {
        this.approvedById = approvedById;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
