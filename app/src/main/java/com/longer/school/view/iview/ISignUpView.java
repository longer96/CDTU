package com.longer.school.view.iview;

/**
 * Created by longer on 2017/4/18.
 */

public interface ISignUpView {

    String getname();

    String gettel();

    String getbj();

    void setname();

    void setbj();

    void settel();

    void showdialog();

    void hidedialog();

    void setenabletrue();

    void setenablefalse();

    void SignUpSuccess();

    void SignUpFailed();

}
