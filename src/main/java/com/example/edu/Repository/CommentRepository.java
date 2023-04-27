package com.example.edu.Repository;

import com.example.edu.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

    @Query("select c.id as id ,c.userTo.id as userToId," +
            "c.userTo.userName as userToName,c.time as time,c.userFrom.id as userFromId," +
            "c.userFrom.userName as userFromName,c.session.id as sessionId ,c.content as content " +
            "from Comment c where c.session.id=?1 order by c.time desc")
    List<Map<String,Object>> findAllBySId(int sId);


    @Query("select c.id as id ,c.userTo.id as userToId," +
            "c.userTo.userName as userToName,c.time as time,c.userFrom.id as userFromId," +
            "c.userFrom.userName as userFromName,c.session.id as sessionId ,c.content as content " +
            "from Comment c where c.userFrom.id=?1 order by c.time desc")
    List<Map<String, Object>> findAllByUserFromId(Integer userId);


    @Query("select c.id as id ,c.userTo.id as userToId," +
            "c.userTo.userName as userToName,c.time as time,c.userFrom.id as userFromId," +
            "c.userFrom.userName as userFromName,c.session.id as sessionId ,c.content as content " +
            "from Comment c where c.session.course.id=?1 order by c.time desc")
    List<Map<String, Object>> findAllByCourseId(Integer courseId);


    @Query("select c.id as id ,c.userTo.id as userToId," +
            "c.userTo.userName as userToName,c.time as time,c.userFrom.id as userFromId," +
            "c.userFrom.userName as userFromName,c.session.id as sessionId ,c.content as content " +
            "from Comment c where c.userTo.id=?1 order by c.time desc")
    List<Map<String, Object>> findBackById(Integer id);

}