package com.example.boe.Service.Impl;

import com.example.boe.Entity.Comment;
import com.example.boe.Entity.User;
import com.example.boe.Form.AddFeedBackDto;
import com.example.boe.Repository.CommentRepository;
import com.example.boe.Repository.SessionRepository;
import com.example.boe.Repository.UserRepository;
import com.example.boe.Service.FeedBackService;
import com.example.boe.result.ExceptionMsg;
import com.example.boe.result.ResponseData;
import com.example.boe.result.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
@Service
public class FeedBackServiceImpl implements FeedBackService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseData getList(int sId , User user) {
//        if(user==null){
//            throw new ServiceException("用户未登录");
//        }
        List<Comment> commentList = commentRepository.findAllBySId(sId);

        return new ResponseData(ExceptionMsg.SUCCESS,commentList);
    }

    @Override
    public ResponseData add(AddFeedBackDto addFeedBackDto) {
        Comment comment = new Comment();
        comment.setSession(Optional.ofNullable( sessionRepository.findById( addFeedBackDto.getSId())).get().orElseThrow(()->new ServiceException("课程节点不存在")));
        comment.setUserFrom(Optional.ofNullable( userRepository.findById( addFeedBackDto.getUserFromId())).get().orElseThrow(()->new ServiceException("用户不存在")));
        comment.setUserTo(Optional.ofNullable( userRepository.findById( addFeedBackDto.getUserToId())).get().orElseThrow(()->new ServiceException("用户不存在")));
        comment.setContent(addFeedBackDto.getContent());
        comment.setTime(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);
        return new ResponseData(ExceptionMsg.SUCCESS,"添加成功");
    }
}
