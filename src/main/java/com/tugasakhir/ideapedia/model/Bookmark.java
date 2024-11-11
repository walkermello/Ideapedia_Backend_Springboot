package com.tugasakhir.ideapedia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MstBookmark")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "IDIdea")
    private Idea idea;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    // Constructors
    public Bookmark() {
    }

    public Bookmark(User user, Idea idea, LocalDateTime createdAt, String createdBy) {
        this.user = user;
        this.idea = idea;
        this.createdAt = createdAt;
    }

    // Getters and Setters
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
