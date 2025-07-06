package com.example.wanderly;

import java.io.Serializable;

public class Place implements Serializable {
    public int id; // 添加的字段
    public String name;
    public String location;
    public int imageResId;
    public String description;

    public Place(int id, String name, String location, int imageResId, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.imageResId = imageResId;
        this.description = description;
    }
}
