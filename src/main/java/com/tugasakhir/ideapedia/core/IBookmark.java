package com.tugasakhir.ideapedia.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IBookmark<T> {

    // Metode untuk menyimpan entitas tanpa file
    public ResponseEntity<Object> bookmark(T t, HttpServletRequest request);

    // Metode untuk menghapus entitas berdasarkan ID
    public ResponseEntity<Object> unbookmark(Long id, HttpServletRequest request);

    // Metode untuk menghapus entitas berdasarkan ID
    public ResponseEntity<Object> findByUserId(Long id, HttpServletRequest request);
}
