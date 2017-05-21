package com.longer.school.modle.biz;

import android.util.Log;

import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.PicHeadTip;
import com.longer.school.modle.bean.User;
import com.longer.school.utils.FileTools;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by longer on 2017/4/17.
 */

public class PicHeadTipBiz implements PicHeadTip.IPicHeadTipBiz {
    @Override
    public void getpicheadtip(final PicHeadTip.OngetPicHeadTip ongetPicHeadTip) {

        BmobQuery<PicHeadTip> query = new BmobQuery<PicHeadTip>();
        query.order("-createdAt");
        query.setLimit(6);
        query.findObjects(new FindListener<PicHeadTip>() {
            @Override
            public void done(List<PicHeadTip> list, BmobException e) {
                if (e != null) {
                    ongetPicHeadTip.Failed();
                } else {
                    ongetPicHeadTip.Success(list);
                }
            }
        });
    }
}
