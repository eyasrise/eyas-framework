package com.eyas.framework;

import com.eyas.framework.enumeration.ErrorFrameworkCodeEnum;
import com.eyas.framework.exception.EyasFrameworkRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Created by yixuan on 2019/6/20.
 */
@Slf4j
public class DateUtil {

    private DateUtil() {
    }

    /**
     * 日期格式
     **/
    public interface DATE_PATTERN {
        String HHMMSS = "HHmmss";
        String HH_MM_SS = "HH:mm:ss";
        String YYYYMMDD = "yyyyMMdd";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    }

    /**
     * 格式化日期
     *
     * @param date    当前日期
     * @param pattern 转化格式
     * @return 转化结果
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 格式化日期
     *
     * @param date 当前日期
     * @return 返回结果
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN.YYYY_MM_DD);
    }

    /**
     * 获取日期时间-YYYY_MM_DD_HH_MM_SS
     *
     * @return 返回结果
     */
    public static String getCurrentDateTime() {
        return format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 字符串转date类型
     *
     * @param date    the date
     * @param pattern the pattern
     * @return date date
     * @throws ParseException the parse exception
     * @see [相关类/方法]（可选）
     * @since [产品 /模块版本] （可选）
     */
    public static Date stringToDate(String date, String pattern){
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date dateNew = null;
        try{
            dateNew = dateFormat.parse(date);
        }catch (ParseException e){
            throw new EyasFrameworkRuntimeException(ErrorFrameworkCodeEnum.DATE_DEAL_ERROR, "时间处理异常!");
        }

        return dateNew;
    }

    /**
     * 字符串转date类型
     *
     * @param date date
     * @param pattern pattern
     * @return Date
     */
    public static Date stringToDate2(String date, String pattern){
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 校验时间格式
     *
     * @param date
     * @param rule
     * @return
     */
    public static boolean dateParse(String date, String rule) {
        DateFormat formatter = new SimpleDateFormat(rule);
        try {
            formatter.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 计算两个时间的天数
     * @param startTime 2019-11-11 11:11:11
     * @param endTime 2019-11-13 11:11:11
     * @param pattern 规则
     */
    public static Integer calculatedDays(Date startTime, Date endTime, String pattern) {
        // 判断时间格式
        String startDate = DateUtil.format(startTime, pattern);
        String endDate = DateUtil.format(endTime, pattern);

        if (EmptyUtil.isEmpty(startDate) || EmptyUtil.isEmpty(endDate)) {
            startDate = DateUtil.format(startTime, "yyyy-MM-dd");
            endDate = DateUtil.format(endTime, "yyyy-MM-dd");
        }
        if (EmptyUtil.isEmpty(startDate) || EmptyUtil.isEmpty(endDate)) {
            return null;
        }
        // 统一转换格式年月日
        startDate = DateUtil.format(startTime, "yyyy-MM-dd");
        endDate = DateUtil.format(endTime, "yyyy-MM-dd");
        // 按-截取
        List<String> startList = StringUtil.stringSplit(startDate, "-");
        int startYear = Integer.valueOf(startList.get(0));
        int startMonth = Integer.valueOf(startList.get(1));
        int startDay = Integer.valueOf(startList.get(2));
        List<String> endList = StringUtil.stringSplit(endDate, "-");
        int endYear = Integer.valueOf(endList.get(0));
        int endMonth = Integer.valueOf(endList.get(1));
        int endDay = Integer.valueOf(endList.get(2));

        LocalDate localDate1 = LocalDate.of(startYear, startMonth, startDay);
        LocalDate localDate2 = LocalDate.of(endYear, endMonth, endDay);
        Long differentTime = localDate2.toEpochDay() - localDate1.toEpochDay();
        int result = differentTime.intValue();
        return result;
    }

    /**
     * 比较日期大小
     *
     * @param dateA 时间a
     * @param dateB 时间b
     * @return true
     */
    public static Boolean compareEarly(Date dateA, Date dateB) {
        return dateA.getTime() <= dateB.getTime();
    }

    /**
     * 根据指定格式日期字符获取相对日期
     *
     * @param date   日期字符串
     * @param index  相对date的索引，-1 表示昨天 ； 1表示明天
     * @param format 日期格式
     * @return 日期
     */
    public static Date getRelationDate(String date, int index, String format){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(date, format));
        calendar.add(Calendar.DATE, index);
        return calendar.getTime();
    }

    /**
     * 获取当前时间去年的年份
     *
     * @param date 当前时间
     * @param format 规则
     * @return 去年时间
     */
    public static Date getYesterday(String date, String format){
        return getRelationDate(date, -1, format);
    }

    /**
     * 判断时间是不是在区间内
     *
     * @param nowTime 需要判断的时间
     * @param startTime 条件开始时间
     * @param endTime 条件结束时间
     * @return 是否在区间
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


}
