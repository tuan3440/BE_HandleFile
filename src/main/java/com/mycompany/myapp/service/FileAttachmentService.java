package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.FileAttachmentDTO;
import com.mycompany.myapp.service.form.FileManagementDetail;

import java.util.List;
import java.util.Optional;

public interface FileAttachmentService {
    FileAttachmentDTO save(FileAttachmentDTO fileAttachmentDTO);

    List<FileManagementDetail> getFileManagement(Integer fileType, Long objectId);

    Optional<FileAttachmentDTO> findOne(Long id);

    void deleteById(Long avatarId);

    void deleteByObjectIdAndFileType(Long objectId, Integer fileType);
}
