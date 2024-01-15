package com.mycompany.myapp.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "file_attachment", schema = "handleFile", catalog = "")
public class FileAttachment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "file_type")
    private Integer fileType;

    @Basic
    @Column(name = "object_id")
    private Long objectId;

    @Basic
    @Column(name = "file_name")
    private String fileName;

    @Basic
    @Column(name = "file_entry_name")
    private String fileEntryName;

    @Basic
    @Column(name = "path")
    private String path;

    @Basic
    @Column(name = "created_date")
    private Timestamp createdDate;

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

    public String getFileEntryName() {
        return fileEntryName;
    }

    public void setFileEntryName(String fileEntryName) {
        this.fileEntryName = fileEntryName;
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
}
