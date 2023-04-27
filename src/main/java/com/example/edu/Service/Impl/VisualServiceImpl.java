package com.example.edu.Service.Impl;

import com.example.edu.Entity.Course;
import com.example.edu.Entity.File;
import com.example.edu.Entity.Session;
import com.example.edu.Entity.User;
import com.example.edu.Repository.*;
import com.example.edu.Service.VisualService;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VisualServiceImpl implements VisualService {

    @Autowired
    private ResourceLogRepository resourceLogRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private StuCourceManageRepository stuCourceManageRepository;
    @Autowired
    private StudentRepository stuRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserLoginLogRepository userLoginLogRepository;

    /**
     *
     *
     * @param user
     * @return {"体育":0.78,"数学":0.64}
     */
    @Override
    public ResponseData getStatus(User user) {
        if(user.getRoleId()!=2){
            return null;
        }
        List<Course> courses=courseRepository.findCourseByStudentId(user.getId());
        List<Map<String,Object>> list=courseRepository.findScore(courses,user);
        return new ResponseData(ExceptionMsg.SUCCESS,list);
    }

    /**
     * {"LoginTime":2020-09-21 12:34:11,"LoginTime":2020-09-21 12:34:11,"LoginTime":2020-09-21 12:34:11}
     * @param user
     * @return
     */
    @Override
    public ResponseData getActiveMap(User user) {
        if(user.getRoleId()!=1){
            return null;
        }
        List<String> studentList=courseRepository.findStuByTeaId(user.getId());
        List<Map<String,Object>> list=userLoginLogRepository.findByName(studentList);
        return new ResponseData(ExceptionMsg.SUCCESS,list);
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public ResponseData getMap(User user) {
        switch (user.getRoleId()){
            case 1:return new ResponseData(ExceptionMsg.SUCCESS,getAdminMsg());
            case 2:return new ResponseData(ExceptionMsg.SUCCESS,getStuMsg(user));
            case 3:return new ResponseData(ExceptionMsg.SUCCESS,getTeaMsg(user));
            default:return new ResponseData(ExceptionMsg.FAILED,"error");
        }

    }

    private List<Map<String,Object>> getTeaMsg(User user) {
        List<Map<String,Object>> map=new ArrayList<>();
        Integer studentNum=stuRepository.findStuNumByTeaId(user.getId());
        map.add(Map.of("studentNum",studentNum));
        List<Course> courseList=courseRepository.findAllByUserId(user.getId());
        map.add(Map.of("courseNum",courseList.size()));
        courseList.forEach(course -> {
            course.getSessions().forEach(session -> {
                map.add(Map.of("files",getFiles(session)));
            });
        });
        return map;
    }

    public  List<File> getFiles(Session session) {
        List<File> files=new ArrayList<>();
        if(session!=null){
            files.addAll(session.getFiles());
        }
        if(session.getChildSessions().size()>0){
            session.getChildSessions().forEach(session1 -> {
                files.addAll(getFiles(session1));
            });
        }
        return files;
    }
    private List<Map<String,Object>> getStuMsg(User user) {
        return null;
    }

    public List<Map<String,Object>> getAdminMsg() {
        return null;
    }
}
