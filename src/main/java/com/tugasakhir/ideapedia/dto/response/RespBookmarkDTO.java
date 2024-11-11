package com.tugasakhir.ideapedia.dto.response;

import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;

import java.time.LocalDateTime;

public class RespBookmarkDTO {

    private Long id;

    private User user;

    private Idea idea;

    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
