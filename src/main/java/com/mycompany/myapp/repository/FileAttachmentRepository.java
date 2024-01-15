package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long>, FileAttachmentRepositoryCustom {
    @Modifying
    @Query(value = "delete from file_attachment where object_id = :objectId and file_type = :fileType", nativeQuery = true)
    void deleteObjectIdAndFileType(@Param("objectId") Long objectId, @Param("fileType") Integer fileType);
}
