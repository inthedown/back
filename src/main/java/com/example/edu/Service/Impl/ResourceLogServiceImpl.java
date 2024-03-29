package com.example.edu.Service.Impl;

import com.example.edu.Entity.*;
import com.example.edu.Form.UserResourceLogForm;
import com.example.edu.Repository.*;
import com.example.edu.Service.ResourceLogService;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import com.example.edu.result.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class ResourceLogServiceImpl implements ResourceLogService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceLogRepository resourceLogRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private StuCourceManageRepository stuCourceManageRepository;
    @Autowired
    private StudentRepository stuRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Override
    public void add(UserResourceLogForm logForm, User user) {
        if(user.getRoleId()!=2){
            return;
        }
        Integer userId = Optional.ofNullable(logForm.getUserId())
                .orElseThrow(() -> {
                    throw new ServiceException("用户id为空");
                });
        Integer fileId=Optional.ofNullable(logForm.getFileInfo().getId())
                .orElseThrow(()->{
                    throw new ServiceException("文件id为空");
                });
        ResourceLog resourceLog = resourceLogRepository.findByUserIdAndFileId(userId, fileId);
        if (resourceLog == null) {
            resourceLog = new ResourceLog();
            resourceLog.setFile(fileRepository.getReferenceById(fileId));
            resourceLog.setUser(userRepository.getReferenceById(userId));
        }else if(resourceLog.getStatus().equals("已完成")){
            return;
        }
        if (logForm.getFileInfo().getType().equals("图片")) {
            float time = (float) ((logForm.getEndTime() - logForm.getStartTime()) / 1000);
            resourceLog.setPercent((float) (time / (60 * 5)));
        } else {
            resourceLog.setPercent(logForm.getPlayedPercentage() / 100f);
        }
        resourceLog.setTime(new Timestamp(System.currentTimeMillis()));
        resourceLog.setStatus(resourceLog.getPercent() < 1 ? "未完成" : "已完成");

        resourceLogRepository.save(resourceLog);

    }

    @Override
    public ResponseData count(Integer userId, Integer courseId) {
        List<Map<String,Float>> percents=new ArrayList<>();
        List<Session> sessionList=sessionRepository.findByCourseId(courseId);
        if(sessionList!=null){
            double count=0;
            for(Session session:sessionList){
               percents.addAll(sessionScore(userId,session.getId()));
            }
            if(percents.size()!=0){
                log.info("percents:{}",percents);
                //加权计算,视频权重为0.4，音频权重为0.3,文档权重为0.3
                for(Map<String,Float> percent:percents){
                    for(Map.Entry<String,Float> entry:percent.entrySet()){
                        if(entry.getKey().equals("video")){
                            count+=entry.getValue()*0.4;
                        }else if(entry.getKey().equals("audio")){
                            count+=entry.getValue()*0.3;
                        }else{
                            count+=entry.getValue()*0.3;
                        }
                    }
                }
                //更新学生课程管理表
                StuCourceManage stuCourceManage=stuCourceManageRepository.findByUserIdAndCourseId(userId,courseId);
                if(stuCourceManage==null){
                    stuCourceManage=new StuCourceManage();
                    stuCourceManage.setCourse(courseRepository.getReferenceById(courseId));
                    stuCourceManage.setStudent(stuRepository.getReferenceById(userId));
                    stuCourceManage.setScore(count);
                }else{
                    stuCourceManage.setScore(count);
                }
                stuCourceManageRepository.save(stuCourceManage);
                return new ResponseData(ExceptionMsg.SUCCESS,count);
            }else{
                return new ResponseData(ExceptionMsg.FAILED,"该课程没有资源");
            }

        }
        return new ResponseData(ExceptionMsg.FAILED,"该课程没有节点");
    }

    public List<Map<String,Float>> sessionScore(Integer userId,Integer sessionId){
        List<Map<String,Float>> percents=new ArrayList<>();
        Session session=sessionRepository.getReferenceById(sessionId);
        List<File> files=session.getFiles();
        if(files!=null){
            for(File file:files){
                ResourceLog resourceLog=resourceLogRepository.findByUserIdAndFileId(userId,file.getId());
                if(resourceLog!=null){
                    percents.add(Map.of(resourceLog.getFile().getType(),resourceLog.getPercent()));
                }else{
                    percents.add(Map.of(file.getType(),(float)0));
                }
            }
        }
        if(session.getChildSessions()!=null){
            for(Session childSession:session.getChildSessions()){
                List<Map<String,Float>> childPercents=sessionScore(userId,childSession.getId());
               if(childPercents!=null){
                   percents.addAll(childPercents);
               }
            }
        }
        return percents;
    }
}
