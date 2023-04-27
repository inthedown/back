package com.example.edu.Controller;

import com.example.edu.Form.AddFeedBackDto;
import com.example.edu.Form.GetFeedBackDto;
import com.example.edu.Service.FeedBackService;
import com.example.edu.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
    @GetMapping("/getBackMsg")
    public ResponseData getBackMsg(){
        return feedBackService.getBackMsg(getUser());
    }

}
