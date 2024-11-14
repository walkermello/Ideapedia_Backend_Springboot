package com.tugasakhir.ideapedia.controller;

import com.tugasakhir.ideapedia.dto.validasi.ValDetailIdeaDTO;
import com.tugasakhir.ideapedia.dto.validasi.ValIdeaDTO;
import com.tugasakhir.ideapedia.model.DetailIdea;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.repo.DetailIdeaRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.service.DetailIdeaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("detail")
public class DetailIdeaController {

    @Autowired
    private DetailIdeaService detailIdeaService;

    @Autowired
    private DetailIdeaRepo detailIdeaRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    private Map<String, Object> map = new HashMap<>();

    public DetailIdeaController() {
        initMap();
    }

    public void initMap() {
        map.clear();
        map.put("status", "status");
        map.put("approvalDate", "approvalDate");
        map.put("feedback", "feedback");
        map.put("pengujiSatu", "pengujiSatu");
        map.put("pengujiDua", "pengujiDua");
        map.put("PengujiTiga", "pengujiTiga");
    }

    // Controller untuk approve ide dengan feedback dan penguji
    @PutMapping("/approve/{id}")
    public ResponseEntity<Object> approve(
            @PathVariable(value = "id") Long id,  // Mengambil id dari URL
            @RequestBody ValIdeaDTO valIdeaDTO,  // Menerima body request untuk feedback dan penguji
            HttpServletRequest request) {

        // Memanggil approve method dari service dengan id dan request
        return detailIdeaService.approve(id, valIdeaDTO, request);
    }

    // Controller untuk reject ide dengan komentar
    @PutMapping("/reject/{id}")
    public ResponseEntity<Object> reject(
            @PathVariable(value = "id") Long id,  // Mengambil id dari URL
            @RequestBody ValDetailIdeaDTO valDetailIdeaDTO,  // Menerima comment dari request body
            HttpServletRequest request) {

        // Memanggil reject method dari service dengan id dan komentar
        return detailIdeaService.reject(id, valDetailIdeaDTO.getComments(), request);
    }

    // jgn di taruh roll
    @GetMapping("/{page}/{sort}/{sort-by}")
    public ResponseEntity<Object> findByParam(
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sort-by") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "col") String column,
            @RequestParam(value = "val") String value,
            HttpServletRequest request
    ) {
        Pageable pageable = null;
        sortBy = map.get(sortBy) == null ? "id" : map.get(sortBy).toString();
        column = map.get(column) == null ? "id" : map.get(column).toString();
        if ("asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));//ASC
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());//DESC
        }
        return detailIdeaService.findByParam(pageable, column, value, request);
    }
}
