package com.longer.school.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.longer.school.Application;
import com.longer.school.R;

public class ImageActivity extends AppCompatActivity {
    ImageView iv_calendar;
    public int pic_id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_image);
        Intent intent = getIntent();
        pic_id = (int) intent.getSerializableExtra("pic_id");
        title = (String) intent.getSerializableExtra("title_name");
        iv_calendar = (ImageView) findViewById(R.id.piv_calan_canlendar);

        if(pic_id == 1000){//如果传过来的数字为1001表示是网络图片
            String url = (String) intent.getSerializableExtra("url");//有的图片传地址过来，加载网络图片
            Log.d("tip","url 不为空:" + url);
            Glide.with(ImageActivity.this).load(url)
                    .placeholder(R.mipmap.imageselector_photo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.imageselector_photo)
                    .into(iv_calendar);
        }else{
            Log.d("tip","url 为空");
            iv_calendar.setImageResource(pic_id);
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
