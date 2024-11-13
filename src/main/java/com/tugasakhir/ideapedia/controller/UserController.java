package com.tugasakhir.ideapedia.controller;

import com.tugasakhir.ideapedia.dto.validasi.ValUserDTO;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    private Map<String, Object> map = new HashMap<>();

    public UserController() {
        initMap();
    }

    public void initMap() {
        map.clear();
        map.put("name", "userName");
        map.put("nip", "nip");
        map.put("mail", "email");
        map.put("hp", "noHp");
    }

    @GetMapping
    public ResponseEntity<Object> getDefault(HttpServletRequest request) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));//ASC
        return userService.findAll(pageable, request);
    }

    /**
     * Usman Save
     */
    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ValUserDTO valUserDTO
            , HttpServletRequest request) {
        return userService.save(userService.convertToEntity(valUserDTO), request);
    }

    /**
     * Usman Update
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long aLong
            , @Valid @RequestBody ValUserDTO valUserDTO
            , HttpServletRequest request) {
        return userService.update(aLong, userService.convertToEntity(valUserDTO), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        return userService.delete(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        return userService.findById(id, request);
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
        return userService.findByParam(pageable, column, value, request);
    }
}