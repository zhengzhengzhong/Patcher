package cn.letterme.tools.patcher.model;

import cn.letterme.tools.patcher.exception.PatchException;
import cn.letterme.tools.patcher.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 补丁信息
 */
public class PatchInfo
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(PatchInfo.class);

    private List<PatchEntry> patchFileList = new ArrayList<PatchEntry>();

    private PatchInfo()
    {
    }

    /**
     * 根据提供的base目录，读取patch.txt文件，将目标目录的补丁路径读取出来，放到patchFileList中
     * @param patchDir 补丁根目录
     * @param backupDirName 备份文件目录，如果存在，则源目录在base目录上新增一级路径
     */
    public static PatchInfo parse(String patchDir, String backupDirName)
    {
        PatchInfo patchInfo = new PatchInfo();
        try
        {
            String patchFile = IOUtils.canonicalPath(patchDir) + File.separator + "patch.txt";
            List<String> content = IOUtils.loadPatchInfo(patchFile);
            for (String remote : content)
            {
                String local = patchInfo.getLocal(patchDir, backupDirName, remote);
                String backup = patchInfo.getBackup(patchDir, backupDirName, remote);
                patchInfo.patchFileList.add(new PatchEntry(local, remote, backup));
            }
        }
        catch (PatchException e)
        {
            LOG.error("Pares patch.txt failed. patchDir = " + patchDir, e);
        }

        return patchInfo;
    }

    private String getLocal (String base, String backupDirName, String remote)
    {
        File file = new File(remote);
        String fileName = file.getName();

        return IOUtils.canonicalPath(base) + File.separator + fileName;
    }

    private String getBackup (String base, String backupDirName, String remote)
    {
        File file = new File(remote);
        String fileName = file.getName();

        return IOUtils.canonicalPath(base) + File.separator + backupDirName + File.separator + fileName;
    }

    public List<PatchEntry> getPatchFileList ()
    {
        return patchFileList;
    }

    public void setPatchFileList (List<PatchEntry> patchFileList)
    {
        this.patchFileList = patchFileList;
    }

    public PatchInfoIterator iterator()
    {
        return new PatchInfoIterator();
    }

    /**
     * 补丁文件信息
     */
    public static class PatchEntry
    {
        String local;

        String remote;

        String backup;

        public PatchEntry (String local, String remote, String backup)
        {
            this.local = local;
            this.remote = remote;
            this.backup = backup;
        }

        public String getLocal ()
        {
            return local;
        }

        public String getRemote ()
        {
            return remote;
        }

        public String getBackup ()
        {
            return backup;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString ()
        {
            return "PatchEntry{" +
                    "local='" + local + '\'' +
                    ", remote='" + remote + '\'' +
                    ", backup='" + backup + '\'' +
                    '}';
        }
    }

    /**
     * 迭代器
     */
    public class PatchInfoIterator  implements Iterator<PatchEntry>
    {
        private int index = 0;

        @Override
        public boolean hasNext ()
        {
            return index < patchFileList.size();
        }

        @Override
        public PatchEntry next ()
        {
            return patchFileList.get(index++);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return "PatchInfo{" +
                "patchFileList=" + patchFileList +
                '}';
    }
}
