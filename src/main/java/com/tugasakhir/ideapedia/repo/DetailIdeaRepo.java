package com.tugasakhir.ideapedia.repo;

import com.tugasakhir.ideapedia.model.DetailIdea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailIdeaRepo extends JpaRepository<DetailIdea, Long> {
    // Retrieves the most recently created Detail Idea by ID in descending order
    Optional<DetailIdea> findTopByOrderByIdDesc();

    // Find detail idea by approvedBy ID (sorted using Pageable)
    Page<DetailIdea> findByApprovedBy(Pageable pageable, String value);

    // Find detail idea by approvalDate (sorted using Pageable)
    Page<DetailIdea> findByApprovalDate(Pageable pageable, String value);

    // Find detail idea by idea ID (sorted using Pageable)
    Page<DetailIdea> findByIdeaId(Pageable pageable, String value);

    // Find detail idea by status containing (ignore case)
    Page<DetailIdea> findByStatusContainingIgnoreCase(Pageable pageable, String value);

    Optional<DetailIdea> findByIdeaId(Long ideaId);
}
