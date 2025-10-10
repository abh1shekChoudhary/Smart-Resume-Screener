package org.assignment.resumescreener.service;

import org.assignment.resumescreener.dto.JobDescriptionDTO;
import org.assignment.resumescreener.model.JobDescription;
import org.assignment.resumescreener.model.Resume;
import org.assignment.resumescreener.model.ScreeningResult;
import org.assignment.resumescreener.repository.JobDescriptionRepository;
import org.assignment.resumescreener.repository.ResumeRepository;
import org.assignment.resumescreener.repository.ScreeningResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ScreeningService {

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ScreeningResultRepository screeningResultRepository;

    @Autowired
    private ResumeParsingService resumeParsingService;

    public JobDescription createJob(JobDescriptionDTO jobDto) {
        JobDescription newJob = new JobDescription();
        newJob.setJobTitle(jobDto.getJobTitle());
        newJob.setContent(jobDto.getContent());
        return jobDescriptionRepository.save(newJob);
    }

    public Resume uploadAndParseResume(String candidateName, MultipartFile file) throws IOException {
        String rawText = resumeParsingService.parseResume(file);
        Resume newResume = new Resume();
        newResume.setCandidateName(candidateName);
        newResume.setRawText(rawText);
        return resumeRepository.save(newResume);
    }

    public List<ScreeningResult> getShortlist(Long jobId) {
        return screeningResultRepository.findAll();
    }

    public ScreeningResult screenResume(Long resumeId, Long jobId) {
        System.out.println("Placeholder for screening logic for resumeId: " + resumeId + " and jobId: " + jobId);
        return null;
    }
}