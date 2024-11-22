package com.tugasakhir.ideapedia.dto.response;

public class RespUserDTO {
    private Long id;
    private String username;
    private String nip;
    private String email;
    private String password;
    private String noHp;
    private String imgProfile;
    private String status;
    private RespUnitKerjaDTO unitKerja;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for nip
    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for noHp
    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    // Getter and Setter for unitKerja
    public RespUnitKerjaDTO getUnitKerja() {
        return unitKerja;
    }

    public void setUnitKerja(RespUnitKerjaDTO unitKerja) {
        this.unitKerja = unitKerja;
    }
}

