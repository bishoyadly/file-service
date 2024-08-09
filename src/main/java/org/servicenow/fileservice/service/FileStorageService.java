package org.servicenow.fileservice.service;

import lombok.extern.slf4j.Slf4j;
import org.servicenow.fileservice.configuration.StorageProperties;
import org.servicenow.fileservice.exception.FileStorageException;
import org.servicenow.fileservice.model.FileStateStoreRecord;
import org.servicenow.fileservice.repositories.FileStateStoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Objects;

@Service
@Slf4j
public class FileStorageService {

    private final Path rootLocation;

    private final FileStateStoreRepository fileStateStoreRepository;

    public FileStorageService(StorageProperties properties, FileStateStoreRepository fileStateStoreRepository) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.fileStateStoreRepository = fileStateStoreRepository;
    }

    public void saveFile(MultipartFile file, Long retentionPeriodInSeconds) {
        if (file.isEmpty()) {
            throw new FileStorageException(HttpStatus.BAD_REQUEST, "File is empty");
        }
        Path filePath = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
        String destinationPath = this.rootLocation.resolve(filePath).normalize().toAbsolutePath().toString();
        writeFileToDisk(file, destinationPath);
        saveFileStateStoreRecord(file.getOriginalFilename(), destinationPath, retentionPeriodInSeconds);
    }

    private void writeFileToDisk(MultipartFile file, String destinationPath) {
        try {
            RandomAccessFile stream = new RandomAccessFile(destinationPath, "rw");
            FileChannel channel = stream.getChannel();
            ByteBuffer buffer = ByteBuffer.wrap(file.getBytes());
            channel.write(buffer);
            stream.close();
            channel.close();
        } catch (Exception exception) {
            log.error("Error storing file: ", exception);
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    private void saveFileStateStoreRecord(String fileName, String destinationPath, Long retentionPeriodInSeconds) {
        FileStateStoreRecord fileStateStoreRecord = new FileStateStoreRecord();
        fileStateStoreRecord.setFileName(fileName);
        fileStateStoreRecord.setFilePath(destinationPath);
        fileStateStoreRecord.setFileRetentionPeriodInMS(Instant.now().getEpochSecond() + retentionPeriodInSeconds);
        fileStateStoreRepository.save(fileStateStoreRecord);
    }
}
