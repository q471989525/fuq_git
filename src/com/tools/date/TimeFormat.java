package com.tools.date;

import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.apache.log4j.*;

public class TimeFormat {

    private static Logger logger = Logger.getLogger(TimeFormat.class);
    private static final Map<String, Locale> LOCALE_CACHE = new HashMap();
    private static final Locale LOCALE = Locale.getDefault();
    private static final String EN_WEEK = "Monday|Mon|Tuesday|Tue|Wednesday|Wed|Thursday|Thu|Friday|Fri|Saturday|Sat|Sunday|Sun";
    private static final String EN_MONTH = "January|Jan|February|Feb|March|Mar|April|Apr|May|May|June|Jun|July|Jul|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec";
    //第一个表示取时间的正则表达式，第二个表示格式化日期的模式，第三个表示时间对应的语言环境（该值等于java.util.Locale里的常量字段的名字）
    //******复杂的模式尽量放前面******
    //比如Thursday September 15, 2011, 3:57 am里面的2011, 3:57会和中文的年月日匹配上
    //在满足该条件的情况下，可以把使用率较高的放前面
    private static final String[][] TIME_FORMAT = {
        {"\\d{4}\\D{1,2}\\d{1,2}\\D{1,2}\\d{1,2}\\D{1,2}\\d{1,2}\\D\\d{1,2}", "yyyy MM dd HH mm", "CHINA"},//年月日时分
        {"\\d{2}年\\d{1,2}月\\d{1,2}日", "yy MM dd", "CHINA"},
        {"(" + EN_WEEK + ")\\D{1,2}(" + EN_MONTH + ")\\D{1,2}\\d{1,2}\\D{1,2}\\d{4}\\D{1,2}\\d{1,2}\\D\\d{1,2}\\D{1,2}(am|AM|pm|PM)", "EEE MMM dd yyyy hh mm aa", "US"},//星期 月 日 年 小时 分钟 am/pm
        {"(" + EN_WEEK + ")\\D{1,2}(" + EN_MONTH + ")\\D{1,2}\\d{1,2}\\D{1,2}\\d{4}\\D{1,2}\\d{1,2}\\D\\d{1,2}", "EEE MMM dd yyyy HH mm", "US"},//星期 月 日 年 小时 分钟
        {"\\d{4}\\D{1,2}\\d{1,2}\\D{1,2}\\d{1,2}", "yyyy MM dd", "CHINA"},//年月日
        {"(" + EN_WEEK + ")\\D{1,2}(" + EN_MONTH + ")\\D{1,2}\\d{1,2}\\D{1,2}\\d{4}", "EEE MMM dd yyyy", "US"},//星期 月 日 年
        {"(" + EN_MONTH + ")\\D{1,2}\\d{1,2}\\D{1,2}\\d{4}", "MMM dd yyyy", "US"},//月 日 年
        {"\\d{4}\\D{1,2}\\d{1,2}\\D{1,2}\\d{1,2}\\D{1,2}\\d{1,2}", "yyyy MM dd HH", "CHINA"},//年月日时
    };

