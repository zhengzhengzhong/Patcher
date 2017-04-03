package cn.letterme.tools.patcher.gui;

import cn.letterme.tools.patcher.constant.ConfigConstant;
import cn.letterme.tools.patcher.constant.I18nConstant;
import cn.letterme.tools.patcher.model.HostInfo;
import cn.letterme.tools.patcher.model.TableData;
import cn.letterme.tools.patcher.util.IOUtils;
import cn.letterme.tools.patcher.util.JsonUtil;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * 主机信息表格
 */
public class HostTable extends JTable
{
    /**
     * 表头标题
     */
    private static final String[] HEADERS = new String[]{"", I18nConstant.GUIMsg.TBL_HEADER_IP, I18nConstant.GUIMsg.TBL_HEADER_PORT, I18nConstant.GUIMsg.TBL_HEADER_USERNAME, I18nConstant.GUIMsg.TBL_HEADER_PASSWORD, I18nConstant.GUIMsg.TBL_HEADER_STATUS};

    /**
     * 列宽
     */
    private static final int[] COLUMN_WIDTH = new int[HEADERS.length];

    /**
     * 列宽百分比
     */
    private static final float[] PERCENT = new float[]{0.05F, 0.2F, 0.15F, 0.2F, 0.2F, 0.2F};

    /**
     * 表头多选框
     */
    private static MyCheckBoxRenderer headerCheck = new MyCheckBoxRenderer();

    /**
     * 表格模型
     */
    private MyTableModel tableModel = new MyTableModel();

    /**
     * 父节点
     */
    private MainFrame parentFrame;

