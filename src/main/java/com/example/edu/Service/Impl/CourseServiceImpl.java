package com.example.edu.Service.Impl;

import com.example.edu.Entity.*;
import com.example.edu.Form.*;
import com.example.edu.Repository.*;
import com.example.edu.Service.CourseService;
import com.example.edu.Util.Util;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import com.example.edu.result.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ResourceLogRepository resourceLogRepository;

    @Override
    @Transactional
    public ResponseData getList(UserInfoDto userInfoDto) {
        List<Course> courseList = new ArrayList<>();
        //获取身份
       String role = userInfoDto.getRole();
        //获取Id
        int id = userInfoDto.getId();
        if (role.equals("stu")||role.equals("2")) {
            //获取学生课程列表
            courseList = courseRepository.findCourseByStudentId(id);
        } else if (role.equals("tea")||role.equals("3")) {
            //获取教师课程列表
            courseList = courseRepository.findCourseByTeacherId(id);
        } else if (role.equals("admin")||role.equals("1")) {
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
        int total = courseFrontDtos.size();
        int start = 0;
        int end = total;

        if (userInfoDto.getCurrent() != null && userInfoDto.getSize() != null) {
            start = (userInfoDto.getCurrent() - 1) * userInfoDto.getSize();
            end = Math.min(start + userInfoDto.getSize(), total);
            List<CourseFrontDto> pageList = courseFrontDtos.subList(start, end);
            Page<CourseFrontDto> page = new PageImpl<>(pageList, PageRequest.of(userInfoDto.getCurrent() - 1, userInfoDto.getSize()), total);
            return new ResponseData(ExceptionMsg.SUCCESS, page);
        }else{
            List<CourseFrontDto> pageList = courseFrontDtos.subList(start, end);
            Page<CourseFrontDto> page = new PageImpl<>(pageList, PageRequest.of(0, total), total);
            return new ResponseData(ExceptionMsg.SUCCESS, page);
        }



    }

    //统计session数量
    public int countSessions(List<Session> sessions) {
        int count = 0;
        for (Session session : sessions) {
          count++;
          if(session.getChildSessions()!=null){
              count+=countSessions(session.getChildSessions());
          }
        }
        return count;
    }
    @Override
    public ResponseData getDetail(int id, User user) {

        Course c = courseRepository.findById(id).get();
        //初始化session,file
        Util.initial(c);
                //Util.initial(c.getSessions());
        //       Util.initial(c.getClasses());
               // c.setSessions(c.getSessions());
        List<CourseDetailForm> courseDetailForms = convertToJsonTree(c.getSessions(), null,user);
        return new ResponseData(ExceptionMsg.SUCCESS, courseDetailForms.get(0));

    }

    public List<CourseDetailForm> convertToJsonTree(List<Session> sessions, String j,User user) {
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
            courseDetailForm.setLabel(Util.getLabel(sessions.get(i - 1).getStartTime(), sessions.get(i - 1).getEndTime()));
            courseDetailForm.setCurrency(Util.getCurrency(sessions.get(i - 1).getStartTime(), sessions.get(i - 1).getEndTime()));

            courseDetailForm.setFileList(sessions.get(i - 1).getFiles());
            //计算当前时间在session时间段内的比例
            String scoreMsg=getVariableName(sessions.get(i-1).getId(),user);
            String[] scoreMsgs=scoreMsg.split("_");
            String status=scoreMsgs[0];
            String score=scoreMsgs[1];

            courseDetailForm.setRate(Float.parseFloat(score)/100);
            courseDetailForm.setStatus(Util.getStatus(sessions.get(i - 1).getStartTime(), sessions.get(i - 1).getEndTime()));
            courseDetailForm.setVariableName(status);
            courseDetailForm.setVariableValue(Float.parseFloat(score)/100);
            courseDetailForm.setVariableUp("variableUp");
            if(sessions.get(i-1).getChildSessions()!=null){
                if(j==null){
                    courseDetailForm.setChildren(convertToJsonTree(sessions.get(i-1).getChildSessions(), i+"",user));
                }else{
                    courseDetailForm.setChildren(convertToJsonTree(sessions.get(i-1).getChildSessions(), j+"-"+ i,user));
                }

            }
            courseDetailForms.add(courseDetailForm);

        }
        return courseDetailForms;
    }
    //学生节点学习情况
    public String   getVariableName(int sessionId,  User user) {

        if(user.getRoleId()!=2){
            return "已完成_100";
        }
        int studentId = user.getId();
        Session session =sessionRepository.getReferenceById(sessionId);
        List<Integer> fileIds = session.getFiles().stream().map(File::getId).collect(Collectors.toList());
        if(fileIds.size()==0){
            return "已完成_100";
        }
        List<ResourceLog> resourceLogs = resourceLogRepository.findResourceLogByFileIdInAndStudentId(fileIds, studentId);
        //遍历fileIds,找到对应的resourceLog,若没有则进度为0
        int count = 0;
        for (Integer fileId : fileIds) {
            for (ResourceLog resourceLog : resourceLogs) {
                if (resourceLog.getFile().getId() == fileId) {
                    count+=resourceLog.getPercent();
                }
            }
        }
        count=count/fileIds.size();
        if(count>=100){
            return "已完成_100";
        }else if(count>0){
            return "进行中_"+count;
        }else{
            return "未开始_0";
        }
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
                      //  System.out.println("fileName" + aFile.getName() + "sessionId" + session.getSessionName());
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
    public ResponseData delete(List<Integer> ids) {
        //批量删除
        courseRepository.deleteAllByIdIn(ids);
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
