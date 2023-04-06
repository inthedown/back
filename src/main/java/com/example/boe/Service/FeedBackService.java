package com.example.boe.Service;

import com.example.boe.Entity.User;
import com.example.boe.Form.AddFeedBackDto;
import com.example.boe.result.ResponseData;

public interface FeedBackService {
    ResponseData getList(int id, User user);

    ResponseData add(AddFeedBackDto addFeedBackDto);
}
