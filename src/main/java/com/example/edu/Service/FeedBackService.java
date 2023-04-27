package com.example.edu.Service;

import com.example.edu.Entity.User;
import com.example.edu.Form.AddFeedBackDto;
import com.example.edu.Form.GetFeedBackDto;
import com.example.edu.result.ResponseData;

public interface FeedBackService {
    ResponseData getList(GetFeedBackDto getFeedBackDto, User user);

    ResponseData add(AddFeedBackDto addFeedBackDto);

    ResponseData getBackMsg(User user);
}
