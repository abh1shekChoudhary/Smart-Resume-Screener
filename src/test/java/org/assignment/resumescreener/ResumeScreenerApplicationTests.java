package org.assignment.resumescreener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assignment.resumescreener.dto.JobDescriptionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResumeScreenerApplicationTests {

    @Autowired
    private MockMvc mockMvc; // Injecting the tool for making mock API calls

    @Autowired
    private ObjectMapper objectMapper; // Injecting the tool for converting objects to JSON

    @Test
    void contextLoads() {

    }


    @Test
    void shouldCreateJobSuccessfully() throws Exception {
        // Preparing the data for our request body
        JobDescriptionDTO newJobDto = new JobDescriptionDTO();
        newJobDto.setJobTitle("Test Engineer");
        newJobDto.setContent("A job for testing purposes.");

        // Perform a mock POST request to the /api/jobs endpoint
        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newJobDto)))

                // Expect (verify) the results
                .andExpect(status().isOk()) // We expect a 200 OK status
                .andExpect(jsonPath("$.id").exists()) // We expect the response to have an 'id' field
                .andExpect(jsonPath("$.jobTitle").value("Test Engineer")); // We expect the job title to match
    }

}