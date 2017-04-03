package cn.letterme.tools.patcher.util;

import cn.letterme.tools.patcher.constant.ErrorCode;
import cn.letterme.tools.patcher.exception.PatchException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * IO工具类
 */
public final class IOUtils
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(IOUtils.class);

    private IOUtils()
    {
    }

    /**
     * 关闭流
     *
     * @param c 待关闭的流
     */
    public static void close(Closeable c)
    {
        if (null != c)
        {
            try
            {
                c.close();
            }
            catch (IOException e)
            {
                LOG.debug("Close failed. ", e);
            }
        }
    }

    /**
     * 加载补丁信息（按行加载）
     *
     * @param file 补丁信息文件路径
     *
     * @return 文件内容列表
     *
     * @throws PatchException 异常
     */
    public static List<String> loadPatchInfo(String file) throws PatchException
    {
        List<String> content = new ArrayList<String>();

        FileReader fr = null;
        BufferedReader br = null;
        try
        {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line = null;

            while ((line = br.readLine()) != null)
            {
                if (!line.startsWith("#"))
                {
                    content.add(line);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            throw new PatchException(ErrorCode.FILE_NOT_FOUND, e);
        }
        catch (IOException e)
        {
            throw new PatchException(ErrorCode.IO_EXCEPTION, e);
        }
        finally
        {
            close(br);
            close(fr);
        }

        return content;
    }

    /**
     * 加载主机信息文件
     * @param path 主机信息文件路径
     * @return 主机信息
     */
    public static String loadHostInfo(String path)
    {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;

        try
        {
            fis = new FileInputStream(path);
            int ch = -1;

            while ((ch = fis.read()) != -1)
            {
                sb.append((char)ch);
            }
        }
        catch (IOException e)
        {
            LOG.error("Load host info failed. ", e);
        }
        finally
        {
            close(fis);
        }

        return sb.toString();
    }

    /**
     * 保存主机信息到文件
     * @param path 主机信息文件路径
     * @param info 主机信息
     * @return true-成功；false-失败
     */
    public static boolean saveHostInfo(String path, String info)
    {
        FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream(path);
            fos.write(info.getBytes());
            return true;
        }
        catch (FileNotFoundException e)
        {
            LOG.error("Save host info failed.", e);
        }
        catch (IOException e)
        {
            LOG.error("Save host info failed.", e);
        }
        finally
        {
            close(fos);
        }

        return false;
    }

    /**
     * 归一化路径
     *
     * @param path 文件路径
     *
     * @return 归一化后的路径
     */
    public static String canonicalPath(String path)
    {
        File f = new File(path);
        try
        {
            return f.getCanonicalPath();
        }
        catch (IOException e)
        {
            LOG.error("Canonical path failed. path = " + path, e);
        }
        return "";
    }

    /**
     * 获取父目录的路径
     *
     * @param path 路径
     *
     * @return 父目录的路径
     */
    public static String getParent(String path)
    {
        File f = new File(path);
        return f.getParent();
    }

    /**
     * 获取文件名
     *
     * @param path 文件路径
     *
     * @return 文件名
     */
    public static String getFileName(String path)
    {
        File f = new File(path);
        return f.getName();
    }

    /**
     * 判断目录是否存在，如果不存在则创建目录
     *
     * @param path 文件路径
     *
     * @return true-创建成功；false-创建失败
     *
     * @throws PatchException 异常
     */
    public static boolean dirIfNotExist(String path) throws PatchException
    {
        File file = new File(path);
        File parentFile = file.getParentFile();

        if (!parentFile.exists())
        {
            return parentFile.mkdirs();
        }

        return true;
    }
}
