package com.longer.school.modle.bean;

import cn.bmob.v3.BmobObject;

/**
 * 学校黄页类
 * Created by longer on 2016/9/14.
 */
public class Yellow extends BmobObject {
    private String type;//类型
    private String tel;
    private String tag;//标签
    private String qq;
    private String name;
    private String infor;//简介
    private String dw;//单位

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }
}
