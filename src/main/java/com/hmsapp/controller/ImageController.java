package com.hmsapp.controller;

import com.hmsapp.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPropertyImage(@RequestParam("propertyId") long propertyId,
                                                      @RequestParam("file") MultipartFile file) throws IOException {
        // Delegate the upload logic to the service layer
        String fileUrl = imageService.uploadImage(propertyId, file);

        // Return success response
        return new ResponseEntity<>("Image uploaded successfully. File URL: " + fileUrl, HttpStatus.OK);
    }
}
