package com.example.boe.Service.Impl;

import com.example.boe.Entity.Course;
import com.example.boe.Entity.File;
import com.example.boe.Entity.Session;
import com.example.boe.Form.CourseDto;
import com.example.boe.Form.SessionDto;
import com.example.boe.Form.UserInfoDto;
import com.example.boe.Repository.CourseRepository;
import com.example.boe.Repository.FileRepository;
import com.example.boe.Service.CourseService;
import com.example.boe.result.ExceptionMsg;
import com.example.boe.result.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
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
//        Course course = courseRepository.findByIdWithSessions(id);
//        if(course==null){
//            return new ResponseData(ExceptionMsg.FAILED,"课程不存在");
//        }
//        return new ResponseData(ExceptionMsg.SUCCESS, JSON.toJSONString(course, SerializerFeature.DisableCircularReferenceDetect));
       Course c= getCourseWithAllChildObjects(id);
       log.info("course:{}",c);
        return new ResponseData(ExceptionMsg.SUCCESS, c);
    }
//    public Course getCourseWithAllChildObjects(int courseId) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        try {
//            // 使用JOIN FETCH语句同时获取所有相关对象
//            TypedQuery<Course> query = entityManager.createQuery(
//                    "select c from Course c left join fetch c.sessions s left join fetch s.files where c.id = :id",
//                    Course.class);
//            query.setParameter("id", courseId);
//            return query.getSingleResult();
//        } finally {
//            entityManager.close();
//        }
//    }
    public Course getCourseWithAllChildObjects(int courseId) {

        List<Course> courses = jdbcTemplate.query("SELECT c.*, s.*, f.* FROM course c " +
                        "LEFT JOIN session s ON s.course_id = c.id " +
                        "LEFT JOIN file f ON f.session_id = s.id " +
                        "WHERE c.id = "+courseId,
                new BeanPropertyRowMapper<>(Course.class));

        if (courses.isEmpty()) {
            return null;
        }
        log.info("id:{}",courseId);
        Map<Integer, Session> sessionMap = new HashMap<>();
        Map<Integer, File> fileMap = new HashMap<>();
        Course course = courses.get(0);
        log.info("course:{}",course==null);
        course.setSessions(new ArrayList<>());
        for (Course c : courses) {
            log.info("a:{}",c.getSessions().get(0).getId());
            Session session = sessionMap.get(c.getSessions().get(0).getId());
            if (session == null) {
                session = c.getSessions().get(0);
                session.setFiles(new ArrayList<>());
                sessionMap.put(session.getId(), session);
                course.getSessions().add(session);
            }
            File file = c.getSessions().get(0).getFiles().get(0);
            log.info("b:{}",file.getId());
            if (file != null &&  Objects.requireNonNull(file.getId())!=null && !fileMap.containsKey(file.getId())) {
                file.setSession(session);
                session.getFiles().add(file);
                fileMap.put(file.getId(), file);
            }
        }
        return course;
    }

    @Override
    @Transactional
    public ResponseData add(CourseDto courseDto) {
        Course course = convertToCourse(courseDto);
        courseRepository.save(course);
        return new ResponseData(ExceptionMsg.SUCCESS);
    }
    private Course convertToCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setCourseName(courseDto.getName());
        course.setTeacherId(courseDto.getTeacherId());

        Session session = convertToSession(courseDto.getData());
        session.setCourse(course);
        course.setSessions(Collections.singletonList(session));

        return course;
    }

    private Session convertToSession(SessionDto sessionDto) {
        Session session = new Session();
        session.setSessionName(sessionDto.getName());
        session.setStartTime(sessionDto.getStartTime());
        session.setEndTime(sessionDto.getEndTime());

        List<File> files = Optional.ofNullable(sessionDto.getFileList())
                .orElse(Collections.emptyList())
                .stream()
                .map(fileDto -> {
                    File aFile = fileRepository.findFileByUid(fileDto.getUid());
                    if (aFile != null) {
                        aFile.setSession(session);
                        return aFile;
                    }else{
                     return null;
                    }
                })
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
        return null;
    }

    @Override
    public ResponseData update(CourseDto courseDto) {
        return null;
    }
}
