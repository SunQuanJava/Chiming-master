package com.baizhi.baseapp.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author xinzhang
 * @description 由于 Calendar 的实例化比较耗时，所以就做成了一个单例。 <br>
 * 这个是线程安全的。
 * @time 2014-8-5 下午1:51:24
 */
public class CalendarUtils {

    public static final long ONE_MINUTE = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final int INVALID_AGE = -1;
    private static final String FORMATE_DATE_STR = "yyyy-MM-DD";
    private static ThreadLocal<Calendar> mTHreadLocal = new ThreadLocal<Calendar>();

    private static Calendar checkInstance() {
        Calendar calendar = mTHreadLocal.get();
        if (calendar != null) {
            return calendar;
        }
        synchronized (Thread.currentThread()) {
            calendar = Calendar.getInstance(Locale.getDefault()); // 这个实例化非常耗时
            mTHreadLocal.set(calendar);
            return calendar;
        }

    }

    public static Calendar getInstance() {
        return getInstance(System.currentTimeMillis());
    }

    public static Calendar getInstance(long timeMillions) {
        Calendar calendar = checkInstance();
        calendar.setTimeInMillis(timeMillions);
        return calendar;
    }

    /**
     * @return 获取当前时间的 Year
     */
    public static int getCurrentYear() {
        return getInstance(System.currentTimeMillis()).get(Calendar.YEAR);
    }

    /**
     * @return 获取当前时间的 月份； 从1 开始
     */
    public static int getCurrentMonth() {
        return getInstance(System.currentTimeMillis()).get(Calendar.MONTH) + 1;
    }

    /**
     * @return 获取当前时间的天； 从1 开始
     */
    public static int getCurrentDay() {
        return getInstance(System.currentTimeMillis()).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return
     * @throws java.text.ParseException 根据生日获取年龄;
     */
    public static int getCurrentAgeByBirthdate(String brithday) {
        if (TextUtils.isEmpty(brithday)) {
            return -1;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatDate = new SimpleDateFormat(FORMATE_DATE_STR);
            String currentTime = formatDate.format(calendar.getTime());
            Date today = formatDate.parse(currentTime);
            Date brithDay = formatDate.parse(brithday);

            return today.getYear() - brithDay.getYear();
        } catch (Exception e) {
            return -1;
        }
    }

    public static Date now() {
        return new Date();
    }

    public static String getTimeForList(long time) {
        String str;
        try {
            final Date date = new Date(time);

            final SimpleDateFormat parseFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm", Locale.CHINA);
            str = parseFormat.format(date);

        } catch (Exception e) {
            str = null;
        }
        return str;
    }

    /**
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空   默认当前时间 ,表示和当前时
     *                间相比)
     * @return -1 同一天.    0昨天 .   1 至少是前天.
     * @throws java.text.ParseException 转换异常
     */
    private static int isYesterday(Date oldTime, Date newTime) throws ParseException {
        if (newTime == null) {
            newTime = new Date();
        }
        //将下面的 理解成  yyyy-MM-dd 000000 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        Date today = format.parse(todayStr);
        //昨天 86400000=24*60*60*1000 一天
        if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime()
                - oldTime.getTime()) <= 86400000) {
            return 0;
        } else if ((today.getTime() - oldTime.getTime()) <= 0) { //至少是今

            return -1;
        } else { //至少是前天
            return 1;
        }

    }

    /**
     * 格式化时间，将其变成00:00的形式
     */
    public static String formatTime(int time) {
        int secondSum = time / 1000;
        int minute = secondSum / 60;
        int second = secondSum % 60;

        String result = "";
        if (minute < 10)
            result = "0";
        result = result + minute + ":";
        if (second < 10)
            result = result + "0";
        result = result + second;

        return result;
    }

}
