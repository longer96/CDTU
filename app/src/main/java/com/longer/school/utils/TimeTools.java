package com.longer.school.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.Log;

public class TimeTools {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 检查更新，
     *
     * @return
     */
    public static boolean update() {
        try {
            Date date_last = df.parse("2016-10-02 00:00:00");// 在2016-09-02之后必须更新
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);

            long diff = date_last.getTime() - date_now.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            // long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 *
            // 60);
            // long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours *
            // (1000 * 60 * 60)) / (1000 * 60);

            if (days <= 0) {
                System.out.println("现在必须更新了");
                return true;
            }

            System.out.println("剩余使用天数：" + days + "天");
            // Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
            // t.setToNow(); // 取得系统时间。
            // int year = t.year;
            // int month = t.month;
            // int date = t.monthDay;
            // int hour = t.hour; // 0-23
            // int minute = t.minute;
            // int second = t.second;
            // System.out.println("时间2："+year+"-"+month+"-"+date);

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 检查主界面card 的刷新 如果差距1分钟 即更新
     *
     * @return
     */
    public static boolean refresh_card(Context context) {
        try {
            // 获取上次储存的时间
            String resh = FileTools.getshare(context, "refresh_card");
            if ("".equals(resh)) {
                FileTools.saveshare(context, "refresh_card", "1996-04-13 00:00:00");
                resh = "1996-04-13 00:00:00";
            }
            Date date_last = df.parse(resh);
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);
            long diff = date_last.getTime() - date_now.getTime();// 这样得到的差值是微秒级别
            long minutes = diff / (1000 * 60);//
            System.out.println("距离上次分钟：" + minutes);
            if (minutes <= -1) {
                FileTools.saveshare(context, "refresh_card", str);
                return true;
            }
            System.out.println("剩余使用天数：" + minutes + "分钟");
        } catch (Exception e) {
            FileTools.saveshare(context, "refresh_card", "1996-04-13 00:00:00");
            return false;
        }
        return false;
    }

