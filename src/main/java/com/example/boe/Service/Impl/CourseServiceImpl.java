package com.example.boe.Service.Impl;

import com.example.boe.Entity.Classes;
import com.example.boe.Entity.Course;
import com.example.boe.Entity.File;
import com.example.boe.Entity.Session;
import com.example.boe.Form.*;
import com.example.boe.Repository.*;
import com.example.boe.Service.CourseService;
import com.example.boe.Util.Util;
import com.example.boe.result.ExceptionMsg;
import com.example.boe.result.ResponseData;
import com.example.boe.result.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TeaRepository teaRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public ResponseData getList(UserInfoDto userInfoDto) {
        List<Course> courseList = new ArrayList<>();
        //获取身份
        String role = userInfoDto.getRole();
        //获取Id
        int id = userInfoDto.getId();
        if (role.equals("student")) {
            //获取学生课程列表
            courseList = courseRepository.findCourseByStudentId(id);
        } else if (role.equals("teacher")) {
            //获取教师课程列表
            courseList = courseRepository.findCourseByTeacherId(id);
        } else if (role.equals("admin")) {
            //获取所有课程列表
            courseList = courseRepository.findAll();

        }
        List<CourseFrontDto> courseFrontDtos = new ArrayList<>();
        courseList.forEach(course -> {
            CourseFrontDto courseFrontDto = new CourseFrontDto();
            BeanUtils.copyProperties(course, courseFrontDto);
            courseFrontDto.setSessionNum(countSessions(course.getSessions()));
            courseFrontDto.setTeacherId(course.getTeacher().getId());
            courseFrontDto.setTeacherName(course.getTeacher().getName());
            courseFrontDtos.add(courseFrontDto);

        });
        return new ResponseData(ExceptionMsg.SUCCESS, courseFrontDtos);
    }

    //TODO: 修改
    public int countSessions(Object obj) {
        int count = 0;
        if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            for (Object item : list) {
                count += countSessions(item);
            }
        } else if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            if (map.containsKey("sessionName")) {
                count += 1;
            }
            for (Object value : map.values()) {
                count += countSessions(value);
            }
        }
        return count;
    }
    @Override
    public ResponseData getDetail(int id) {

        Course c = courseRepository.findById(id).get();
        //初始化session,file
        Util.initial(c);
        Util.initial(c.getSessions());
//       Util.initial(c.getClasses());
        c.setSessions(c.getSessions());
        List<CourseDetailForm> courseDetailForms = convertToJsonTree(c.getSessions(), null);
        return new ResponseData(ExceptionMsg.SUCCESS, courseDetailForms);

    }

    public List<CourseDetailForm> convertToJsonTree(List<Session> sessions, String j) {
        if (sessions == null) {
            return null;
        }
        List<CourseDetailForm> courseDetailForms = new ArrayList<>();
        for (int i = 1; i <= sessions.size(); i++) {
            CourseDetailForm courseDetailForm = new CourseDetailForm();
            if(j==null){
                courseDetailForm.setId(i + "");
            }else{
                courseDetailForm.setId(j + "-" + i);
            }
            courseDetailForm.setSId(Integer.valueOf( sessions.get(i - 1).getId()));
            courseDetailForm.setName(sessions.get(i - 1).getSessionName());
            courseDetailForm.setDate(new Timestamp[]{sessions.get(i - 1).getStartTime(), sessions.get(i - 1).getEndTime()});
            courseDetailForm.setLabel("label");
            courseDetailForm.setCurrency("currency");
            courseDetailForm.setFileList(sessions.get(i - 1).getFiles());
            //计算当前时间在session时间段内的比例
            courseDetailForm.setRate(Util.getRate(sessions.get(i - 1).getStartTime(), sessions.get(i - 1).getEndTime()));
            courseDetailForm.setStatus(Util.getStatus(sessions.get(i - 1).getStartTime(), sessions.get(i - 1).getEndTime()));
            courseDetailForm.setVariableName(Util.getVariableName(sessions.get(i - 1).getStartTime(), sessions.get(i - 1).getEndTime()));
            courseDetailForm.setVariableValue(0);
            courseDetailForm.setVariableUp("variableUp");
            if(sessions.get(i-1).getChildSessions()!=null){
                if(j==null){
                    courseDetailForm.setChildren(convertToJsonTree(sessions.get(i-1).getChildSessions(), i+""));
                }else{
                    courseDetailForm.setChildren(convertToJsonTree(sessions.get(i-1).getChildSessions(), j+"-"+ i));
                }

            }
            courseDetailForms.add(courseDetailForm);

        }
        return courseDetailForms;
    }


    @Override
    @Transactional
    public ResponseData add(CourseDto courseDto) {

        Course course = convertToCourse(courseDto);
        courseRepository.save(course);
        //把file与session关联
        convertToFile(course.getSessions(), Collections.singletonList(courseDto.getData()));
        return new ResponseData(ExceptionMsg.SUCCESS, "添加成功");
    }

    public List<Timestamp> getAllTimes(Session session) {
        List<Timestamp> times = new ArrayList<>();
        times.add(session.getStartTime());
        times.add(session.getEndTime());
        for (Session childSession : session.getChildSessions()) {
            times.addAll(getAllTimes(childSession));
        }
        return times;
    }

    private void convertToFile(List<Session> sessions, List<SessionDto> sessionDtos) {
        if (sessionDtos == null || sessions == null) {
            return;
        } else {
            for (int i = 0; i < sessionDtos.size(); i++) {
                if (sessionDtos.get(i).getFileList() != null) {
                    sessionToFile(sessions.get(i), sessionDtos.get(i).getFileList());
                    convertToFile(sessions.get(i).getChildSessions(), sessionDtos.get(i).getChildren());
                } else if (sessionDtos.get(i).getChildren() != null) {
                    convertToFile(sessions.get(i).getChildSessions(), sessionDtos.get(i).getChildren());
                } else {
                    continue;
                }

            }
        }
    }

    private Course convertToCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setCourseName(courseDto.getName());
        course.setTeacher(teaRepository.findById(courseDto.getTeacherId()).orElseThrow(() -> new ServiceException("教师不存在")));


        Session session = convertToSession(courseDto.getData());
        session.setCourse(course);
        course.setSessions(Collections.singletonList(session));
        List<Timestamp> times = getAllTimes(session);
        course.setStartTime(Collections.min(times));
        course.setEndTime(Collections.max(times));
        return course;
    }

    private void sessionToFile(Session session, List<FileDto> fileDtos) {
        List<File> files = Optional.ofNullable(fileDtos)
                .orElse(Collections.emptyList())
                .stream()
                .map(fileDto -> {
                    File aFile = fileRepository.findFileByUid(fileDto.getUid());
                    if (aFile != null) {
                        System.out.println("fileName" + aFile.getName() + "sessionId" + session.getSessionName());
                        aFile.setSession(session);
                        return aFile;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
//        session.setFiles(files);


    }

    private Session convertToSession(SessionDto sessionDto) {
        Session session = new Session();
        session.setSessionName(sessionDto.getName());
        if (sessionDto.getDate().length > 0) {
            session.setStartTime(sessionDto.getStartTime());
            session.setEndTime(sessionDto.getEndTime());
        } else {
            session.setStartTime(null);
            session.setEndTime(null);
        }

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

    @Autowired
    private ClassesRepository classRepository;
    @Override
    public ResponseData importCou(List<Integer> courseIds, int classId) {
        courseIds.forEach(courseId->{
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new ServiceException("课程不存在"));
            Classes aClass = classRepository.findById(classId).orElseThrow(() -> new ServiceException("班级不存在"));
            course.setClasses(aClass);
            courseRepository.save(course);
        });

        return new ResponseData(ExceptionMsg.SUCCESS, "导入成功");
    }
}
