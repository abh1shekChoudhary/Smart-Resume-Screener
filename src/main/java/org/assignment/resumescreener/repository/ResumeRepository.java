package org.assignment.resumescreener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.assignment.resumescreener.model.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}