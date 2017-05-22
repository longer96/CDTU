package com.longer.school.view.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.RegexUtils;
import com.longer.school.Application;
import com.longer.school.Config;
import com.longer.school.R;
import com.longer.school.modle.bean.User;
import com.longer.school.presenter.LoginPhone_ActivityPresenter;
import com.longer.school.utils.Toast;
import com.longer.school.view.iview.ILoginPhone_ActivityView;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginPhone_Activity extends AppCompatActivity implements ILoginPhone_ActivityView {

    public Context context;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edt_login_phone)
    EditText edtLoginPhone;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.edt_login_register)
    TextView edtLoginRegister;
    @Bind(R.id.ll_login_qq)
    LinearLayout llLoginQq;
    public static LoginPhone_Activity instant;
    @Bind(R.id.edt_login_phone_yzm)
    EditText edtLoginPhoneYzm;
    @Bind(R.id.rl_loginphone)
    RelativeLayout rlLoginphone;
    ProgressDialog dialog;
    ProgressDialog progressDialog;

    private Tencent mTencent; //qq主操作对象
    private IUiListener loginListener; //授权登录监听器
    private IUiListener userInfoListener; //获取用户信息监听器
    private String scope; //获取信息的范围参数
    private UserInfo userInfo; //qq用户信息
    private static final String APPID = Config.QQ_APP_ID;

    private LoginPhone_ActivityPresenter activityPresenter = new LoginPhone_ActivityPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_login_phone);
        ButterKnife.bind(this);
        toolbar.setTitle("手机登录");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        inti();
    }

    public void inti() {
        context = LoginPhone_Activity.this;
        instant = LoginPhone_Activity.this;
        activityPresenter.checkLogin();

        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("正在登录中...");

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在发送验证码");

        initData();
    }


    @Override
    public void showSendSMSdialogTip(String phone) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Tip");
        dialog.setCancelable(true);
        dialog.setMessage("发送验证码到：\r\n" + phone + "\r\n请确认手机号无误？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activityPresenter.SendSMSNoInfor();
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
    public void sendSMSSuccess() {
        Toast.show("短信发送成功");
    }

    @Override
    public void sendSMSFailed(String e) {
        Toast.show("短信发送失败:" + e);
    }

    @Override
    public void setBtnText(String str) {
        btnLogin.setText(str);
    }

    @Override
    public void setPhone(String phone) {
        edtLoginPhone.setText(phone);
    }

    @Override
    public void setPhoneEnable(boolean enable) {
        if (enable) {
            edtLoginPhone.setEnabled(true);
        } else {
            edtLoginPhone.setEnabled(false);
        }
    }

    @Override
    public void setYzmVisibale(boolean isshow) {
        if (isshow) {
            rlLoginphone.setVisibility(View.VISIBLE);
        } else {
            rlLoginphone.setVisibility(View.GONE);
        }
    }

    @Override
    public String getBtnText() {
        return btnLogin.getText().toString();
    }

    @Override
    public String getPhone() {
        return edtLoginPhone.getText().toString().trim();
    }

    @Override
    public String getYzm() {
        return edtLoginPhoneYzm.getText().toString().trim();
    }

    @Override
    public void showLoginDialog() {
        dialog.show();
    }

    @Override
    public void hideLoginDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void loginSuccess() {
        Toast.show("登录成功");
    }

    @Override
    public void loginFailed(String e) {
        Toast.show("登录失败:" + e);
    }

    @Override
    public void toMain_Activity() {
        User_Activity.instant.finish();
        this.finish();
    }

    @Override
    public void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("发现改手机号尚未注册，是否前往注册");
        builder.setPositiveButton("注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toRegister_Activity();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public void toRegister_Activity() {
        Intent intent = new Intent(context, Register_Activity.class);
        intent.putExtra("phone", getPhone());
        intent.putExtra("yzm", getYzm());
        startActivity(intent);
    }

    @OnClick({R.id.btn_login, R.id.edt_login_register, R.id.ll_login_qq, R.id.btn_login_phone_yzm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if ("登录".equals(getBtnText())) {
                    if (!RegexUtils.isMobileExact(getPhone())) {
                        edtLoginPhone.setError("手机号格式不正确");
                        break;
                    }
                    if (getYzm().isEmpty()) {
                        Toast.show("验证码不能为空！");
                        break;
                    }
                    activityPresenter.login();
                } else if ("注销".equals(getBtnText())) {
                    activityPresenter.logout();
                    Application.setuser(null);
                }
                break;
            case R.id.edt_login_register:
                startActivity(new Intent(context, Register_Activity.class));
                break;
            case R.id.ll_login_qq:
                login();
                break;
            case R.id.btn_login_phone_yzm:
                if (!RegexUtils.isMobileExact(getPhone())) {
                    edtLoginPhone.setError("手机号格式不正确");
                    break;
                }
                showSendSMSdialogTip(getPhone());
                break;
        }
    }


    private void login() {
        //如果session无效，就开始登录
        if (!mTencent.isSessionValid()) {
            //开始qq授权登录
            mTencent.login(LoginPhone_Activity.this, scope, loginListener);
        }
    }

//      得到QQ登录用户的个人数据
//    userInfo = new UserInfo(LoginPhone_Activity.this, mTencent.getQQToken());
//                userInfo.getUserInfo(userInfoListener);
//

    //    关联已经登录的Bmob账户
//BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("qq", "F18C3779D2195AE4DCC637C6A6C14A85", "7776000", "37EB5296292EBD43F07F3AC055439311");
//                BmobUser.associateWithAuthData(authInfo, new UpdateListener() {
//        @Override
//        public void done(BmobException e) {
//            if (e == null) {
//                Log.i("bmob", "关联成功");
//            } else {
//                Log.i("bmob", "关联失败：code =" + e.getErrorCode() + ",msg = " + e.getMessage());
//            }
//        }
//    });
    private void initData() {
        //初始化qq主操作对象
        mTencent = Tencent.createInstance(APPID, LoginPhone_Activity.this);
        //要所有权限，不然会再次申请增量权限，这里不要设置成get_user_info,add_t
        scope = "all";

        loginListener = new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                Log.e("GET_QQ_INFO_ERROR", "登录出错");
                Toast.show("登录出错");
            }

            /**
             * 返回json数据样例
             *
             * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
             * "pf":"desktop_m_qq-10000144-android-2002-",
             * "query_authority_cost":448,
             * "authority_cost":-136792089,
             * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
             * "expires_in":7776000,
             * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
             * "msg":"",
             * "access_token":"A2455F491478233529D0106D2CE6EB45",
             * "login_cost":499}
             */
            @Override
            public void onComplete(Object value) {
                System.out.println("有数据返回..");
                if (value == null) {
                    return;
                }

                try {
                    JSONObject jo = (JSONObject) value;

                    int ret = jo.getInt("ret");

                    System.out.println("json=" + String.valueOf(jo));

                    if (ret == 0) {
//                        Toast.show("登录成功");
                        String openID = jo.getString("openid");
                        String accessToken = jo.getString("access_token");
                        String expires = jo.getString("expires_in");
                        mTencent.setOpenId(openID);
                        mTencent.setAccessToken(accessToken, expires);


                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth("qq", accessToken, expires, openID);
                        //判断是否有缓存用户，有的话关联，没有注册
                        if (Application.my != null) {
                            //    关联已经登录的Bmob账户
                            BmobUser.associateWithAuthData(authInfo, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Log.i("bmob", "关联成功");
                                    } else {
                                        Log.i("bmob", "关联失败：code =" + e.getErrorCode() + ",msg = " + e.getMessage());
                                    }
                                }
                            });
                        } else {
                            //测试bmob 一键登录
                            BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {

                                @Override
                                public void done(JSONObject userAuth, BmobException e) {
                                    if (e == null) {
                                        Log.i("bmob", "登录成功：" + userAuth.toString());
                                        User user = BmobUser.getCurrentUser(User.class);
                                        Application.setuser(user);
                                        //检测手机号是否未验证，如果没有验证跳到注册界面
                                        if (user.getMobilePhoneNumberVerified() == null || !user.getMobilePhoneNumberVerified()) {
                                            startActivity(new Intent(context, Register_Activity.class));
                                        } else if (user.getMobilePhoneNumberVerified() != null && user.getMobilePhoneNumberVerified()) {
                                            User_Activity.instant.finish();
                                            finish();
                                            Log.d("tip", "验证");
                                        }
                                    } else {
                                        Log.i("bmob", "登录失败：code =" + e.getErrorCode() + ",msg = " + e.getMessage());
                                    }
                                }
                            });
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.show("登录失败");
                }

            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.e("GET_QQ_INFO_CANCEL", "QQ登录取消");
            }
        };

        userInfoListener = new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
                Toast.show("获取数据出错");
            }

            /**
             * 返回用户信息样例
             *
             * {"is_yellow_year_vip":"0","ret":0,
             * "figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/40",
             * "figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "nickname":"攀爬←蜗牛","yellow_vip_level":"0","is_lost":0,"msg":"",
             * "city":"黄冈","
             * figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/50",
             * "vip":"0","level":"0",
             * "figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "province":"湖北",
             * "is_yellow_vip":"0","gender":"男",
             * "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/30"}
             */
            @Override
            public void onComplete(Object arg0) {
                if (arg0 == null) {
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) arg0;
                    int ret = jo.getInt("ret");
                    System.out.println("json=" + String.valueOf(jo));
//                    String nickName = jo.getString("nickname");
//                    String gender = jo.getString("gender");
//                    Toast.show("你好：" + nickName);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.show("获取数据出错2");
                }

            }

            @Override
            public void onCancel() {
                Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息取消");
            }
        };
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
    }

    @Override
    protected void onDestroy() {
        if (mTencent != null) {
            //注销登录
            mTencent.logout(LoginPhone_Activity.this);
        }
        super.onDestroy();
    }

}
