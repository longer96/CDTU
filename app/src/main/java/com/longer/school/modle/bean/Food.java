package com.longer.school.modle.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 同学的店的商品类
 * Created by longer on 2017/5/5.
 */

public class Food extends BmobObject {

    private Store store;//来自那个店铺
    private String name;//商品名字
    private String price;//价格
    private String type;//商品类型  比如：饮料  小吃  生活用品  其他
    private boolean over;//是否已售罄，true为已售罄
    private Integer sellnum;//总共销售数量

    private BmobFile pic1;//图片
    private BmobFile pic2;//图片

    public interface IFoodBiz {

        void getFood(int skip, List<Store> list, String type, OnGetFoodLister onGetFoodLister);

    }

    public interface OnGetFoodLister {

        void Success(List<Food> list);

        void Failed(String e);

    }

    public Store getStore() {
        return store;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public boolean isOver() {
        return over;
    }

    public Integer getSellnum() {
        return sellnum;
    }

    public BmobFile getPic1() {
        return pic1;
    }

    public BmobFile getPic2() {
        return pic2;
    }
}
