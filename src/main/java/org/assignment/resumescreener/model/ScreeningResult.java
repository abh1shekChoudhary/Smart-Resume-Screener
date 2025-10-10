package org.assignment.resumescreener.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ScreeningResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "job_description_id")
    private JobDescription jobDescription;

    private Integer matchScore;

    @Column(columnDefinition = "TEXT")
    private String justification;

    @Column(columnDefinition = "TEXT")
    private String extractedSkills;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public JobDescription getJobDescription() {
        return jobDescription;
    }

    public Resume getResume() {
        return resume;
    }

    public String getJustification() {
        return justification;
    }

    public void setJobDescription(JobDescription jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getExtractedSkills() {
        return extractedSkills;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public void setExtractedSkills(String extractedSkills) {
        this.extractedSkills = extractedSkills;
    }
}
