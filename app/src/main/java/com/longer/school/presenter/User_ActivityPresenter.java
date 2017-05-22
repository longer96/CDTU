package com.longer.school.presenter;

import android.util.Log;
import android.view.View;

import com.longer.school.Application;
import com.longer.school.utils.FileTools;
import com.longer.school.view.activity.User_Activity;
import com.longer.school.view.iview.IUserActivityView;

/**
 * Created by longer on 2017/4/27.
 */

public class User_ActivityPresenter {

    private IUserActivityView userActivityView;

    public User_ActivityPresenter(User_Activity userActivityView) {
        this.userActivityView = userActivityView;
    }

    public void setPhonestate() {
        if (Application.my != null && Application.my.getMobilePhoneNumberVerified() != null) {
            userActivityView.setPhoneState(Application.my.getMobilePhoneNumberVerified());
        }
    }

    public void setSchoolstate() {
        String str = FileTools.getshareString("login");
        if ("true".equals(str)) {
            userActivityView.setSchoolState(true);
        }
    }
}
