package org.servicenow.fileservice.repositories;

import org.servicenow.fileservice.model.FileStateStoreRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStateStoreRepository extends JpaRepository<FileStateStoreRecord, String> {
}
