package cn.letterme.tools.patcher.exception;

import cn.letterme.tools.patcher.constant.ErrorCode;
import cn.letterme.tools.patcher.constant.I18nConstant;

/**
 * 补丁相关异常
 */
public class PatchException extends Exception
{
    private int errorCode;

    public PatchException(int errorCode)
    {
        super();
        this.errorCode = errorCode;
    }

    public PatchException(int errorCode, Throwable t)
    {
        super(t);
        this.errorCode = errorCode;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorMsg()
    {
        switch (errorCode)
        {
            case ErrorCode.FILE_NOT_FOUND:
                return I18nConstant.Tips.FILE_NOT_FOUND;
            case ErrorCode.IO_EXCEPTION:
                return I18nConstant.Tips.IO_EXCEPTION;
            case ErrorCode.CONNECT_FAILED:
                return I18nConstant.Tips.CONNECT_FAILED;
            case ErrorCode.PATCH_FAILED:
                return I18nConstant.Tips.PATCH_FAILED;
            case ErrorCode.BACKUP_FAILED:
                return I18nConstant.Tips.BACKUP_FAILED;
            case ErrorCode.ROLLBACK_FAILED:
                return I18nConstant.Tips.ROLLBACK_FAILED;
        }

        return "";
    }
}
