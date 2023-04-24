package com.example.edu.Repository;

import com.example.edu.Entity.StuCourceManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface StuCourceManageRepository extends JpaRepository<StuCourceManage, Integer>, JpaSpecificationExecutor<StuCourceManage> {
   @Query(value = "select * from stu_cource_manage where user_id = ?1 and course_id = ?2",nativeQuery = true)
    StuCourceManage findByUserIdAndCourseId(Integer userId, Integer courseId);
}