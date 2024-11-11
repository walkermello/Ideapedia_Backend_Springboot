package com.tugasakhir.ideapedia.repo;

import com.tugasakhir.ideapedia.model.UnitKerja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitKerjaRepo extends JpaRepository<UnitKerja, Long> {
    Optional<UnitKerja> findTopByOrderByIdDesc();

    Page<UnitKerja> findByUnitNameContainingIgnoreCase(Pageable pageable, String value);

    Optional<UnitKerja> findByUnitName(String value);
}
