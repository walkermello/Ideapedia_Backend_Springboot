package com.tugasakhir.ideapedia.dto.validasi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tugasakhir.ideapedia.model.UnitKerja;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Pattern;

public class ValUserDTO {

    //@Pattern(regexp = "^([a-z\\.]{8,16})$",message = "Format Username: Huruf kecil dan numeric saja, min 8 max 16 karakter, contoh: paulch123")
    private String username;

    //@Pattern(regexp = "^[0-9]{18}$",message = "Format NIP tidak valid, harus 18 digit angka.")
    private String nip;

    private String email;

    //@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[_#\\-$])[\\w].{8,15}$",message = "Password harus memenuhi syarat: minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", atau $ \"Dollar\"). Panjang 8-15 karakter.")
    private String password;

    //@Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,13}$",message = "Format No HP Tidak Valid, min 9 max 13 digit setelah angka 8, contoh: (0/62/+62)81111111")
    @JsonProperty("no-hp")
    private String noHp;

    private ValUnitKerjaDTO unitKerja;

    private String imgProfile;


    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public @NotNull(message = "ID Unit Kerja tidak boleh kosong.") ValUnitKerjaDTO getUnitKerja() {
        return unitKerja;
    }

    public void setUnitKerja(@NotNull(message = "ID Unit Kerja tidak boleh kosong.") ValUnitKerjaDTO unitKerja) {
        this.unitKerja = unitKerja;
    }
}
