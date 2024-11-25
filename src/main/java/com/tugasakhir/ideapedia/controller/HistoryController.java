package com.tugasakhir.ideapedia.controller;


import com.tugasakhir.ideapedia.repo.HistoryRepo;
import com.tugasakhir.ideapedia.service.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @Autowired
    private HistoryRepo historyRepo;


    @GetMapping("/{userId}")
    public ResponseEntity<Object> getByUserId(@PathVariable Long userId, HttpServletRequest request) {
        return historyService.findByUserId(userId, request); // Pass request for token extraction
    }
}
