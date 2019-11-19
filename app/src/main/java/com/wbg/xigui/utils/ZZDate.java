package com.wbg.xigui.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类，直接调用方法，无需初始化
 * Created by Administrator on 2016/1/14.
 */
public class ZZDate {

    /**
     * isBeforeNow
     * 指定时间是否在现在以前
     *
     * @param time long毫秒时间
     * @return 在现在时间以前
     * @author zb
     */
    public static boolean isBeforeNow(long time) {
        long dateTaken = System.currentTimeMillis();
        return time < dateTaken;
    }

    /**
     * isBeforeNow
     * 指定时间是否在现在以前
     *
     * @param time 时间戳格式 yy-mm-dd hh:mm:ss
     * @return 在现在时间以前
     * @author zb
     */
    public static boolean isBeforeNow(String time) {
        return isBeforeNow(getStringToDate(time));
    }

    /**
     * getHMS
     * 只获取时分秒，不要年月日
     *
     * @param date 标准时间戳格式 2016-08-12 13:07:00
     * @return 13:07:00
     * @author zb
     */
    public static String getHMS(String date) {
        if (date.length() > 11) {
            return date.substring(11, date.length());
        } else {
            long time = getStringToDate(date);
            return DateFormat.format("HH:mm:ss", time).toString();
        }
    }

    /**
     * getDate
     * 获取当前时间的时间戳
     *
     * @return yy-mm-dd hh:mm:ss
     * @author zb
     */
    public static String getDate() {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString();
    }

    /**
     * getDate
     * 获取当前时间的时间戳
     *
     * @return yy-mm-dd hh:mm:ss
     * @author zb
     */
    public static String getDateYMD() {
        return DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
    }

