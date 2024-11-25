package com.tugasakhir.ideapedia.controller;


import com.tugasakhir.ideapedia.model.Bookmark;
import com.tugasakhir.ideapedia.model.DetailIdea;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.repo.BookmarkRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.repo.UserRepo;
import com.tugasakhir.ideapedia.service.BookmarkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("bookmark")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private BookmarkRepo bookmarkRepo;

    @Autowired
    private IdeaRepo ideaRepo;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/{ideaId}")
    public ResponseEntity<Object> bookmark(@PathVariable Long ideaId, HttpServletRequest request) {
        Bookmark bookmark = new Bookmark();

        // Ambil Idea dari database berdasarkan ID
        Optional<Idea> optionalIdea = ideaRepo.findById(ideaId); // Pastikan Anda memiliki repository untuk Idea
        if (optionalIdea.isPresent()) {
            Idea idea = optionalIdea.get();
            bookmark.setIdea(idea);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Idea dengan ID " + ideaId + " tidak ditemukan.");
        }

        return bookmarkService.bookmark(bookmark, request);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getByUserId(@PathVariable Long userId, HttpServletRequest request) {
        return bookmarkService.findByUserId(userId, request); // Pass request for token extraction
    }

}
