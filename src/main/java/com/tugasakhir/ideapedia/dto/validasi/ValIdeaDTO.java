package com.tugasakhir.ideapedia.dto.validasi;

import jakarta.validation.constraints.*;

public class ValIdeaDTO {

    private String judul;

    private String deskripsi;

    private Long pengujiPertama;

    private Long pengujiKedua;

    private Long pengujiKetiga;

    private String feedback;

    private String fileName;

    private String filePath;

    private String fileImage;


    // Getter and Setter methods

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
}

