package com.tugasakhir.ideapedia.dto.validasi;

import jakarta.validation.constraints.*;

public class ValIdeaDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 5, max = 100, message = "Judul harus antara 5 dan 100 karakter.")
    private String judul;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 10, max = 1000, message = "Deskripsi harus antara 10 dan 1000 karakter.")
    private String deskripsi;

    private String pengujiPertama;

    private String pengujiKedua;

    private String pengujiKetiga;

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
}

