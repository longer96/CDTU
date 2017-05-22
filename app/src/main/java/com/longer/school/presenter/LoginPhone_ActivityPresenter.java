package com.longer.school.presenter;

import android.util.Log;

import com.longer.school.Application;
import com.longer.school.modle.bean.SignUp;
import com.longer.school.modle.bean.User;
import com.longer.school.modle.biz.SignUpBiz;
import com.longer.school.view.activity.LoginPhone_Activity;
import com.longer.school.view.iview.ILoginPhone_ActivityView;
import com.longer.school.view.iview.ISignUpView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by longer on 2017/4/18.
 */

public class LoginPhone_ActivityPresenter {

    private ILoginPhone_ActivityView phoneActivityView;

    public LoginPhone_ActivityPresenter(LoginPhone_Activity phone_activity) {
        this.phoneActivityView = phone_activity;
    }

    public void checkLogin() {
        User my = Application.my;
        if (my != null && my.getMobilePhoneNumberVerified() != null) {
            if (my.getMobilePhoneNumberVerified()) {
                //表示已经登录过了
                phoneActivityView.setBtnText("注销");
                phoneActivityView.setPhone(my.getMobilePhoneNumber());
                phoneActivityView.setPhoneEnable(false);
                phoneActivityView.setYzmVisibale(false);

            } else {
                //表示手机号没有认证
            }
        } else {
            //表示没有登录过
        }
    }

    public void logout() {
        phoneActivityView.setBtnText("登录");
        phoneActivityView.setPhone("");
        phoneActivityView.setPhoneEnable(true);
        phoneActivityView.setYzmVisibale(true);
        BmobUser.logOut();
        User user = BmobUser.getCurrentUser(User.class);
        Application.setuser(user);
    }

    public void SendSMSNoInfor() {
        phoneActivityView.showSendSMSdialog();
        //发送短信
        BmobSMS.requestSMSCode(phoneActivityView.getPhone(), "登录验证", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                phoneActivityView.hideSendSMSdialog();
                if (ex == null) {//验证码发送成功
                    Log.i("smile", "短信id：" + smsId);//用于查询本次短信发送详情
                    phoneActivityView.sendSMSSuccess();
                } else {
                    Log.d("tip", "发送短信失败：" + ex.toString());
                    ex.printStackTrace();
                    phoneActivityView.sendSMSFailed(ex.toString());
                }
            }
        });
    }

    public void login() {
        phoneActivityView.showLoginDialog();
        //先检查是否已经注册过了，没有注册过的话，直接携带手机号以及验证码到RegisterActivity.class
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("mobilePhoneNumber", phoneActivityView.getPhone());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() != 0) {
                        if (list.get(0).getMobilePhoneNumberVerified() != null && list.get(0).getMobilePhoneNumberVerified()) {
                            //  1.表示已经注册过的手机号，直接登录
                            loginNow();
                            return;
                        } else {
                            //  2.表示已经填写过手机号但是没有验证
//                            user = list.get(0);//记录已经有过正方系统登录
                            phoneActivityView.hideLoginDialog();
                            phoneActivityView.showRegisterDialog();
                        }
                    } else {
                        //  3.表示没有过登记，相当于是新用户，可以直接提示注册注册
                        phoneActivityView.hideLoginDialog();
                        phoneActivityView.showRegisterDialog();
                    }
                } else {
                    e.printStackTrace();
                    phoneActivityView.loginFailed(e.toString());
                }
            }
        });
    }

    private void loginNow() {
        BmobUser.loginBySMSCode(phoneActivityView.getPhone(), phoneActivityView.getYzm(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    Log.i("smile", "登陆成功");
                    Log.d("tip", "手机：" + user.getMobilePhoneNumber() + "  有效性：" + user.getMobilePhoneNumberVerified());
                    phoneActivityView.hideLoginDialog();
                    Application.setuser(user);
                    phoneActivityView.loginSuccess();
                    phoneActivityView.toMain_Activity();
                } else {
                    Log.i("smile", "用户登陆失败");
                    e.printStackTrace();
                    phoneActivityView.hideLoginDialog();
                    phoneActivityView.loginFailed(e.toString());
                }
            }
        });
    }
}
