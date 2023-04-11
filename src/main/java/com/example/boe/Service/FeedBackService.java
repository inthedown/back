package com.example.boe.Service;

import com.example.boe.Entity.User;
import com.example.boe.Form.AddFeedBackDto;
import com.example.boe.Form.GetFeedBackDto;
import com.example.boe.result.ResponseData;

public interface FeedBackService {
    ResponseData getList(GetFeedBackDto getFeedBackDto, User user);

    ResponseData add(AddFeedBackDto addFeedBackDto);
}
