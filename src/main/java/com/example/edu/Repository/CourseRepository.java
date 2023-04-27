package com.example.edu.Repository;

import com.example.edu.Entity.Course;
import com.example.edu.Entity.Session;
import com.example.edu.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    //q:如何关联course和student,依靠student的classes_id和course的classes_id
    @Query(value = "select * from course where classes_id in (select classes_id from student where id=?1)", nativeQuery = true)
    List<Course> findCourseByStudentId(int id);

    @Query(value = "select * from course where teacher_id = ?1", nativeQuery = true)
    List<Course> findCourseByTeacherId(int id);

@Query("select c from Course c left join fetch c.sessions where c.id = :id")
    Course findByIdWithSessions(@Param("id")  int id);

    @Query("select s from Session s where s.course.id = :courseId")
    Session findSessionsByCourseId(@Param("courseId") int courseId);

    @Query(value = "select * from course where course_name = ?1", nativeQuery = true)
    Course findByName(String courseName);

    @Query(value = "select * from course where classes_id = ?1", nativeQuery = true)
    void deleteAllByIdIn(List<Integer> ids);
    @Query(value = "select * from course where teacher_id = ?1", nativeQuery = true)
    List<Course> findAllByUserId(Integer id);
    @Query(value = "select c.course.courseName,c.score from StuCourceManage c where c.course in ?1 and c.student = ?2")
    List<Map<String, Object>> findScore(List<Course> courses, User user);
    @Query(value = "select s.userName from Student s where s.classes.id " +
            "in (SELECT c.classes.id FROM Course c where c.teacher.id = ?1)")
    List<String> findStuByTeaId(Integer id);

}
