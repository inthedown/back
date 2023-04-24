package com.example.edu.Form;

import lombok.Data;

/**
 *  const data = {
 *     resource_type: "video",
 *     url: state.url,
 *     played_percentage: playedPercentage,
 *     played_time: playedTime,
 *     total_time: videoDuration,
 *     start_time: state.startTime,
 *     end_time: endTime,
 *     fileInfo: state.row,
 *   };
 */
@Data
public class UserResourceLogForm {
    private String resourceType;
    private String url;
    private Integer playedPercentage;
    private Integer playedTime;
    private Integer totalTime;
    private long startTime;
    private long endTime;
    private FileForm fileInfo;
    private Integer userId;
}
