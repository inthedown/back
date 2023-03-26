package com.example.boe.Form;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
public class WebMessage {
    private String deviceId;
    private String type;
    private Timestamp startTime;
    private Timestamp endTime;
    private int toatalTime;
    private List<Map<String,Object>> fileList;
}
