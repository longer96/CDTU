package com.longer.school.modle.biz;

import com.longer.school.Application;
import com.longer.school.modle.bean.Food;
import com.longer.school.modle.bean.Store;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by longer on 2017/5/15.
 */

public class FoodBiz implements Food.IFoodBiz {
    @Override
    public void getFood(int skip, List<Store> list, String type, final Food.OnGetFoodLister onGetFoodLister) {
        //进行or 查询
        BmobQuery<Food> eq = null;
        List<BmobQuery<Food>> queries = new ArrayList<BmobQuery<Food>>();
        for (Store shore : list) {
            eq = new BmobQuery<Food>();
            eq.addWhereEqualTo("store", shore);

            queries.add(eq);
            eq = null;
        }

        BmobQuery<Food> query = new BmobQuery<Food>();
        query.order("-updatedAt");
        if (type != null && !"所有商品".equals(type)) {
            query.addWhereEqualTo("type", type);
        }
        query.or(queries);
        query.setLimit(10);
        if (skip != 0) {
            query.setSkip(skip);
        }
        query.findObjects(new FindListener<Food>() {
            @Override
            public void done(List<Food> list, BmobException e) {
                if (e == null) {
                    onGetFoodLister.Success(list);
                } else {
                    onGetFoodLister.Failed(e.toString());
                }
            }
        });
    }
}
