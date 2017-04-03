package cn.letterme.tools.patcher.util;

import cn.letterme.tools.patcher.api.IFileCopy;
import cn.letterme.tools.patcher.impl.LocalFileCopy;
import cn.letterme.tools.patcher.impl.SFTPFileCopy;

import java.util.Objects;

/**
 * API工具类，获取接口的实现类
 */
public final class ApiUtil
{
    private ApiUtil ()
    {
    }

    /**
     * 获取文件拷贝的实现类
     *
     * @param isLocal 是否是本地机器
     *
     * @return 文件实现类
     */
    public static IFileCopy fileCopy (boolean isLocal)
    {
        return isLocal ? new LocalFileCopy() : new SFTPFileCopy();
    }
}
