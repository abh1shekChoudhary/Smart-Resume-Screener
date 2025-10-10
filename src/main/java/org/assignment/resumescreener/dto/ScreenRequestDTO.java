package org.assignment.resumescreener.dto;

import lombok.Data;

@Data
public class ScreenRequestDTO {
    private Long resumeId;
    private Long jobId;

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobId() {
        return jobId;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }
}