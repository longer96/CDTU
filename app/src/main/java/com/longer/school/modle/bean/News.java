package com.longer.school.modle.bean;

import cn.bmob.v3.BmobObject;

/**
 * 失物招领 类
 * Created by longer on 2016/9/14.
 */
public class News extends BmobObject {

    private String title;
    private String time;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
