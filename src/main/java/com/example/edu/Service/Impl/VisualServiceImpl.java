package com.example.edu.Service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.example.edu.Entity.*;
import com.example.edu.Repository.*;
import com.example.edu.Service.VisualService;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import com.example.edu.result.ServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
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
     * @param user
     * @return {"体育":0.78,"数学":0.64}
     */
    @Override
    public ResponseData getStatus(User user) {
        if (user.getRoleId() != 2) {
            return null;
        }
        List<Course> courses = courseRepository.findCourseByStudentId(user.getId());
        List<Map<String, Object>> list = courseRepository.findScore(courses, user);
        return new ResponseData(ExceptionMsg.SUCCESS, list);
    }

    /**
     * {"LoginTime":2020-09-21 12:34:11,"LoginTime":2020-09-21 12:34:11,"LoginTime":2020-09-21 12:34:11}
     *
     * @param user
     * @return
     */
    @Override
    public ResponseData getActiveMap(User user) {
        if (user.getRoleId() == 2) {
            return null;
        }

        // 获取当前时间
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 计算一个月前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Timestamp oneMonthAgo = new Timestamp(calendar.getTimeInMillis());
        List<String> usernames;
        if (user.getRoleId() == 1) {
            usernames = stuRepository.findAll().stream().map(Student::getUserName).toList();
        } else {
            usernames = stuRepository.findStuByTeaId(user.getId()).stream().map(Student::getUserName).toList();
        }

        List<Timestamp> lists = userLoginLogRepository.findLoginTimeByUsernamesAndDateAfter(usernames, oneMonthAgo);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            Timestamp timestamp = lists.get(i);
            String time = new Random().nextBoolean() ? "long" : "short";
            list.add(Map.of("name", i, "time", time, "dd", timestamp.getDate(), "hh", timestamp.getHours()));
        }

        return new ResponseData(ExceptionMsg.SUCCESS, list);



    }

    /**
     * @param user
     * @return
     */
    @Override
    public ResponseData getMap(User user) {
        switch (user.getRoleId()) {
            case 1:
                return new ResponseData(ExceptionMsg.SUCCESS, getAdminMsg());
            case 2:
                return new ResponseData(ExceptionMsg.SUCCESS, getStuMsg(user));
            case 3:
                return new ResponseData(ExceptionMsg.SUCCESS, getTeaMsg(user));
            default:
                return new ResponseData(ExceptionMsg.FAILED, "error");
        }

    }

    private List<Map<String, Object>> getTeaMsg(User user) {
        List<Map<String, Object>> map = new ArrayList<>();
        Integer studentNum = stuRepository.findStuNumByTeaId(user.getId());
        map.add(Map.of("studentNum", studentNum));
        List<Course> courseList = courseRepository.findAllByUserId(user.getId());
        map.add(Map.of("courseNum", courseList.size()));
        courseList.forEach(course -> {
            course.getSessions().forEach(session -> {
                map.add(Map.of("files", getFiles(session)));
            });
        });
        return map;
    }

    public List<File> getFiles(Session session) {
        List<File> files = new ArrayList<>();
        if (session != null) {
            files.addAll(session.getFiles());
        }
        if (session.getChildSessions().size() > 0) {
            session.getChildSessions().forEach(session1 -> {
                files.addAll(getFiles(session1));
            });
        }
        return files;
    }

    private List<Map<String, Object>> getStuMsg(User user) {
        return null;
    }

    public List<Map<String, Object>> getAdminMsg() {
        return null;
    }

    /**
     *  nodes: [
     *           {
     *             id: "数据库原理",
     *             label: "数据库原理",
     *             donutAttrs: {
     *               income: 4,
     *               outcome: 12,
     *               unknown: 22,
     *             },
     *           },
     *           {
     *             id: "范式",
     *             label: "范式",
     *             donutAttrs: {
     *               income: 4,
     *               outcome: 32,
     *               unknown: 12,
     *             },
     *           },
     *           {
     *             id: "mysql",
     *             label: "mysql",
     *             donutAttrs: {
     *               income: 12,
     *               outcome: 22,
     *               unknown: 2,
     *             },
     *           },
     *           {
     *             id: "关系型数据库",
     *             label: "关系型数据库",
     *             donutAttrs: {
     *               income: 12,
     *               outcome: 12,
     *               unknown: 22,
     *             },
     *           },
     *           {
     *             id: "非关系型数据库",
     *             label: "非关系型数据库",
     *             donutAttrs: {
     *               income: 14,
     *               outcome: 2,
     *               unknown: 22,
     *             },
     *           },
     *         ],
     *         edges: [
     *           { source: "数据库原理", target: "范式", size: 10 },
     *           { source: "范式", target: "mysql", size: 5 },
     *           { source: "范式", target: "关系型数据库", size: 20 },
     *           { source: "关系型数据库", target: "非关系型数据库", size: 5 },
     *         ],
     * @param courseId
     * @param user
     * @return
     */
    @Override
    public ResponseData getDonutMap(int courseId, User user) {
        if(user.getRoleId() == 2) {
            return null;
        }
        if(Integer.valueOf(courseId).equals(null)){
            if(user.getRoleId() == 1){
                List<Integer> ids = courseRepository.findAll().stream().map(Course::getId).toList();
                //随机取一个id
                int id = ids.get(new Random().nextInt(ids.size()));
                return getDonutMap(id, user);
            }else{
                List<Integer> ids = courseRepository.findCourseByTeacherId(user.getId()).stream().map(Course::getId).toList();
                //随机取一个id
                int id = ids.get(new Random().nextInt(ids.size()));
                return getDonutMap(id, user);
            }
        }
        //获取课程的所有节点
        Course course=Optional.ofNullable( courseRepository.findById(courseId)).get().orElseThrow(()->new ServiceException("课程不存在"));
        List<Session> sessions = course.getSessions();
        List<Map<String,Object>> nodes = new ArrayList<>();
        List<Map<String,Object>> edges = new ArrayList<>();
        List<Integer> studentIds=stuRepository.findStuByCourseId(course.getId()).stream().map(Student::getId).toList();
        for (Session session : sessions) {
            Map<String,Object> node = new HashMap<>();
            node.put("id",session.getId());
            node.put("label",session.getSessionName());
            DonutAttrs donutAttrs = getState(session.getId(),studentIds);
            node.put("donutAttrs",donutAttrs);
            nodes.add(node);
            if(session.getChildSessions()!=null){
                for (Session session1:session.getChildSessions()){
                    Map<String,Object> node1 = new HashMap<>();
                    node1.put("id",session1.getId());
                    node1.put("label",session1.getSessionName());
                    DonutAttrs donutAttrs1 = getState(session1.getId(),studentIds);
                    node1.put("donutAttrs",donutAttrs1);
                    nodes.add(node1);
                    Map<String,Object> edge = new HashMap<>();
                    edge.put("source",session.getId());
                    edge.put("target",session1.getId());
                    edge.put("size",donutAttrs.outcome-donutAttrs1.outcome);
                    edges.add(edge);
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("nodes",nodes);
        json.put("edges",edges);

        return new ResponseData(ExceptionMsg.SUCCESS,json);
    }

    private DonutAttrs getState(int sessionId, List<Integer> studentIds) {
        DonutAttrs donutAttrs = new DonutAttrs();
        Session session = Optional.ofNullable(sessionRepository.findById(sessionId)).get().orElseThrow(()->new ServiceException("session不存在"));
        List<File> files = session.getFiles();
        for (File file : files) {
            for (ResourceLog resourceLog : file.getResourceLogs()) {
                if (studentIds.contains(resourceLog.getUser().getId())) {
                    //学生id包含在内
                    if (resourceLog.getStatus().equals("已完成")) {
                        donutAttrs.outcome++;
                    }else if(resourceLog.getStatus().equals("未完成")){
                        donutAttrs.unknown++;
                    }else{
                        donutAttrs.income++;
                    }
                }
            }
        }
        return donutAttrs;
    }
}
@Data
class DonutAttrs{
     int income;
    int outcome;
     int unknown;

    public DonutAttrs() {
        this.income = 0;
        this.outcome = 0;
        this.unknown = 0;
    }
}
