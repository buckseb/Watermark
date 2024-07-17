package org.example.Controller;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.ShapeType;
import com.spire.doc.fields.ShapeObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WordController {

    @PostMapping("/api/addMultipleWatermarks")
    public ResponseEntity<InputStreamResource> addMultipleWatermarks(
            @RequestPart("file") MultipartFile file) {

        try {
            // Validate input
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            // Load the uploaded Word document
            Document document = new Document();
            document.loadFromStream(file.getInputStream(), FileFormat.Docx);

            // Add multiple text watermarks
            addMultipleTextWatermarks(document);

            // Prepare the response with the modified document
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.saveToStream(outputStream, FileFormat.Docx);

            ByteArrayInputStream bis = new ByteArrayInputStream(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "document_with_watermarks.docx");

            return new ResponseEntity<>(new InputStreamResource(bis), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception stack trace
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception stack trace
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addMultipleTextWatermarks(Document doc) {
        try {
            Section section;
            Paragraph paragraph;
            List<ShapeObject> shapes = new ArrayList<>();

            // Create a WordArt shape for each watermark
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    ShapeObject shape = new ShapeObject(doc, ShapeType.Text_Plain_Text);
                    shape.setWidth(60);
                    shape.setHeight(20);
                    shape.setVerticalPosition(50 + 150 * i);
                    shape.setHorizontalPosition(20 + 160 * j);
                    shape.setRotation(315);
                    shape.getWordArt().setText("watermark");
                    shape.setFillColor(java.awt.Color.red);
                    shape.setStrokeColor(new java.awt.Color(245, 192, 192, 255));
                    shape.setStrokeWeight(1);
                    shapes.add(shape);
                }
            }

            // Iterate through document sections and add shapes to headers
            for (int n = 0; n < doc.getSections().getCount(); n++) {
                section = doc.getSections().get(n);
                Paragraph headerParagraph;
                section.getPageSetup().setHeaderDistance(0.1f); // Adjust this value as needed


                for (ShapeObject shape : shapes) {
                    headerParagraph = section.getHeadersFooters().getHeader().addParagraph();
                    headerParagraph.getFormat().setAfterSpacing(0);  // No spacing after the paragraph
                    headerParagraph.getFormat().setBeforeSpacing(0); // No spacing before the paragraph
                    headerParagraph.getChildObjects().add(shape);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception stack trace
        }
    }
}
