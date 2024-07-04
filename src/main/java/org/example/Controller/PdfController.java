package org.example.Controller;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class PdfController {

    @PostMapping("/upload")
    public ResponseEntity<byte[]> uploadAndWatermarkPdf(@RequestParam("file") MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());

        for (PDPage page : document.getPages()) {
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20); // Smaller font size
            contentStream.setNonStrokingColor(220, 220, 220); // light gray color

            // Adding multiple tilted watermarks on the page
            for (int x = 100; x < page.getMediaBox().getWidth(); x += 200) {
                for (int y = 100; y < page.getMediaBox().getHeight(); y += 200) {
                    contentStream.beginText();
                    // Rotate the text and set the position
                    contentStream.setTextMatrix(org.apache.pdfbox.util.Matrix.getRotateInstance(Math.toRadians(45), x, y));
                    contentStream.showText("WATERMARK");
                    contentStream.endText();
                }
            }

            contentStream.close();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "watermarked.pdf");

        return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
    }
}
