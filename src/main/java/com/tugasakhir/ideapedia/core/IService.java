package com.tugasakhir.ideapedia.core;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IService<T> {

    // Metode untuk menyimpan entitas tanpa file
    public ResponseEntity<Object> save(T t, HttpServletRequest request); // 001-010 Tanpa file

    // Metode untuk memperbarui entitas berdasarkan ID
    public ResponseEntity<Object> update(Long id, T t, HttpServletRequest request); // 011-020

    // Metode untuk menghapus entitas berdasarkan ID
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request); // 021-030

    // Metode untuk mendapatkan semua entitas dengan pagination
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request); // 031-040

    // Metode untuk mendapatkan entitas berdasarkan ID
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request); // 041-050

    // Metode untuk mencari entitas berdasarkan kolom dan nilai dengan pagination
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request); // 051-060

}
