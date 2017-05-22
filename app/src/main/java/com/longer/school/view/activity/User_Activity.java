package com.longer.school.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.presenter.User_ActivityPresenter;
import com.longer.school.utils.GlideCircleTransform;
import com.longer.school.view.iview.IUserActivityView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class User_Activity extends AppCompatActivity implements IUserActivityView {

    public Context context;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_user_phone_state)
    TextView tvUserPhoneState;
    @Bind(R.id.tv_user_school_state)
    TextView tvUserSchoolState;
    @Bind(R.id.iv_user_bg)
    ImageView ivUserBg;
    @Bind(R.id.iv_user_headpic)
    ImageView ivUserHeadpic;
    private User_ActivityPresenter activityPresenter = new User_ActivityPresenter(this);
    public static User_Activity instant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_user);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        inti();
    }

    public void inti() {
        context = User_Activity.this;
        instant = User_Activity.this;

        Glide.with(context)
                .load(R.drawable.ali_head)
                .bitmapTransform(new BlurTransformation(context, 12, 1))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(ivUserBg);

        Glide.with(context)
                .load(R.drawable.head_boy).centerCrop()
                .transform(new GlideCircleTransform(context, 1, context.getResources().getColor(R.color.white)))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivUserHeadpic);

        activityPresenter.setPhonestate();
        activityPresenter.setSchoolstate();


    }


    @Override
    public void setPhoneState(boolean islogin) {
        if (islogin) {
            tvUserPhoneState.setText("已认证");
            tvUserPhoneState.setTextColor(Color.parseColor("#00CDCD"));
        } else {
            tvUserPhoneState.setText("未认证");
            tvUserPhoneState.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    @Override
    public void setSchoolState(boolean islogin) {
        if (islogin) {
            tvUserSchoolState.setText("已认证");
            tvUserSchoolState.setTextColor(Color.parseColor("#00CDCD"));
        } else {
            tvUserSchoolState.setText("未认证");
            tvUserSchoolState.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    @OnClick({R.id.card_user_phone, R.id.card_user_school, R.id.iv_user_headpic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_user_phone:
                startActivity(new Intent(context, LoginPhone_Activity.class));
                break;
            case R.id.card_user_school:
                startActivity(new Intent(context, LoginSchool_Activity.class));
                break;
            case R.id.iv_user_headpic:
                startActivity(new Intent(context, UserInfor_Activity.class));
                break;
        }
    }

}
