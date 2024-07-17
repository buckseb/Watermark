package org.example.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.Dao.PdfRepository;
import org.example.Model.StoredPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class PdfService {

    @Autowired
    private PdfRepository pdfRepository;


    public StoredPdf findById(Long id) {
        return pdfRepository.findById(id).orElse(null);
    }

    public StoredPdf uploadAndSavePDF(MultipartFile file, String watermarkText) throws IOException {
        byte[] watermarkedPDFBytes = generateWatermarkedPDF(file, watermarkText);
        String base64EncodedPDF = Base64.getEncoder().encodeToString(watermarkedPDFBytes);
        StoredPdf storedPdf = new StoredPdf(base64EncodedPDF);
        return pdfRepository.save(storedPdf);
    }

    private byte[] generateWatermarkedPDF(MultipartFile file, String watermarkText) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());

        for (PDPage page : document.getPages()) {
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20); // Smaller font size
            contentStream.setNonStrokingColor(220, 220, 220); // light gray color

            for (int x = 100; x < page.getMediaBox().getWidth(); x += 200) {
                for (int y = 100; y < page.getMediaBox().getHeight(); y += 200) {
                    contentStream.beginText();
                    contentStream.setTextMatrix(org.apache.pdfbox.util.Matrix.getRotateInstance(Math.toRadians(45), x, y));
                    contentStream.showText(watermarkText); // Use the provided watermark text
                    contentStream.endText();
                }
            }

            contentStream.close();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }
}

