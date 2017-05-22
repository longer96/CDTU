package com.longer.school.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.FileTools;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SkinActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_skin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("切换主题");
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }
        ButterKnife.bind(this);
        context = SkinActivity.this;
    }

    @OnClick({R.id.cardview_skin_blue, R.id.cardview_skin_girl, R.id.cardview_skin_boy, R.id.cardview_skin_night, R.id.cardview_skin_purple})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardview_skin_blue:
                savetheme(R.style.AppTheme_NoActionBar,"#3F51B5");
                break;
            case R.id.cardview_skin_girl:
                savetheme(R.style.AppThemeGirl_NoActionBar,"#fb7299");
                break;
            case R.id.cardview_skin_boy:
                savetheme(R.style.AppThemeBoy_NoActionBar,"#03a9f4");
                break;
            case R.id.cardview_skin_night:
                savetheme(R.style.AppThemeDark_NoActionBar,"#696969");
                break;
            case R.id.cardview_skin_purple:
                savetheme(R.style.AppThemePorple_NoActionBar,"#673ab7");
                break;
        }
    }

    /**
     * 保存所选的主题
     * @param mtheme
     */
    private void savetheme(int mtheme,String color) {
        Application.settheme(mtheme);
        FileTools.saveshareInt("theme", mtheme);
        FileTools.saveshareString("refreshcolor", color);//保存一个颜色，用来设置下拉刷新的颜色
        MainActivity.instance.finish();
        this.finish();
        startActivity(new Intent(context, MainActivity.class));
    }
}
