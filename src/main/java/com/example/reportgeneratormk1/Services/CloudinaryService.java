package com.example.reportgeneratormk1.Services;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {
    private Cloudinary cloudinary;
    @Autowired
    private Environment environment;

//    @Value("${CLOUDINARY_URL}")
    private String url;
    @Autowired
    public CloudinaryService(@Value("${CLOUDINARY_URL}") String url){

        cloudinary = new Cloudinary(url);
        System.out.println("cloudinary initialized " +cloudinary.config.cloudName);
    }

    public String upload(ByteArrayInputStream file){
        //since I will directly save then direct file needed to save
        try {
            String uniqueName="report"+ UUID.randomUUID()+".docx";
            Map uploadResult = cloudinary.uploader().upload(file.readAllBytes(), ObjectUtils.asMap("resource_type","auto",
            "public_id",uniqueName,"overwrite","true","folder","ReportGeneratorMK1"));

            //extract the url
            String url=(String)uploadResult.get("secure_url");
            System.out.println(url);
            return url;

        }
        catch(IOException e){
            System.out.println("failed to upload the image "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        catch(Exception e){
            System.out.println("cloudinary upload failed "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
