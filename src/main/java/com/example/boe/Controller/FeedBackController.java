package com.example.boe.Controller;

import com.example.boe.Form.AddFeedBackDto;
import com.example.boe.Service.FeedBackService;
import com.example.boe.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FeedBackController extends BaseController{

    @Autowired
    private FeedBackService feedBackService;

    @RequestMapping("/getList")
    public ResponseData getList(int id){
        return feedBackService.getList(id,getUser());
    }
    @RequestMapping("/add")
    public ResponseData add(@RequestBody AddFeedBackDto addFeedBackDto){
        return feedBackService.add(addFeedBackDto);
    }
}
