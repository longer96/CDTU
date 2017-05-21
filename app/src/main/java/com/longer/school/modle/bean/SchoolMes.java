package com.longer.school.modle.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by longer on 2017/3/31.
 * 接受系统消息,显示在主界面的菜单卡片上
 */
public class SchoolMes extends BmobObject {

    private String type;//消息的类型,比如“校园公告”

    private String infor;//提示的信息

    private String sponsor;//发起者，主办者;  比如 电竞协会

    private String time;//活动时间  比如 2017-3-31 或者 加 15:00

    private String link;//前期可以使用第三方H5界面来实现详细界面  比如 1.指尖秀  2.易企秀

    private boolean show;// 是否显示，用于一段时间后是否还显示 true:显示  false:不显示

    private String qqgroup;//qq群的key  ,可以动态的设置加入QQ群

    private boolean sign;//是否显示报名的按钮

    public interface  ISchoolMesBiz{
        void getschoolmes(OngetSchoolMes ongetSchoolMes);
    }

    public interface OngetSchoolMes{
        void Success(SchoolMes schoolMes);
        void Failes();
    }

    public String getQqgroup() {
        return qqgroup;
    }

    public boolean isSign() {
        return sign;
    }

    public String getType() {
        return type;
    }


    public String getInfor() {
        return infor;
    }


    public String getSponsor() {
        return sponsor;
    }


    public String getTime() {
        return time;
    }


    public String getLink() {
        return link;
    }


    public boolean isShow() {
        return show;
    }

}
