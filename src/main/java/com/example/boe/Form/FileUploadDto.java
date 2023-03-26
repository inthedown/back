package com.example.boe.Form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadDto {
    private final String author;
    private final int belongProgram;
}
