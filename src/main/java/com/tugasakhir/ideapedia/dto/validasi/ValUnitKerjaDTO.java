package com.tugasakhir.ideapedia.dto.validasi;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

public class ValUnitKerjaDTO {


    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    //@Size(max = 100, message = "Unit name must not exceed 100 characters")
    private String unitName;

    private boolean isAdmin;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }
}
