package com.sofiaswing.sofiaswingdancefestival.models;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorModel {
    private String id;
    private String name;
    private String imageUrl;
    private String type;
    private String description;

    public InstructorModel(String id, String name, String imageUrl, String type, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.description = description;
    }

    public InstructorModel(String id, String name, String imageUrl, String type) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.description = "";
    }

    public InstructorModel(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = "";
        this.description = "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
