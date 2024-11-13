package com.tugasakhir.ideapedia.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IDetail<T>{
    // Metode untuk menyimpan entitas tanpa file
    public ResponseEntity<Object> approve(T t, HttpServletRequest request); // 001-010 Tanpa file
    // Metode untuk mencari entitas berdasarkan kolom dan nilai dengan pagination
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request);
}
