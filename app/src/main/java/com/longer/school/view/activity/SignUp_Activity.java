package com.longer.school.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.RegexUtils;
import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.modle.bean.User;
import com.longer.school.presenter.SignUp_ActivityPresenter;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.iview.ISignUpView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUp_Activity extends AppCompatActivity implements ISignUpView {

    public Context context;
    @Bind(R.id.tv_signup_name)
    EditText tvSignupName;
    @Bind(R.id.tv_signup_bj)
    EditText tvSignupBj;
    @Bind(R.id.tv_signup_tel)
    EditText tvSignupTel;
    @Bind(R.id.tv_signup_infor)
    EditText tvSignupInfor;
    @Bind(R.id.btn_signup)
    Button btnSignup;
    private ProgressDialog dialog;
    private String sponsor;

    private SignUp_ActivityPresenter signUpActivityPresenter = new SignUp_ActivityPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("报名");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        sponsor = (String) getIntent().getSerializableExtra("sponsor");
        inti();
    }

    public void inti() {
        context = SignUp_Activity.this;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("正在提交评论...");

        signUpActivityPresenter.setdata();
    }


    @Override
    public String getname() {
        return tvSignupName.getText().toString().trim();
    }

    @Override
    public String gettel() {
        return tvSignupTel.getText().toString().trim();
    }

    @Override
    public String getbj() {
        return tvSignupBj.getText().toString().trim();
    }

    @Override
    public void setname() {
        String name = FileTools.getshareString("name");
        if (!"".equals(name)) {
            tvSignupName.setText(name);
        }
    }

    @Override
    public void setbj() {
        String bj = FileTools.getshareString("banji");
        if (!"".equals(bj)) {
            tvSignupBj.setText(bj);
        }
    }

    @Override
    public void settel() {
        String tel = FileTools.getshareString("tel");
        if (!"".equals(tel)) {
            tvSignupTel.setText(tel);
        }
    }

    @Override
    public void showdialog() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void hidedialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void setenabletrue() {
        btnSignup.setEnabled(true);
        btnSignup.setText("提交");
    }

    @Override
    public void setenablefalse() {
        btnSignup.setEnabled(false);
        btnSignup.setText("提交中...");
    }

    @Override
    public void SignUpSuccess() {
        Toast.show("报名成功");
    }

    @Override
    public void SignUpFailed() {
        Toast.show("提交失败");
    }

    @OnClick(R.id.btn_signup)
    public void onViewClicked() {
        String name = tvSignupName.getText().toString().trim();
        String bj = tvSignupBj.getText().toString().trim();
        String tel = tvSignupTel.getText().toString().trim();

        if (name.isEmpty() || bj.isEmpty() || tel.isEmpty()) {
            Toast.show("不能为空");
            return;
        }
        if (!RegexUtils.isMobileExact(tel)) {
            Toast.show("手机号好像不正确啊");
            return;
        }
        String infor = tvSignupInfor.getText().toString().trim();
        signUpActivityPresenter.putsignup(sponsor, name, tel, bj, infor);
    }


}
