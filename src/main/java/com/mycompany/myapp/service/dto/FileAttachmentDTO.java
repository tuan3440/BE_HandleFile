package com.mycompany.myapp.service.dto;

import java.sql.Timestamp;

public class FileAttachmentDTO {
    Long id;
    Integer fileType;
    Long objectId;
    String fileName;
    String path;
    Timestamp createdDate;
    String fileEntryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getFileEntryName() {
        return fileEntryName;
    }

    public void setFileEntryName(String fileEntryName) {
        this.fileEntryName = fileEntryName;
    }
}
