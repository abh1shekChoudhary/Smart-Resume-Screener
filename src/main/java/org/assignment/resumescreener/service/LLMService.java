package org.assignment.resumescreener.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assignment.resumescreener.model.ScreeningResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LLMService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScreeningResult scoreResume(String resumeText, String jobDescText) {
        String prompt = "Compare the following resume with this job description and rate the fit on a scale of 1-10, with a detailed justification for your rating. The resume is: \n\n " + resumeText + " \n\n The job description is: \n\n " + jobDescText + ". Return your answer as a clean JSON object with two keys: 'score' (an integer) and 'justification' (a string).";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String fullApiUrl = apiUrl + "?key=" + apiKey;

        try {
            // Building the request body using Java objects to safely handle special characters
            Map<String, Object> textPart = Map.of("text", prompt);
            Map<String, Object> parts = Map.of("parts", List.of(textPart));
            Map<String, Object> requestBodyMap = Map.of("contents", List.of(parts));

            // Converting the map to a JSON string using ObjectMapper
            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // The API call
            String response = restTemplate.postForObject(fullApiUrl, entity, String.class);

            // Parse the top-level response from Gemini
            JsonNode root = objectMapper.readTree(response);
            String llmResponseText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            // Clean the response from the LLM to remove markdown backticks
            String cleanedJson = llmResponseText.trim().replace("```json", "").replace("```", "");

            // Parse the cleaned, inner JSON string
            JsonNode innerJson = objectMapper.readTree(cleanedJson);
            // Create and populate the result object
            ScreeningResult result = new ScreeningResult();
            result.setMatchScore(innerJson.path("score").asInt());
            result.setJustification(innerJson.path("justification").asText());

            return result;
        } catch (Exception e) {
            System.err.println("Error calling LLM API: " + e.getMessage());
            ScreeningResult errorResult = new ScreeningResult();
            errorResult.setMatchScore(0);
            errorResult.setJustification("Error processing request: " + e.getMessage());
            return errorResult;
        }
    }
}