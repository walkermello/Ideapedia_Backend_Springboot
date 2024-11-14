package com.tugasakhir.ideapedia.core;

import com.tugasakhir.ideapedia.dto.validasi.ValIdeaDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IDetail<T>{
    // Approve idea dengan feedback dan penguji yang diambil dari request body
    ResponseEntity<Object> approve(Long id, ValIdeaDTO valIdeaDTO, HttpServletRequest request); // 001-010 Dengan feedback dan penguji

    // Reject idea
    public ResponseEntity<Object> reject(Long id, String comment, HttpServletRequest request); // 001-010 Tanpa file

    // Metode untuk mencari entitas berdasarkan kolom dan nilai dengan pagination
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request);
}
