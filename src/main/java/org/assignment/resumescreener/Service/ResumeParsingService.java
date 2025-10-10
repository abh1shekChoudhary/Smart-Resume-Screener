package org.assignment.resumescreener.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeParsingService {

    /**
     * Parses the uploaded resume file and extracts its text content.
     * Supports both PDF (.pdf) and plain text (.txt) files.
     *
     * @param file The uploaded file.
     * @return The extracted text content as a single String.
     * @throws IOException if there is an error reading the file.
     * @throws IllegalArgumentException if the file type is not supported.
     */
    public String parseResume(MultipartFile file) throws IOException {
        String contentType = file.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException("Could not determine file type.");
        }
        if (contentType.equals("application/pdf")) {
            // Using Apache PDFBox to extract text from the PDF
            try (PDDocument document = PDDocument.load(file.getInputStream())) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        } else if (contentType.equals("text/plain")) {
            // Read text directly from the .txt file
            return new String(file.getBytes());
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + contentType + ". Please upload a PDF or TXT file.");
        }
    }
}