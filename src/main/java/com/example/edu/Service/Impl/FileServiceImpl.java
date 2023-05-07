package com.example.edu.Service.Impl;

import com.example.edu.Entity.File;
import com.example.edu.Entity.ResourceLog;
import com.example.edu.Entity.User;
import com.example.edu.Form.LogDto;
import com.example.edu.Repository.FileRepository;
import com.example.edu.Repository.ResourceLogRepository;
import com.example.edu.Repository.UserRepository;
import com.example.edu.Service.FileService;
import com.example.edu.Util.FastImageInfo;
import com.example.edu.Util.FileUitl;
import com.example.edu.result.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private String  url;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ResourceLogRepository resourceLogRepository;

    @Override
    public List<File> upload(List<MultipartFile> files, String uidList) {
        String[] strArray = null;
        if (uidList != null) {
            strArray = uidList.split(",");
        }
        List<File>uploadFile=new ArrayList<>();
        if(files!=null){
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                //判断文件是否为空
                if (file.isEmpty()) {
                    //抛出异常
                    throw new RuntimeException("文件为空");
                }
                // 获取文件名
                String name = file.getOriginalFilename();
                String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + name;
                System.out.print("文件名 "+fileName+"\n");
                //加个时间戳，尽量避免文件名称重复
                String path = "/Users/no1/final/res/" +fileName;//mac路径
//                String path = "E:/res/" +fileName;//windows路径
                //文件绝对路径
                System.out.print("绝对路径"+path+"\n");
                //创建文件路径
                java.io.File dest = new java.io.File(path);
                //判断文件是否已经存在
                if (dest.exists()) {
                    throw new RuntimeException("文件已存在");
                }
                //判断文件父目录是否存在
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdir();
                    System.out.println("make");
                }
                try {
                    //上传文件
                    file.transferTo(dest); //保存文件

                    System.out.print("path:"+path+"\n");
                    url="http://localhost:8080/res/"+fileName;

                    String size= FileUitl.getFormatSize(file.getSize());
                    File newFile=new File();
                    newFile.setName(name);
                    newFile.setRelativePath(path);
                    newFile.setUrl(url);
                    if(FileUitl.isImage(dest)){
                        FastImageInfo imageInfo = new FastImageInfo(dest);
                        int width = imageInfo.getWidth();
                        int height = imageInfo.getHeight();
                        String resolution=width+" * "+height;
                        newFile.setType("图片");
                        newFile.setResolution(resolution);
                    }else if(FileUitl.isVideo(dest)){
                        newFile.setType("视频");
                    }else {
                        newFile.setType("音频");
                    }
                    newFile.setSize(size);
                    newFile.setAuthor("author");
                    newFile.setUid(strArray[i]);
                    newFile.setUpdateTime(new Timestamp(new Date().getTime()));
                    System.out.print("url:"+url+"\n");
                    File aFile= fileRepository.save(newFile);
                    uploadFile.add(aFile);
                } catch (IOException e) {
                    throw new RuntimeException("上传失败"+e);
                }
            }

        }
       return  uploadFile;
    }

    @Override
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public void delete(int[] id) {
        System.out.println(id.length);
        //遍历id
        for (int i : id) {
            fileRepository.deleteById(i);
        }

    }

    @Autowired
    private UserRepository userRepository;
    @Override
    public void addLog(LogDto logDto) {
        int userId=logDto.getUserId();
        int fileId=logDto.getFileId();
        float percent=logDto.getPercent();

        User user= Optional.ofNullable( userRepository.findById(userId)).get().orElseThrow(()->{throw new ServiceException("用户不存在");});
        File file= Optional.ofNullable( fileRepository.findById(fileId)).get().orElseThrow(()->{throw new ServiceException("文件不存在");});

        ResourceLog resourceLog=new ResourceLog();
        resourceLog.setFile(file);
        resourceLog.setUser(user);
        resourceLog.setPercent(percent);
        resourceLog.setTime(new Timestamp(new Date().getTime()));
        if(percent<1){
            resourceLog.setStatus("未完成");
        }else {
            resourceLog.setStatus("已完成");
        }
        resourceLogRepository.save(resourceLog);
    }

}
