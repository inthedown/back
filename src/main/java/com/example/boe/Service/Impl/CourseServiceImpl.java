package com.example.boe.Service.Impl;

import com.example.boe.Entity.Classes;
import com.example.boe.Entity.Course;
import com.example.boe.Entity.File;
import com.example.boe.Entity.Session;
import com.example.boe.Form.CourseDto;
import com.example.boe.Form.SessionDto;
import com.example.boe.Form.UserInfoDto;
import com.example.boe.Repository.ClassesRepository;
import com.example.boe.Repository.CourseRepository;
import com.example.boe.Repository.FileRepository;
import com.example.boe.Repository.SessionRepository;
import com.example.boe.Service.CourseService;
import com.example.boe.result.ExceptionMsg;
import com.example.boe.result.ResponseData;
import com.example.boe.result.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    @Autowired
    private  CourseRepository courseRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ResponseData getList(UserInfoDto userInfoDto) {
        List<Course> courseList = new ArrayList<>();
        //获取身份
        String role = userInfoDto.getRole();
        //获取Id
        int id = userInfoDto.getId();
        if(role.equals("student")){
            //获取学生课程列表
           courseList= courseRepository.findCourseByStudentId(id);
        }else if(role.equals("teacher")){
            //获取教师课程列表
          courseList=  courseRepository.findCourseByTeacherId(id);
        }
        return new ResponseData(ExceptionMsg.SUCCESS,courseList);
    }

    @Override
    public ResponseData getDetail(int id) {

       Course c= courseRepository.findById(id).get();
        return new ResponseData(ExceptionMsg.SUCCESS, c);

    }


    @Override
    @Transactional
    public ResponseData add(CourseDto courseDto) {

        Course course = convertToCourse(courseDto);
        courseRepository.save(course);
        //把file与session关联
        associateFileWithSession(course,courseDto.getData());
        return new ResponseData(ExceptionMsg.SUCCESS,"添加成功");
    }

    private Timestamp[] calculateTime(List<Session> sessions){
        //定义数组存放开始时间和结束时间
        List<Timestamp> times = new ArrayList<>();
        sessions.forEach(session -> {
            times.add(session.getStartTime());
            times.add(session.getEndTime());
            if(session.getChildSessions().size()>0){
                calculateTime(session.getChildSessions());
            }
        });
        if (times.isEmpty()) {
            // handle case where there are no sessions
            return new Timestamp[]{null, null};
        }
        Timestamp startTime = Collections.min(times);
        Timestamp endTime = Collections.max(times);
        return new Timestamp[]{startTime,endTime};
    }
    public List<Timestamp> getAllTimes(Session session) {
        List<Timestamp> times = new ArrayList<>();
        times.add(session.getStartTime());
        times.add(session.getEndTime());
        for(Session childSession : session.getChildSessions()) {
            times.addAll(getAllTimes(childSession));
        }
        return times;
    }





    private Course convertToCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setCourseName(courseDto.getName());
        course.setTeacherId(courseDto.getTeacherId());

            Classes classes = Optional.ofNullable(classesRepository.findByClassName(courseDto.getClasses()))
                    .orElseThrow(() ->new ServiceException("班级不存在"));
            course.setClasses(classes);

        Session session = convertToSession(courseDto.getData());
        session.setCourse(course);
        course.setSessions(Collections.singletonList(session));
        List<Timestamp>times=getAllTimes(session);
        log.info("times{}",times.size());
        course.setStartTime(Collections.min(times));
        course.setEndTime(Collections.max(times));
        return course;
    }
    private void associateFileWithSession(Course course,SessionDto sessionDto) {
        log.info("course:{}",course.getSessions().size());
        course.getSessions().forEach(session -> {
            List<File> files = Optional.ofNullable(sessionDto.getFileList())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(fileDto -> {
                        log.info("file:{}",fileDto.getUid());
                        File aFile = fileRepository.findFileByUid(fileDto.getUid());
                        if (aFile != null) {
                            aFile.setSession(session);
                            return aFile;
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        });

    }
    private Session convertToSession(SessionDto sessionDto) {
        Session session = new Session();
        session.setSessionName(sessionDto.getName());
        if(sessionDto.getDate().length>0){
            session.setStartTime(sessionDto.getStartTime());
            session.setEndTime(sessionDto.getEndTime());
        }else{
            session.setStartTime(null);
            session.setEndTime(null);
        }


        List<File> files = Optional.ofNullable(sessionDto.getFileList())
                .orElse(Collections.emptyList())
                .stream()
                .map(fileDto -> {
                    File aFile = fileRepository.findFileByUid(fileDto.getUid());
                    if (aFile != null) {
                        return aFile;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        session.setFiles(files);


        List<Session> childSessions = Optional.ofNullable(sessionDto.getChildren())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convertToSession)
                .peek(childSession -> childSession.setParentSession(session))
                .collect(Collectors.toList());
        session.setChildSessions(childSessions);


        return session;

    }

    @Override
    public ResponseData delete(int id) {
        courseRepository.deleteById(id);
        return new ResponseData(ExceptionMsg.SUCCESS, "删除成功");
    }

    @Override
    public ResponseData update(CourseDto courseDto) {
        return null;
    }

    @Override
    public ResponseData getSessionList(int id) {
        Session session = courseRepository.findSessionsByCourseId(id);
        return new ResponseData(ExceptionMsg.SUCCESS, session);
    }
}
