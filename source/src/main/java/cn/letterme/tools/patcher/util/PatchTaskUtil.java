package cn.letterme.tools.patcher.util;

import cn.letterme.tools.patcher.api.IPatchTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 补丁任务工具类，用于执行补丁操作
 */
public final class PatchTaskUtil
{
    /**
     * 线程工具类
     */
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private PatchTaskUtil ()
    {
    }

    /**
     * 添加补丁运行任务
     *
     * @param patchTask 补丁任务
     */
    public static void addPatchTask (IPatchTask patchTask)
    {
        EXECUTOR.submit(patchTask);
    }
}
