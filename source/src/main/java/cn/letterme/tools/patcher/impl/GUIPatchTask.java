package cn.letterme.tools.patcher.impl;

import cn.letterme.tools.patcher.api.IPatchTask;
import cn.letterme.tools.patcher.constant.I18nConstant;
import cn.letterme.tools.patcher.exception.PatchException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * 图形化任务（装饰器）
 */
public class GUIPatchTask extends PatchTask
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(GUIPatchTask.class);

    private IPatchTask patchTask;
    private List<Observer> observerList = new ArrayList<Observer>();

    /**
     * 构造函数
     *
     * @param patchTask 补丁任务
     */
    public GUIPatchTask(IPatchTask patchTask)
    {
        super();
        this.patchTask = patchTask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforePatch() throws PatchException
    {
        LOG.info("Before patch..." + patchTask);
        notify(I18nConstant.Tips.STR_BEFORE_PATCH);
        try
        {
            patchTask.beforePatch();
        }
        catch (PatchException e)
        {
            notify(e.getErrorMsg() + e.toString());
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPatch() throws PatchException
    {
        LOG.info("Do patch: " + patchTask);
        notify(isRollback ? I18nConstant.Tips.STR_DO_ROLLBACK : I18nConstant.Tips.STR_DO_PATCH);
        try
        {
            patchTask.doPatch();
        }
        catch (PatchException e)
        {
            notify(e.getErrorMsg() + e.toString());
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPatch() throws PatchException
    {
        LOG.info("After patch: " + patchTask);
        notify(I18nConstant.Tips.STR_AFTER_PATCH);
        try
        {
            patchTask.afterPatch();
        }
        catch (PatchException e)
        {
            notify(e.getErrorMsg() + e.toString());
            throw e;
        }
        notify(I18nConstant.Tips.STR_SUCCESS);
    }

    /**
     * 添加观察者
     * @param observer 观察者
     */
    public void attach(Observer observer)
    {
        observerList.add(observer);
    }

    /**
     * 通知表格数据更新数据
     * @param message 消息
     */
    private void notify(String message)
    {
        for (Observer observer : observerList)
        {
            observer.update(null, message);
        }
    }
}
