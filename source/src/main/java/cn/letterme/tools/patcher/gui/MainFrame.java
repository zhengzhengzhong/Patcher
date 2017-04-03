package cn.letterme.tools.patcher.gui;

import cn.letterme.tools.patcher.constant.I18nConstant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;

/**
 * 图形化界面主入口
 */
public class MainFrame extends JFrame
{
    /**
     * 宽度
     */
    private static final int WIDTH = 700;

    /**
     * 间距
     */
    private static final int PADDING = 5;

    /**
     * 单行高度
     */
    private static final int HEIGHT = 30;

    /**
     * 表格最大行数（包含表头）
     */
    private static final int TABLE_MAX_ROW = 9;

    /**
     * 图标
     */
    private static final ImageIcon ICON = new ImageIcon(MainFrame.class.getResource("/icon.png"));

    /**
     * 单例
     */
    private static final MainFrame MAIN_FRAME = new MainFrame();

    /**
     * 表格控件
     */
    private HostTable hostTable;

    /**
     * 打补丁按钮
     */
    private JButton rollbackBtn;

    /**
     * 回退补丁按钮
     */
    private JButton delBtn;

    /**
     * 删除主机按钮
     */
    private JButton patchBtn;

    /**
     * 获取主界面
     * @return 主界面对象
     */
    public static MainFrame getMainFrame()
    {
        return MAIN_FRAME;
    }

    private MainFrame()
    {
        setLayout(null);

        int y = PADDING;

        // 操作按钮面板
        JPanel opPanel = initOpPanel();
        opPanel.setBounds(PADDING, y, WIDTH - 2 * PADDING, HEIGHT + PADDING);
        y += 2 * PADDING + HEIGHT;
        add(opPanel, BorderLayout.NORTH);

        // 表格面板
        JScrollPane tablePanel = initTablePanel();
        tablePanel.setBounds(PADDING, y, WIDTH - 2 * PADDING, HEIGHT * TABLE_MAX_ROW + PADDING);
        y += 2 * PADDING + HEIGHT * (TABLE_MAX_ROW + 1);
        add(tablePanel, BorderLayout.CENTER);

        this.setIconImage(ICON.getImage());

        setTitle(I18nConstant.GUIMsg.MAIN_FRAME_TITLE);
        setSize(WIDTH, y);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * 初始化表格面板
     *
     * @return 表格面板
     */
    private JScrollPane initTablePanel()
    {
        hostTable = new HostTable(this, WIDTH, HEIGHT * TABLE_MAX_ROW, HEIGHT, PADDING);
        JScrollPane scrollPane = new JScrollPane(hostTable);
        scrollPane.setViewportView(hostTable);
        return scrollPane;
    }

    /**
     * 初始化操作按钮面板
     *
     * @return 操作按钮面板
     */
    private JPanel initOpPanel()
    {
        JPanel jpanel = new JPanel();

        // 打补丁按钮
        patchBtn = new JButton(I18nConstant.GUIMsg.PATCH_BTN);
        patchBtn.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.setDialogTitle(I18nConstant.DialogInfo.SELECT_PATCH_DIR);
                jfc.showOpenDialog(MAIN_FRAME);
                File file = jfc.getSelectedFile();
                if (Objects.nonNull(file))
                {
                    hostTable.firePatch(file.getAbsolutePath());
                }
            }
        });
        jpanel.add(patchBtn);

        // 回退按钮
        rollbackBtn = new JButton(I18nConstant.GUIMsg.ROLLBACK_BTN);
        rollbackBtn.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.setDialogTitle(I18nConstant.DialogInfo.SELECT_ROLLBACK_DIR);
                jfc.showOpenDialog(MAIN_FRAME);
                File file = jfc.getSelectedFile();
                if (Objects.nonNull(file))
                {
                    hostTable.fireRollback(file.getAbsolutePath());
                }
            }
        });
        jpanel.add(rollbackBtn);

        // 删除机器按钮
        delBtn = new JButton(I18nConstant.GUIMsg.DEL_HOST_BTN);
        delBtn.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                hostTable.fireRemoveHostInfo();
            }
        });
        jpanel.add(delBtn);

        // 添加机器按钮
        JButton addBtn = new JButton(I18nConstant.GUIMsg.ADD_HOST_BTN);
        addBtn.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                AddHostInfoDlg addHostInfoDlg = new AddHostInfoDlg(MAIN_FRAME, hostTable);
                addHostInfoDlg.setVisible(true);
            }
        });
        jpanel.add(addBtn);

        // 导出机器信息按钮
        JButton exportBtn = new JButton(I18nConstant.GUIMsg.EXPORT_HOST_BTN);
        exportBtn.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setDialogTitle(I18nConstant.DialogInfo.SELECT_EXPORT_FILE);
                jfc.showSaveDialog(MAIN_FRAME);
                File file = jfc.getSelectedFile();
                if (Objects.nonNull(file))
                {
                    hostTable.fireExportHostInfo(file.getAbsolutePath());
                }
            }
        });
        jpanel.add(exportBtn);

        // 导入机器信息按钮
        JButton importBtn = new JButton(I18nConstant.GUIMsg.IMPORT_HOST_BTN);
        importBtn.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setDialogTitle(I18nConstant.DialogInfo.SELECT_IMPORT_FILE);
                jfc.showOpenDialog(MAIN_FRAME);
                File file = jfc.getSelectedFile();
                if (Objects.nonNull(file))
                {
                    hostTable.fireImportHostInfo(file.getAbsolutePath());
                }
            }
        });
        jpanel.add(importBtn);

        return jpanel;
    }

    /**
     * 根据下方表格数据来刷新按钮状态
     *
     * @param isAnyHostSelected 是否有主机选中
     */
    public void updateBtnState(boolean isAnyHostSelected)
    {
        patchBtn.setEnabled(isAnyHostSelected);
        rollbackBtn.setEnabled(isAnyHostSelected);
        delBtn.setEnabled(isAnyHostSelected);
    }
}
