package com.example.edu.Entity;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private String title;
    private String name;
    private String path;
    private List<Menu> children;
}
