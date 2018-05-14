package com.sofiaswing.sofiaswingdancefestival.models;

import java.util.Date;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticleModel {
    private String id;
    private Date postedOn;
    private String imageUrl;
    private String text;

    public NewsArticleModel(Date postedOn, String imageUrl, String text) {
        this.postedOn = postedOn;
        if (postedOn == null) this.postedOn = new Date(0);
        this.imageUrl = imageUrl;
        this.text = text;
    }

    public NewsArticleModel(String id, Date postedOn, String imageUrl, String text) {
        this(postedOn, imageUrl, text);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getPostedOn() {
        return postedOn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }
}
