package org.ljx.util;

import java.sql.Timestamp;
import java.util.Calendar;

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
        c.set(Calendar.HOUR, 23);
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
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return new Timestamp(c.getTimeInMillis());
    }
}
