package cn.letterme.tools.patcher.model;

import cn.letterme.tools.patcher.util.IOUtils;
import cn.letterme.tools.patcher.util.JsonUtil;
import cn.letterme.tools.patcher.util.PatchTaskUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import java.util.UUID;

/**
 * 表格数据
 */
public class TableData
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(TableData.class);

    /**
     * 表格数据
     */
    private List<HostInfo> tableData;

    public TableData()
    {
        tableData = new ArrayList<HostInfo>();
    }

    public List<HostInfo> getTableData()
    {
        return tableData;
    }

    public void setTableData(List<HostInfo> tableData)
    {
        this.tableData = tableData;
    }

    /**
     * 获取主机数量
     *
     * @return 主机数量
     */
    public int size()
    {
        return tableData.size();
    }

    /**
     * 获取值
     *
     * @param row 行
     * @param col 列
     *
     * @return 值
     */
    public Object get(int row, int col)
    {
        if (row > tableData.size() - 1)
        {
            LOG.error("The row is overflow. Row = " + row + ", max size = " + tableData.size());
            return null;
        }
        return tableData.get(row).get(col);
    }

    /**
     * 设置值
     *
     * @param row 行
     * @param col 列
     * @param val 新值
     */
    public void set(int row, int col, Object val)
    {
        if (row > tableData.size() - 1)
        {
            LOG.error("The row is overflow. Row = " + row + ", max size = " + tableData.size());
            return;
        }
        tableData.get(row).set(col, val);
    }

    /**
     * 添加主机信息
     *
     * @param hostInfo 主机信息
     */
    public void add(HostInfo hostInfo)
    {
        LOG.debug("Fire add hostInfo.");
        if (null != hostInfo)
        {
            tableData.add(hostInfo);
        }
    }

    /**
     * 删除选中的主机信息
     */
    public void remove()
    {
        LOG.debug("Fire remove selected hostInfo.");
        Iterator<HostInfo> it = tableData.iterator();
        while (it.hasNext())
        {
            HostInfo hostInfo = it.next();
            if (hostInfo.isSelected())
            {
                LOG.debug("Remove hostInfo: " + hostInfo);
                it.remove();
            }
        }
    }

    /**
     * 打补丁
     *
     * @param patchDir 补丁的存放路径
     * @param observer 观察者
     */
    public void patch(String patchDir, Observer observer)
    {
        LOG.debug("Fire patch: patchDir = " + patchDir);
        for (HostInfo hostInfo : tableData)
        {
            if (hostInfo.isSelected())
            {
                LOG.debug("Add patch task: " + hostInfo);
                PatchTaskUtil.addPatchTask(hostInfo.createPatchTask(patchDir, observer));
            }
        }
    }

    /**
     * 回退补丁
     *
     * @param rollbackDir 待回退的补丁路径
     * @param observer 观察者
     */
    public void rollback(String rollbackDir, Observer observer)
    {
        LOG.debug("Fire rollback: rollbackDir = " + rollbackDir);
        for (HostInfo hostInfo : tableData)
        {
            if (hostInfo.isSelected())
            {
                LOG.debug("Add rollback task: " + hostInfo);
                PatchTaskUtil.addPatchTask(hostInfo.createRollbackTask(rollbackDir, observer));
            }
        }
    }

    /**
     * 导出数据到文件
     *
     * @param savedPath 保存的文件路径
     */
    public void export(String savedPath)
    {
        LOG.debug("Fire export hostInfo: savedPath = " + savedPath);
        IOUtils.saveHostInfo(savedPath, JsonUtil.beanToJson(this));
    }
}
