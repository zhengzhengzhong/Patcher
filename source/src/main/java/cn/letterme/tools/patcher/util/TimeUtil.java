package cn.letterme.tools.patcher.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public final class TimeUtil
{
    private TimeUtil ()
    {
    }

    /**
     * 获取格式化时间
     * @return 格式化后的时间
     */
    public static String getTime()
    {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd-HHmmss");
        return simpleDateFormat.format(date);
    }
}
