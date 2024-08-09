package org.servicenow.leaderelection.service;

import lombok.extern.slf4j.Slf4j;
import org.servicenow.leaderelection.configuration.StorageProperties;
import org.servicenow.leaderelection.exception.FileStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Slf4j
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public void saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException(HttpStatus.BAD_REQUEST, "File is empty");
        }
        Path filePath = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
        String destinationPath = this.rootLocation.resolve(filePath).normalize().toAbsolutePath().toString();
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

    private void writeFileToDisk(MultipartFile file) {

    }
}
