package com.longer.school.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.modle.bean.User;
import com.longer.school.presenter.Register_ActivityPresenter;
import com.longer.school.utils.Toast;
import com.longer.school.view.iview.IRegister_ActivityView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class Register_Activity extends AppCompatActivity implements IRegister_ActivityView {

    public Context context;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edt_register_phone)
    EditText edtRegisterPhone;
    @Bind(R.id.btn_register_yzm)
    Button btnRegisterYzm;
    @Bind(R.id.edt_register_yzm)
    EditText edtRegisterYzm;
    @Bind(R.id.checkBox_register)
    CheckBox checkBoxRegister;
    @Bind(R.id.tv_register_yhxy)
    TextView tvRegisterYhxy;
    @Bind(R.id.btn_register)
    Button btnRegister;
    AlertDialog.Builder dialog;
    ProgressDialog progressDialog;

    private Register_ActivityPresenter activityPresenter = new Register_ActivityPresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        toolbar.setTitle("注册帐号");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        inti();
    }

    public void inti() {
        context = Register_Activity.this;
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String yzm = intent.getStringExtra("yzm");
        if(phone != null){
            edtRegisterPhone.setText(phone);
            edtRegisterYzm.setText(yzm);
            btnRegisterYzm.setEnabled(false);
            btnRegisterYzm.setBackgroundColor(getResources().getColor(R.color.btn_notenable));
        }else {
            if (Application.my != null && Application.my.getMobilePhoneNumber() != null) {
                edtRegisterPhone.setText(Application.my.getMobilePhoneNumber());
            }
        }

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在发送验证码");
    }


    @Override
    public void setBtnRegisterEnable(boolean enable) {
        if (enable) {
            btnRegister.setBackgroundColor(getResources().getColor(R.color.btn_isenable));
            btnRegister.setEnabled(true);
        } else {
            btnRegister.setBackgroundColor(getResources().getColor(R.color.btn_notenable));
            btnRegister.setEnabled(false);
        }
    }

    @Override
    public boolean getischecked() {
        return checkBoxRegister.isChecked();
    }

    @Override
    public void sendSMSSuccess() {
        Toast.show("验证码发送成功");
    }

    @Override
    public void sendSMSFailed(int code) {
        if (code == 10010) {
            Toast.show("一分钟只能发一条哦！");
        } else {
            Toast.show("验证码发送失败");
        }
    }

    @Override
    public void showRegisterError(int code) {
        if (code == 207) {
            Toast.show("验证码错误");
        } else {
            Toast.show("验证错误:" + code);
        }
    }

    @Override
    public void RegisterSuccess() {
        Toast.show("注册成功！");
    }

    @Override
    public void showSendSMSdialogTip(String phone) {
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Tip");
        dialog.setCancelable(true);
        dialog.setMessage("发送验证码到：\r\n" + phone + "\r\n请确认手机号无误？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activityPresenter.sendSMS();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    @Override
    public void showSendSMSdialog() {
        progressDialog.show();
    }

    @Override
    public void hideSendSMSdialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showUserhaveRegister() {
        Toast.show("该手机号已经注册过了");
    }

    @Override
    public void showBindingSchoolDialog(final User user) {
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Tip");
        dialog.setCancelable(true);
        dialog.setMessage("发现已经有登记学号：\r\n" + user.getUsername() + "\r\n是否进行绑定？");
        dialog.setPositiveButton("绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activityPresenter.Bindingschool(user);
            }
        });
        dialog.setNegativeButton("不绑定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activityPresenter.NotBindingschool();
            }
        });
        dialog.show();
    }

    @Override
    public void registerSuccess() {
        Toast.show("注册成功");
    }

    @Override
    public void registerFailed() {
        Toast.show("注册失败");
    }

    @Override
    public String getPhone() {
        return edtRegisterPhone.getText().toString().trim();
    }

    @Override
    public String getYzm() {
        return edtRegisterYzm.getText().toString().trim();
    }

    @Override
    public void toMain_Activity() {
//        Log.d("tip","帐号：" + bmobUser.getUsername()) ;
        LoginPhone_Activity.instant.finish();
        User_Activity.instant.finish();
        this.finish();
    }

    @OnClick({R.id.btn_register_yzm, R.id.tv_register_yhxy, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register_yzm:
                if (!activityPresenter.IsPhone()) {
                    edtRegisterPhone.setError("手机号格式不正确");
                    break;
                }
                showSendSMSdialogTip(getPhone());
                break;
            case R.id.tv_register_yhxy:
                Intent intent = new Intent(context,WebView_Activity.class);
                intent.putExtra("title","用户协议");
                intent.putExtra("url","file:///android_asset/fuwuxieyi.html");
                startActivity(intent);
                break;
            case R.id.btn_register:
                if (!getischecked()) {
                    Toast.show("请先阅读并同意用户协议！");
                    return;
                }
                if (getYzm().isEmpty() ) {
                    Toast.show("验证码不能为空！");
                    return;
                }
                activityPresenter.register();
                break;
        }
    }
}
