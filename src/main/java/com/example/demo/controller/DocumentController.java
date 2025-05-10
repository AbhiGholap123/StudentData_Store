package com.example.demo.controller;

import com.example.demo.entitiy.Document;
import com.example.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents") 
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping( consumes = "multipart/form-data")
    public Document uploadDocument(
            @RequestParam("userId") Long userId,
            @RequestParam("documentType") String documentType,
            @RequestParam("documentIssueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate documentIssueDate,  // ✅ Fix: Accepting date
            @RequestParam("documentFrontImage") MultipartFile documentFrontImage,
            @RequestParam("documentBackImage") MultipartFile documentBackImage,
            @RequestParam("documentVideo") MultipartFile documentVideo // ✅ Added video parameter
            
    ) throws IOException {
    	
    	// ✅ Check if the uploaded files are JPG/JPEG
        if (!isJpgFile(documentFrontImage) || !isJpgFile(documentBackImage)) {
            throw new IllegalArgumentException("Only JPG images are allowed!");
        }
        
        if (!isMp4File(documentVideo)) {
            throw new IllegalArgumentException("Only MP4 videos are allowed!");
        }
        
        Document document = new Document();
        document.setUserId(userId);
        document.setDocumentType(documentType);
        document.setDocumentIssueDate(documentIssueDate);  // ✅ Fix: Setting date explicitly

        
        // Convert MultipartFile to byte[]
        document.setDocumentFrontImage(documentFrontImage.getBytes());
        document.setDocumentBackImage(documentBackImage.getBytes());
        document.setDocumentVideo(documentVideo.getBytes()); // ✅ Save video in the database
        
        return documentService.saveDocument(document);
    }

    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public Optional<Document> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id);
    }
    
    // ✅ PUT method to update document details (including images)
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public Document updateDocument(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            @RequestParam("documentType") String documentType,
            @RequestParam("documentIssueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate documentIssueDate,  // ✅ Fix: Accepting date in update
            @RequestParam(value = "documentFrontImage", required = false) MultipartFile documentFrontImage,
            @RequestParam(value = "documentBackImage", required = false) MultipartFile documentBackImage,
            @RequestParam(value = "documentVideo", required = false) MultipartFile documentVideo // ✅ Added optional video update
    ) throws IOException {
        Optional<Document> existingDocument = documentService.getDocumentById(id);
        if (existingDocument.isPresent()) {
            Document document = existingDocument.get();
            document.setUserId(userId);
            document.setDocumentType(documentType);
            document.setDocumentIssueDate(documentIssueDate);  // ✅ Fix: Setting date explicitly in update

            if (documentFrontImage != null && !documentFrontImage.isEmpty()) {
                if (!isJpgFile(documentFrontImage)) {
                    throw new IllegalArgumentException("Only JPG images are allowed!");
                }
                document.setDocumentFrontImage(documentFrontImage.getBytes());
            }

            if (documentBackImage != null && !documentBackImage.isEmpty()) {
                if (!isJpgFile(documentBackImage)) {
                    throw new IllegalArgumentException("Only JPG images are allowed!");
                }
                document.setDocumentBackImage(documentBackImage.getBytes());
            }
            
            // ✅ Update video if provided
            if (documentVideo != null && !documentVideo.isEmpty()) {
                if (!isMp4File(documentVideo)) {
                    throw new IllegalArgumentException("Only MP4 videos are allowed!");
                }
                document.setDocumentVideo(documentVideo.getBytes());
            }

            return documentService.saveDocument(document);
        } else {
            throw new RuntimeException("Document not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
    }
    
    private boolean isJpgFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/jpg"));
    }
    
    // ✅ Method to check if the file is an MP4 video
    private boolean isMp4File(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("video/mp4");
    }
}
