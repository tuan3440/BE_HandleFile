package com.mycompany.myapp.utils;

import com.mycompany.myapp.service.FileAttachmentService;
import com.mycompany.myapp.service.dto.FileAttachmentDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class FileUtil {
    public static void saveMultiFile(
        List<MultipartFile> files,
        Long objectId,
        Integer fileType,
        Environment environment,
        FileAttachmentService fileAttachmentService
    ) {
        String uploadRootPath = environment.getProperty("upload.path");
        File uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        String uploadPath = uploadRootPath + "OBJECT_TYPE_" + fileType.toString() + File.separator + objectId.toString() + File.separator;
        File uploadPathDir = new File(uploadPath);
        if (!uploadPathDir.exists()) {
            uploadPathDir.mkdirs();
        }
        for (MultipartFile file : files) {
            Date date = new Date();
            Timestamp createDate = new Timestamp(date.getTime());
            FileAttachmentDTO fileAttachmentDTO = new FileAttachmentDTO();
            fileAttachmentDTO.setFileType(fileType);
            fileAttachmentDTO.setObjectId(objectId);
            fileAttachmentDTO.setCreatedDate(createDate);
            // save file
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.length() > 0) {
                try {
                    fileAttachmentDTO.setFileName(fileName);
                    String fileEntryName =
                        FilenameUtils.removeExtension((file.getOriginalFilename())) +
                            "_" +
                            createDate.toString().replace(":", "-") +
                            "." +
                            FilenameUtils.getExtension(file.getOriginalFilename());
                    fileAttachmentDTO.setFileEntryName(fileEntryName);
                    fileAttachmentDTO.setPath(uploadPath);
                    File serverFile = new File(uploadPath + File.separator + fileEntryName);
                    try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                        stream.write(file.getBytes());
                    }
                    FileAttachmentDTO result = fileAttachmentService.save(fileAttachmentDTO);
                } catch (Exception e) {}
            }
        }
    }

    public static Long uploadAvatar(
        MultipartFile file,
        Long objectId,
        Integer fileType,
        Environment environment,
        FileAttachmentService fileAttachmentService
    ) {
        String uploadRootPath = environment.getProperty("upload.path");
        File uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        String uploadPath = uploadRootPath + "OBJECT_TYPE_" + fileType.toString() + File.separator + objectId.toString() + File.separator;
        File uploadPathDir = new File(uploadPath);
        if (!uploadPathDir.exists()) {
            uploadPathDir.mkdirs();
        }
        Date date = new Date();
        Timestamp createDate = new Timestamp(date.getTime());
        FileAttachmentDTO fileAttachmentDTO = new FileAttachmentDTO();
        fileAttachmentDTO.setFileType(fileType);
        fileAttachmentDTO.setObjectId(objectId);
        fileAttachmentDTO.setCreatedDate(createDate);
        // save file
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.length() > 0) {
            try {
                fileAttachmentDTO.setFileName(fileName);
                String fileEntryName =
                    FilenameUtils.removeExtension((file.getOriginalFilename())) +
                        "_" +
                        createDate.toString().replace(":", "-") +
                        "." +
                        FilenameUtils.getExtension(file.getOriginalFilename());
                fileAttachmentDTO.setFileEntryName(fileEntryName);
                fileAttachmentDTO.setPath(uploadPath);
                File serverFile = new File(uploadPath + File.separator + fileEntryName);
                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                    stream.write(file.getBytes());
                }
                FileAttachmentDTO result = fileAttachmentService.save(fileAttachmentDTO);
                return result.getId();
            } catch (Exception e) {}
        }
        return null;
    }

    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        // application/pdf
        // application/xml
        // image/gif, ...
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
