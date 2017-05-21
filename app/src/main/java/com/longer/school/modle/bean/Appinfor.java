package com.longer.school.modle.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by longer on 2016/8/4.
 */
public class Appinfor extends BmobObject {

    private float version;
    private String infor;
    private Boolean important;
    private float minversion;//最低的版本，也就是低于此版本都会强制他升级(最低版本不包含当前版本，也就是比如2.4，那么2.4版本也会强制更新)
    private BmobFile app;

    public float getMinversion() {
        return minversion;
    }


    public float getVersion() {
        return version;
    }


    public String getInfor() {
        return infor;
    }


    public Boolean getImportant() {
        return important;
    }


    public BmobFile getApp() {
        return app;
    }

}
