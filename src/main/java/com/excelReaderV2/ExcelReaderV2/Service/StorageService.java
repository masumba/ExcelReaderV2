package com.excelReaderV2.ExcelReaderV2.Service;


import com.excelReaderV2.ExcelReaderV2.Test.ExcelTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class StorageService {

    @Autowired
    ExcelReader excelReader;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final Path uploadsRootLocation = Paths.get("upload-dir");

    /*Add Uploaded file to directory*/
    public void storeFile(MultipartFile file){
        try {
            /**/
            LocalDate localDate = LocalDate.now();
            LocalDateTime localDateTime = LocalDateTime.now();
            LocalTime localTime = LocalTime.now();

            String timestamp = localDate.toString()+localTime.toString();
            timestamp = timestamp.replace(":","-");

            Files.copy(file.getInputStream(), this.uploadsRootLocation.resolve(timestamp+file.getOriginalFilename()));
            /**/
            excelReader.excelColumnReader(uploadsRootLocation+"/"+timestamp+file.getOriginalFilename(), 0);
            /**/
        } catch (Exception e){
            throw new RuntimeException("Fail! Service Error: "+e);
        }
    }

    /*Loads Excel Document*/
    public Resource loadFile(String filename){
        try {
            Path file  = uploadsRootLocation.resolve(filename);
            Resource resource = new UrlResource(filename);
            if (((UrlResource)resource).exists() || ((UrlResource)resource).isReadable()){
                return resource;
            } else {
                throw new RuntimeException("Failed to load file");
            }
        } catch (MalformedURLException e){
            throw new RuntimeException("Failed to Load File Error: "+e);
        }
    }

    /*Deletes file from directory*/
    public void deleteAll(){
        FileSystemUtils.deleteRecursively(uploadsRootLocation.toFile());
    }

    /*Creates file directory*/
    public void init(){
        try {
            Files.createDirectory(uploadsRootLocation);
        } catch (IOException e){
            throw new RuntimeException("Could Not Create Directory: "+e);
        }
    }
}
