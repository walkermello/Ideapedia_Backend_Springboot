package com.tugasakhir.ideapedia.dto.validasi;

import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import jakarta.validation.constraints.*;


public class ValBookmarkDTO {

//    @NotNull
//    @NotBlank
//    @NotEmpty
    private User user;

//    @NotNull
//    @NotBlank
//    @NotEmpty
    private Idea idea;

//    @NotNull
//    @NotBlank
//    @NotEmpty
    private String createdAt;

    // Getters dan Setters
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {  // Jika menggunakan LocalDateTime, ubah menjadi LocalDateTime
        this.createdAt = createdAt;
    }
}
