package com.longer.school.presenter;


import android.util.Log;

import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.LoveComment;
import com.longer.school.modle.biz.LoveBiz;
import com.longer.school.modle.biz.LoveCommentBiz;
import com.longer.school.utils.PublicTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.iview.ILoveOne_ActivityView;

import java.util.List;

/**
 * Created by longer on 2017/4/13.
 */

public class LoveOneActivityPresenter {

    private ILoveOne_ActivityView loveOneActivityView;

    private LoveComment.ILoveCommentBiz commentBiz;

    private Love.ILoveBiz iLoveBiz;

    public LoveOneActivityPresenter(ILoveOne_ActivityView loveOneActivityView) {
        this.loveOneActivityView = loveOneActivityView;
        this.commentBiz = new LoveCommentBiz();
        this.iLoveBiz = new LoveBiz();
    }

    public void showCommentview() {
        PublicTools.openkeyboard(loveOneActivityView.getedittext());
        loveOneActivityView.showCommentView();
    }

    public void putCommenting() {
        loveOneActivityView.hideComentView();
        loveOneActivityView.showCommenting();
        commentBiz.save(loveOneActivityView.getLove(), loveOneActivityView.getComment(), new LoveComment.OnSetLoveCommentLister() {
            @Override
            public void putSuccess(LoveComment comment) {
                loveOneActivityView.clearCommnet();
                loveOneActivityView.addCommentData(comment);
            }

            @Override
            public void putFailed() {
                loveOneActivityView.showCommentFail();
            }
        });
        PublicTools.closekeyboard(loveOneActivityView.getedittext());
        loveOneActivityView.showCommenting();
    }

    public void getCommentdata() {
        commentBiz.get(loveOneActivityView.getLove(), new LoveComment.OnGetLoveComnentLister() {
            @Override
            public void Success(List<LoveComment> list) {
//                Log.d("tip", "得到数据成功" + list.size() + "条");
                int commentsize = list.size();
                if (commentsize > 0) {
                    loveOneActivityView.setSms(commentsize);
                    loveOneActivityView.setCommentData(list);
                }
                //并且
            }

            @Override
            public void Failed() {
                loveOneActivityView.showsetCommentDataFail();
            }
        });

    }

    public void upLoveLike() {
        if (!loveOneActivityView.checksblike()) {
            return;
        }
        iLoveBiz.updataLike(loveOneActivityView.getLove(),true, new Love.OnUpdataLikeOrComment() {
            @Override
            public void Success() {
                Log.d("tip", "点赞成功");
            }

            @Override
            public void Failed() {
                Log.d("tip", "点赞失败");
            }
        });
    }

    public void upLoveComment() {
        iLoveBiz.updataComment(loveOneActivityView.getLove(), new Love.OnUpdataLikeOrComment() {
            @Override
            public void Success() {
                Log.d("tip", "更新评论数量成功");
            }

            @Override
            public void Failed() {
                Log.d("tip", "更新评论数量失败");
            }
        });
    }

    public void getLoveLike() {
        iLoveBiz.getLike(loveOneActivityView.getLove(), new Love.OnGetLike() {
            @Override
            public void Success(Love love) {
                if (love.getLike() != null && love.getLike() != 0) {
                    loveOneActivityView.setLike(love.getLike());
                }
            }

            @Override
            public void Failed() {
                Log.d("tip", "获取点赞数失败");
            }
        });
    }

}
