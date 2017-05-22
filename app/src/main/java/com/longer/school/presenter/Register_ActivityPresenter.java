package com.longer.school.presenter;

import android.util.Log;

import com.blankj.utilcode.utils.RegexUtils;
import com.longer.school.Application;
import com.longer.school.modle.bean.User;
import com.longer.school.modle.biz.UserBiz;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.activity.Register_Activity;
import com.longer.school.view.activity.User_Activity;
import com.longer.school.view.iview.IRegister_ActivityView;
import com.longer.school.view.iview.IUserActivityView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobSmsState;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by longer on 2017/4/27.
 */

public class Register_ActivityPresenter {

    private IRegister_ActivityView registerActivityView;
    private User.UserBiz userBiz;

    public Register_ActivityPresenter(Register_Activity registerActivity) {
        this.registerActivityView = registerActivity;
        userBiz = new UserBiz();
    }

    public boolean IsPhone() {
        if (RegexUtils.isMobileExact(registerActivityView.getPhone())) {
            return true;
        }
        return false;
    }

    public void sendSMS() {
        registerActivityView.showSendSMSdialog();
        //先验证是否已经注册过的
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("mobilePhoneNumber", registerActivityView.getPhone());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() != 0) {
                        if (list.get(0).getMobilePhoneNumberVerified() != null && list.get(0).getMobilePhoneNumberVerified()) {
                            //  1.表示已经注册过的手机号，返回，提示已经注册过了
                            registerActivityView.hideSendSMSdialog();
                            registerActivityView.showUserhaveRegister();
                            return;
                        } else {
                            //  2.表示已经填写过手机号但是没有验证
//                            user = list.get(0);//记录已经有过正方系统登录
                            SendSMSNoInfor();
                        }
                    } else {
                        //  3.表示没有过登记，相当于是新用户，可以直接注册
                        SendSMSNoInfor();
                    }
                } else {
                    e.printStackTrace();
                    registerActivityView.showRegisterError(e.getErrorCode());
                }
            }
        });
    }

    private void SendSMSNoInfor() {
        //发送短信
        BmobSMS.requestSMSCode(registerActivityView.getPhone(), "登录验证", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException ex) {
                registerActivityView.hideSendSMSdialog();
                if (ex == null) {//验证码发送成功
                    Log.i("smile", "短信id：" + smsId);//用于查询本次短信发送详情
                    registerActivityView.sendSMSSuccess();
                } else {
                    Log.d("tip", "发送短信失败：" + ex.toString());
                    ex.printStackTrace();
                    registerActivityView.sendSMSFailed(ex.getErrorCode());
                }
            }
        });
    }

    public void register() {
        //通过检测本地缓存的账户来判断是否已经登录，如果有并且没有验证手机号那么进行绑定
        User user = Application.my;
//        Log.d("tip", "user：" + user.getMobilePhoneNumberVerified());
        if (user != null) {
            if (user.getUsername().length() == 10) {//如果有登录 学校帐号 先提示用户是否绑定
                registerActivityView.showBindingSchoolDialog(user);
                return;
            }
            if (user.getUsername().length() > 12) {//没有登录校园帐号只是通过QQ登录过来的
                Bindingschool(user);
            }
        } else {
            //没有的话直接注册新用户
            registerNewUser();
        }
    }

    public void registerNewUser() {
        User user = new User();
        user.setMobilePhoneNumber(registerActivityView.getPhone());//设置手机号码（必填）
        user.setPassword(registerActivityView.getPhone());  //设置用户密码(就设置为手机号)
        user.signOrLogin(registerActivityView.getYzm(), new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Log.i("smile", "注册通过");
                    registerActivityView.RegisterSuccess();
                    login();
                } else {
                    Log.i("smile", "注册失败" + e.getMessage());
                    registerActivityView.showRegisterError(e.getErrorCode());
                }
            }

        });
    }

    public void NotBindingschool() {
        registerNewUser();
    }

    public void Bindingschool(User user) {
        userBiz.upPhoneVerified(user, registerActivityView.getPhone(), true, new User.OnUpUser() {
            @Override
            public void Success() {
                Application.setuser(BmobUser.getCurrentUser(User.class));
                registerActivityView.toMain_Activity();
            }

            @Override
            public void Failed() {
                registerActivityView.registerFailed();
            }
        });
    }

    private void login() {
        BmobUser.loginByAccount(registerActivityView.getPhone(), registerActivityView.getPhone(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    Log.i("smile", "用户登陆成功");
                    Log.d("tip", "手机：" + user.getMobilePhoneNumber() + "  有效性：" + user.getMobilePhoneNumberVerified());
                    Application.setuser(user);
                    registerActivityView.registerSuccess();
                    registerActivityView.toMain_Activity();
                } else {
                    Log.i("smile", "用户自动登陆失败");
                    e.printStackTrace();
                    registerActivityView.registerFailed();
                }
            }
        });
    }
}
