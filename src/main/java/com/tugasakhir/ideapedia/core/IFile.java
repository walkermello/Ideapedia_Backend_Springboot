package com.tugasakhir.ideapedia.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IFile<G> {

      public ResponseEntity<Object> saveFile(G g, HttpServletRequest request, MultipartFile file, MultipartFile image);

      public ResponseEntity<Object> hideIdea(Long id, HttpServletRequest request);

      public ResponseEntity<Object> unhideIdea(Long id, HttpServletRequest request);
      // Metode untuk mendapatkan semua entitas dengan pagination
      public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request);

      // Metode untuk mencari entitas berdasarkan kolom dan nilai dengan pagination
      public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request);

//    public ResponseEntity<Object> uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request);//061-070
//    public List<G> convertListWorkBookToListEntity(List<Map<String, String>> workBookData, Long userId);//071-080
//    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response);//081-090
//    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response);//091-100
}

