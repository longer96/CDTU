package com.longer.school.modle.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 表白墙 类
 * Created by longer on 2016/9/14.
 */
public class Love extends BmobObject {

    private String object;//表白对象
    private String content;//表白内容
    private String name;//表白者姓名
    private User user;//表白者
    private Integer like;//点赞
    private Integer commentnum;//评论的数量

    public interface ILoveBiz {

        void updataLike(Love love, boolean isadd ,OnUpdataLikeOrComment updataLoveLike);

        void updataComment(Love love, OnUpdataLikeOrComment updataLoveLike);

        void getLike(Love love, OnGetLike getLike);

        void getLove(OnGetLove onGetLove);

    }

    public interface OnUpdataLikeOrComment {

        void Success();

        void Failed();
    }

    public interface OnGetLike {

        void Success(Love love);

        void Failed();
    }
    public interface OnGetLove {

        void Success(List<Love> list);

        void Failed();
    }

    public Integer getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(Integer commentnum) {
        this.commentnum = commentnum;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getLike() {

        return like;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
