package com.mycompany.myapp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Service
public class AmazonClient {
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey); // tạo key để truy cập vào hệ thống aws
        s3client = new AmazonS3Client(credentials); // tạo kết nối đến server
        s3client.setEndpoint(endpointUrl); //set endpoint url
        S3ClientOptions options = new S3ClientOptions();
        options.setPathStyleAccess(true);
        s3client.setS3ClientOptions(options);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private String generateFileName(MultipartFile multiPart, String path) {
        return path + "/" + new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    // upload file
    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public void uploadFile(MultipartFile[] multipartFiles) {
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile);
                uploadFileTos3bucket(fileName, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String uploadFileWithPath(MultipartFile multipartFile, String path) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile, path);
            fileUrl = bucketName + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public byte[] getFileFromS3(String key) {
        try {
            InputStream obj = s3client.getObject(bucketName, key).getObjectContent();
            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAvatrFromS3(String key) {
        String encode = null;
        try {
            InputStream obj = s3client.getObject(bucketName, key).getObjectContent();
            byte[] content = IOUtils.toByteArray(obj);
            encode = org.apache.commons.codec.binary.Base64.encodeBase64String(content);
            if (key.contains("jpg") || key.contains("jpeg")) {
                encode = "data:image/jpg;base64," + encode;
            }
            if (key.contains("png")) {
                encode = "data:image/png;base64," + encode;
            }
            obj.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }

    public void deleteFileS3(String key) { //function delete file on server
        String[] path = key.split("/", 2);
        try {
            s3client.deleteObject(path[0], path[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
