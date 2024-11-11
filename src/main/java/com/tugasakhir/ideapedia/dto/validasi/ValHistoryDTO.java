package com.tugasakhir.ideapedia.dto.validasi;

import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ValHistoryDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    private User user;

    @NotNull
    @NotBlank
    @NotEmpty
    private Idea idea;

    @NotBlank
    @NotBlank
    @NotEmpty
    @Size(max = 50, message = "Action tidak boleh lebih dari 50 karakter")
    private String action;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(max = 255, message = "DetailAction tidak boleh lebih dari 255 karakter")
    private String detailAction;

    @NotNull
    @NotBlank
    @NotEmpty
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
