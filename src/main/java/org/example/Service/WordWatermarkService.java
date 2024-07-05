package org.example.Service;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class WordWatermarkService {

    public byte[] addWatermarks(byte[] fileBytes) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
             XWPFDocument document = new XWPFDocument(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                XWPFRun run = paragraph.createRun();
                run.setText("WATERMARK");
                run.setFontSize(20);
                run.setColor("DCDCDC"); // light gray color

                for (int x = 100; x < 500; x += 200) {
                    for (int y = 100; y < 500; y += 200) {
                        run.setTextPosition(x); // Position of the text
                        run.setTextPosition(y); // Position of the text
                        run.setText("WATERMARK");
                    }
                }
            }

            document.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error processing the Word document", e);
        }
    }
}
