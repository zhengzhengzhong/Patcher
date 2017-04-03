package cn.letterme.tools.patcher;

import cn.letterme.tools.patcher.gui.MainFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.io.IOException;
import java.util.Properties;

/**
 * 图形化界面入口
 */
public class GUILauncher
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(GUILauncher.class);

    static
    {
        PropertyConfigurator.configure(System.clearProperty("log4j"));
    }

    /**
     * 主入口
     */
    public static void main(String[] args)
    {
        LOG.error("Start the patcher...");
        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try
        {
            UIManager.setLookAndFeel(lookAndFeel);
            SwingUtilities.updateComponentTreeUI(MainFrame.getMainFrame());
        }
        catch (Exception e)
        {
            LOG.error("Start the patcher failed.", e);
        }
    }
}
