package com.longer.school.modle.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 表白墙 对应的评论表
 * Created by longer on 2017/4/13.
 */
public class LoveComment extends BmobObject {

    private String content;//评论内容

    private Love otherlove;//对应的表白

    private User my;//对评论的人

    public interface ILoveCommentBiz {
        void save(Love love, String content, OnSetLoveCommentLister onSetLoveCommentLister);

        void get(Love love, OnGetLoveComnentLister onGetLoveComnentLister);

    }

    public interface OnSetLoveCommentLister {

        void putSuccess(LoveComment comment);

        void putFailed();

    }

    public interface OnGetLoveComnentLister {

        void Success(List<LoveComment> list);

        void Failed();
    }




    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Love getOtherlove() {
        return otherlove;
    }

    public void setOtherlove(Love otherlove) {
        this.otherlove = otherlove;
    }

    public User getMy() {
        return my;
    }

    public void setMy(User my) {
        this.my = my;
    }

}
