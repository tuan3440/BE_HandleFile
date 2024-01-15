package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.FileAttachmentService;
import com.mycompany.myapp.service.dto.FileAttachmentDTO;
import com.mycompany.myapp.service.form.FileManagementDetail;
import com.mycompany.myapp.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/file-managements")
public class FileAttachmentResource {

    @Autowired
    public FileAttachmentService fileAttachmentService;

    @Autowired
    private ServletContext servletContext;

    @PostMapping("/search")
    public ResponseEntity<?> getListFile(@RequestBody FileAttachmentDTO fileAttachmentDTO) {
        List<FileManagementDetail> fileManagementDetails = new ArrayList<>();
        if (fileAttachmentDTO != null) {
            fileManagementDetails =
                fileAttachmentService.getFileManagement(fileAttachmentDTO.getFileType(), fileAttachmentDTO.getObjectId());
        }
        return ResponseEntity.ok(fileManagementDetails);
    }


    @GetMapping("view")
    public ResponseEntity<ByteArrayResource> viewFile(@RequestParam("id") Long id) throws IOException {
        Optional<FileAttachmentDTO> fileManagementDTOOptional = fileAttachmentService.findOne(id);
        if (!fileManagementDTOOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        FileAttachmentDTO fileManagementDTO = fileManagementDTOOptional.get();
        MediaType mediaType = FileUtil.getMediaTypeForFileName(this.servletContext, fileManagementDTO.getFileName());

        Path path = Paths.get(fileManagementDTO.getPath() + fileManagementDTO.getFileEntryName());
        if (Files.exists(path)) {
            byte[] data = Files.readAllBytes(path);
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                .ok()
                // Content-Type
                .contentType(mediaType) //
                // Content-Lengh
                .contentLength(data.length) //
                .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("id") Long id) throws IOException {
        Optional<FileAttachmentDTO> fileManagementDTOOptional = fileAttachmentService.findOne(id);
        if (!fileManagementDTOOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        FileAttachmentDTO fileManagementDTO = fileManagementDTOOptional.get();
        MediaType mediaType = FileUtil.getMediaTypeForFileName(this.servletContext, fileManagementDTO.getFileName());
        Path path = Paths.get(fileManagementDTO.getPath() + fileManagementDTO.getFileEntryName());
        return getByteArrayResourceResponseEntity(fileManagementDTO, mediaType, path);
    }

    private ResponseEntity<ByteArrayResource> getByteArrayResourceResponseEntity(
        FileAttachmentDTO fileManagementDTO,
        MediaType mediaType,
        Path path
    ) throws IOException {
        if (Files.exists(path)) {
            long startTime = System.currentTimeMillis();
            byte[] data = Files.readAllBytes(path);
            long endTime = System.currentTimeMillis();
            System.out.println("getByteArrayResourceResponseEntity readAllBytes: " + (endTime - startTime));
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                .ok()
                // Content-Type
                .contentType(mediaType) //
                // Content-Lengh
                .contentLength(data.length) //
                .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