    /**
     * 寻找字符串中的日期，至少得包含年月日,返回格式yyyy-MM-dd HH:mm
     *
     * @param timeString
     * @return
     */
    public static String timeStringToDate(String timeString) {
        String time = "";
        for (int j = 0; j < TIME_FORMAT.length; j++) {
            String reg = TIME_FORMAT[j][0];
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(timeString);
            if (m.find()) {
                time = m.group(0);
                time = time.replaceAll("\\W+", " ");
                Date date = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT[j][1], getLocale(TIME_FORMAT[j][2]));
                    sdf.setLenient(false);
                    date = sdf.parse(time);
                } catch (Exception e) {
                    logger.error(time + " " + e.getMessage());
                    return "";
                }
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time = sim.format(date);
                break;
            }
        }
        return time;
    }

    /**
     * 根据语言获取Locale
     *
     * @param lang
     * @return
     * @throws Exception
     */
    private static Locale getLocale(String lang) throws Exception {
        Locale loc = LOCALE_CACHE.get(lang);
        if (loc == null) {
            loc = (Locale) Locale.class.getDeclaredField(lang).get(LOCALE);
            LOCALE_CACHE.put(lang, loc);
        }
        return loc;
    }

    /**
     * 在某个日期上添加 N天、月、年
     */
    public void addDate() {
        Calendar cal = Calendar.getInstance();
        //  cal.set(Calendar.YEAR,Calendar.MONTH,Calendar.DATE);
        // cal.add(cal.MARCH, 3);添加月份
        cal.add(cal.DAY_OF_YEAR, 40);
        SimpleDateFormat sFmt = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");

        System.out.println(sFmt.format(cal.getTime()));
    }

    /**
     * 得到当前时间
     *
     * @return
     */
    public String getNowTime() {
        //得到系统当前的毫秒数
        Date valiDate = new Date(System.currentTimeMillis());
        System.out.println(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(valiDate));
        return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(valiDate);
    }

    /**
     * 得到当前时间yyyy-MM-dd HH:mm
     *
     * @return String
     */
    public static String getNowDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    /**
     * 得到当前时间HH:mm:ss
     *
     * @return String
     */
    public static String getNowHHmmss() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    /**
     * 获取该日期所在的周的所有日期
     *
     * @param day 日期,格式yyyy-MM-dd HH:mm
     * @return 返回周的所有日期格式yyyy-MM-dd HH:mm
     * @throws ParseException 日期解析出错
     */
    public static String[] getWeekDays(String day) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String[] weekDays = new String[7];
        Date d = dateFormat.parse(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int count = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_WEEK, -1 * count + 1);
        for (int i = 0; i < 7; i++) {
            weekDays[i] = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return weekDays;
    }

    /**
     * 获取该日期所在的月的所有日期
     *
     * @param day 日期,格式yyyy-MM-dd HH:mm
     * @return 返回月的所有日期格式yyyy-MM-dd HH:mm
     */
    public static String[] getMonthDays(String day) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = dateFormat.parse(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int length = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] monthDays = new String[length];
        int count = cal.get(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.DAY_OF_MONTH, -1 * count + 1);
        for (int i = 0; i < length; i++) {
            monthDays[i] = dateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return monthDays;
    }

    /**
     * 是否一天,按24小时计算差值
     *
     * @param time1
     * @param time2
     * @return boolean
     * @throws ParseException
     */
    public static boolean isADay(String time1, String time2) throws ParseException {
        int hours = getHoursOffset(time1, time2);
        if (hours < 24) {
            return true;
        }
        return false;
    }

    /**
     * 获取两个日期相差的小时数
     *
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public static int getHoursOffset(String time1, String time2) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1 = dateFormat.parse(time1);
        Date d2 = dateFormat.parse(time2);
        long offset = Math.abs(d1.getTime() - d2.getTime());
        int hours = (int) (offset / (3600 * 1000));
        return hours;
    }

    /**
     * 获取两个时间之间相差的天数
     *
     * @param time1
     * @param time2
     * @return int
     * @throws ParseException
     */
    public static int getDaysOffset(String time1, String time2) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1 = dateFormat.parse(time1);
        Date d2 = dateFormat.parse(time2);
        long offset = Math.abs(d1.getTime() - d2.getTime());
        int days = (int) (offset / (24 * 3600 * 1000));
        return days;
    }

    /**
     * 是否一周，按七天计算差值，而不是自然时间的周,包括今天
     *
     * @param time1
     * @param time2
     * @return boolean
     * @throws ParseException
     */
    public static boolean isAWeek(String time1, String time2) throws ParseException {
        int days = getDaysOffset(time1, time2);
        if (days < 7) {
            return true;
        }
        return false;
    }

    /**
     * 是否一个月,按30天计算差值
     *
     * @param time1
     * @param time2
     * @return boolean
     * @throws ParseException
     */
    public static boolean isAMonth(String time1, String time2) throws ParseException {
        int days = getDaysOffset(time1, time2);
        if (days < 30) {
            return true;
        }
        return false;
    }

    /**
     * 得到当前日起加或者减去month个月的时间。
     *
     * @param month
     * @return 返回
     */
    public static String getTimeOffsetMonths(int month) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, month);
        String dateString = now.get(Calendar.YEAR) + "-"
                + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);
        return timeStringToDate(dateString);
    }

    /**
     * 得到当前日起加或者减去day个天的时间。
     *
     * @param day
     * @return 返回
     */
    public static String getTimeOffsetDays(int day) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, day);
        String dateString = now.get(Calendar.YEAR) + "-"
                + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);
        return timeStringToDate(dateString);
    }

    /**
     * 得到当前日起加或者减去hour个小时时间。
     *
     * @param hour
     * @return String
     */
    public static String getTimeOffsetHours(int hour) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, hour);
        String dateString = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-"
                + now.get(Calendar.DAY_OF_MONTH) + "-" + now.get(Calendar.HOUR_OF_DAY);
        return timeStringToDate(dateString);
    }

    /**
     * 得到当前日起加或者减去min分钟时间。
     *
     * @param min
     * @return
     */
    public static String getTimeOffsetMin(int min) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, min);
        String dateString = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-"
                + now.get(Calendar.DAY_OF_MONTH) + "-" + now.get(Calendar.HOUR_OF_DAY) + "-" + now.get(Calendar.MINUTE);
        return timeStringToDate(dateString);
    }

    /**
     * 比较两个时间的先后，时间格式yyyy-MM-dd HH:mm
     * 第一个日期大于第二个日期 返回1 第二个日期大于第一个日期 返回-1
     * 如果两个日期相等 返回0
     * @param o1
     * @param o2
     * @return
     * @throws ParseException
     */
    public static int compareTime(String o1, String o2) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Date d1 = dateFormat.parse(o1);
        Date d2 = dateFormat.parse(o2);
        Calendar ca1 = Calendar.getInstance();
        ca1.setTime(d1);
        Calendar ca2 = Calendar.getInstance();
        ca2.setTime(d2);
        return ca1.compareTo(ca2);
    }

    /**
     * 格式化日期
     *
     * @param t
     * @return 返回日期格式yyyy-MM-dd HH:mm
     */
    public static String formatTime(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(t));
    }

    /**
     * 格式化日期 返回long
     *
     * @param timeStr 日期字符串
     * @return
     */
    public static long formatTime(String timeStr) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateFormatH = new SimpleDateFormat("yyyy-MM-dd HH");
        SimpleDateFormat dateFormatD = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = dateFormat.parse(timeStr);
        } catch (Exception e) {
            try {
                d = dateFormatH.parse(timeStr);
            } catch (Exception e1) {
                try {
                    d = dateFormatD.parse(timeStr);
                } catch (Exception e2) {
                    throw e2;
                }
            }
        }
        return d.getTime();
    }

    public static void main(String[] args) throws Exception { //Thursday September 15, 2011, 3:57 am
        //        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", getLocale("US"));//Thursday, Sep. 15, 2011
        //        dateFormat.setLenient(false);
        //        Date d = dateFormat.parse("September 15, 2011");
        //        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //        System.out.println(sim.format(d));
        //   System.out.println(timeStringToDate("09年09月27日"));//2011年2月22日 11:22
        int compareTime = compareTime("2012-5-10 17:17:07", "2012-5-10 17:17:15");
        System.out.println(compareTime);
    }
}
