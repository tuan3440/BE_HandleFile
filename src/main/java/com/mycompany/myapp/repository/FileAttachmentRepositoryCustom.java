package com.mycompany.myapp.repository;

import com.mycompany.myapp.service.form.FileManagementDetail;

import java.util.List;

public interface FileAttachmentRepositoryCustom {
    List<FileManagementDetail> getFileManagement(Integer fileType, Long longList);
}
