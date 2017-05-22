package com.longer.school.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.longer.school.R;
import com.longer.school.adapter.PictureAdapter;
import com.longer.school.utils.PinchImageViewPager;

import java.util.ArrayList;

/**
 * Created by Axu on 2016/9/20.
 */
public class Picture_Activity extends Activity {
    private String str;
    private TextView tv_posotion;
    public Context context;
    public int position;
    public PictureAdapter imageAdapter;

    public ArrayList<String> url = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        tv_posotion = (TextView) findViewById(R.id.tv_picture_position);
        context = Picture_Activity.this;
        Intent intent = getIntent();
        url = (ArrayList<String>) intent.getSerializableExtra("url");
        position = (int) intent.getSerializableExtra("position");
        str = Integer.toString(position + 1) + "/" + Integer.toString(url.size());
        tv_posotion.setText(str);
        final PinchImageViewPager pager = (PinchImageViewPager) findViewById(R.id.pic);
        pager.setOnPageChangeListener(listen);
        imageAdapter = new PictureAdapter(context, url, pager);
        pager.setAdapter(imageAdapter);
        pager.setCurrentItem(position);
    }


    public PinchImageViewPager.OnPageChangeListener listen = new PinchImageViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            str = Integer.toString(position + 1) + "/" + Integer.toString(url.size());
            tv_posotion.setText(str);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}