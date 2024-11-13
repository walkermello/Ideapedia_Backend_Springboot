package com.tugasakhir.ideapedia.controller;

import com.tugasakhir.ideapedia.dto.validasi.ValIdeaDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValUnitKerjaDTO;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.repo.UnitKerjaRepo;
import com.tugasakhir.ideapedia.service.IdeaService;
import com.tugasakhir.ideapedia.service.UnitKerjaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("idea")
public class IdeaController {
    @Autowired
    private IdeaService ideaService;

    @Autowired
    private IdeaRepo ideaRepo;

    private Map<String, Object> map = new HashMap<>();

    public IdeaController() {
        initMap();
    }

    public void initMap() {
        map.clear();
        map.put("judul", "judul");
        map.put("deskripsi", "deskripsi");
        map.put("file-image", "file-image");
        map.put("file-path", "file-path");

    }

    @PostMapping
    public ResponseEntity<Object> saveFile(
            @RequestParam("judul") String judul, // Menambahkan judul sebagai parameter form-data
            @RequestParam("deskripsi") String deskripsi, // Menambahkan deskripsi sebagai parameter form-data
            @RequestParam("file") MultipartFile file,  // file parameter untuk file .ppt, .pptx, .pdf
            @RequestParam("image") MultipartFile image, // image parameter untuk file gambar .jpg, .jpeg, .png
            HttpServletRequest request) {

        // Membuat objek Idea dari judul dan deskripsi yang diterima
        Idea idea = new Idea();
        idea.setJudul(judul);
        idea.setDeskripsi(deskripsi);

        // Menyimpan file dan image menggunakan service
        return ideaService.saveFile(idea, request, file, image);  // Menggunakan saveFile yang sudah ada
    }

    @GetMapping
    public ResponseEntity<Object> getDefault(HttpServletRequest request) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));//ASC
        return ideaService.findAll(pageable, request);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long id,
            @RequestParam(value = "fileType", defaultValue = "file") String fileType) {

        // Memanggil service untuk mengunduh file berdasarkan ID dan tipe file
        return ideaService.downloadFile(id, fileType, null);
    }
}
