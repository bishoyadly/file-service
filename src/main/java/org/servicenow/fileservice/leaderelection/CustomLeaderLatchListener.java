package org.servicenow.fileservice.leaderelection;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.servicenow.fileservice.schedulers.FileSchedulerService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomLeaderLatchListener implements LeaderLatchListener {

    private final FileSchedulerService fileSchedulerService;

    public CustomLeaderLatchListener(FileSchedulerService fileSchedulerService) {
        this.fileSchedulerService = fileSchedulerService;
    }

    @Override
    public void isLeader() {
        log.info("#######################");
        log.info("I'm the leader instance");
        log.info("#######################");
        fileSchedulerService.scheduleFileDeletionTask();
    }

    @Override
    public void notLeader() {
        fileSchedulerService.cancelScheduledFileDeletionTask();
    }
}
