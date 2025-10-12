package org.assignment.resumescreener.controller;

import org.assignment.resumescreener.dto.JobDescriptionDTO;
import org.assignment.resumescreener.dto.ScreenRequestDTO;
import org.assignment.resumescreener.model.JobDescription;
import org.assignment.resumescreener.model.Resume;
import org.assignment.resumescreener.model.ScreeningResult;
import org.assignment.resumescreener.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScreeningController {

    @Autowired
    private ScreeningService screeningService;

    @PostMapping("/jobs")
    public ResponseEntity<JobDescription> createJob(@RequestBody JobDescriptionDTO jobDto) {
        JobDescription savedJob = screeningService.createJob(jobDto);
        return ResponseEntity.ok(savedJob);
    }


    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        screeningService.deleteJobById(jobId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resumes/upload")
    public ResponseEntity<Resume> uploadResume(@RequestParam("name") String candidateName, @RequestParam("file") MultipartFile file) {
        try {
            Resume savedResume = screeningService.uploadAndParseResume(candidateName, file);
            return ResponseEntity.ok(savedResume);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/resumes/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long resumeId) {
        screeningService.deleteResumeById(resumeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDescription>> getAllJobs() {
        return ResponseEntity.ok(screeningService.getAllJobs());
    }

    @GetMapping("/resumes")
    public ResponseEntity<List<Resume>> getAllResumes() {
        return ResponseEntity.ok(screeningService.getAllResumes());
    }

    @PostMapping("/screen")
    public ResponseEntity<ScreeningResult> screenResume(@RequestBody ScreenRequestDTO screenRequest) {
        ScreeningResult result = screeningService.screenResume(screenRequest.getResumeId(), screenRequest.getJobId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/jobs/{jobId}/shortlist")
    public ResponseEntity<List<ScreeningResult>> getShortlist(@PathVariable Long jobId) {
        List<ScreeningResult> shortlist = screeningService.getShortlist(jobId);
        return ResponseEntity.ok(shortlist);
    }
}