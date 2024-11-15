package com.tugasakhir.ideapedia.dto.validasi;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;

public class ValUnitKerjaDTO {


    private Long id;

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
