package com.longer.school.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.PublicTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentActivity extends AppCompatActivity {

    @Bind(R.id.card_student_stucard)
    CardView cardStudentStucard;
    @Bind(R.id.card_student_1)
    CardView cardStudent1;
    @Bind(R.id.card_student_2)
    CardView cardStudent2;
    @Bind(R.id.card_student_3)
    CardView cardStudent3;
    @Bind(R.id.card_student_4)
    CardView cardStudent4;
    @Bind(R.id.card_student_5)
    CardView cardStudent5;
    @Bind(R.id.card_student_6)
    CardView cardStudent6;
    @Bind(R.id.card_student_7)
    CardView cardStudent7;
    @Bind(R.id.card_student_8)
    CardView cardStudent8;
    @Bind(R.id.card_student_9)
    CardView cardStudent9;
    @Bind(R.id.ll_student_stucard)
    LinearLayout llStudentStucard;
    @Bind(R.id.ll_student_page)
    LinearLayout llStudentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(llStudentStucard.isShown()){
                    llStudentStucard.setVisibility(View.GONE);
                    llStudentPage.setVisibility(View.VISIBLE);
                }else{
                    finish();
                }
            }
        });

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitle("学生处");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicTools.call("02887992174");
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicTools.joinQQGroup(StudentActivity.this, "Fhsjxh3HoMvZ0KxFpP6Ij941kzo_UP5j4");
            }
        });

    }


    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "学生处");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }


    @OnClick({R.id.card_student_stucard, R.id.card_student_1, R.id.card_student_2, R.id.card_student_3, R.id.card_student_4, R.id.card_student_5, R.id.card_student_6, R.id.card_student_7, R.id.card_student_8, R.id.card_student_9, R.id.ll_student_stucard})
    public void onViewClicked(View view) {
        Intent intent = new Intent(StudentActivity.this, ImageActivity.class);
        intent.putExtra("pic_id", 1000);
        switch (view.getId()) {
            case R.id.card_student_stucard:
                if(llStudentStucard.isShown()){
                    Log.d("tip","show");
                }else{
                    llStudentStucard.setVisibility(View.VISIBLE);
                    llStudentPage.setVisibility(View.GONE);
                    Log.d("tip","noshow");
                }
                return;
            case R.id.card_student_1:
                intent.putExtra("url", "http://img.hb.aicdn.com/e4c148ee54432de0b0372ca311bbfa16c7793bd42029b-S0F9jD");
                intent.putExtra("title_name", "国家奖学金申请");
                break;
            case R.id.card_student_2:
                intent.putExtra("url", "http://img.hb.aicdn.com/3e6e84165e8f4c19e8046e33e42594c73e9403b81a972-wW7Riq");
                intent.putExtra("title_name", "国家励志奖学金申请");
                break;
            case R.id.card_student_3:
                intent.putExtra("url", "http://img.hb.aicdn.com/42484161ad4b20fe8523f3ceaa909db00ea5377c26f71-Ev7pBH");
                intent.putExtra("title_name", "国家助学金申请");
                break;
            case R.id.card_student_4:
                intent.putExtra("url", "http://img.hb.aicdn.com/92f988499682cfe58ddcc00552d5ea0b2ddebd2b1b036-vudY9O");
                intent.putExtra("title_name", "家庭经济困难学生申请认定");
                break;
            case R.id.card_student_5:
                intent.putExtra("url", "http://img.hb.aicdn.com/c29ff0cd90a1e518df06f49a18c7d86ebb6d604f267a8-Eir1fy");
                intent.putExtra("title_name", "学生生源地信用助学贷款办理");
                break;
            case R.id.card_student_6:
                intent.putExtra("url", "http://img.hb.aicdn.com/311a6a941e4b5fa1a0045aa6f9e6ae5b71eb6a6813a6b-fKkOkP");
                intent.putExtra("title_name", "新生绿色通道办理");
                break;
            case R.id.card_student_7:
                intent.putExtra("url", "http://img.hb.aicdn.com/6fa6eec4629e0fbde867f48de449ebb5469cedde3970b-7ezyhI");
                intent.putExtra("title_name", "学生应征入伍服义务兵役国家资助和退役士兵学费资助办理");
                break;
            case R.id.card_student_8:
                intent.putExtra("url", "http://img.hb.aicdn.com/94a13ab7f97526b4110f29bf5866917982cf324a1c6cd-hgOqjs");
                intent.putExtra("title_name", "学生临时困难补贴申请");
                break;
            case R.id.card_student_9:
                intent.putExtra("url", "http://img.hb.aicdn.com/9a532a36aefcb28166b090fbb46dba81d61665ea1f859-aioj3N");
                intent.putExtra("title_name", "学生申请勤工助学岗位");
                break;
        }
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(llStudentStucard.isShown()){
            llStudentStucard.setVisibility(View.GONE);
            llStudentPage.setVisibility(View.VISIBLE);
            return false;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
}
