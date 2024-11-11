package com.tugasakhir.ideapedia.dto.validasi;

import jakarta.validation.constraints.*;

import java.util.Date;

public class ValDetailIdeaDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    private Long ideaId;

    @NotNull
    @NotBlank
    @NotEmpty
    private Long approvedById;

    @NotNull
    @NotBlank
    @NotEmpty
    private String status;

    @NotNull
    @NotBlank
    @NotEmpty
    private Date approvalDate;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(max = 255, message = "Komentar maksimal 255 karakter")
    private String comments;

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

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
