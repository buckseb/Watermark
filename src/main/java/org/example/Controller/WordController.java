package org.example.Controller;

import org.example.Service.WordWatermarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class WordController {

    private final WordWatermarkService wordWatermarkService;

    @Autowired
    public WordController(WordWatermarkService wordWatermarkService) {
        this.wordWatermarkService = wordWatermarkService;
    }

    @PostMapping("/word/upload")
    public ResponseEntity<byte[]> uploadAndWatermarkWord(@RequestParam("file") MultipartFile file) {
        try {
            return wordWatermarkService.addWatermark(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
