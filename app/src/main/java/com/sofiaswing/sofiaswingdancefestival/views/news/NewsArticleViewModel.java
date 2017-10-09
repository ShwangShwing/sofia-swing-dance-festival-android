package com.sofiaswing.sofiaswingdancefestival.views.news;

import java.util.Date;

/**
 * Created by shwangshwing on 10/9/17.
 */

public class NewsArticleViewModel {
    private Date postedOn;
    private String imageUrl;
    private String text;

    public NewsArticleViewModel(Date postedOn, String imageUrl, String text) {
        this.postedOn = postedOn;
        this.text = text;
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
