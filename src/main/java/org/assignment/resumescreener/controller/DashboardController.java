package org.assignment.resumescreener.controller;

import org.assignment.resumescreener.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @Autowired
    private ScreeningService screeningService;

    @GetMapping("/")
    public String getDashboard(Model model, @RequestParam(name = "jobId", required = false) Long jobId) {
        model.addAttribute("jobs", screeningService.getAllJobs());
        model.addAttribute("resumes", screeningService.getAllResumes());

        if (jobId != null) {
            model.addAttribute("shortlist", screeningService.getShortlist(jobId));
        }

        return "dashboard";
    }
}