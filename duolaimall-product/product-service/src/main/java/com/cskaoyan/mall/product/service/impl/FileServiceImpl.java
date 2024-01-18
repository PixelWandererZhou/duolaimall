package com.cskaoyan.mall.product.service.impl;

import com.cskaoyan.mall.common.config.MinioConfig;
import com.cskaoyan.mall.common.util.FileUtil;
import com.cskaoyan.mall.product.service.FileService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    MinioClient minioClient;
    @Autowired
    MinioConfig minioConfig;
    @Override
    public String fileUpload(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketName = minioConfig.getBucketName();
        String key = UUID.randomUUID().toString().replaceAll("-","") + FileUtil.getFileExtension(file.getOriginalFilename());
        File tempFile = File.createTempFile(file.getOriginalFilename(), null, null);
        file.transferTo(tempFile);
        try {
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .filename(tempFile.getPath())
                            .build());
            System.out.println(
                    "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
                            + "object 'asiaphotos-2015.zip' to bucket 'asiatrip'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
        return minioConfig.getEndpointUrl() + "/" + minioConfig.getBucketName() + "/" +key;
    }
}
