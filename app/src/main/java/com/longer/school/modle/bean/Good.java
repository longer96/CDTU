package com.longer.school.modle.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 跳蚤市场类
 * Created by longer on 2016/9/14.
 */
public class Good extends BmobObject {
    private String title;//标题
    private String infor;//介绍
    private String price;//价格
    private boolean complete;//是否已经完成交易，true 为是
    private String name;
    private String link_qq;//联系方式，QQ
    private String link_tel;//联系方式，电话
    private User seller;//卖家
    private BmobFile pic1;//图片
    private BmobFile pic2;//图片
    private BmobFile pic3;//图片
    private boolean knife;//是否可面议价格，true为可以面议价格
    private boolean type;//  true: 出售   false:求购
    private String kind;//种类  学习相关  生活用品  其他

    public interface GoodBiz{
        void getgoods(OnGetGoods onGetGoods);
    }

    public interface OnGetGoods{
        void Success(List<Good> list);
        void Failed();
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public boolean isType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getLink_tel() {
        return link_tel;
    }

    public void setLink_tel(String link_tel) {
        this.link_tel = link_tel;
    }

    public String getLink_qq() {
        return link_qq;
    }

    public void setLink_qq(String link_qq) {
        this.link_qq = link_qq;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isKnife() {
        return knife;
    }

    public void setKnife(boolean knife) {
        this.knife = knife;
    }


}
