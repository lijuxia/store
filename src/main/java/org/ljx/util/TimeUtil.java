package org.ljx.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ljx on 2018/7/11.
 */
public class TimeUtil {

    /**
     * 获取时间当天的最后时间
     * @param time
     * @return
     */
    public static Timestamp getEndTime(Timestamp time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time.getTime());
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return new Timestamp(c.getTimeInMillis());
    }

    /**
     * 获取时间当天的开始时间
     * @param time
     * @return
     */
    public static Timestamp getBeginTime(Timestamp time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time.getTime());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }

    /**
     * 判断是否同一天
     * @param start
     * @param end
     * @return
     */
    public static boolean isSameday(Timestamp start,Timestamp end){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = dateFormat.format(end);
        String recordTime = dateFormat.format(start);
        if(now.equals(recordTime)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取两个日期之间的所有日期列表（一天一条）
     * @param start
     * @param end
     * @return
     */
    public static List<Timestamp> duringTimes(Timestamp start, Timestamp end){
        if(start.after(end) && !isSameday(start,end)){//开始日期大于结束日期，返回null
            return null;
        }
        List<Timestamp> result = new ArrayList<Timestamp>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);

        while (!isSameday(new Timestamp(tempStart.getTimeInMillis()),new Timestamp(tempEnd.getTimeInMillis()))
        && new Timestamp(tempStart.getTimeInMillis()).before(new Timestamp(tempEnd.getTimeInMillis()))) {
            result.add(new Timestamp(tempStart.getTimeInMillis()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    public static Timestamp addDay(Timestamp time,int day){
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.DAY_OF_YEAR, day);
        return new Timestamp(c.getTimeInMillis());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static Timestamp getNow(){
        return new Timestamp(System.currentTimeMillis());
    }
}
