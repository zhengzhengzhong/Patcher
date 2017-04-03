package cn.letterme.tools.patcher.model;

/**
 * 远程主机信息，用于建立SFTP连接
 */
public class EnvInfo
{
    private String ipAddr;
    private int port;
    private String userName;
    private String password;
    private boolean isLocal;

    private EnvInfo ()
    {
    }

    /**
     * 创建远程的环境信息
     *
     * @param ipAddr   IP地址
     * @param port     端口
     * @param userName 用户名
     * @param password 密码
     *
     * @return 环境信息
     */
    public static EnvInfo remote (String ipAddr, int port, String userName, String password)
    {
        EnvInfo envInfo = new EnvInfo();
        envInfo.ipAddr = ipAddr;
        envInfo.port = port;
        envInfo.userName = userName;
        envInfo.password = password;
        envInfo.isLocal = false;
        return envInfo;
    }

    /**
     * 创建本地的环境信息
     * @return 环境信息
     */
    public static EnvInfo local ()
    {
        EnvInfo envInfo = new EnvInfo();
        envInfo.isLocal = true;
        return envInfo;
    }

    public String getIpAddr ()
    {
        return ipAddr;
    }

    public int getPort ()
    {
        return port;
    }

    public String getUserName ()
    {
        return userName;
    }

    public String getPassword ()
    {
        return password;
    }

    public boolean isLocal ()
    {
        return isLocal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        if (isLocal)
        {
            return "EnvInfo{isLocal=" + isLocal + "}";
        }

        return "EnvInfo{" +
                "ipAddr='" + ipAddr + '\'' +
                ", port=" + port +
                ", userName='" + userName + '\'' +
                ", password='" + password + "\'}";
    }
}
