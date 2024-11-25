package com.tugasakhir.ideapedia.repo;

import com.tugasakhir.ideapedia.model.Bookmark;
import com.tugasakhir.ideapedia.model.DetailIdea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepo extends JpaRepository<Bookmark, Long> {
    // Retrieves the most recently created Detail Idea by ID in descending order
    Optional<Bookmark> findTopByOrderByIdDesc();

    // Find Bookmark by Idea ID (sorted using Pageable)
    Page<Bookmark> findByIdeaId(Pageable pageable, String value);

    // Find Bookmark by User ID (sorted using Pageable)
    List<Bookmark> findByUserId(Long userId);
}