    /**
     * getDate
     * long 的毫秒时间返回时间戳
     *
     * @param time long ms
     * @return String, 时间戳
     * @author zb
     */
    public static String getDate(long time) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", time).toString();
    }

    /**
     * getDate
     * long 的毫秒时间返回时间戳
     *
     * @param time long ms
     * @return String, 时间戳
     * @author zb
     */
    public static String getDateRemoveSecond(long time) {
        return DateFormat.format("yyyy-MM-dd HH:mm", time).toString();
    }

    /**
     * getDate
     * long 的毫秒时间返回时间戳
     *
     * @param time long ms
     * @return String, 时间戳
     * @author zb
     */
    public static String getDateHMS(long time) {
        return DateFormat.format("HH:mm:ss", time).toString();
    }

    /**
     * getDateYearToMinute
     * 年月日 时分
     *
     * @param time long
     * @return 去秒的时间戳
     * @author zb
     */
    public static String getDateYearToMinute(long time) {
        return DateFormat.format("yyyy-MM-dd HH:mm", time).toString();
    }

    /**
     * getDateYearToMinute
     * 年月日 时分
     *
     * @param time long
     * @return 去秒的时间戳
     * @author zb
     */
    public static String getDateYearToMinute(String time) {
        return DateFormat.format("yyyy年MM月dd日 HH:mm", getStringToDate(time)).toString();
    }


    private static SimpleDateFormat sf = null;

    /*时间戳转换成字符窜*/
    public static String getDateToString(String time) {
        long i = Long.parseLong(time);
        Date d = new Date(i);
        sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToStringmmss(String time) {
        long i = Long.parseLong(time);
        Date d = new Date(i);
        sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return sf.format(d);
    }

    /**
     * strToDate
     * String转换成DATE
     *
     * @param str 标准时间戳
     * @return date
     * @author zb
     */
    public static Date strToDate(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = simpleDateFormat.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String(年月日)转换成DATE
     *
     * @param string
     * @return date
     */
    public static Date strYMDToDate(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(string);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 是否是今日时间
     *
     * @param time
     * @return
     */
    public static boolean isToday(long time) {
        return isToday(getDate(time));
    }

    /**
     * 是否是今日时间
     *
     * @param time
     * @return
     */
    public static boolean isToday(String time) {
        return time.contains(getYearMonDay()); // 有相同的年月日
    }

//    /**
//     * 时间戳比较
//     *
//     * @param time
//     * @return
//     */
//    public static boolean isToday(String time) {
//        String datetime = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString();
//        if (time.length() >= 10) {
//            String oldTime = datetime.substring(0, 10);
//            return datetime.contains(oldTime);
//        } else {
//            return datetime.contains(time);
//        }
//    }

    /**
     * 将字符串转为时间戳
     * 去掉毫秒
     */
    public static long getStringToDate(String time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 将字符串转为时间戳
     * 去掉毫秒
     */
    public static long getStringToDateYMD(String time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 将时分（HH:mm）转化为long值
     */
    public static long getStringToDateFromHM(String time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 将分（mm）转化为long值
     */
    public static long getStringToDateFromM(String time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 将秒（mm）转化为long值
     */
    public static long getStringToDateFromS(String time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    // a integer to xx:xx:xx
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 将年月日（yyyy-MM-dd）转化为long值
     *
     * @param time
     * @return
     */
    public static long getStringToDateFromYMD(String time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 时间戳字符串转化成时分秒
     *
     * @param time 时间戳字符串yyyy-MM-dd HH:mm:ss
     * @return 时分秒
     */
    public static String strToDateShort(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(Long.parseLong(time));
        return dateString;
    }

    /**
     * 时间戳转化成时分
     *
     * @param time 时间戳字符串
     * @return 时分
     */
    public static String longToHourAndMinAndSecond(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String dateString = format.format(time);
        return dateString;
    }

    /**
     * 时间戳转化成时分秒
     *
     * @param time 时间戳
     * @return 时分
     */
    public static String longToHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String dateString = format.format(time);
        return dateString;
    }

    /**
     * 时分秒加上今天的年月日
     *
     * @param time 时间戳
     * @return 时间戳
     */
    public static long timeToToday(long time) {
        String hms = longToHourAndMinAndSecond(time);
        String ymd = getYearMonDay();
//        L.i("result", "--ymd--" + ymd + "--hms--" + hms);
        return getStringToDate(ymd + " " + hms);
    }

    /**
     * 获取当前日期（月日）
     *
     * @return 当前日期
     */
    public static String getMonthAndDay() {
        return DateFormat.format("MM月dd日", System.currentTimeMillis()).toString();
    }

    /**
     * 获取当前日期（月日）
     *
     * @return 当前日期
     */
    public static String getMonthAndDay(String time) {
        if (time != null && time.length() > 16) {
            return time.substring(5, 10);
        } else {
            return getMonthToMinute(getDate());
        }
    }

    /**
     * 获取当前时间的小时，分
     *
     * @return
     */
    public static String getNowHourAndMin() {
        return DateFormat.format("HH:mm", System.currentTimeMillis()).toString();
    }

    /**
     * 获取指定时间的小时，分
     *
     * @return
     */
    public static String getHourAndMin(String time) {
        if (time != null && time.length() > 16) {
            return time.substring(11, 16);
        } else {
            return getNowHourAndMin();
        }
    }

    /**
     * 月日时分
     *
     * @param time yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getMonthToMinute(String time) {
        return DateFormat.format("MM月dd日 HH:mm", getStringToDate(time)).toString();
    }

    /**
     * 获取当前时间的小时
     *
     * @return
     */
    public static String getNowHour() {
        return DateFormat.format("HH", System.currentTimeMillis()).toString();
    }

    /**
     * 获取当前时间的year
     *
     * @return
     */
    public static String getNowYear() {
        return DateFormat.format("yyyy", System.currentTimeMillis()).toString();
    }

    /**
     * 获取当前时间的小时
     *
     * @return
     */
    public static String getHour(long time) {
        return DateFormat.format("HH", time).toString();
    }

    /**
     * 获取当前时间的小时
     *
     * @return
     */
    public static String getMin(long time) {
        return DateFormat.format("mm", time).toString();
    }

    /**
     * 获取当前时间的分钟
     *
     * @return
     */
    public static String getNowMin() {
        return DateFormat.format("mm", System.currentTimeMillis()).toString();

    }

    /***
     * 获取当前年月日
     *
     * @return
     */
    public static String getYearMonDay() {
        return DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()).toString();
    }


    public static String getYearMonDay(String time) {
        if (time != null && time.length() > 10) {
            return time.substring(0, 10);
        } else {
            return getYearMonDay();
        }
    }

    public static String getYearMon(String time) {
        return DateFormat.format("yyyy年MM月", getStringToDate(time)).toString();
    }

    public static String getYear(String time) {
        return DateFormat.format("yyyy", getStringToDateYMD(time)).toString();
    }

    public static String getMonth(String time) {
        return DateFormat.format("MM", getStringToDateYMD(time)).toString();
    }

    public static String getDay(String time) {
        return DateFormat.format("dd", getStringToDateYMD(time)).toString();
    }

    public static String getMonToMin(String time) {
        return DateFormat.format("MM-dd HH:mm", getStringToDate(time)).toString();
    }

    /***
     * 获取当前年月日
     *
     * @return
     */
    public static String getNowDate() {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString();
    }

    /**
     * 获取指定时间的年月日
     *
     * @param time
     * @return
     */
    public static String getYearMonDay(long time) {
        return DateFormat.format("yyyy-MM-dd", time).toString();
    }

    /**
     * 获取指定时间的月日
     *
     * @param time
     * @return
     */
    public static String getMonthAndDay(long time) {
        return DateFormat.format("yyyy-MM-dd", time).toString().substring(5, 10);
    }

    /**
     * 获取指定时间的时分
     *
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", time).toString().substring(11, 16);
    }

    /**
     * 提供详情展示的时间显示（空格隔开，小时和分钟的显示）
     *
     * @param remindTime 逗号分隔的时间戳
     * @return
     */
    public static String getModeDetailTime(String remindTime) {
        StringBuilder builder = new StringBuilder();
        String[] times = remindTime.split(",");
        for (int i = 0; i < times.length; i++) {
            String time = times[i];
            if (i != 0) {
                builder.append(" ");
            }
            builder.append(time.substring(time.indexOf(" ") + 1).substring(0, 5));
        }
        return builder.toString();
    }

    /**
     * 取出时间戳的小时和分钟，并以数组的形式返回
     *
     * @param remindTime 逗号分隔的时间戳（yy-MM-dd hh:mm:ss）
     * @return
     */
    public static String[] getRemindTimes(String remindTime) {
        String[] times = remindTime.split(",");
        String[] newTimes = new String[times.length];
        for (int i = 0; i < times.length; i++) {
            newTimes[i] = times[i].substring(times[i].indexOf(" ") + 1).substring(0, 5);
        }
        return newTimes;
    }

    /**
     * 将时分的数组变为空格分隔的字符串
     *
     * @param times
     * @return
     */
    public static String getRemindTimes(String[] times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times.length; i++) {
            String time = times[i];
            if (i != 0) {
                builder.append(" ");
            }
            builder.append(time);
        }
        return builder.toString();
    }

    /**
     * 返回时间差（与当前时间，秒数）
     *
     * @param endDate 即将响铃时间
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date endDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endDate = sdf.parse(sdf.format(endDate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        long time1 = System.currentTimeMillis();
        long time2 = calendar.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000);
        return (int) betweenDays;
    }

    public static int calLastedTime(Date startDate) {
        long a = new Date().getTime();
        long b = startDate.getTime();
        int c = (int) ((b - a) / 1000);
        return c;
    }

    /**
     * 返回时间差（与当前时间，分钟数）
     *
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String startTime, String endTime) {
//        L.i("result", "--startTime--" + startTime + "--endTime--=" + endTime);
        long time1 = ZZDate.getStringToDate(startTime);
        long time2 = ZZDate.getStringToDate(endTime);
        long betweenDays = (time2 - time1) / (1000 * 60);
        return (int) betweenDays;
    }

    public static boolean isLaterThanBefore(String before, String later) {
        long b = getStringToDateFromYMD(before);
        long l = getStringToDateFromYMD(later);
        return l >= b;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate (2019-03-03 10:25:11)   yyyy-MM-dd HH:mm:ss  类型的格式
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                if (Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1)==1){
                    ftime ="刚刚";
                }else {
                    ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
                }
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                if (Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1)==1){
                    ftime ="刚刚";
                }else {
                    ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
                }
            } else {
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 7) {
            ftime = days + "天前";
        } else if (days > 7) {
            ftime = dateFormater.get().format(time);
        }
        return ftime;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };


    /**
     * GMT 时间转换成string
     * @param
     * @return
     * @throws ParseException
     */
    public final static String GMTToString(Date date)  {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}