package org.assignment.resumescreener.controller;


import org.assignment.resumescreener.dto.JobDescriptionDTO;
import org.assignment.resumescreener.dto.ScreenRequestDTO;
import org.assignment.resumescreener.service.ResumeParsingService;
import org.assignment.resumescreener.model.JobDescription;
import org.assignment.resumescreener.model.ScreeningResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScreeningController {

    //Injecting service
    @Autowired
    private ResumeParsingService resumeParsingService;

    @PostMapping("/jobs")
    public ResponseEntity<JobDescription> createJob(@RequestBody JobDescriptionDTO jobDto) {
        System.out.println("Received new job description: " + jobDto.getJobTitle());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resumes/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("name") String candidateName, @RequestParam("file") MultipartFile file) {
        try {
            String rawText = resumeParsingService.parseResume(file);

            System.out.println("Parsed resume for: " + candidateName);
            System.out.println("Text length: " + rawText.length());

            return ResponseEntity.ok("Successfully parsed resume. Text length: " + rawText.length());
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/screen")
    public ResponseEntity<ScreeningResult> screenResume(@RequestBody ScreenRequestDTO screenRequest) {
        System.out.println("Received screening request for resume " + screenRequest.getResumeId() + " and job " + screenRequest.getJobId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/jobs/{jobId}/shortlist")
    public ResponseEntity<List<ScreeningResult>> getShortlist(@PathVariable Long jobId) {
        System.out.println("Fetching shortlist for job ID: " + jobId);
        return ResponseEntity.ok(Collections.emptyList());
    }
}
