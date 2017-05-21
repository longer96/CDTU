package com.longer.school.modle.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 报名表
 * Created by longer on 2017/4/13.
 */
public class SignUp extends BmobObject {

    private String object;//报名的对象，比如手游协会

    private String name;//姓名

    private String tel;//电话

    private String bj;//班级

    private User my;//提交报名的人

    private String infor;//补充


    public interface ISignUpBiz {
        void set(String object,String name, String tel, String bj,String infor, OnsetSignUpLister onsetSignUpLister);
    }

    public interface OnsetSignUpLister {

        void Success();

        void Failed();
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }

    public void setMy(User my) {
        this.my = my;
    }
}
