package com.longer.school.modle.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 主界面顶部的图片，一般有6个 类
 * 数据库中至少保证有4条数据
 * 如果不希望显示网络图片可以设置url 为空
 * Created by longer on 2016/9/14.
 */
public class PicHeadTip extends BmobObject {

    private String url;//图片的地址
    private String infor;//内容简介

    public interface IPicHeadTipBiz {
        void getpicheadtip(OngetPicHeadTip ongetPicHeadTip);
    }

    public interface OngetPicHeadTip {

        void Success(List<PicHeadTip> list);

        void Failed();
    }

    public String getUrl() {
        return url;
    }

    public String getInfor() {
        return infor;
    }
}
