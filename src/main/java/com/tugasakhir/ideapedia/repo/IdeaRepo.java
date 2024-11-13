package com.tugasakhir.ideapedia.repo;

import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdeaRepo extends JpaRepository<Idea, Long> {
    // Retrieves the most recently created Idea by ID in descending order
    Optional<Idea> findTopByOrderByIdDesc();

    // Finds Idea where the Judul contains the given value, ignoring case
    Page<Idea> findByJudulContainingIgnoreCase(Pageable pageable, String value);

    Page<Idea> findByDeskripsiContainingIgnoreCase(Pageable pageable, String value);

    Page<Idea> findByUserIdOrderByIdDesc(Pageable pageable, Long userId);

}
