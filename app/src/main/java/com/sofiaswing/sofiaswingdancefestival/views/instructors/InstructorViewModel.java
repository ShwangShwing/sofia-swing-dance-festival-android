package com.sofiaswing.sofiaswingdancefestival.views.instructors;

/**
 * Created by shwangshwing on 10/10/17.
 */

public class InstructorViewModel {
    String name;
    String imageUrl;
    String type;
    String description;

    public InstructorViewModel(String name, String imageUrl, String type, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.description = description;
    }

    public InstructorViewModel(String name, String imageUrl, String type) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.description = "";
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
