package com.longer.school.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.longer.school.Application;
import com.longer.school.modle.bean.Love;
import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.TimeTools;
import com.longer.school.utils.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Add_loveActivity extends AppCompatActivity {

    public Context context;
    @Bind(R.id.edt_addlove_object)
    EditText edtAddloveObject;
    @Bind(R.id.edt_addlove_name)
    EditText edtAddloveName;
    @Bind(R.id.edt_addlove_content)
    EditText edtAddloveContent;
    @Bind(R.id.btn_addlove_up)
    Button btnAddloveUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_add_love);
        ButterKnife.bind(this);
        context = Add_loveActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("表白");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtAddloveName.setText(FileTools.getshareString("name") + "童学");
    }

    @OnClick(R.id.btn_addlove_up)
    public void onClick() {
        uplove();
    }

    private void uplove() {
        String object = edtAddloveObject.getText().toString().trim();
        String name = edtAddloveName.getText().toString().trim();
        String content = edtAddloveContent.getText().toString().trim();

        if (TextUtils.isEmpty(object) ||  TextUtils.isEmpty(name) || TextUtils.isEmpty(content)) {
            Toast.show("不能为空哦");
            return;
        }
        if(!TimeTools.limit("timeLove",22)){
            Toast.show("不能太花心哦哦，明天再来表白呗~");
            return;
        }
        clickable(false);
        Love love = new Love();
        love.setName(name);
        love.setContent(content);
        love.setObject(object);
        love.setUser(Application.my);
        love.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.show("表白成功");
                    Add_loveActivity.this.finish();
                    if(LoveActivity.instance != null){
                        LoveActivity.instance.finish();
                    }
                    startActivity(new Intent(Add_loveActivity.this, LoveActivity.class));
                } else {
                    clickable(true);
                    Toast.show("发布表白失败：" + e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    private void clickable(boolean b) {
        if(b){
            btnAddloveUp.setEnabled(true);
            btnAddloveUp.setText("表白");
        }else {
            btnAddloveUp.setEnabled(false);
            btnAddloveUp.setText("表白中...");
        }
    }
}

