package com.longer.school.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.longer.school.Application;

public class FileTools {

    public static boolean saveFile(Context context, String filename, String text) {

        try {
            File file = new File(context.getFilesDir(), filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFile(Context context, String filename) {

        try {
            String result = "";
            File file = new File(context.getFilesDir(), filename);
            if (!file.exists()) {
                // 文件不存在
                return "";
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            // 分行读取
            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void saveshare(Context context, String name, String date) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(name, date);
        editor.commit();
        System.out.println("保存到share 成功:" + name + "->" + date);
    }

    public static String getshare(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String result = sp.getString(name, "");
        System.out.println("得到share 数据成功:" + result);
        return result;
    }

    /* 保存数据到SharedPreferences(Int)
    * @param name    数据名称
    * @param date    数据内容
    */
    public static void saveshareInt(String name, int date) {
        SharedPreferences sp = Application.getINSTANCE().getSharedPreferences("config", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(name, date);
        editor.commit();
        System.out.println("保存到share 成功:" + name + "->" + date);
    }

    /**
     * 从SharedPreferences读取数据 (Int)
     *
     * @param name 数据名
     * @return 存在返回内容，不存在返回404
     */
    public static int getshareInt(String name) {
        try {
            SharedPreferences sp = Application.getINSTANCE().getSharedPreferences("config", Context.MODE_PRIVATE);
            int result = sp.getInt(name, 404);
            System.out.println("得到share 数据成功:" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return 404;
        }
    }

    /**
     * 从SharedPreferences读取数据 String
     *
     * @param name 数据名
     * @return 存在返回内容，不存在返回""
     */
    public static String getshareString(String name) {
        try {
            SharedPreferences sp = Application.getINSTANCE().getSharedPreferences("config", Context.MODE_PRIVATE);
            String result = sp.getString(name, "");
            System.out.println("得到share 数据成功:" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 保存数据到SharedPreferences(String)
     *
     * @param name 数据名称
     * @param date 数据内容
     */
    public static void saveshareString(String name, String date) {
        SharedPreferences sp = Application.getINSTANCE().getSharedPreferences("config", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(name, date);
        editor.commit();
        System.out.println("保存到share 成功:" + name + "->" + date);
    }

}
