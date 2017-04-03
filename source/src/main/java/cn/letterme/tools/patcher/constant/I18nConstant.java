package cn.letterme.tools.patcher.constant;

/**
 * 国际化常量
 */
public class I18nConstant
{
    /**
     * 界面显示国际化
     */
    public static class GUIMsg
    {
        /**
         * 主界面标题
         */
        public static final String MAIN_FRAME_TITLE = "批量补丁工具";

        /**
         * 添加主机
         */
        public static final String ADD_HOST_BTN = "添加主机";

        /**
         * 删除主机
         */
        public static final String DEL_HOST_BTN = "删除主机";

        /**
         * 更新补丁
         */
        public static final String PATCH_BTN = "更新补丁";

        /**
         * 回退补丁
         */
        public static final String ROLLBACK_BTN = "回退补丁";

        /**
         * 导出主机
         */
        public static final String EXPORT_HOST_BTN = "导出主机";

        /**
         * 导入主机
         */
        public static final String IMPORT_HOST_BTN = "导入主机";

        /**
         * IP地址
         */
        public static final String TBL_HEADER_IP = "IP地址";

        /**
         * 端口
         */
        public static final String TBL_HEADER_PORT = "端口";

        /**
         * 用户名
         */
        public static final String TBL_HEADER_USERNAME = "用户名";

        /**
         * 密码
         */
        public static final String TBL_HEADER_PASSWORD = "密码";

        /**
         * 状态
         */
        public static final String TBL_HEADER_STATUS = "状态";

        /**
         * 是否本机
         */
        public static final String CHK_IS_LOCAL_HOST = "是否本机";

        /**
         * 添加主机信息-对话框标题
         */
        public static final String TITLE_ADD_HOST_INFO = "添加主机信息";

        /**
         * 添加-按钮
         */
        public static final String BTN_ADD = "添加";

        /**
         * 取消-按钮
         */
        public static final String BTN_CANCEL = "取消";
    }

    public static class DialogInfo
    {
        public static final String SELECT_PATCH_DIR = "请选择补丁目录";

        public static final String SELECT_ROLLBACK_DIR = "请选择备份目录";

        public static final String SELECT_EXPORT_FILE = "请选择保存主机信息文件路径";

        public static final String SELECT_IMPORT_FILE = "请选择导入主机信息文件路径";
    }

    /**
     * 提示国际化
     */
    public static class Tips
    {
        /**
         * 文件不存在
         */
        public static final String FILE_NOT_FOUND = "失败！文件不存在！";

        /**
         * IO异常
         */
        public static final String IO_EXCEPTION = "失败！IO异常！";

        /**
         * 连接失败
         */
        public static final String CONNECT_FAILED = "连接失败！";

        /**
         * 打补丁失败
         */
        public static final String PATCH_FAILED = "打补丁失败！";

        /**
         * 备份失败
         */
        public static final String BACKUP_FAILED = "备份失败！";

        /**
         * 回滚失败
         */
        public static final String ROLLBACK_FAILED = "回滚失败！";

        /**
         * 前置处理
         */
        public static final String STR_BEFORE_PATCH = "前置处理...";

        /**
         * 正在打补丁
         */
        public static final String STR_DO_PATCH = "正在打补丁...";

        /**
         * 正在回滚
         */
        public static final String STR_DO_ROLLBACK = "正在回滚...";

        /**
         * 后置处理
         */
        public static final String STR_AFTER_PATCH = "后置处理...";

        /**
         * 成功
         */
        public static final String STR_SUCCESS = "成功！";
    }
}
