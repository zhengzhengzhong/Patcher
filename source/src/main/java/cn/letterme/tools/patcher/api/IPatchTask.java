package cn.letterme.tools.patcher.api;

import cn.letterme.tools.patcher.exception.PatchException;

/**
 * 补丁任务
 */
public interface IPatchTask extends Runnable
{
    /**
     * 前置处理
     * @throws PatchException 异常
     */
    void beforePatch() throws PatchException;

    /**
     * 执行补丁/回滚
     * @throws PatchException 异常
     */
    void doPatch() throws PatchException;

    /**
     * 后置处理
     * @throws PatchException 异常
     */
    void afterPatch() throws PatchException;
}
