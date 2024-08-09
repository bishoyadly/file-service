package org.servicenow.fileservice.controller;

import org.servicenow.fileservice.exception.FileStorageException;
import org.servicenow.fileservice.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

    final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("retentionPeriodInSeconds") Long retentionPeriodInSeconds) {
        try {
            fileStorageService.saveFile(file, retentionPeriodInSeconds);
        } catch (FileStorageException exception) {
            return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

}
