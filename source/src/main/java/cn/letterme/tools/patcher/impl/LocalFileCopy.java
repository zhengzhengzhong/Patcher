package cn.letterme.tools.patcher.impl;

import cn.letterme.tools.patcher.api.IFileCopy;
import cn.letterme.tools.patcher.constant.ErrorCode;
import cn.letterme.tools.patcher.exception.PatchException;
import cn.letterme.tools.patcher.model.EnvInfo;
import cn.letterme.tools.patcher.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 本地文件拷贝
 */
public class LocalFileCopy implements IFileCopy
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(LocalFileCopy.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(EnvInfo envInfo) throws PatchException
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backup(String backup, String remote) throws PatchException
    {
        LOG.info("Backup: backup path = " + backup + ", remote path = " + remote);
        copy(remote, backup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void patch(String local, String remote) throws PatchException
    {
        LOG.info("Patch: local path = " + local + ", remote path = " + remote);
        copy(local, remote);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback(String backup, String remote) throws PatchException
    {
        LOG.info("Rollback: backup path = " + backup + ", remote path = " + remote);
        copy(backup, remote);
    }

    private void copy(String src, String dst) throws PatchException
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        // 如果目标目录不存在则创建
        IOUtils.dirIfNotExist(dst);

        try
        {
            fis = new FileInputStream(src);
            fos = new FileOutputStream(dst);

            int ch;
            while ((ch = fis.read()) != -1)
            {
                fos.write(ch);
            }
            fos.flush();
        }
        catch (FileNotFoundException e)
        {
            LOG.error("FileNotFoundException while copy.", e);
            throw new PatchException(ErrorCode.FILE_NOT_FOUND, e);
        }
        catch (IOException e)
        {
            LOG.error("IOException while copy.", e);
            throw new PatchException(ErrorCode.IO_EXCEPTION, e);
        }
        finally
        {
            IOUtils.close(fos);
            IOUtils.close(fis);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        // do noting
    }
}
