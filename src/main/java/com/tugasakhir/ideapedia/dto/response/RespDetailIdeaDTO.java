package com.tugasakhir.ideapedia.dto.response;

import java.util.Date;

public class RespDetailIdeaDTO {
    private Long id;
    private String status;
    private Date approvalDate;
    private String comments;
    private RespIdeaDTO idea;
    private RespUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public RespIdeaDTO getIdea() {
        return idea;
    }

    public void setIdea(RespIdeaDTO idea) {
        this.idea = idea;
    }

    public RespUserDTO getUser() {
        return user;
    }

    public void setUser(RespUserDTO user) {
        this.user = user;
    }
}
