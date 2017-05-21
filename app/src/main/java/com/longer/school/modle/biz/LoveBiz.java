package com.longer.school.modle.biz;

import android.util.Log;

import com.longer.school.modle.bean.Love;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by longer on 2017/4/15.
 */

public class LoveBiz implements Love.ILoveBiz {
    @Override
    public void updataLike(Love love, boolean isadd, final Love.OnUpdataLikeOrComment likeOrComment) {
        if (isadd) {
            love.increment("like");
        } else {
            love.increment("like", -1);
        }
        love.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    likeOrComment.Failed();
                } else {
                    likeOrComment.Success();
                }
            }
        });
    }

    @Override
    public void updataComment(Love love, final Love.OnUpdataLikeOrComment likeOrComment) {
        love.increment("commentnum");
        love.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    likeOrComment.Failed();
                    e.printStackTrace();
                } else {
                    likeOrComment.Success();
                }
            }
        });
    }

    @Override
    public void getLike(Love love, final Love.OnGetLike onGetLike) {
        BmobQuery<Love> query = new BmobQuery<Love>();
        query.getObject(love.getObjectId(), new QueryListener<Love>() {
            @Override
            public void done(Love love, BmobException e) {
                if (e != null) {
                    onGetLike.Failed();
                    e.printStackTrace();
                } else {
                    onGetLike.Success(love);
                }
            }
        });
    }

    @Override
    public void getLove(final Love.OnGetLove onGetLove) {
        BmobQuery<Love> bmobQuery = new BmobQuery<Love>();
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(20);//默认返回10条数据
        bmobQuery.findObjects(new FindListener<Love>() {
            @Override
            public void done(List<Love> list, BmobException e) {
                if (e != null) {
                    onGetLove.Failed();
                    e.printStackTrace();
                } else {
                    List<Love> mlist = new ArrayList<Love>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getContent().length() > 60) {
                            mlist.add(list.get(i));
                        }
                    }
                    if(mlist.size() > 6){
                        onGetLove.Success(mlist.subList(0,5));
                    }
                    if (mlist.size() > 2) {
                        onGetLove.Success(mlist);
                    }
                }
            }
        });
    }

}
