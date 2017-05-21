package com.longer.school.modle.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by longer on 2016/8/4.
 * 接受系统消息
 */
public class Message extends BmobObject {

    private String infor;//提示的信息

    private boolean show;//是否显示

    public boolean isShow() {
        return show;
    }

    public String getInfor() {
        return infor;
    }

}
