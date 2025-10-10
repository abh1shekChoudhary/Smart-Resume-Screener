package org.assignment.resumescreener.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assignment.resumescreener.model.ScreeningResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LLMService {

    private static final Logger logger = LoggerFactory.getLogger(LLMService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ScreeningResult scoreResume(String resumeText, String jobDescText) {
        try {
            logger.info("Starting AI screening process...");

            String prompt = buildPrompt(resumeText, jobDescText);
            String requestBody = buildRequestBody(prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String fullApiUrl = apiUrl + "?key=" + apiKey;
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            logger.info("Sending request to Gemini API...");
            String response = restTemplate.postForObject(fullApiUrl, entity, String.class);

            return parseLLMResponse(response);

        } catch (RestClientException e) {
            logger.error("Error calling Gemini API (RestClientException): {}", e.getMessage());
            return createErrorResult("Network or API endpoint error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON from Gemini API (JsonProcessingException): {}", e.getMessage());
            return createErrorResult("Failed to parse AI response: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred in LLMService", e);
            return createErrorResult("An unexpected error occurred: " + e.getMessage());
        }
    }

    private String buildPrompt(String resumeText, String jobDescText) {
        return "Compare the following resume with this job description. Your task is to: \n 1. Rate the fit on a scale of 1-10. \n 2. Provide a detailed justification for your rating. \n 3. Extract a list of the top 5-7 most relevant skills from the resume that match the job description. \n\n The resume is: \n\n " + resumeText + " \n\n The job description is: \n\n " + jobDescText + ". \n\n Return your answer as a clean JSON object with three keys: 'score' (an integer), 'justification' (a string), and 'skills' (an array of strings).";
    }

    private String buildRequestBody(String prompt) throws JsonProcessingException {
        Map<String, Object> generationConfig = Map.of(
                "temperature", 0.7,
                "topP", 0.95,
                "topK", 64,
                "maxOutputTokens", 8192
        );

        List<Map<String, Object>> safetySettings = List.of(
                Map.of("category", "HARM_CATEGORY_HARASSMENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_HATE_SPEECH", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_SEXUALLY_EXPLICIT", "threshold", "BLOCK_MEDIUM_AND_ABOVE"),
                Map.of("category", "HARM_CATEGORY_DANGEROUS_CONTENT", "threshold", "BLOCK_MEDIUM_AND_ABOVE")
        );

        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> parts = Map.of("parts", List.of(textPart));

        Map<String, Object> requestBodyMap = Map.of(
                "contents", List.of(parts),
                "generationConfig", generationConfig,
                "safetySettings", safetySettings
        );

        return objectMapper.writeValueAsString(requestBodyMap);
    }

    private ScreeningResult parseLLMResponse(String response) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(response);
        String llmResponseText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        String cleanedJson = llmResponseText.trim().replace("```json", "").replace("```", "");
        JsonNode innerJson = objectMapper.readTree(cleanedJson);

        ScreeningResult result = new ScreeningResult();
        //Parsing the result
        result.setMatchScore(innerJson.path("score").asInt());
        result.setJustification(innerJson.path("justification").asText());
        result.setExtractedSkills(innerJson.path("skills").toString());

        logger.info("Successfully parsed AI response. Score: {}", result.getMatchScore());
        return result;
    }

    private ScreeningResult createErrorResult(String errorMessage) {
        ScreeningResult errorResult = new ScreeningResult();
        errorResult.setMatchScore(0);
        errorResult.setJustification("Error processing request: " + errorMessage);
        return errorResult;
    }
}