    public HostTable(MainFrame parentFrame, int width, int height, int heightPerRow, int padding)
    {
        super();

        this.parentFrame = parentFrame;

        // 设置表格模型
        setModel(tableModel);
        getTableHeader().setPreferredSize(new Dimension(width, heightPerRow));

        // 表格的显示尺寸
        setPreferredScrollableViewportSize(new Dimension(width, height));
        for (int i = 0; i < COLUMN_WIDTH.length; i++)
        {
            // 去掉前后padding
            COLUMN_WIDTH[i] = (int) ((width - padding * 2) * PERCENT[i]);
        }

        // 设置表格行高度
        setRowHeight(heightPerRow);
        TableColumnModel columnModel = getColumnModel();
        int columnCount = columnModel.getColumnCount();
        for (int i = 0; i < COLUMN_WIDTH.length && i < columnCount; i++)
        {
            TableColumn column = columnModel.getColumn(i);
            column.setMaxWidth(COLUMN_WIDTH[i]);
            column.setMinWidth(COLUMN_WIDTH[i]);
        }

        // 设置表头样式
        initHeaderRenderer();

        // 表示所有的列都不可以拖动
        tableHeader.setReorderingAllowed(false);

        // 设置表头的多选框的鼠标点击事件
        tableHeader.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int column = columnAtPoint(e.getPoint());
                if (0 == column)
                {
                    //如果点击的是第0列，即checkbox这一列
                    boolean b = !headerCheck.isSelected();
                    headerCheck.setSelected(b);
                    int rowCount = getRowCount();
                    for (int i = 0; i < rowCount; i++)
                    {
                        getModel().setValueAt(b, i, 0);
                    }

                    // 刷新表头
                    refreshTableHeader();
                }
            }
        });

        addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // 获得行位置
                int row = rowAtPoint(e.getPoint());
                // 获得列位置
                int column = columnAtPoint(e.getPoint());
                if (0 == column)
                {
                    //如果点击的是第0列，即checkbox这一列
                    boolean newVal = !Boolean.valueOf(Objects.toString(getModel().getValueAt(row, column)));
                    getModel().setValueAt(newVal, row, column);
                }
                else
                {
                    // 双击选中
                    int clickCount = e.getClickCount();
                    if (2 == clickCount)
                    {
                        boolean newVal = !Boolean.valueOf(Objects.toString(getModel().getValueAt(row, 0)));
                        getModel().setValueAt(newVal, row, 0);
                    }
                }

                // 刷新表头
                refreshTableHeader();
            }
        });

        addMouseMotionListener(new MouseAdapter()
        {
            public void mouseMoved(MouseEvent e)
            {
                int row = rowAtPoint(e.getPoint());
                int col = columnAtPoint(e.getPoint());
                if (row > -1 && col > -1)
                {
                    Object value = getValueAt(row, col);
                    if (null != value && !"".equals(value))
                    {
                        //悬浮显示单元格内容
                        setToolTipText(value.toString());
                    }
                    else
                    {
                        //关闭提示
                        setToolTipText(null);
                    }
                }
            }
        });

        refreshTableHeader();
    }

    private void initHeaderRenderer()
    {
        for (int i = 0; i < HEADERS.length; i++)
        {
            TableColumn tableColumn = columnModel.getColumn(i);
            if (0 == i)
            {
                // 设置第一列的表头为多选框
                tableColumn.setHeaderRenderer(headerCheck);
            }
            else
            {
                // 如果是其他地方的表格，沿用父类中提供的渲染器
                DefaultTableCellRenderer r = new MyTableHeaderRenderer();
                r.setHorizontalAlignment(JLabel.CENTER);
                r.setBackground(tableHeader.getBackground());
                r.setForeground(tableHeader.getForeground());
                tableColumn.setHeaderRenderer(r);
            }
        }
    }

    /**
     * 刷新表头 获取所有的行，如果都选中，表头默认选中；如果有一个不选中，表头不选中。
     */
    public void refreshTableHeader()
    {
        int rowCount = getRowCount();

        boolean isHeaderSelected = false;
        boolean isAnyHostSelected = false;
        if (0 != rowCount)
        {
            isHeaderSelected = true;
            for (int i = 0; i < rowCount; i++)
            {
                Object val = getModel().getValueAt(i, 0);
                boolean selectedStatus = Boolean.valueOf(Objects.toString(val));
                isHeaderSelected &= selectedStatus;
                isAnyHostSelected = isAnyHostSelected || selectedStatus;
            }
        }
        headerCheck.setSelected(isHeaderSelected);
        // 刷新表头
        getTableHeader().repaint();
        // 刷新父窗体的按钮
        parentFrame.updateBtnState(isAnyHostSelected);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        if (column == 0)
        {
            return super.getDefaultRenderer(Boolean.class);
        }
        else
        {
            // 如果是其他地方的表格，沿用父类中提供的渲染器
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            r.setHorizontalAlignment(JLabel.CENTER);
            return r;
        }
    }

    /**
     * 添加主机数据
     *
     * @param hostInfo 主机数据
     */
    public void fireAddHostInfo(HostInfo hostInfo)
    {
        if (null != hostInfo)
        {
            tableModel.fireAddHostInfo(hostInfo);
            refreshTableHeader();
        }
    }

    /**
     * 删除选中的行
     */
    public void fireRemoveHostInfo()
    {
        tableModel.fireRemoveHostInfo();
        refreshTableHeader();
    }

    /**
     * 打补丁
     *
     * @param patchDir 补丁目录
     */
    public void firePatch(String patchDir)
    {
        tableModel.firePatch(patchDir);
    }

    /**
     * 回退
     *
     * @param rollbackDir 回退目录
     */
    public void fireRollback(String rollbackDir)
    {
        tableModel.fireRollback(rollbackDir);
    }

    /**
     * 导出选中的主机信息
     *
     * @param savedPath 导出的文件路径
     */
    public void fireExportHostInfo(String savedPath)
    {
        tableModel.fireExportHostInfo(savedPath);
    }

    /**
     * 导入主机信息
     *
     * @param importPath 导入的文件路径
     */
    public void fireImportHostInfo(String importPath)
    {
        tableModel.fireImportHostInfo(importPath);
    }

    private static class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column)
        {
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
    }

    // 把要显示在表格中的数据存入字符串数组和Object数组中
    private class MyTableModel extends AbstractTableModel implements Observer
    {
        /**
         * 表格数据
         */
        private TableData tableData = new TableData();

        public MyTableModel()
        {
            super();
            // 加载配置文件数据
            fireImportHostInfo(ConfigConstant.CFG_HOST_INFO);
        }

        /**
         * 获得列的数目
         */
        public int getColumnCount()
        {
            return HEADERS.length;
        }

        /**
         * 获得行的数目
         */
        public int getRowCount()
        {
            return tableData.size();
        }

        /**
         * 获得某列的名字
         */
        public String getColumnName(int col)
        {
            return HEADERS[col];
        }

        /**
         * 获得某行某列的数据
         */
        public Object getValueAt(int row, int col)
        {
            return tableData.get(row, col);
        }

        /**
         * 判断每个单元格的类型
         */
        public Class getColumnClass(int c)
        {
            if (0 == c)
            {
                return Boolean.class;
            }

            return String.class;
        }

        /**
         * 将表格声明为不可编辑的
         */
        public boolean isCellEditable(int row, int col)
        {
            return false;
        }

        /**
         * 改变某个数据的值
         */
        public void setValueAt(Object value, int row, int col)
        {
            tableData.set(row, col, value);
            fireTableCellUpdated(row, col);
        }

        /**
         * 添加主机数据
         *
         * @param hostInfo 主机数据
         */
        public void fireAddHostInfo(HostInfo hostInfo)
        {
            tableData.add(hostInfo);
            fireTableDataChanged();
            // 将数据保存到后台
            saveHostInfo();
        }

        /**
         * 删除选中行
         */
        public void fireRemoveHostInfo()
        {
            tableData.remove();
            fireTableDataChanged();
            // 将数据保存到后台
            saveHostInfo();
        }

        /**
         * 打补丁
         *
         * @param patchDir 补丁目录
         */
        public void firePatch(String patchDir)
        {
            tableData.patch(patchDir, this);
        }

        /**
         * 回退
         *
         * @param rollbackDir 回退目录
         */
        public void fireRollback(String rollbackDir)
        {
            tableData.rollback(rollbackDir, this);
        }

        /**
         * 导出选中的主机信息
         *
         * @param savedPath 导出的文件路径
         */
        public void fireExportHostInfo(String savedPath)
        {
            tableData.export(savedPath);
        }

        /**
         * 导入主机信息
         *
         * @param importPath 导入的文件路径
         */
        public void fireImportHostInfo(String importPath)
        {
            TableData tblData = JsonUtil.jsonToBean(IOUtils.loadHostInfo(importPath), TableData.class);
            if (Objects.nonNull(tblData))
            {
                List<HostInfo> hostInfoList = tblData.getTableData();
                for (HostInfo hostInfo : hostInfoList)
                {
                    if (Objects.nonNull(hostInfo))
                    {
                        tableData.add(hostInfo);
                    }
                }
            }
            fireTableDataChanged();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void update(Observable o, Object arg)
        {
            fireTableDataChanged();
        }

        private void saveHostInfo()
        {
            tableData.export(ConfigConstant.CFG_HOST_INFO);
        }
    }

    /**
     * 表格样式渲染
     */
    private class MyTableHeaderRenderer extends DefaultTableCellRenderer
    {
        /**
         * {@inheritDoc}
         */
        public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column)
        {
            super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
            if (jTable != null)
            {
                JTableHeader tableHeader = jTable.getTableHeader();
                if (tableHeader != null)
                {
                    Color cellForegroundColor = null;
                    Color cellBackgroundColor = null;
                    if (hasFocus)
                    {
                        cellForegroundColor = DefaultLookup.getColor(this, this.ui, "TableHeader.focusCellForeground");
                        cellBackgroundColor = DefaultLookup.getColor(this, this.ui, "TableHeader.focusCellBackground");
                    }

                    if (cellForegroundColor == null)
                    {
                        cellForegroundColor = tableHeader.getForeground();
                    }

                    if (cellBackgroundColor == null)
                    {
                        cellBackgroundColor = tableHeader.getBackground();
                    }

                    this.setForeground(cellForegroundColor);
                    this.setBackground(cellBackgroundColor);
                }
            }

            Border border = null;
            if (hasFocus)
            {
                border = DefaultLookup.getBorder(this, this.ui, "TableHeader.focusCellBorder");
            }

            if (border == null)
            {
                border = DefaultLookup.getBorder(this, this.ui, "TableHeader.cellBorder");
            }

            this.setBorder(border);
            return this;
        }
    }
}
