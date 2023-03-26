package com.example.boe.Repository;

import com.example.boe.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query(value = "select * from course where id in (select cla_id from cla_stu_sheet where stu_id = ?1)", nativeQuery = true)
    List<Course> findCourseByStudentId(int id);

    @Query(value = "select * from course where teacher_id = ?1", nativeQuery = true)
    List<Course> findCourseByTeacherId(int id);

@Query("select c from Course c left join fetch c.sessions where c.id = :id")
//    @Query(value = "select * from course where id = :id", nativeQuery = true)
    Course findByIdWithSessions(@Param("id")  int id);


}
