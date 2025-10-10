package org.assignment.resumescreener.repository;


import org.assignment.resumescreener.model.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDescriptionRepository extends JpaRepository<JobDescription, Long> {
}
