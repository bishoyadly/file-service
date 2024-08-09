package org.servicenow.fileservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@EnableJpaAuditing
public class ApplicationConfig {

    @Value("${fileSchedulerPoolSize}")
    private Integer fileSchedulerPoolSize;

    @Value("${fileSchedulerName}")
    private String fileSchedulerName;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadGroupName(fileSchedulerName);
        scheduler.setPoolSize(fileSchedulerPoolSize);
        scheduler.initialize();
        return scheduler;
    }
}
