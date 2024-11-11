package com.tugasakhir.ideapedia.dto.validasi;

import jakarta.validation.constraints.*;

public class ValLoginDTO {
    @NotNull
    @NotEmpty
    @NotBlank
    private String username;

    @NotBlank
    @NotEmpty
    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
