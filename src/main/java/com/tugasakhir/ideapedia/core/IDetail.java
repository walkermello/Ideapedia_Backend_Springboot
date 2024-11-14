package com.tugasakhir.ideapedia.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IDetail<T>{
    // Approve idea
    public ResponseEntity<Object> approve(Long id, HttpServletRequest request); // 001-010 Tanpa file

    // Reject idea
    public ResponseEntity<Object> reject(Long id, String comment, HttpServletRequest request); // 001-010 Tanpa file

    // Metode untuk mencari entitas berdasarkan kolom dan nilai dengan pagination
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request);
}
