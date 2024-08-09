package org.servicenow.leaderelection.controller;

import org.servicenow.leaderelection.exception.FileStorageException;
import org.servicenow.leaderelection.service.FileStorageService;
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
                                                   @RequestParam("retentionPeriodInSeconds") Integer retentionPeriodInSeconds) {
        try {
            fileStorageService.saveFile(file);
        } catch (FileStorageException exception) {
            return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

}
