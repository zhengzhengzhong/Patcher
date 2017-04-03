package cn.letterme.tools.patcher.gui;

import cn.letterme.tools.patcher.constant.I18nConstant;
import cn.letterme.tools.patcher.model.HostInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * 添加主机信息对话框
 */
public class AddHostInfoDlg extends JDialog
{
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(AddHostInfoDlg.class);

    /**
     * 本地IP
     */
    private static final String LOCAL_HOST = "127.0.0.1";

    /**
     * 高度
     */
    private static final int HEIGHT = 30;

    /**
     * 第一列起始X
     */
    private static final int X1 = 10;

    /**
     * 第二列起始X
     */
    private static final int X2 = 60;

    /**
     * 行间距
     */
    private static final int PADDING = 5;

    /**
     * 总宽度
     */
    private static final int WIDTH =  300;

    /**
     * 第一列宽度
     */
    private static final int WIDHT1 = X2 - X1 - PADDING;

    /**
     * 第二列宽度
     */
    private static final int WIDHT2 = WIDTH - X2 - 2 * PADDING;

    /**
     * IP地址
     */
    private JLabel ipAddrLabel;
    private JTextField ipAddr;

    /**
     * 端口
     */
    private JLabel portLable;
    private JTextField port;

    /**
     * 用户名
     */
    private JLabel userNameLabel;
    private JTextField userName;

    /**
     * 密码
     */
    private JLabel passwordLabel;
    private JTextField password;

    /**
     * 添加按钮
     */
    private JButton addBtn;

    /**
     * 取消按钮
     */
    private JButton cancelBtn;

    /**
     * 是否本机
     */
    private JCheckBox isLocal;

    private HostTable hostTable;

    public AddHostInfoDlg(Component parent, HostTable hostTable)
    {
        super();

        this.hostTable = hostTable;
        setLayout(null);

        int y = PADDING;

        /**
         * 是否本机
         */
        isLocal = new JCheckBox(I18nConstant.GUIMsg.CHK_IS_LOCAL_HOST);
        initCheckBox(isLocal);
        isLocal.setBounds(X1, y, WIDHT2, HEIGHT);
        y += PADDING + HEIGHT;
        add(isLocal);

        /**
         * IP地址
         */
        ipAddrLabel = new JLabel(I18nConstant.GUIMsg.TBL_HEADER_IP);
        ipAddr = initTextField();
        ipAddrLabel.setBounds(X1, y, WIDHT1, HEIGHT);
        ipAddr.setBounds(X2, y, WIDHT2, HEIGHT);
        y += PADDING + HEIGHT;
        add(ipAddrLabel);
        add(ipAddr);

        /**
         * 端口
         */
        portLable = new JLabel(I18nConstant.GUIMsg.TBL_HEADER_PORT);
        port = initTextField();
        portLable.setBounds(X1, y, WIDHT1, HEIGHT);
        port.setBounds(X2, y, WIDHT2, HEIGHT);
        y += PADDING + HEIGHT;
        add(portLable);
        add(port);

        /**
         * 用户名
         */
        userNameLabel = new JLabel(I18nConstant.GUIMsg.TBL_HEADER_USERNAME);
        userName = initTextField();
        userNameLabel.setBounds(X1, y, WIDHT1, HEIGHT);
        userName.setBounds(X2, y, WIDHT2, HEIGHT);
        y += PADDING + HEIGHT;
        add(userNameLabel);
        add(userName);

        /**
         * 密码
         */
        passwordLabel = new JLabel(I18nConstant.GUIMsg.TBL_HEADER_PASSWORD);
        password = initTextField();
        passwordLabel.setBounds(X1, y, WIDHT1, HEIGHT);
        password.setBounds(X2, y, WIDHT2, HEIGHT);
        y += PADDING + HEIGHT;
        add(passwordLabel);
        add(password);

        // 添加按钮
        addBtn = new JButton(I18nConstant.GUIMsg.BTN_ADD);
        initAddBtn(addBtn);
        // 取消按钮
        cancelBtn = new JButton(I18nConstant.GUIMsg.BTN_CANCEL);
        initCancelBtn(cancelBtn);
        JPanel addBtnJPanel = new JPanel();
        addBtnJPanel.add(addBtn, BorderLayout.CENTER);
        JPanel cancelBtnJPanel = new JPanel();
        cancelBtnJPanel.add(cancelBtn, BorderLayout.CENTER);
        JPanel btnJPanel = new JPanel();
        btnJPanel.add(addBtnJPanel, BorderLayout.CENTER);
        btnJPanel.add(cancelBtnJPanel, BorderLayout.CENTER);
        btnJPanel.setBounds(X1, y, WIDTH, HEIGHT + 2 * PADDING);
        y += 2 * PADDING + HEIGHT;
        add(btnJPanel);

        // 表格
        setTitle(I18nConstant.GUIMsg.TITLE_ADD_HOST_INFO);
        setLocationRelativeTo(parent);
        y += PADDING + HEIGHT;
        setSize(WIDTH, y);
        setResizable(false);
    }

