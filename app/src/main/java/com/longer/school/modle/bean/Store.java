package com.longer.school.modle.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 同学的店的 店铺类
 * Created by longer on 2017/5/5.
 */

public class Store extends BmobObject {

    private String name;//店铺名字  6471小卖部
    private String where;//几舍的  1 2 3 4 5 6 7   0：表示所有
    private String number;//寝室号   6471
    private String boss;// 负责人   longer
    private String tel;//负责人电话
    private boolean state;//店铺是否还在卖东西  true:在

    public interface IStoreBiz{
        void getStore(String where,OnGetStoreLister onGetStoreLister);
    }


    public interface OnGetStoreLister{

        void Success(List<Store> list);

        void Failed(String e);
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getWhere() {
        return where;
    }

    public String getBoss() {
        return boss;
    }

    public String getTel() {
        return tel;
    }

    public boolean isState() {
        return state;
    }
}
