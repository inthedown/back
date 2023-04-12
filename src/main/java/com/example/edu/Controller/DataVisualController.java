package com.example.edu.Controller;

import com.example.edu.Service.VisualService;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("visual")
public class DataVisualController {
    @Autowired
    private VisualService visualService;

    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public ResponseData upload(@RequestPart(value="fileName")@Nullable List<MultipartFile> files, @RequestPart(value="uidList")@Nullable String uidList){
       // List<File> aFiles=fileService.upload(files,uidList);
        return new ResponseData(ExceptionMsg.SUCCESS);
    }
}
