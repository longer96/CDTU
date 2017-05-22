package com.longer.school.utils;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.longer.school.R;

/**
 * longer
 */
public class ScrollingFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

//定义toolbar高度和状态栏高度
    private int toolbarHeight;
    private double statusBarHeight;

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        //得到两个高度值
        this.toolbarHeight = getToolbarHeight(context);
        this.statusBarHeight = getStatusBarHeight(context);

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        //设定依赖的父布局为AppBarLayout
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
//        Log.d("tgp", "onDependentViewChanged: ");
        if (dependency instanceof AppBarLayout) {
            //FAB的自身高度+据底边高度的和=要实现FAB隐藏需要向下移动的距离
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = (float)dependency.getY()/(float)toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }

    private int getToolbarHeight(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = android.support.v7.appcompat.R.attr.actionBarSize;
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }

        return actionBarHeight;
    }

    //状态栏高度的方法
    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}