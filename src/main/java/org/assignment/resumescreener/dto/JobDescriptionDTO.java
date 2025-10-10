package org.assignment.resumescreener.dto;

// import lombok.Data;

public class JobDescriptionDTO {

    private String jobTitle;
    private String content;


    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}