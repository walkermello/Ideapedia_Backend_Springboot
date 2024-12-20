package com.tugasakhir.ideapedia.dto.response;

import java.time.LocalDateTime;

public class RespIdeaDTO {
    private Long id;
    private String judul;
    private String deskripsi;
    private Long pengujiPertama;
    private Long pengujiKedua;
    private Long pengujiKetiga;
    private String feedback;
    private String fileName;
    private String filePath;
    private String fileImage;
    private LocalDateTime createdAt;
    private RespUserDTO user;

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public Long getPengujiPertama() {
        return pengujiPertama;
    }

    public void setPengujiPertama(Long pengujiPertama) {
        this.pengujiPertama = pengujiPertama;
    }

    public Long getPengujiKedua() {
        return pengujiKedua;
    }

    public void setPengujiKedua(Long pengujiKedua) {
        this.pengujiKedua = pengujiKedua;
    }

    public Long getPengujiKetiga() {
        return pengujiKetiga;
    }

    public void setPengujiKetiga(Long pengujiKetiga) {
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

    public RespUserDTO getUser() {
        return user;
    }

    public void setUser(RespUserDTO user) {
        this.user = user;
    }
}
