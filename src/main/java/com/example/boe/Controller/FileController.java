package com.example.boe.Controller;

import com.example.boe.Entity.File;
import com.example.boe.Service.FileService;
import com.example.boe.result.ExceptionMsg;
import com.example.boe.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     *
     * @param
     * @return
     */
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public ResponseData upload(@RequestPart(value="fileName")@Nullable List<MultipartFile> files, @RequestPart(value="uidList")@Nullable String uidList){
        List<File> aFiles=fileService.upload(files,uidList);
        return new ResponseData(ExceptionMsg.SUCCESS,aFiles);
    }
    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    public ResponseData getAll(){
        List<File> files=fileService.getAll();
        return  new ResponseData(ExceptionMsg.SUCCESS, files);
    }
    @RequestMapping(value = "delete",method = RequestMethod.DELETE)
    public ResponseData delete(@RequestBody int[] id){
        fileService.delete(id);
        return  new ResponseData(ExceptionMsg.SUCCESS);
    }

}
