package org.example.Service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class WordWatermarkService {

    public ResponseEntity<byte[]> addWatermark(MultipartFile file) throws IOException {
        XWPFDocument document = new XWPFDocument(file.getInputStream());

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            addWatermarkToParagraph(paragraph);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "watermarked.docx");

        return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
    }

    private void addWatermarkToParagraph(XWPFParagraph paragraph) {
        XWPFRun run = paragraph.createRun();
        run.setText("WATERMARK");
        run.setColor("DCDCDC"); // Lighter gray color
        run.setFontSize(10); // Smaller font size
        run.setTextPosition(20);
    }
}
