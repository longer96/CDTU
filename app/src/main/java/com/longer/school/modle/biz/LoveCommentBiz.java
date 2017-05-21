package com.longer.school.modle.biz;

import android.util.Log;

import com.longer.school.Application;
import com.longer.school.adapter.GoodsAdapter;
import com.longer.school.modle.bean.Good;
import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.LoveComment;
import com.longer.school.utils.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by longer on 2017/4/13.
 */

public class LoveCommentBiz implements LoveComment.ILoveCommentBiz {

    @Override
    public void save(Love love, String content, final LoveComment.OnSetLoveCommentLister loveCommentLister) {
        LoveComment loveComment = new LoveComment();
        loveComment.setContent(content);
        loveComment.setMy(Application.my);
        loveComment.setOtherlove(love);
        final LoveComment mloveComment = loveComment;
        loveComment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    loveCommentLister.putSuccess(mloveComment);
                } else {
                    loveCommentLister.putFailed();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void get(Love love, final LoveComment.OnGetLoveComnentLister onGetLoveComnentLister) {
        BmobQuery<LoveComment> query = new BmobQuery<LoveComment>();
        query.order("createdAt");
        query.setLimit(50);
        query.addWhereEqualTo("otherlove", love);
        query.include("my");
        query.findObjects(new FindListener<LoveComment>() {
            @Override
            public void done(List<LoveComment> list, BmobException e) {
                if (e != null) {
                    Log.d("查询表白墙评论出错", e.toString());
                    onGetLoveComnentLister.Failed();
                } else {
                    onGetLoveComnentLister.Success(list);
                }
            }
        });
    }

}
