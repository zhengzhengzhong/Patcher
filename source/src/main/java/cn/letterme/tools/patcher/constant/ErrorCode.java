package cn.letterme.tools.patcher.constant;

/**
 * 错误码
 */
public final class ErrorCode
{
    private ErrorCode()
    {
    }

    /**
     * 文件不存在
     */
    public static final int FILE_NOT_FOUND = 1;

    /**
     * IO异常
     */
    public static final int IO_EXCEPTION = 2;

    /**
     * 连接失败
     */
    public static final int CONNECT_FAILED = 3;

    /**
     * 打补丁失败
     */
    public static final int PATCH_FAILED = 4;

    /**
     * 备份失败
     */
    public static final int BACKUP_FAILED = 6;

    /**
     * 回滚失败
     */
    public static final int ROLLBACK_FAILED = 7;
}
