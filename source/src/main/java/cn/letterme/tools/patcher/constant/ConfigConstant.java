package cn.letterme.tools.patcher.constant;

import java.io.File;

/**
 * Created by WHY on 2017-04-02.
 */
public final class ConfigConstant
{
    private ConfigConstant()
    {
    }

    /**
     * 根目录
     */
    public static final String ROOT_DIR = System.getProperty("root.dir");

    /**
     * 配置文件目录
     */
    public static final String ETC_DIR = ROOT_DIR + File.separatorChar + "etc";

    /**
     * 主机信息配置目录
     */
    public static final String CFG_HOST_INFO = ETC_DIR + File.separatorChar + "hostInfo.json";
}
