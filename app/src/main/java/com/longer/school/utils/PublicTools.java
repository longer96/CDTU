package com.longer.school.utils;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.longer.school.Application;
import com.longer.school.Config;

/**
 * Created by longer on 2017/3/17.
 * 公共的方法，比如加QQ群，或者分享应用
 */
public class PublicTools {


    /**
     * 打开指定网页，意图
     */
    public static void openhtml(Context context, String link) {
        Intent intent = new Intent();
        intent.setAction(intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        context.startActivity(intent);
    }

    /**
     * 侧滑菜单分享app
     */
    public static void shareapp(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "成都工业学院App\n" + Config.pre);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }

    /**
     * 跳转到应用平台进行评论
     * 可以跳转到几乎任何存在你的应用的市场上了
     */
    public static void comment(Context context) {
        try {
            String mAddress = "market://details?id=" + Application.getINSTANCE().getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show("发现你没有应用商店啊");
        }
    }

    /**
     * 打电话
     */
    public static void call(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Application.getINSTANCE().startActivity(intent);
    }

    /****************
     * 发起添加群流程。群号：学生事务委员会(585954384) 的 key 为： Fhsjxh3HoMvZ0KxFpP6Ij941kzo_UP5j
     * 发起添加群流程。群号：成工助手(623840300) 的 key 为： drHL1h7gqcaKC-kaNohbkzos5FtH6BQo
     * 调用 joinQQGroup(Fhsjxh3HoMvZ0KxFpP6Ij941kzo_UP5j) 即可发起手Q客户端申请加群 学生事务委员会(585954384)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制文字到剪切板
     *
     * @param str
     */
    public static void copy(String str) {
        ClipboardManager cm = (ClipboardManager) Application.getINSTANCE().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(str);
        Toast.show("复制到剪切板");
    }

    /**
     * 打开键盘
     */
    public static void openkeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) Application.getINSTANCE().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //对应的Edittext 得到光标
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();//获取焦点 光标出现

    }


    /**
     * 关闭键盘
     */
    public static void closekeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
    }

    /**
     * 检查是否是友善的评论数据
     * 比如一行打一个字符，超多行
     *
     * @return true 友善的字符串   false 字符串有点违规
     */
    public static boolean checkfriendtext(String str) {
        int hang = str.split("\n").length;
        if(hang <= 2){//如果小于2行不做判断
            return true;
        }
        //如果一行小于6个支付那么，呵呵。。。。
        return hang * 6 > str.length() ? false : true;
    }

}
