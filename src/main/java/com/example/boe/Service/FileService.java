package com.example.boe.Service;

import com.example.boe.Entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<File> upload(List<MultipartFile> fileUploadDto, String uidList);

    List<File> getAll();

    void delete(int[] id);
}
