package com.tugasakhir.ideapedia.controller;

import com.tugasakhir.ideapedia.model.DetailIdea;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.repo.DetailIdeaRepo;
import com.tugasakhir.ideapedia.repo.IdeaRepo;
import com.tugasakhir.ideapedia.service.DetailIdeaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{ideaId}")
    public ResponseEntity<Object> approve(@PathVariable Long ideaId, HttpServletRequest request) {
        DetailIdea detailIdea = new DetailIdea();

        // Ambil Idea dari database berdasarkan ID
        Optional<Idea> optionalIdea = ideaRepo.findById(ideaId); // Pastikan Anda memiliki repository untuk Idea
        if (optionalIdea.isPresent()) {
            Idea idea = optionalIdea.get();
            detailIdea.setIdea(idea);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Idea dengan ID " + ideaId + " tidak ditemukan.");
        }

        return detailIdeaService.approve(detailIdea, request);
    }
}