    /**
     * 初始化是否本地的选择框
     *
     * @param isLocal 选择框
     */
    private void initCheckBox(final JCheckBox isLocal)
    {
        isLocal.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                boolean isSelected = isLocal.isSelected();
                if (isSelected)
                {
                    ipAddr.setText(LOCAL_HOST);
                    ipAddr.setEnabled(false);
                    port.setEnabled(false);
                    userName.setEnabled(false);
                    password.setEnabled(false);
                }
                else
                {
                    String ipAddrStr = ipAddr.getText();
                    if (LOCAL_HOST.equals(ipAddrStr.trim()))
                    {
                        ipAddr.setText("");
                    }
                    ipAddr.setEnabled(true);
                    port.setEnabled(true);
                    userName.setEnabled(true);
                    password.setEnabled(true);
                }
            }
        });
    }

    /**
     * 初始化取消按钮事件
     *
     * @param cancelBtn 取消按钮
     */
    private void initCancelBtn(JButton cancelBtn)
    {
        cancelBtn.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dispose();
            }
        });
    }

    /**
     * 初始化添加按钮事件
     *
     * @param addBtn 添加按钮
     */
    private void initAddBtn(JButton addBtn)
    {
        addBtn.addMouseListener(new MouseAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (!checkInput())
                {
                    return;
                }
                HostInfo hostInfo = new HostInfo();
                hostInfo.setSelected(false);
                if (isLocal.isSelected())
                {
                    hostInfo.setIpAddr(LOCAL_HOST);
                    hostInfo.setLocal(true);
                }
                else
                {
                    hostInfo.setPort(Integer.valueOf(port.getText()));
                    hostInfo.setIpAddr(ipAddr.getText());
                    hostInfo.setUserName(userName.getText());
                    hostInfo.setPassword(password.getText());
                    hostInfo.setLocal(false);
                }
                hostTable.fireAddHostInfo(hostInfo);
                dispose();
            }
        });
    }

    private boolean checkInput()
    {
        if (isLocal.isSelected())
        {
            return true;
        }

        // IP地址
        String ipAddrStr = ipAddr.getText();
        if (!ipAddrStr.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))"))
        {
            LOG.error("IP address is invalid, value = " + ipAddrStr);
            ipAddr.setBackground(Color.RED);
            return false;
        }

        // 端口
        String portStr = port.getText();
        if (!portStr.matches("\\d+"))
        {
            LOG.error("Port is invalid, value = " + portStr);
            port.setBackground(Color.RED);
            return false;
        }

        // 用户名
        String userNameStr = userName.getText();
        if (Objects.isNull(userNameStr) || userNameStr.trim().isEmpty())
        {
            LOG.error("Username is invalid, value = " + userNameStr);
            userName.setBackground(Color.RED);
            return false;
        }

        // 密码
        String passwordStr = password.getText();
        if (Objects.isNull(passwordStr) || passwordStr.trim().isEmpty())
        {
            LOG.error("Password is invalid, value = " + passwordStr);
            password.setBackground(Color.RED);
            return false;
        }

        return true;
    }

    private JTextField initTextField()
    {
        final JTextField textField = new JTextField();
        textField.addKeyListener(new KeyAdapter()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void keyReleased(KeyEvent e)
            {
                String str = textField.getText();
                if (Objects.nonNull(str) && !str.trim().isEmpty())
                {
                    textField.setBackground(Color.WHITE);
                }
            }
        });
        return textField;
    }
}
