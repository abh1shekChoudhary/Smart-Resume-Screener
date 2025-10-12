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
    private LLMService llmService;

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
        return screeningResultRepository.findAll().stream()
                .filter(result -> result.getJobDescription().getId().equals(jobId))
                .toList();
    }

    public List<JobDescription> getAllJobs() {
        return jobDescriptionRepository.findAll();
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }


    public void deleteJobById(Long jobId) {
        List<ScreeningResult> relatedResults = screeningResultRepository.findAll().stream()
                .filter(result -> result.getJobDescription() != null && result.getJobDescription().getId().equals(jobId))
                .toList();
        screeningResultRepository.deleteAll(relatedResults);
        jobDescriptionRepository.deleteById(jobId);
    }
    public void deleteResumeById(Long resumeId) {
        // We should also delete related screening results
        List<ScreeningResult> relatedResults = screeningResultRepository.findAll().stream()
                .filter(result -> result.getResume() != null && result.getResume().getId().equals(resumeId))
                .toList();
        screeningResultRepository.deleteAll(relatedResults);
        resumeRepository.deleteById(resumeId);
    }

    public ScreeningResult screenResume(Long resumeId, Long jobId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        JobDescription job = jobDescriptionRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        ScreeningResult result = llmService.scoreResume(resume.getRawText(), job.getContent());

        result.setResume(resume);
        result.setJobDescription(job);

        return screeningResultRepository.save(result);
    }
}