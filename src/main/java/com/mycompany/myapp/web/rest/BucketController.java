package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.config.AmazonClient;
import com.mycompany.myapp.config.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/storage/")
public class BucketController {
    private AmazonClient amazonClient;

    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    @PostMapping("/uploadMultiFile")
    public void uploadFile(@RequestPart(value = "file") MultipartFile[] files) {
        this.amazonClient.uploadFile(files);
    }

    @PostMapping("/uploadAvatarUser")
    public String uploadFileWithPath(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonClient.uploadFileWithPath(file, Constants.BUCKET_PATH.AVATAR);
    }

    @PostMapping("getFile")
    public ResponseEntity<byte[]> getFile(@RequestBody Map<String, Object> request) {
        String key = (String) request.get("path");
//        return ResponseEntity.ok(this.amazonClient.getFileFromS3(key));
        return ResponseEntity
            .ok()
            // Content-Type
//            .contentType(mediaType) //
            // Content-Lengh
//            .contentLength(data.length) //
            .body(this.amazonClient.getFileFromS3(key));
    }

    @PostMapping("getAvatar")
    public ResponseEntity<String> getAvatar(@RequestBody Map<String, Object> request) {
        String key = (String) request.get("path");
        return ResponseEntity
            .ok()
            .body(this.amazonClient.getAvatrFromS3(key));
    }

    @PostMapping("deleteFile")
    public void deleteFile(@RequestBody Map<String, Object> request) {
        String key = (String) request.get("path");
        this.amazonClient.deleteFileS3(key);
    }
}
