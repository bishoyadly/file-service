package org.servicenow.leaderelection.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.servicenow.leaderelection.model.FileStateStoreRecord;
import org.servicenow.leaderelection.repositories.FileStateStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class FileSchedulerService {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final int fileSchedulerTaskDelay;

    private final FileStateStoreRepository fileStateStoreRepository;

    @Autowired
    public FileSchedulerService(ThreadPoolTaskScheduler threadPoolTaskScheduler,
                                @Value("${fileSchedulerTaskDelayInMS:10000}") int fileSchedulerTaskDelay,
                                FileStateStoreRepository fileStateStoreRepository) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.fileSchedulerTaskDelay = fileSchedulerTaskDelay;
        this.fileStateStoreRepository = fileStateStoreRepository;
        scheduleFileDeletionTask();
    }

    public void scheduleFileDeletionTask() {
        threadPoolTaskScheduler.scheduleWithFixedDelay(this::deleteExpiredFilesTask, Duration.ofMillis(fileSchedulerTaskDelay));
    }

    public void deleteExpiredFilesTask() {
        try {
            deleteFile();
        } catch (Exception exception) {
            log.error("Error deleting file : ", exception);
        }
    }

    private void deleteFile() throws IOException {
        List<FileStateStoreRecord> fileStateStoreRecordList = fileStateStoreRepository.findAll();
        for (FileStateStoreRecord fileStateStoreRecord : fileStateStoreRecordList) {
            long currentTimestamp = Instant.now().getEpochSecond();
            if (currentTimestamp > fileStateStoreRecord.getFileRetentionPeriodInMS()) {
                log.info("file expired and scheduled for deletion thread : {}", Thread.currentThread().getName());
                fileStateStoreRepository.delete(fileStateStoreRecord);
                Path filePath = Path.of(fileStateStoreRecord.getFilePath());
                Files.deleteIfExists(filePath);
            }
        }
    }
}
