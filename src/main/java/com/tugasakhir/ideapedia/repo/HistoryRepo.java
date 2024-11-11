package com.tugasakhir.ideapedia.repo;

import com.tugasakhir.ideapedia.model.History;
import com.tugasakhir.ideapedia.model.Idea;
import com.tugasakhir.ideapedia.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepo extends JpaRepository <History, Long> {
    Optional<History> findTopByOrderByIdDesc();

    Page<History> findByActionContainingIgnoreCase(Pageable pageable, String value);

}
