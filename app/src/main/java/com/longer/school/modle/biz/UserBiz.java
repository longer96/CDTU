package com.longer.school.modle.biz;

import android.util.Log;

import com.longer.school.modle.bean.User;
import com.longer.school.utils.FileTools;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by longer on 2017/4/17.
 */

public class UserBiz implements User.UserBiz {
    @Override
    public void upDateUserInfor(String objectid) {
        User user = new User();
        user.setSex(FileTools.getshareString("sex"));
        user.setPwd(FileTools.getshareString("password"));
        user.update(objectid, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.d("tip", "更新用户信息成功");
                } else {
                    Log.d("tip", "更新用户信息失败" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void upPhoneVerified(User my,String phone, boolean isbinding, final User.OnUpUser onUpUser) {
        User user = new User();
        user.setMobilePhoneNumber(phone);
        user.setMobilePhoneNumberVerified(isbinding);
        user.update(my.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.d("tip", "更新用户信息成功");
                    onUpUser.Success();
                } else {
                    Log.d("tip", "更新用户信息失败" + e.getMessage());
                    onUpUser.Failed();
                }
            }
        });
    }

    @Override
    public void clearPhoneNum(User my, final User.OnUpUser onUpUser) {
        User user = new User();
        user.remove("mobilePhoneNumber");
        user.remove("mobilePhoneNumberVerified");
        user.update(my.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.d("tip", "更新用户信息成功");
                    onUpUser.Success();
                } else {
                    Log.d("tip", "更新用户信息失败" + e.getMessage());
                    onUpUser.Failed();
                }
            }
        });
    }

    @Override
    public void upDataNicenameandRoom(User my, String nicename, String sex, String room, final User.OnUpUser onUpUser) {
        User user = new User();
        user.setNickname(nicename);
        user.setSex(sex);
        user.setRoom(room);
        user.update(my.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.d("tip", "更新用户信息成功");
                    onUpUser.Success();
                } else {
                    Log.d("tip", "更新用户信息失败" + e.getMessage());
                    onUpUser.Failed();
                }
            }
        });
    }
}
