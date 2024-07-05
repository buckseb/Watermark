package org.example.Controller;

import org.example.Service.WordWatermarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class WordController {

    @Autowired
    private WordWatermarkService wordWatermarkService;

    @PostMapping("/uploadWord")
    public ResponseEntity<byte[]> uploadAndWatermarkWord(@RequestParam("file") MultipartFile file) {
        try {
            byte[] watermarkedFile = wordWatermarkService.addWatermarks(file.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "watermarked.docx");

            return ResponseEntity.ok().headers(headers).body(watermarkedFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(("IOException: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(("General Exception: " + e.getMessage()).getBytes());
        }
    }
}
