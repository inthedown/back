package com.example.boe.Controller;

import com.example.boe.Form.AddFeedBackDto;
import com.example.boe.Form.GetFeedBackDto;
import com.example.boe.Service.FeedBackService;
import com.example.boe.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("feedback")
public class FeedBackController extends BaseController{

    @Autowired
    private FeedBackService feedBackService;

    @PostMapping("/getList")
    public ResponseData getList( @RequestBody GetFeedBackDto getFeedBackDto){
        return feedBackService.getList(getFeedBackDto,getUser());
    }
    @RequestMapping("/add")
    public ResponseData add(@RequestBody AddFeedBackDto addFeedBackDto){
        return feedBackService.add(addFeedBackDto);
    }
}
