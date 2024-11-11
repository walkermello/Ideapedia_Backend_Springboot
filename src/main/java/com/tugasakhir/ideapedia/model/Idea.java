package com.tugasakhir.ideapedia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MstIdea")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Judul")
    private String judul;

    @Column(name = "Deskripsi")
    private String deskripsi;

    @Column(name = "PengujiPertama")
    private String pengujiPertama;

    @Column(name = "PengujiKedua")
    private String pengujiKedua;

    @Column(name = "PengujiKetiga")
    private String pengujiKetiga;

    @Column(name = "Feedback")
    private String feedback;

    @Column(name = "FileName")
    private String fileName;

    @Column(name = "FilePath")
    private String filePath;

    @Column(name = "FileImage")
    private String fileImage;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "ModifiedAt")
    private LocalDateTime modifiedAt;

    @Column(name = "ModifiedBy", columnDefinition = "BIGINT")
    private Long modifiedBy;

    @ManyToOne
    @JoinColumn(name = "IDUser", nullable = false)
    private User user;

    // Getter and Setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getPengujiPertama() {
        return pengujiPertama;
    }

    public void setPengujiPertama(String pengujiPertama) {
        this.pengujiPertama = pengujiPertama;
    }

    public String getPengujiKedua() {
        return pengujiKedua;
    }

    public void setPengujiKedua(String pengujiKedua) {
        this.pengujiKedua = pengujiKedua;
    }

    public String getPengujiKetiga() {
        return pengujiKetiga;
    }

    public void setPengujiKetiga(String pengujiKetiga) {
        this.pengujiKetiga = pengujiKetiga;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileImage() {
        return fileImage;
    }

    public void setFileImage(String fileImage) {
        this.fileImage = fileImage;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
