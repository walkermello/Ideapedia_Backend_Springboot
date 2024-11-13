package com.tugasakhir.ideapedia.controller;

import com.tugasakhir.ideapedia.dto.validasi.ValUnitKerjaDTO;
import com.tugasakhir.ideapedia.repo.UnitKerjaRepo;
import com.tugasakhir.ideapedia.service.UnitKerjaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("unit")
public class UnitKerjaController {
    @Autowired
    private UnitKerjaService unitKerjaService;

    @Autowired
    private UnitKerjaRepo unitKerjaRepo;

    private Map<String, Object> map = new HashMap<>();

    public UnitKerjaController() {
        initMap();
    }

    public void initMap() {
        map.clear();
        map.put("unitname", "unitName");
        map.put("isadmin", "isAdmin");
    }

    @GetMapping
    public ResponseEntity<Object> getDefault(HttpServletRequest request) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));//ASC
        return unitKerjaService.findAll(pageable, request);
    }

    /**
     * Usman Save
     */
    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody ValUnitKerjaDTO valUnitKerjaDTO
            , HttpServletRequest request) {
        return unitKerjaService.save(unitKerjaService.convertToEntity(valUnitKerjaDTO), request);
    }

    /**
     * Usman Update
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long aLong
            , @Valid @RequestBody ValUnitKerjaDTO valUnitKerjaDTO
            , HttpServletRequest request) {
        return unitKerjaService.update(aLong, unitKerjaService.convertToEntity(valUnitKerjaDTO), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        return unitKerjaService.delete(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        return unitKerjaService.findById(id, request);
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
        return unitKerjaService.findByParam(pageable, column, value, request);
    }
}
