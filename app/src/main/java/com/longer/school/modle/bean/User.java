package com.longer.school.modle.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 用户类
 * Created by longer on 2016/9/14.
 */
public class User extends BmobUser {
    private String sex;//性别
    private String name;//姓名
    private String bj;//班级
    private String yx;//院系
    private String zy;//专业
    private String birthday;//生日
    private String place;//老家

    private String room;//寝室号
    private String nickname;//昵称
    private String pwd;//可见的pwd
    private BmobFile headpic;//头像

    public interface UserBiz {
        void upDateUserInfor(String objectid);

        void upPhoneVerified(User user, String phone,boolean isbinding, OnUpUser onUpUser);//更新手机是否已经验证

        void clearPhoneNum(User user, OnUpUser onUpUser);//清楚原有的手机号

        void upDataNicenameandRoom(User user,String nicename,String sex,String room, OnUpUser onUpUser);
    }

    public interface OnUpUser {

        void Success();

        void Failed();
    }


    public BmobFile getHeadpic() {
        return headpic;
    }

    public void setHeadpic(BmobFile headpic) {
        this.headpic = headpic;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getYx() {
        return yx;
    }

    public void setYx(String yx) {
        this.yx = yx;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBj() {
        return bj;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }

}
