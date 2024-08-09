package org.servicenow.leaderelection.repositories;

import org.servicenow.leaderelection.model.FileStateStoreRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStateStoreRepository extends JpaRepository<FileStateStoreRecord, String> {
}
