package org.servicenow.fileservice.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity(name = "file_state_store")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class FileStateStoreRecord {

    @Id
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_retention_period_in_ms")
    private Long fileRetentionPeriodInMS;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private Date modifiedDate;
}
