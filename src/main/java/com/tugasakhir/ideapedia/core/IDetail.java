package com.tugasakhir.ideapedia.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IDetail<T>{
    // Metode untuk menyimpan entitas tanpa file
    public ResponseEntity<Object> approve(T t, HttpServletRequest request); // 001-010 Tanpa file
}
