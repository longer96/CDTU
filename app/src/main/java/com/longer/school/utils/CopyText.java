package com.longer.school.utils;

import android.content.ClipboardManager;
import android.content.Context;

import com.longer.school.Application;

/**
 * 复制文字到剪贴板
 * Created by longer on 2016/9/20.
 */
public class CopyText {
    public static void settext(String str){
        ClipboardManager cm = (ClipboardManager) Application.getINSTANCE().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(str);
        Toast.show("复制到剪切板");
    }
}
