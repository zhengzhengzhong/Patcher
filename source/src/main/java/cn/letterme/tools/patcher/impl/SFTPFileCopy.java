package cn.letterme.tools.patcher.impl;

import cn.letterme.tools.patcher.api.IFileCopy;
import cn.letterme.tools.patcher.constant.ErrorCode;
import cn.letterme.tools.patcher.exception.PatchException;
import cn.letterme.tools.patcher.model.EnvInfo;
import cn.letterme.tools.patcher.util.IOUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Objects;
import java.util.Properties;

/**
 * SFTP文件拷贝实现类
 */
public class SFTPFileCopy implements IFileCopy
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(SFTPFileCopy.class);

    private ChannelSftp sftp;

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect (EnvInfo envInfo) throws PatchException
    {
        try
        {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(envInfo.getUserName(), envInfo.getIpAddr(), envInfo.getPort());
            sshSession.setPassword(envInfo.getPassword());
            // 3s超时
            sshSession.setTimeout(3000);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            sftp = (ChannelSftp)sshSession.openChannel("sftp");
            sftp.connect();
        }
        catch (JSchException e)
        {
            LOG.error("Connect to remote failed.", e);
            throw new PatchException(ErrorCode.CONNECT_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backup (String backup, String remote) throws PatchException
    {
        LOG.info("Backup: backup path = " + backup + ", remote path = " + remote);
        // 从远程下载到本地
        try
        {
            sftp.get(canonicalRemote(remote), backup, null, ChannelSftp.OVERWRITE);
        }
        catch (SftpException e)
        {
            throw new PatchException(ErrorCode.BACKUP_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void patch (String local, String remote) throws PatchException
    {
        LOG.info("Patch: local path = " + local + ", remote path = " + remote);
        // 上传本地文件到远程
        try
        {
            sftp.put(local, canonicalRemote(remote), ChannelSftp.OVERWRITE);
        }
        catch (SftpException e)
        {
            throw new PatchException(ErrorCode.PATCH_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback (String backup, String remote) throws PatchException
    {
        LOG.info("Rollback: backup path = " + backup + ", remote path = " + remote);
        // 回退，上传本地文件到远程
        try
        {
            sftp.put(backup, canonicalRemote(remote), ChannelSftp.OVERWRITE);
        }
        catch (SftpException e)
        {
            throw new PatchException(ErrorCode.ROLLBACK_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close ()
    {
        if (Objects.nonNull(sftp))
        {
            sftp.disconnect();
        }
    }

    /**
     * 归一化远程的文件路径（Windows下使用SSH通道，文件路径需要特殊处理）
     * @param remote 初始的远程文件路径
     * @return 处理后的SSH的远程文件路径
     */
    private String canonicalRemote(String remote)
    {
        if (Objects.isNull(remote))
        {
            return null;
        }

        // Windows下的路径带冒号
        if (remote.contains(":"))
        {
            String canonicalPath = IOUtils.canonicalPath(remote);
            String afterRelace = canonicalPath.replaceAll("\\\\", "/").replaceAll("\\:", "");
            LOG.debug("Before path = " + remote + ", after path = " + afterRelace);
            return afterRelace;
        }

        return remote;
    }
}