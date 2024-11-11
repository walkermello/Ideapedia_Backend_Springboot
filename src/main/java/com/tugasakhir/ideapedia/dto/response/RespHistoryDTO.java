package com.tugasakhir.ideapedia.dto.response;

import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class RespHistoryDTO {

    private Long id;

    private User user;

    private Idea idea; // Asumsi Anda memiliki entitas Idea

    private String action; // Contoh nilai: upload, download, bookmark

    private String detailAction; // Opsional

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetailAction() {
        return detailAction;
    }

    public void setDetailAction(String detailAction) {
        this.detailAction = detailAction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    private Long modifiedBy;
}
