package cn.letterme.tools.patcher.api;

import cn.letterme.tools.patcher.exception.PatchException;
import cn.letterme.tools.patcher.model.EnvInfo;

import java.io.Closeable;

/**
 * 拷贝文件接口
 */
public interface IFileCopy extends Closeable
{
    /**
     * 连接远程环境
     *
     * @param envInfo 环境信息
     *
     * @throws PatchException 异常
     */
    void connect(EnvInfo envInfo) throws PatchException;

    /**
     * 备份文件
     *
     * @param backup 备份文件路径
     * @param remote 远程文件路径
     *
     * @throws PatchException 异常
     */
    void backup(String backup, String remote) throws PatchException;

    /**
     * 拷贝文件接口
     *
     * @param local  本地文件路径
     * @param remote 远程文件路径
     *
     * @throws PatchException 异常
     */
    void patch(String local, String remote) throws PatchException;

    /**
     * 回滚补丁
     *
     * @param backup 备份文件路径
     * @param remote 远程文件路径
     *
     * @throws PatchException 异常
     */
    void rollback(String backup, String remote) throws PatchException;

    /**
     * 关闭远程环境连接
     */
    void close();
}
