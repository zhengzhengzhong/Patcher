package cn.letterme.tools.patcher.impl;

import cn.letterme.tools.patcher.api.IFileCopy;
import cn.letterme.tools.patcher.api.IPatchTask;
import cn.letterme.tools.patcher.exception.PatchException;
import cn.letterme.tools.patcher.model.EnvInfo;
import cn.letterme.tools.patcher.model.PatchInfo;
import cn.letterme.tools.patcher.util.ApiUtil;
import cn.letterme.tools.patcher.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 打补丁任务
 */
public class PatchTask implements IPatchTask
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(PatchTask.class);

    protected boolean isRollback;
    private PatchInfo patchInfo;
    private EnvInfo envInfo;
    private IFileCopy fileCopy;

    protected PatchTask()
    {
    }

    public static PatchTask createTask(PatchInfo patchInfo, EnvInfo envInfo, boolean isRollback)
    {
        PatchTask patchTask = new PatchTask();
        patchTask.patchInfo = patchInfo;
        patchTask.envInfo = envInfo;
        patchTask.isRollback = isRollback;
        patchTask.fileCopy = ApiUtil.fileCopy(envInfo.isLocal());

        LOG.info("Create task successfully. " + patchTask);
        return patchTask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforePatch () throws PatchException
    {
        if (isRollback)
        {
            // 回滚不需要备份文件
            return;
        }

        // 建立连接
        fileCopy.connect(envInfo);

        // 备份文件：远程->本地
        PatchInfo.PatchInfoIterator iterator = patchInfo.iterator();
        while (iterator.hasNext())
        {
            PatchInfo.PatchEntry entry = iterator.next();
            fileCopy.backup(entry.getBackup(), entry.getRemote());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPatch () throws PatchException
    {
        // 打补丁 or 回滚
        PatchInfo.PatchInfoIterator iterator = patchInfo.iterator();
        while (iterator.hasNext())
        {
            PatchInfo.PatchEntry entry = iterator.next();

            if (isRollback)
            {
                // 回滚：从本地backup->远程
                fileCopy.rollback(entry.getBackup(), entry.getRemote());
            }
            else
            {
                // 打补丁：从本地->远程
                fileCopy.patch(entry.getLocal(), entry.getRemote());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPatch () throws PatchException
    {
        IOUtils.close(fileCopy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run ()
    {
        try
        {
            beforePatch();
            doPatch();
            afterPatch();
        }
        catch (PatchException e)
        {
            LOG.error("Exception threw while doing task. ", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return "PatchTask{" +
                "patchInfo=" + patchInfo +
                ", envInfo=" + envInfo +
                ", isRollback=" + isRollback +
                '}';
    }
}
