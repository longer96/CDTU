package com.longer.school.presenter;

import android.util.Log;

import com.longer.school.modle.bean.Food;
import com.longer.school.modle.bean.Store;
import com.longer.school.modle.biz.FoodBiz;
import com.longer.school.modle.biz.StoreBiz;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.activity.Store_Activity;
import com.longer.school.view.iview.IStore_ActivityView;

import java.util.List;

/**
 * Created by longer on 2017/5/15.
 */

public class Store_ActivityPresenter {

    private IStore_ActivityView activityView;
    private Food.IFoodBiz foodBiz;
    private Store.IStoreBiz storeBiz;

    public Store_ActivityPresenter(Store_Activity activityView) {
        this.activityView = activityView;
        foodBiz = new FoodBiz();
        storeBiz = new StoreBiz();
    }

    public void getData() {
        activityView.showrefresh(true);
        //先查询 寝室类 where为 所选值 和 0的寝室
        storeBiz.getStore(FileTools.getshareString("sushe"), new Store.OnGetStoreLister() {
            @Override
            public void Success(List<Store> list) {
                if (list.size() == 0) {
                    Toast.show("不好意思，没有找到小卖部~");
                } else {
                    activityView.getStoreSuccess(list);
                    activityView.createBottomSheetDialog(list);
                }
            }

            @Override
            public void Failed(String e) {
                activityView.getFoodFailed(e);
            }
        });
    }

    public void getFood(final int skip, List<Store> list, String type) {
        activityView.showrefresh(true);
        foodBiz.getFood(skip, list, type, new Food.OnGetFoodLister() {
            @Override
            public void Success(List<Food> list) {
                Log.d("tip", "返回的商品数量：" + list.size());
                activityView.showrefresh(false);
                if (skip != 0) {
                    //i 加 10
                    Store_Activity.i = skip + 10;
                    activityView.addData(list);
                } else {
                    activityView.setData(list);
                }
            }

            @Override
            public void Failed(String e) {
                activityView.showrefresh(false);
                activityView.getFoodFailed(e);
            }
        });

    }
}
