package com.tugasakhir.ideapedia.dto.response;

public class RespUnitKerjaDTO {
    private Long id;
    private String unitName;
    private boolean isAdmin;

    // Getter for id
    public Long getId() {
        return id;
    }

    // Setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter for unitName
    public String getUnitName() {
        return unitName;
    }

    // Setter for unitName
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    // Getter for isAdmin
    public boolean isAdmin() {
        return isAdmin;
    }

    // Setter for isAdmin
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
