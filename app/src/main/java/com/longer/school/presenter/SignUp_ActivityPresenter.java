package com.longer.school.presenter;

import com.longer.school.modle.bean.SignUp;
import com.longer.school.modle.bean.User;
import com.longer.school.modle.biz.SignUpBiz;
import com.longer.school.view.activity.SignUp_Activity;
import com.longer.school.view.iview.ISignUpView;

/**
 * Created by longer on 2017/4/18.
 */

public class SignUp_ActivityPresenter {

    private ISignUpView signUpView;
    private SignUp.ISignUpBiz signUpBiz;

    public SignUp_ActivityPresenter(SignUp_Activity signUpView) {
        this.signUpView = signUpView;
        signUpBiz = new SignUpBiz();
    }

    public void setdata() {
        signUpView.setbj();
        signUpView.setname();
        signUpView.settel();
    }

    public void putsignup(String object, String name, String tel, String bj, String infor) {
        signUpView.showdialog();
        signUpView.setenablefalse();
        signUpBiz.set(object, name, tel, bj, infor, new SignUp.OnsetSignUpLister() {
            @Override
            public void Success() {
                signUpView.SignUpSuccess();
                signUpView.hidedialog();
                signUpView.setenabletrue();
            }

            @Override
            public void Failed() {
                signUpView.SignUpFailed();
                signUpView.hidedialog();
                signUpView.setenabletrue();
            }
        });
    }

}
