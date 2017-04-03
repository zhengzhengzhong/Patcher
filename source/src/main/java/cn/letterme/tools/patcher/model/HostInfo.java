package cn.letterme.tools.patcher.model;

import cn.letterme.tools.patcher.api.IPatchTask;
import cn.letterme.tools.patcher.impl.GUIPatchTask;
import cn.letterme.tools.patcher.impl.PatchTask;
import cn.letterme.tools.patcher.util.IOUtils;
import cn.letterme.tools.patcher.util.TimeUtil;

import java.io.File;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * 主机信息模型
 */
public class HostInfo implements Observer
{
    private static final int IDX_SELECTED = 0;
    private static final int IDX_IP_ADDR = 1;
    private static final int IDX_PORT = 2;
    private static final int IDX_USERNAME = 3;
    private static final int IDX_PASSWORD = 4;
    private static final int IDX_STATUS = 5;

    private boolean isSelected = false;

    private boolean isLocal = true;

    private String ipAddr;

    private int port;

    private String userName;

    private String password;

    private String status;

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    public boolean isLocal()
    {
        return isLocal;
    }

    public void setLocal(boolean local)
    {
        isLocal = local;
    }

    public String getIpAddr()
    {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr)
    {
        this.ipAddr = ipAddr;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Object get(int index)
    {
        switch (index)
        {
            case IDX_SELECTED:
                return isSelected;

            case IDX_IP_ADDR:
                return adjustString(ipAddr);

            case IDX_PORT:
                return 0 >= port ? "--" : port;

            case IDX_USERNAME:
                return adjustString(userName);

            case IDX_PASSWORD:
                return adjustString(password);

            case IDX_STATUS:
                return adjustString(status);

            default:
                return "NULL";
        }
    }

    private String adjustString(String str)
    {
        if (Objects.isNull(str) || str.trim().isEmpty())
        {
            return "--";
        }
        return str;
    }

    public void set(int index, Object value)
    {
        if (Objects.isNull(value))
        {
            return;
        }

        String strValue = Objects.toString(value);
        switch (index)
        {
            case IDX_SELECTED:
                isSelected = Boolean.valueOf(strValue);
                break;

            case IDX_IP_ADDR:
                ipAddr = strValue;
                break;

            case IDX_PORT:
                if (strValue.matches("\\d+"))
                {
                    port = Integer.valueOf(strValue);
                }
                break;

            case IDX_USERNAME:
                userName = strValue;
                break;

            case IDX_PASSWORD:
                password = strValue;
                break;

            case IDX_STATUS:
                status = strValue;
                break;

            default:
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "HostInfo{" +
                "isSelected=" + isSelected +
                ", ipAddr='" + ipAddr + '\'' +
                ", port=" + port +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    /**
     * 创建补丁任务
     *
     * @param patchDir 补丁目录
     * @param observer 观察者
     *
     * @return 任务
     */
    public IPatchTask createPatchTask(String patchDir, Observer observer)
    {
        String backupDirName = TimeUtil.getTime() + File.separator + ipAddr;
        PatchInfo patchInfo = PatchInfo.parse(patchDir, backupDirName);
        EnvInfo envInfo = isLocal ? EnvInfo.local() : EnvInfo.remote(ipAddr, port, userName, password);
        GUIPatchTask guiPatchTask = new GUIPatchTask(PatchTask.createTask(patchInfo, envInfo, false));
        guiPatchTask.attach(this);
        guiPatchTask.attach(observer);
        return guiPatchTask;
    }

    /**
     * 创建补丁回退任务
     *
     * @param rollbackDir 待回退的补丁目录
     * @param observer 观察者
     *
     * @return 任务
     */
    public IPatchTask createRollbackTask(String rollbackDir, Observer observer)
    {
        String backupPath = IOUtils.getFileName(rollbackDir) + File.separator + ipAddr;
        String patchDir = IOUtils.getParent(rollbackDir);
        PatchInfo patchInfo = PatchInfo.parse(patchDir, backupPath);
        EnvInfo envInfo = isLocal ? EnvInfo.local() : EnvInfo.remote(ipAddr, port, userName, password);
        GUIPatchTask guiPatchTask =  new GUIPatchTask(PatchTask.createTask(patchInfo, envInfo, true));
        guiPatchTask.attach(this);
        guiPatchTask.attach(observer);
        return guiPatchTask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable o, Object arg)
    {
        set(IDX_STATUS, arg);
    }
}
