package com.longer.school.modle.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 失物招领 类
 * Created by longer on 2016/9/14.
 */
public class Lost extends BmobObject {

    private String title;
    private String infor;
    private String name;
    private boolean complete;//是否已经完成交易，true 为是
    private String link_qq;//联系方式，可以为QQ
    private String link_tel;//联系方式，电话
    private User user;//发布者
    private boolean type;// true: 招领   false:失物
    private BmobFile pic1;//图片
    private BmobFile pic2;//图片
    private BmobFile pic3;//图片
    private String thing;//物品
    private String where;//地点

    public String getThing() {return thing;}

    public void setThing(String thing){this.thing=thing;}

    public String getWhere(){return where;}

    public void setWhere(String where){this.where=where;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getLink_qq() {
        return link_qq;
    }

    public void setLink_qq(String link_qq) {
        this.link_qq = link_qq;
    }

    public String getLink_tel() {
        return link_tel;
    }

    public void setLink_tel(String link_tel) {
        this.link_tel = link_tel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public BmobFile getPic1() {
        return pic1;
    }

    public void setPic1(BmobFile pic1) {
        this.pic1 = pic1;
    }

    public BmobFile getPic2() {
        return pic2;
    }

    public void setPic2(BmobFile pic2) {
        this.pic2 = pic2;
    }

    public BmobFile getPic3() {
        return pic3;
    }

    public void setPic3(BmobFile pic3) {
        this.pic3 = pic3;
    }
}
