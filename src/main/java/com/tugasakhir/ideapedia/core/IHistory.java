package com.tugasakhir.ideapedia.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IHistory<T> {
    // Metode untuk menghapus entitas berdasarkan ID
    public ResponseEntity<Object> findByUserId(Long id, HttpServletRequest request);
}
