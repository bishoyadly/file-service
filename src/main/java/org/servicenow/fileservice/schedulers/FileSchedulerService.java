package org.servicenow.fileservice.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.servicenow.fileservice.model.FileStateStoreRecord;
import org.servicenow.fileservice.repositories.FileStateStoreRepository;
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
        log.info("##############################################################");
        log.info("Leader Instance Started Scheduled Expired Files Deletion Task");
        log.info("##############################################################");
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
                Path filePath = Path.of(fileStateStoreRecord.getFilePath());
                Files.deleteIfExists(filePath);
                fileStateStoreRepository.delete(fileStateStoreRecord);
            }
        }
    }
}
