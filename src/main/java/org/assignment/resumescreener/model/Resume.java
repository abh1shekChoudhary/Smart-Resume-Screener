package org.assignment.resumescreener.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String candidateName;

    @Column(columnDefinition = "TEXT")
    private String rawText;
}