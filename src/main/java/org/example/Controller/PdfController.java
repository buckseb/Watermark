package org.example.Controller;

import org.example.Model.StoredPdf;
import org.example.Service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/uploadAndSavePDF")
    public ResponseEntity<String> uploadAndSavePDF(
            @RequestParam("file") MultipartFile file,
            @RequestParam("watermarkText") String watermarkText) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        try {
            pdfService.uploadAndSavePDF(file, watermarkText);
            return ResponseEntity.ok().body("Watermarked PDF saved to MySQL database successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading and saving PDF: " + e.getMessage());
        }
    }

    @GetMapping("/downloadPDF/{id}")
    public ResponseEntity<InputStreamResource> downloadPDF(@PathVariable("id") Long id) {
        StoredPdf storedPdf = pdfService.findById(id);
        if (storedPdf == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdfBytes = Base64.getDecoder().decode(storedPdf.getPdfData());
        ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
