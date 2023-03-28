package com.example.boe.Form;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 {"id":"1-3","name":"阿松大","label":"大苏打","date":["2023-03-08T16:00:00.000Z","2023-04-11T16:00:00.000Z"],"fileList":[{"name":"Angel By The Wings.m4a","percentage":0,"status":"ready","size":3904632,"raw":{"uid":1679382228976},"uid":1679382228976},{"name":"Elastic Heart.m4a","percentage":0,"status":"ready","size":3127461,"raw":{"uid":1679382228978},"uid":1679382228978},{"name":"binding lights.m4a","percentage":0,"status":"ready","size":2468765,"raw":{"uid":1679382228979},"uid":1679382228979}]}
 */
@Data
@Slf4j
public class SessionDto {

    private String id;
    private String name;
    private String label;

    @Nullable
    private List<FileDto> fileList;

    @Nullable
    private String[] date;
    @Nullable
    private List<SessionDto> children;

    // 2023-03-08T16:00:00.000Z转成Timestamp

    public Timestamp getStartTime() {
        Timestamp timestamp = changeTime(date[0]);
        return timestamp;
    }
    public Timestamp getEndTime() {
        Timestamp timestamp = changeTime(date[1]);
        return timestamp;
    }
    @SneakyThrows
    private Timestamp changeTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date parsedDate =dateFormat.parse(time);

        return new Timestamp(parsedDate.getTime());
    }
}