    /**
     * 检查主界面校园公告 的刷新 如果差距5分钟 即更新
     *
     * @return
     */
    public static boolean refresh_news(Context context) {
        try {
            // 获取上次储存的时间
            String resh = FileTools.getshare(context, "refresh_news");
            if ("".equals(resh)) {
                FileTools.saveshare(context, "refresh_news", "1996-04-13 00:00:00");
                resh = "1996-04-13 00:00:00";
            }
            Date date_last = df.parse(resh);
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);
            long diff = date_last.getTime() - date_now.getTime();// 这样得到的差值是微秒级别
            long minutes = diff / (1000 * 60);//
            System.out.println("距离上次分钟：" + minutes);
            if (minutes <= -5) {
                FileTools.saveshare(context, "refresh_news", str);
                return true;
            }
            //System.out.println("距离上次分钟：" + minutes + "分钟");
        } catch (Exception e) {
            FileTools.saveshare(context, "refresh_news", "1996-04-13 00:00:00");
            return false;
        }
        return false;
    }

    /**
     * 用来限制次数，比如一天只能表白一次
     *
     * @return true :可以发布  false : 不能发布
     */
    public static boolean limit(String name, int time) {
        try {
            // 获取上次储存的时间
            String resh = FileTools.getshareString(name);
            if ("".equals(resh)) {
                FileTools.saveshareString(name, "1996-04-13 00:00:00");
                return true;
            }
            Date date_last = df.parse(resh);
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);
            long diff = date_now.getTime() - date_last.getTime();// 这样得到的差值是微秒级别
            long hours = diff / (1000 * 60 * 60);//
            System.out.println("距离上次小时：" + hours);
            if (hours >= time) {
                FileTools.saveshareString(name, str);
                Log.d("大于了", "limit: ");
                return true;
            }
            return false;
        } catch (Exception e) {
            FileTools.saveshareString(name, "1996-04-13 00:00:00");
            return false;
        }
    }

    /**
     * 检查版本更新 如果差距12小时 即再次提示
     *
     * @return
     */
    public static boolean refresh_baidu(Context context) {
        try {
            // 获取上次储存的时间
            String resh = FileTools.getshare(context, "refresh_baidu");
            if ("".equals(resh)) {
                FileTools.saveshare(context, "refresh_baidu", "1996-04-13 00:00:00");
                resh = "1996-04-13 00:00:00";
            }
            Date date_last = df.parse(resh);
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);
            long diff = date_last.getTime() - date_now.getTime();// 这样得到的差值是微秒级别
            long hours = diff / (1000 * 60 * 60);//
            System.out.println("更新：距离上次小时：" + hours);
            if (hours <= -12) {
                FileTools.saveshare(context, "refresh_baidu", str);
                return true;
            }
            System.out.println("更新：距离上次小时：" + hours + "小时");
        } catch (Exception e) {
            FileTools.saveshare(context, "refresh_baidu", "1996-04-13 00:00:00");
            return false;
        }
        return false;
    }


    /**
     * 检查消息提示，12小时之内提示过就不用再提示
     *
     * @return
     */
    public static boolean toast_news(Context context) {
        try {
            // 获取上次储存的时间
            String resh = FileTools.getshare(context, "toast_news");
            if ("".equals(resh)) {
                FileTools.saveshare(context, "refresh_baidu", "1996-04-13 00:00:00");
                resh = "1996-04-13 00:00:00";
            }
            Date date_last = df.parse(resh);
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);
            long diff = date_last.getTime() - date_now.getTime();// 这样得到的差值是微秒级别
            long hours = diff / (1000 * 60 * 60);//
            System.out.println("更新：距离上次小时：" + hours);
            if (hours <= -3) {
                FileTools.saveshare(context, "toast_news", str);
                return true;
            }
        } catch (Exception e) {
            FileTools.saveshare(context, "toast_news", "1996-04-13 00:00:00");
            return false;
        }
        return false;
    }

    /**
     * 计算当前周数
     *
     * @return
     */
    public static String course_zhoushu() {
        try {
            Date date_last = df.parse("2017-02-19 00:00:00");// 记得写开学前7天
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);

            long diff = date_now.getTime() - date_last.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            System.out.println("当前已经开学：" + days + "天");
            String zhoushu = "0";
            if (days >= 0 && days < 7) {
                zhoushu = "1";
            } else if (days >= 7 && days < 14) {
                zhoushu = "2";
            } else if (days >= 14 && days < 21) {
                zhoushu = "3";
            } else if (days >= 21 && days < 28) {
                zhoushu = "4";
            } else if (days >= 28 && days < 35) {
                zhoushu = "5";
            } else if (days >= 35 && days < 42) {
                zhoushu = "6";
            } else if (days >= 42 && days < 49) {
                zhoushu = "7";
            } else if (days >= 49 && days < 56) {
                zhoushu = "8";
            } else if (days >= 56 && days < 63) {
                zhoushu = "9";
            } else if (days >= 63 && days < 70) {
                zhoushu = "10";
            } else if (days >= 70 && days < 77) {
                zhoushu = "11";
            } else if (days >= 77 && days < 84) {
                zhoushu = "12";
            } else if (days >= 84 && days < 91) {
                zhoushu = "13";
            } else if (days >= 91 && days < 98) {
                zhoushu = "14";
            } else if (days >= 98 && days < 105) {
                zhoushu = "15";
            } else if (days >= 105 && days < 112) {
                zhoushu = "16";
            } else if (days >= 112 && days < 119) {
                zhoushu = "17";
            } else if (days >= 119 && days < 126) {
                zhoushu = "18";
            } else if (days >= 126 && days < 133) {
                zhoushu = "19";
            } else if (days >= 133 && days < 140) {
                zhoushu = "20";
            }

            return zhoushu;

        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 计算2个时间之差，返回天数(实际的情况是还书时间减去现在时间)
     * DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
     * Date date_return = df.parse(return_time);
     * Date date_borrow = df.parse(borrow_time);
     * <p>
     * long diff = date_return.getTime() - date_borrow.getTime();// 这样得到的差值是微秒级别
     * long days = diff / (1000 * 60 * 60 * 24);
     * <p>
     * //System.out.println("剩余使用天数：" + days + "天");
     * return days + "";
     *
     * @return
     */
    public static String getday(String secourd) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date_return = df.parse(secourd);
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);

            long diff = date_return.getTime() - date_now.getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);

            //System.out.println("剩余使用天数：" + days + "天");
            return days + "";

        } catch (Exception e) {
            return null;
        }
    }

    // 将日期转化为对应的星期几
    public static String xinqi(String day) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(day);
        } catch (Exception e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
//		System.out.println(dayNames[dayOfWeek]);
        return dayNames[dayOfWeek];
    }

    // 将日期转化为对应的星期数字 星期天是0，星期一是1
    public static Integer xq() {
        try {
            final Integer dayNames[] = {0, 1, 2, 3, 4, 5, 6};
//            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
//            date = sdfInput.parse(day);


            // 获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = df.format(curDate);
            Date date = df.parse(str);


            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek < 0)
                dayOfWeek = 0;
            System.out.println("今天是：" + dayNames[dayOfWeek]);
            return dayNames[dayOfWeek];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
