package com.tugasakhir.ideapedia.dto.validasi;

import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ValHistoryDTO {

    private User user;

    private Idea idea;

    private String action;

    private String detailAction;

    private LocalDateTime createdAt;

    // Getters and Setters
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
}
