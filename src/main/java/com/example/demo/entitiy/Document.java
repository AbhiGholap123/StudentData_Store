package com.example.demo.entitiy;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "documents1")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String documentType;
    private LocalDate documentIssueDate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] documentFrontImage;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] documentBackImage;

    
    public Document() {
        this.documentIssueDate = LocalDate.now(); // Saves the current date
    }

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] documentVideo;
    
    
    // Getters & Setters
    
    
    public Long getId() {
        return id;
    }

    public byte[] getDocumentVideo() {
		return documentVideo;
	}

	public void setDocumentVideo(byte[] documentVideo) {
		this.documentVideo = documentVideo;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public byte[] getDocumentFrontImage() {
        return documentFrontImage;
    }

    public void setDocumentFrontImage(byte[] documentFrontImage) {
        this.documentFrontImage = documentFrontImage;
    }

    public byte[] getDocumentBackImage() {
        return documentBackImage;
    }

    public void setDocumentBackImage(byte[] documentBackImage) {
        this.documentBackImage = documentBackImage;
    }

	public LocalDate getDocumentIssueDate() {
		return documentIssueDate;
	}

	public void setDocumentIssueDate(LocalDate documentIssueDate) {
		this.documentIssueDate = documentIssueDate;
	}
    
    
	
	
	
    
    // Getters and Setters
}
