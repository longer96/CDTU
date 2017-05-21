package com.longer.school.modle.biz;

import android.util.Log;

import com.longer.school.modle.bean.Store;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by longer on 2017/5/11.
 */

public class StoreBiz implements Store.IStoreBiz {
    @Override
    public void getStore(String where, final Store.OnGetStoreLister onGetStoreLister) {

        BmobQuery<Store> eq1 = new BmobQuery<Store>();
        eq1.addWhereEqualTo("where", where);
        BmobQuery<Store> eq2 = new BmobQuery<Store>();
        eq2.addWhereEqualTo("where", "0");
        List<BmobQuery<Store>> queries = new ArrayList<BmobQuery<Store>>();
        queries.add(eq1);
        queries.add(eq2);
        BmobQuery<Store> query = new BmobQuery<Store>();
        query.or(queries);
        query.findObjects(new FindListener<Store>() {
            @Override
            public void done(List<Store> list, BmobException e) {
                if (e == null) {
                    Log.d("tip", "该宿舍有多少间：" + list.size());
                    onGetStoreLister.Success(list);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    onGetStoreLister.Failed(e.getMessage());
                }
            }
        });
    }
}













