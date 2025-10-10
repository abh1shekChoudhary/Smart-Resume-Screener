package org.assignment.resumescreener.repository;

import org.assignment.resumescreener.model.ScreeningResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningResultRepository extends JpaRepository<ScreeningResult, Long> {
}
