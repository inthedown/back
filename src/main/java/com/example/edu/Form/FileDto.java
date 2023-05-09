package com.example.edu.Form;

import lombok.Data;
/**
 *  {
 *      *      *                         "name": "binding lights.m4a",
 *      *      *                         "percentage": 0,
 *      *      *                         "size": 2468765,
 *      *      *                         "raw": {
 *      *      *                             "uid": 1679379584179
 *      *      *                         },
 *      *      *                         "uid": 1679379584179
 *      *      *                     }
 */
@Data
public class FileDto {

    private String name;
    private int percentage;
    private String size;
    private String uid;
}
