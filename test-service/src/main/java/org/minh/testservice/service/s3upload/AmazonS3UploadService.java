package org.minh.testservice.service.s3upload;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import jakarta.annotation.PostConstruct;
import org.minh.testservice.exception.S3UploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executors;
@Service
public class AmazonS3UploadService {
    private TransferManager transferManager;
    @Value("${amazon-properties.bucket-name}")
    private String bucketName;
    @Value("${amazon-properties.access-key}")
    private String accessKey;
    @Value("${amazon-properties.secret-key}")
    private String secretKey;
    @Value("${amazon-properties.region}")
    private String region;

    @PostConstruct
    private void initializeAmazon(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3client = new AmazonS3Client(credentials);
//        this.transferManager = TransferManagerBuilder.standard().withS3Client(s3client).build();
        this.transferManager = new TransferManager(
                s3client,
                Executors.newFixedThreadPool(30));
    }

    public String uploadFile(MultipartFile multipartFile) throws S3UploadException {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = "https://s3."+region+".amazonaws.com/"+bucketName+"/"+fileName;
            uploadFileToS3Bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            throw new S3UploadException(e.getMessage());
        }
        return fileUrl;
    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
    @Async
    public void uploadFileToS3Bucket(String fileName, File file) throws AmazonS3Exception {
        try {
            Upload upload = transferManager.upload(bucketName, fileName, file);
            upload.waitForCompletion();
        } catch (AmazonS3Exception e) {
            throw new SdkClientException("Error uploading file to S3 bucket: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
