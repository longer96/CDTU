package com.longer.school.presenter;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.longer.school.Application;
import com.longer.school.modle.bean.Good;
import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.LoveComment;
import com.longer.school.modle.bean.PicHeadTip;
import com.longer.school.modle.bean.SchoolMes;
import com.longer.school.modle.biz.GoodBiz;
import com.longer.school.modle.biz.LoveBiz;
import com.longer.school.modle.biz.LoveCommentBiz;
import com.longer.school.modle.biz.PicHeadTipBiz;
import com.longer.school.modle.biz.SchoolMesBiz;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.PublicTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.iview.IFragment_MenuView;
import com.longer.school.view.iview.ILoveOne_ActivityView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by longer on 2017/4/13.
 */

public class Fragement_MenuPresenter {

    private IFragment_MenuView fragmentMenuView;

    private PicHeadTip.IPicHeadTipBiz picHeadTipBiz;
    private SchoolMes.ISchoolMesBiz schoolMesBiz;
    private Love.ILoveBiz loveBiz;
    private Good.GoodBiz goodBiz;
    private Handler mHander = new Handler();

    public Fragement_MenuPresenter(IFragment_MenuView fragmentMenuView) {
        this.fragmentMenuView = fragmentMenuView;
        picHeadTipBiz = new PicHeadTipBiz();
        schoolMesBiz = new SchoolMesBiz();
        loveBiz = new LoveBiz();
        goodBiz = new GoodBiz();
    }

    public void setHeadPic() {
        picHeadTipBiz.getpicheadtip(new PicHeadTip.OngetPicHeadTip() {
            @Override
            public void Success(List<PicHeadTip> list) {
//                Log.d("tip","长度：" + list.size());
                fragmentMenuView.setHeadPic(list);
            }

            @Override
            public void Failed() {

            }
        });
    }

    public void setMenuMessage() {
        fragmentMenuView.setmenu_message_llhide();
        fragmentMenuView.setmenu_message_cardhide();
        fragmentMenuView.setmenu_message_qqhide();
        fragmentMenuView.setmenu_message_signhide();
        schoolMesBiz.getschoolmes(new SchoolMes.OngetSchoolMes() {
            @Override
            public void Success(SchoolMes schoolMes) {
                if (schoolMes.isShow()) {
                    fragmentMenuView.setmenu_message_cardshow();
                    fragmentMenuView.setmenu_message(schoolMes);
                }
            }

            @Override
            public void Failes() {

            }
        });
    }

    public void setMenuLove() {
        fragmentMenuView.setmenu_love_cardhide();
        loveBiz.getLove(new Love.OnGetLove() {
            @Override
            public void Success(List<Love> list) {
                fragmentMenuView.setmenu_love(list);
                fragmentMenuView.setmenu_love_cardshow();
            }

            @Override
            public void Failed() {

            }
        });
    }

    public void setMenuLibrary(boolean login) {
        fragmentMenuView.setmenu_library_cardshow(false);
        if (!login) {//表示没有登录
            return;
        }
        new Thread() {
            @Override
            public void run() {
                LoginService.getlibrary(new LoginService.OnGetLibrary() {
                    @Override
                    public void Success(final String str) {
                        mHander.post(new Runnable() {
                            @Override
                            public void run() {
                                fragmentMenuView.setmenu_librarydata(str);
                                fragmentMenuView.setmenu_library_cardshow(true);
                            }
                        });
                    }

                    @Override
                    public void Failed() {
                    }
                });
            }
        }.start();
    }

    public void setMenuGoods(){
        goodBiz.getgoods(new Good.OnGetGoods() {
            @Override
            public void Success(List<Good> list) {
                fragmentMenuView.setmenu_goodsdata(list);
            }

            @Override
            public void Failed() {

            }
        });
    }


}
