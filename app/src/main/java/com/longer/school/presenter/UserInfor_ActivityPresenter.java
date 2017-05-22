package com.longer.school.presenter;

import com.longer.school.Application;
import com.longer.school.modle.bean.User;
import com.longer.school.modle.biz.UserBiz;
import com.longer.school.utils.FileTools;
import com.longer.school.view.activity.UserInfor_Activity;
import com.longer.school.view.activity.User_Activity;
import com.longer.school.view.iview.IUserActivityView;
import com.longer.school.view.iview.IUserInfor_Activity;

/**
 * Created by longer on 2017/4/27.
 */

public class UserInfor_ActivityPresenter {

    private IUserInfor_Activity inforActivity;
    private User.UserBiz userBiz;

    public UserInfor_ActivityPresenter(UserInfor_Activity inforActivity) {
        this.inforActivity = inforActivity;
        userBiz = new UserBiz();
    }

    public void updata(){
        inforActivity.showSubmiting();
        userBiz.upDataNicenameandRoom(Application.my, inforActivity.getnicename(), inforActivity.getsex(), inforActivity.getroom(), new User.OnUpUser() {
            @Override
            public void Success() {
                inforActivity.hideSubmiting();
                inforActivity.SubmitSuccess();
                inforActivity.toUser_Activity();
            }

            @Override
            public void Failed() {
                inforActivity.hideSubmiting();
                inforActivity.SubmitFailed();
            }
        });
    }


}
