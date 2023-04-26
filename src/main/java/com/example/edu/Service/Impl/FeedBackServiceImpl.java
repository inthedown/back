package com.example.edu.Service.Impl;

import com.example.edu.Entity.Comment;
import com.example.edu.Entity.User;
import com.example.edu.Form.AddFeedBackDto;
import com.example.edu.Form.GetFeedBackDto;
import com.example.edu.Repository.CommentRepository;
import com.example.edu.Repository.SessionRepository;
import com.example.edu.Repository.UserRepository;
import com.example.edu.Service.FeedBackService;
import com.example.edu.result.ExceptionMsg;
import com.example.edu.result.ResponseData;
import com.example.edu.result.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
@Slf4j
public class FeedBackServiceImpl implements FeedBackService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseData getList(GetFeedBackDto getFeedBackDto , User user) {
        if(user==null){
            throw new ServiceException("用户未登录");
        }
        String type=getFeedBackDto.getType();
        List<Map<String,Object>> commentList=null;
        switch (type){
            case "课程":commentList = commentRepository.findAllByCourseId(getFeedBackDto.getCourseId());;
                break;
            case "节点":commentList=commentRepository.findAllBySId(getFeedBackDto.getSessionId());
                break;
            case "用户":commentList=commentRepository.findAllByUserFromId(getFeedBackDto.getUserId());
                break;
            default:
                return new ResponseData(ExceptionMsg.FAILED,"type参数错误");
        }


        return new ResponseData(ExceptionMsg.SUCCESS,commentList);
    }


    @Override
    public ResponseData add(AddFeedBackDto addFeedBackDto) {
        Comment comment = new Comment();
        log.info("addFeedBackDto:"+addFeedBackDto.toString());
        comment.setSession(Optional.ofNullable( sessionRepository.findById( addFeedBackDto.getSId())).get().orElseThrow(()->new ServiceException("课程节点不存在")));
        comment.setUserFrom(Optional.ofNullable( userRepository.findById( addFeedBackDto.getUserFromId())).get().orElseThrow(()->new ServiceException("用户不存在")));
        comment.setUserTo(Optional.ofNullable( userRepository.findById( addFeedBackDto.getUserToId())).get().orElseThrow(()->new ServiceException("用户不存在")));
        comment.setContent(addFeedBackDto.getContent());
        comment.setTime(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);
        return new ResponseData(ExceptionMsg.SUCCESS,"添加成功");
    }
}
