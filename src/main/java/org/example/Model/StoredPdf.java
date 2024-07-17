package org.example.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;

@Entity
@Table(name = "pdf_storage")
public class StoredPdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pdfData;


    public StoredPdf() {
    }

    public StoredPdf(String pdfData) {
        this.pdfData = pdfData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPdfData() {
        return pdfData;
    }

    public void setPdfData(String pdfData) {
        this.pdfData = pdfData;
    }
}

