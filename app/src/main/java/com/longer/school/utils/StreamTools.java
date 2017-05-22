package com.longer.school.utils;

import android.content.Context;
import android.util.Log;


import com.blankj.utilcode.utils.TimeUtils;
import com.longer.school.modle.bean.CardClass;
import com.longer.school.modle.bean.CourseClass;
import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.Score2Class;
import com.longer.school.modle.bean.ScoreClass;
import com.longer.school.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamTools {
    /*
     *  将网页源码转换为String
     */
    public static String readInputStream(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            byte[] result = baos.toByteArray();
            // 试着解析result里面的字符串
            String temp = new String(result);
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        // 方法二
        /*
         * if (httpResponse.getStatusLine().getStatusCode() == 200) { HttpEntity
		 * entity = httpResponse.getEntity(); return
		 * EntityUtils.toString(entity, "utf-8");
		 */
    }

    /**
     * 将网页源码转换为String_正方系统
     *
     * @param is
     * @return
     */
    public static String readInputStream_zfxt(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            byte[] result = baos.toByteArray();
            // 试着解析result里面的字符串
            String temp = new String(result, "gbk");
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        // 方法二
        /*
         * if (httpResponse.getStatusLine().getStatusCode() == 200) { HttpEntity
		 * entity = httpResponse.getEntity(); return
		 * EntityUtils.toString(entity, "utf-8");
		 */
    }

    // 将网页中返回的js 从unicode 编码转化为 UTF-8编码，不然json无法解析；
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    // 用于截取js返回数据中的数组-查询余额
    public static String getarray(String text) {
        int str_end = text.lastIndexOf("]") + 1;
        int str_stra = text.indexOf("FLW") - 3;
//        Log.d("cd", "总长度：" + text.length() + "  -" + str_end + "-" + str_stra);
//        Log.d("cd", "总文字：" + text);
        if (text.length() < 210) {
            return "null";
        }
        text = text.substring(str_stra, str_end);
        return text;
    }
    // 用于截取js返回数据中的数组-查询余额
    public static String getlibrary_array(String text) {
        int str_end = text.lastIndexOf("]") + 1;
        int str_stra = text.indexOf("BT") - 3;
        if (text.length() < 210) {
            return "null";
        }
        text = text.substring(str_stra, str_end);
        return text;
    }

    // 用于截取js返回数据中的对象
    public static String getobject(String text) {
        int str_end = text.lastIndexOf("}") + 1;
        int str_stra = text.indexOf("{");
        text = text.substring(str_stra, str_end);
        return text;
    }

    // 用于截取js返回数据中的数组-查询图书馆未还书籍
    public static String getarray2(String text) {
        int str_end = text.lastIndexOf("]") + 1;
        int str_stra = text.indexOf("[");
        text = text.substring(str_stra, str_end);
        return text;
    }

    /**
     * 简化课程表，得到 1.3.5.7.9 这几个对象
     *
     * @param context
     * @param str
     * @return
     */
    public static List<CourseClass> getcourse(Context context, String str) {
        List<CourseClass> courses = new ArrayList<CourseClass>();
        int bg_num = 0;// 背景图
        Document doc1 = Jsoup.parse(str);
        // 处理之后只剩下5行
        Elements ele1 = doc1.select("table[class=blacktab] tbody tr");
        Log.d("长度", ele1.size() + "");
        ele1.remove(12);
        ele1.remove(11);
        ele1.remove(9);
        ele1.remove(7);
        ele1.remove(5);
        ele1.remove(3);
        ele1.remove(1);
        ele1.remove(0);
        for (int i = 0; i < 5; i++) {
            // 8-9列，以为有的 上午占了4行
            Elements ele2 = ele1.get(i).select("td");
            CourseClass course = null;

            for (int j = 0; j < ele2.size(); j++) {
                Element ele = ele2.get(j);
                // String table = null;
                // 先判断长度,1.2行不用解析
                if (ele.text().length() > 5) {
                    // if (ele.text().length() > 8 &&
                    // !ele.toString().contains("(调")) {
                    course = new CourseClass();
                    // 保存Course的id，就是课程的位置
                    //要判断他的长度，因为有的课占了3格，导致下一行少一列或者2列
                    int id = 0;
                    if (ele2.size() >= 6 && ele2.size() <= 8) {
                        // Log.d("列数:", ele2.size() + "");
                        // table = ele.text().replaceAll(" ", "#");
                        // Log.d("内容：" + i + "行" + j + "列", table);
                        id = j * 10 + (i + 1);
                    } else if (ele2.size() == 9) {
                        // Log.d("列数:", ele2.size() + "");
                        // table = ele.text().replaceAll(" ", "#");
                        // Log.d("内容：" + i + "行" + j + "列", table);
                        id = (j - 1) * 10 + (i + 1);
                    }
//                    else if (ele2.size() == 7) {
//                        // Log.d("列数:", ele2.size() + "");
//                        // table = ele.text().replaceAll(" ", "#");
//                        // Log.d("内容：" + i + "行" + j + "列", table);
//                        id = j * 10 + (i + 1);
//                    }

                    String[] cou = ele.text().split(" ");
                    // Log.d("id：", id + "");
                    course.setId(id);


                    if (cou.length <= 2) {//有的课程只有课名
                        course.setName(cou[0]);
                        course.setZoushu("null");
                        course.setTeacher("null");
                        course.setRoom("null");
                    } else if (cou.length == 3) {
                        course.setName(cou[0]);
                        course.setZoushu(cou[1]);
                        course.setTeacher("null");
                        course.setRoom(cou[2]);
                    } else {
                        course.setName(cou[0]);
                        course.setZoushu(cou[1]);
                        course.setTeacher(cou[2]);
                        course.setRoom(cou[3]);
                    }
//					 Log.d("课程：" ,course.getName());
//					 Log.d("老师：" ,course.getTeacher());
//					 Log.d("周数：" ,course.getZoushu());
                    // 设置背景图
                    int t = 0;
                    for (CourseClass info : courses) {
                        if (info.getName().contains(cou[0])) {
                            t = 1;
                            course.setBg(info.getBg());
                            break;
                        } else {
                            course.setBg(bg_num);
                        }
                    }
                    if (t != 1) {
                        bg_num++;
                    }
                    courses.add(course);
                    course = null;
                }
            }
        }
        return courses;
    }

    /*
     * 获取专周信息
     */
    public static void getzhuanzhou(Context context, String str) {
        try {
            Document doc1 = Jsoup.parse(str);
            Elements ele1 = doc1.select("table[class=datelist]table[id=DataGrid1] tbody tr");
            // Log.d("专周", ele1.toString()+"#");
            // 去除第一行
            ele1.remove(0);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ele1.size(); i++) {
                // 8-9列，以为有的 上午占了4行
                Elements ele2 = ele1.get(i).select("td");
                sb.append(ele2.get(0).text());
                sb.append("#");
                sb.append(ele2.get(1).text());
                sb.append("#");
                sb.append(ele2.get(2).text());
                sb.append("#");
                sb.append(ele2.get(3).text());
                sb.append("@");
            }
            String text = sb.toString().substring(0, sb.length() - 1);
            Log.d("专周", text);
            // 保存文件，并设置share 的course 为true;
            FileTools.saveFile(context, "zhuanzhou.txt", text);
            FileTools.saveshare(context, "zhuanzhou", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析成绩链表
     */
    public static List<ScoreClass> getscore(String str) {
        // 放入解析器，返回List
        List<ScoreClass> scores = new ArrayList<ScoreClass>();
        Document doc2 = Jsoup.parse(str);
        Elements ele2 = doc2.select("table[id=Datagrid1]table[class=datelist] tbody tr");
        // System.out.println("内容：" + ele2.toString());
        ele2.remove(0);
        Log.d("多少节课长度", ele2.size() + "");

        ScoreClass score = null;
        for (int i = 0; i < ele2.size(); i++) {
            // 8-9列，以为有的 上午占了4行
            Elements ele3 = ele2.get(i).select("td");
            Log.d("第几门：", i + "");

            // 13个元素
            score = new ScoreClass();
            score.setXn(ele3.get(0).text());
            score.setXq(ele3.get(1).text());
            score.setName(ele3.get(3).text());
            score.setSx(ele3.get(4).text());
            score.setXf(ele3.get(6).text());
            score.setJd(ele3.get(7).text());
            score.setCj_ps(ele3.get(8).text());
            score.setCj_qz(ele3.get(9).text());
            score.setCj_qm(ele3.get(10).text());
            score.setCj_sy(ele3.get(11).text());
            score.setCj_zp(ele3.get(12).text());
            score.setXy(ele3.get(17).text());
            score.setCx(ele3.get(19).text());

            // Log.d("学期：", score.getXq());
            // Log.d("学年：", score.getXn());
            // Log.d("课名：", score.getName());
            // Log.d("属性：", score.getSx());
            // Log.d("学分：", score.getXf());
            // Log.d("绩点：", score.getJd());
            // Log.d("平时成绩：", score.getCj_ps());
            // Log.d("期中成绩：", score.getCj_qz());
            // Log.d("期末成绩：", score.getCj_qm());
            // Log.d("实验成绩：", score.getCj_sy());
            // Log.d("总评成绩：", score.getCj_zp());
            // Log.d("开课校园：", score.getXy());
            // Log.d("重修：", score.getCx());

            scores.add(score);
            score = null;
        }
        return scores;
    }

    /**
     * 解析成绩链表  的学年，比如2014-2015
     */
    public static String[] getscorexn(String str) {
        try {
            // 放入解析器，返回List
            List<ScoreClass> scores = new ArrayList<ScoreClass>();
            Document doc = Jsoup.parse(str);
            Elements ele = doc.select("div [class=searchbox] p select[id=ddlXN] option");
            ele.remove(0);//去掉第一个空白
//            Log.d("tip","学年1" + ele.toString());
            String[] xn = new String[ele.size()];
            for (int i=0; i<ele.size(); i++) {
                xn[i] = ele.get(i).text();
            }
            return xn;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析考试教室
     */
    public static List<String[]> getscore_djks(Context context, String str) {
        // 放入解析器，返回List
        Document doc2 = Jsoup.parse(str);
        Elements ele2 = doc2.select("table[id=Datagrid1]table[class=datelist] tbody tr");
        // System.out.println("内容：" + ele2.toString());
        ele2.remove(0);
        Log.d("多少节课长度", ele2.size() + "");
        String[] cj = null;
        List<String[]> lis = new ArrayList<String[]>();

        for (int i = 0; i < ele2.size(); i++) {
            cj = new String[4];
            // 8-9列，以为有的 上午占了4行
            Elements ele3 = ele2.get(i).select("td");
            Log.d("第几门：", i + "");
            cj[0] = "名称：" + ele3.get(2).text();
            cj[1] = "成绩：" + ele3.get(5).text();
            cj[2] = "学期：" + ele3.get(0).text() + "第" + ele3.get(1).text() + "学期";
            cj[3] = "考试时间：" + ele3.get(4).text();
            lis.add(cj);
        }
        return lis;
    }

    /**
     * 解析图书馆，我的书籍
     */
    public static List<String[]> getmybook(Context context, String str) {
        List<String[]> lis = new ArrayList<String[]>();
        // 放入解析器，返回List
        Document doc = Jsoup.parse(str);
        //得到欠罚款，欠赔款	，已外借书籍
        FileTools.saveshare(context, "library_qfk", doc.select("span[id=LblQfk]").text());
        FileTools.saveshare(context, "library_qpk", doc.select("span[id=LblQpk]").text());
        FileTools.saveshare(context, "library_book", doc.select("span[id=LblBrooyCount]").text());

        String qfk = doc.select("span[id=LblQfk]").text();
        Elements ele = doc.select("table[id=DataGrid1] tbody tr");
        System.out.println("内容：" + ele.toString());
        if (ele.toString().length() < 5) {
            return lis;
        }
        ele.remove(0);
        Log.d("多少本书", ele.size() + "");
        String[] cj = null;

        for (int i = 0; i < ele.size(); i++) {
            cj = new String[6];
            Elements ele3 = ele.get(i).select("td");
            cj[0] = ele3.get(1).text();//书名
            cj[1] = ele3.get(2).text();//外借时间
            cj[2] = ele3.get(3).text();//应还时间
            cj[3] = ele3.get(6).text();//馆藏地点
            cj[4] = ele3.get(4).text();//续借次数
            cj[5] = "http://211.83.32.195/gdnetweb/" + ele3.get(4).select("a").attr("href");
            Log.e("续借网站", cj[5] + "#");
            lis.add(cj);
        }
        return lis;
    }

    /**
     * 解析图书馆，我的信息
     */
    public static String[] get_card_infor(Context context, String str) {

        String[] lis = new String[3];
        // 放入解析器，返回List
        Document doc = Jsoup.parse(str);
        Log.e("学号", doc.select("span[id=LblCardNO]").text() + "#");
        lis[0] = doc.select("span[id=LblCardNO]").text().trim();//学号
        lis[1] = doc.select("span[id=LblreaderName]").text();//姓名
        lis[2] = doc.select("span[id=LblReaderUnit]").text();//班级

        return lis;
    }

    /**
     * 解析学分统计
     */
    public static List<Score2Class> getscore_xftj(Context context, String str) {
        List<Score2Class> score2s = new ArrayList<Score2Class>();
        Score2Class score2 = null;
        try {
            String[] cj;
            // 放入解析器，返回List
            Document doc2 = Jsoup.parse(str);
            Elements ele2 = doc2.select("table[id=Datagrid2]table[class=datelist] tbody tr");
            ele2.remove(0);
            // cj = new String[5];

            for (int i = 0; i < ele2.size(); i++) {
                Elements ele3 = ele2.get(i).select("td");
                // Log.d("学分：", ele3.get(2).text());

                score2 = new Score2Class();
                score2.setName(ele3.get(0).text());
                score2.setXf_hx(ele3.get(4).text());
                score2.setXf_yq(ele3.get(1).text());
                score2.setXf_wtg(ele3.get(3).text());
                score2.setXf_yh(ele3.get(2).text());

                score2s.add(score2);
                score2 = null;
            }
            return score2s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析课表查询中的班级信息
     */
    public static Map<String, String> getscore_course_bj(Context context, String str) {
        Map<String, String> map_xy = new HashMap<String, String>();
        try {
            // 放入解析器，返回List
            Document doc2 = Jsoup.parse(str);
            Elements ele2 = doc2.select("table[id=Table1] tbody tr").get(1).select("td").get(2).select("option");
            ele2.remove(0);
            for (int i = 0; i < ele2.size(); i++) {
                map_xy.put(ele2.get(i).text(), ele2.attr("value"));
            }
            return map_xy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析图书馆中的借阅历史（简单的解析）返回String
     */
    public static Elements get_library_history(String str) {
        try {
            // 放入解析器，返回List
            Document doc2 = Jsoup.parse(str);
            Elements ele2 = doc2.select("table[id=DataGrid1] tbody");
//			ele2.remove(0);
            ele2 = ele2.select("tr[bgcolor!=#006699]");
//			Log.e("借书记录",ele2.toString() +"#");

            return ele2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 简化课程表，得到 1.3.5.7.9 这几个对象
     *
     * @param context
     * @param str
     * @return
     */
    public static List<CourseClass> get_scorecourse(Context context, String str) {
        List<CourseClass> courses = new ArrayList<CourseClass>();
        int bg_num = 0;// 背景图
        Document doc1 = Jsoup.parse(str);
        // 处理之后只剩下5行
        Elements ele1 = doc1.select("table[id=Table6]table[class=blacktab] tbody tr");
        Log.d("长度", ele1.size() + "");

        ele1.remove(12);
        ele1.remove(11);
        ele1.remove(9);
        ele1.remove(7);
        ele1.remove(5);
        ele1.remove(3);
        ele1.remove(1);
        ele1.remove(0);

        for (int i = 0; i < 5; i++) {
            // 8-9列，以为有的 上午占了4行
            Elements ele2 = ele1.get(i).select("td");
            CourseClass course = null;

            for (int j = 0; j < ele2.size(); j++) {
                Element ele = ele2.get(j);
                // String table = null;
                // 先判断长度,1.2行不用解析
                if (ele.text().length() > 8 && !ele.toString().contains("(调")) {
                    course = new CourseClass();
                    // 保存Course的id
                    int id = 0;
                    if (ele2.size() == 8) {
                        // Log.d("列数:", ele2.size() + "");
                        // table = ele.text().replaceAll(" ", "#");
                        // Log.d("内容：" + i + "行" + j + "列", table);
                        id = j * 10 + (i + 1);
                    } else if (ele2.size() == 9) {
                        // Log.d("列数:", ele2.size() + "");
                        // table = ele.text().replaceAll(" ", "#");
                        // Log.d("内容：" + i + "行" + j + "列", table);
                        id = (j - 1) * 10 + (i + 1);
                    }
                    String[] cou = ele.text().split(" ");
                    // Log.d("id：", id + "");
                    course.setId(id);
                    course.setName(cou[0]);
                    course.setZoushu(cou[1]);
                    course.setTeacher(cou[2]);
                    course.setRoom(cou[3]);
                    // Log.d("课程：" ,course.getName());
                    // Log.d("老师：" ,course.getTeacher());
                    // Log.d("周数：" ,course.getZoushu());
                    // 设置背景图
                    int t = 0;
                    for (CourseClass info : courses) {
                        if (info.getName().contains(cou[0])) {
                            t = 1;
                            course.setBg(info.getBg());
                            break;
                        } else {
                            course.setBg(bg_num);
                        }
                    }
                    if (t != 1) {
                        bg_num++;
                    }
                    courses.add(course);
                    course = null;
                }
            }
        }
        return courses;
    }

    /**
     * 得到一条消费记录
     *
     * @return
     */
    public static String getonecard() {
        String text = FileTools.getFile(MainActivity.instance, "card.txt");
        if (!"".equals(text)) {// 读取文件失败
            try {
                JSONArray js = new JSONArray(text);
                JSONObject jo = (JSONObject) js.opt(0);
                return jo.getString("TRD_CNAME") + "   " + TimeUtils.getFriendlyTimeSpanByNow(jo.getString("FLW_CONTIME")) + "  " + jo.getString("FLW_AMOUNT") + "元";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("数据读取产生异常");
                return "null";
            }
        }
        return "null";
    }


    /**
     * 得到今天的课表
     *
     * @return
     */
    public static List<CourseClass> gettodaycourse() {
        try {
            String str = FileTools.getFile(MainActivity.instance, "course.txt");
            if ("".equals(str)) {
                return null;
            }

            List<CourseClass> courses = new ArrayList<CourseClass>();
            CourseClass course = null;
            //得到今天是星期几，星期天是0，星期一是1
            int xq = TimeTools.xq();
            String[] str1 = str.split("@");
            for (String info : str1) {
                //得到每一节课的列数，如果和星期相等那么就是今天的课
                String[] str2 = info.split("#");
                int lie = Integer.parseInt(str2[1]);
                int lie2 = (lie / 10) % 10;
//                Log.d("tip", "列数：" + lie);
                if (lie2 == xq) {
                    course = new CourseClass();
                    course.setName(str2[2]);
                    course.setRoom(str2[3]);
                    course.setId(lie);
                    courses.add(course);
                    course = null;
                }
            }
//            Log.d("有多少节课：", courses.size() + "");
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获得指定范围内的随机数
     *
     * @param max
     * @return int
     */

    public static int getRandom(int max) {
        return (int) (Math.random() * max);
    }
}
