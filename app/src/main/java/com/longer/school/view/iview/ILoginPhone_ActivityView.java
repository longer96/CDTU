package com.longer.school.view.iview;

/**
 * Created by longer on 2017/4/28.
 */

public interface ILoginPhone_ActivityView {

    void setBtnText(String str);

    void setPhone(String phone);

    void setPhoneEnable(boolean enable);

    void  setYzmVisibale(boolean isshow);

    String getBtnText();

    String getPhone();

    String getYzm();

    void showLoginDialog();

    void hideLoginDialog();

    void loginSuccess();

    void loginFailed(String e);

    void toMain_Activity();

    void showRegisterDialog();

    void toRegister_Activity();

    void showSendSMSdialogTip(String phone);

    void showSendSMSdialog();

    void hideSendSMSdialog();

    void sendSMSSuccess();

    void sendSMSFailed(String e);

}
