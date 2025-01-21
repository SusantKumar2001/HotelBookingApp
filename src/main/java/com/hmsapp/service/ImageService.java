package com.hmsapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${bucket.name}")
    private String bucketName;

    public String uploadImage(long propertyId, MultipartFile file)throws IOException{
        if(file.isEmpty()){
            throw new IllegalArgumentException("File is empty");
        }
        String fileName = propertyId+"/"+file.getOriginalFilename();
        amazonS3.putObject(bucketName,fileName,file.getInputStream(),null);
        return amazonS3.getUrl(bucketName,fileName).toString();
    }

}
