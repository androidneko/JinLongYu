package com.androidcat.jly.application;

/**
 * @ClassName AppConstants
 * @Description 程序常量类
 * @author xuxiang
 * @date 2014-9-11
 */
public class AppConstants {



	/*************************************************************************************************
	/**
	 * 银行卡管理标志，用于在Activity之间跳转时标志是点击了我的银行卡
	 */
	public static final String BANKCARD_MANAGER = "bankcardManager";

	/**
	 * 刷新卡支付标志，用于在Activity之间跳转时，ReadCardActivity界面绑完卡后标志是否需要支付
	 */
	public static final String BIND_BANKCARD_PAY = "bindBankcardPay";

	/**
	 * 设备检测失败标志，用于在Activity之间跳转时标志是否是设备检测失败了进入到DeviceSelectActivity
	 */
	public static final String ERROR_DEVICE = "errorDevice";

	/**
	 * 代扣银行卡列表标志
	 */
	public static final String UPOP_BANKCARD_LIST = "upopBankcardList";
	/*************************************************************************************************



	/*************************************************************************************************
	/**
	 * 提示信息弹出形式为气泡
	 */
	public static final String REQUEST_TOAST = "1";

	/**
	 * 提示信息弹出形式为弹出框
	 */
	public static final String REQUEST_DIALOG = "0";
	/*************************************************************************************************



	/*************************************************************************************************
	/**
	 * 交易卡类型，2个字节的ASC码
	 * 00  ->  不区分借贷记卡
	 */
	public static final String CARD_ALL = "00";

	/**
	 * 交易卡类型，2个字节的ASC码
	 * 01  ->  借记卡
	 */
	public static final String CARD_DEBIT = "01";

	/**
	 * 交易卡类型，2个字节的ASC码
	 * 02  ->  贷记卡
	 */
	public static final String CARD_CREDIT = "02";

	/**
	 * 交易卡类型，2个字节的ASC码
	 * 03  ->  准贷记卡
	 */
	public static final String CARD_CREDIT_QUASI = "03";

	/**
	 * 交易卡类型，2个字节的ASC码
	 * 04  ->  预付费卡
	 */
	public static final String CARD_PREPAID = "04";
	/*************************************************************************************************



	/*************************************************************************************************
	/**
	 * 区分是余额查询、付款标志、补打印、撤销
	 * 0 -> 余额查询
	 */
	public static final int TYPE_QUERY_BALANCE = 0;

	/**
	 * 区分是付款、余额查询标志、补打印、撤销
	 * 1 -> 付款
	 */
	public static final int TYPE_PAY = 1;

	/**
	 * 区分是补打印、付款、余额查询、撤销
	 * 2 -> 补打印
	 */
	public static final int TYPE_REPLENISH_PRINT = 2;

	/**
	 * 区分是撤销、补打印、付款、余额查询
	 * 3 -> 撤销
	 */
	public static final int TYPE_REPLENISH_DISCARD = 3;

	/**
	 * 通联圈存
	 * 4 -> 通联圈存
	 */
	public static final int TYPE_TL_PBOC_LOAD = 4;

	/**
	 * 区分是补打印、付款、余额查询、撤销、现金打印、其它打印、自定义打印
	 * 5 -> 现金打印
	 */
	public static final int TYPE_CASH_PRINT = 5;

	/**
	 * 区分是补打印、付款、余额查询、撤销、现金打印、其它打印、自定义打印
	 * 6 -> 其它打印
	 */
	public static final int TYPE_OTHER_PRINT = 6;


	/**
	 * 交易结果未知
	 */
	public static final int TYPE_REASON_UNKNOW = 7;

	/**
	 * 区分是补打印、付款、余额查询、撤销、现金打印、其它打印、自定义打印
	 * 8 -> 自定义打印
	 */
	public static final int TYPE_CUSTOM_PRINT = 8;

	public static final String PRINT_DISCAED_TYPE_CANUSE = "撤销";

	/*************************************************************************************************



	/*************************************************************************************************
	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要跳转去获取密码
	 */
	public static final int GET_BANKCARD_PASSWORD = 10;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要跳转去支付
	 */
	public static final int PAYMENT_RESULT = 11;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要去绑定银行卡
	 */
	public static final int BIND_BANKCARD = 12;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要去更新银行卡界面BankcardUpdateActivity
	 */
	public static final int UPDATE_BANKCARD_TYPE = 13;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要去蓝牙连接界面ConnectionBluetoothActivity
	 */
	public static final int TYPE_BLUETOOTH_CONNECTION = 14;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要去金额键盘界面AmountKeyboardActivity
	 */
	public static final int TYPE_AMOUNT_KEYBOARD = 15;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要去二维码扫描界面CaptureActivity
	 */
	public static final int CODE_SCANNER_CONNECTION = 16;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是补打印跳转去蓝牙连接
	 */
	public static final int TYPE_REPLENISH_BLUETOOTH_CONNECTION = 17;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是去进行余额查询
	 */
	public static final int TYPE_QUERY_BANLANCE = 18;

	/**
	 * doStartUnionPayPlugin 函数中的requestCode 用于在Activity之间跳转时标志是去进行无卡支付的请求标识
	 */
	public static final int TYPE_UPMP_REQUEST = 10;

	/**
	 * doStartUnionPayPlugin 函数中的resultCode 用于在Activity之间跳转时标志是去进行无卡支付的返回标识
	 */
	public static final int TYPE_UPMP_RESULT = -1;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要跳转去获取卡号明文
	 */
	public static final int GET_BANKCARD_NUMBER = 19;

	/**
	 * startActivityForResult 函数中的requestCode 用于webViewHostAvtivity跳转去获取本地相册
	 */
	public static final int GET_PIC_ALBUM = 20;

	/**
	 * startActivityForResult 函数中的requestCode 用于webViewHostAvtivity跳转去获取拍照图片
	 */
	public static final int GET_PIC_PHOTOGRAPH = 21;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要跳转去从设备键盘输入数据返回给app
	 * 目前支持landi 和 newland
	 */
	public static final int GET_DATA_FROM_DEVICE_KEYBOARD = 22;
	/**
	 * 设置快捷支付密码返回
	 */
	public static final int SETTING_PAYPSW = 23;

	/**
	 * 支付前签名
	 */
	public static final int SIGN_BEFORE_PAY = 24;

	/**
	 * 补签名
	 */
	public static final int RECEIPTS_SIGN = 24;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是要去(微信/支付宝主扫)二维码扫描界面CaptureActivity
	 */
	public static final int CODE_SCANNER = 26;


	public static final int AUTO_DEVICE_SELECT = 27;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是支付完成后去打印界面PrintBillsActivity
	 */
	public static final int PAY_SUCCESS_GO_PRINT = 28;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是支付完成后去电子签名界面SignActivity
	 */
	public static final int PAY_SUCCESS_GO_SIGN = 29;

	/**
	 * startActivityForResult 函数中的requestCode 用于在Activity之间跳转时标志是余额查询完成后走中金白条流程
	 */
	public static final int QUERY_BALANCE_IOUS = 30;

	/*************************************************************************************************

	/****************QuickPay Action_Result Param START*********************************/

	public static final int QP_ADDCARD_RESULT=0;

	public static final int QP_SETPSW_RESULT=1;

	public static final int QP_ADDCARD_REQUEST=0;

	public static final int QP_GUSTURESET_REQUEST=1;

	/****************QuickPay Action_Result Param END*********************************/

	/*************************************************************************************************
	/**
	 * 打印时设置消费 还是 撤销
	 * 0 -> 消费
	 */
	public static final String PRINT_PAY_TYPE = "0";

	/**
	 * 打印时设置消费 还是 撤销
	 * 1 -> 撤销
	 */
	public static final String PRINT_DISCAED_TYPE = "1";
	/*************************************************************************************************



	/*************************************************************************************************
	/**
	 * 支付通道为UPOP
	 */
	public static final String CHANNEL_TYPE_UPOP = "0003";

	/**
	 * 支付通道为银联无卡支付插件
	 */
	public static final String CHANNEL_TYPE_UPMP = "0005";

	/**
	 * 支付通道为银联无卡支付插件(新)
	 */
	public static final String CHANNEL_TYPE_UPMP_NEW = "0017";

	/**
	 * 支付通道为银联多渠道实时代付
	 */
	public static final String CHANNEL_TYPE_MULTI_CHANNEL_PAY = "0006";

	/**
	 * 支付通道为通联卡卡转账
	 */
	public static final String CHANNEL_TYPE_TRANSFER = "0007";

	/**
	 * 支付通道为电子现金
	 */
	public static final String CHANNEL_TYPE_ELECTRONIC_CASH = "0008";

	/**
	 * 全渠道无卡（全渠道辅助消费）
	 */
	public static final String CHANEL_TYPE_FC_NOCARD_PAY = "0010";

	/**
	 * 快付通
	 */
	public static final String CHANEL_TYPE_KFT = "0018";

	/**
	 * 批量代收
	 */
	public static final String CHANEL_TYPE_BATCH_COLLECTION = "0042";

	/**
	 * 虚拟账户代付
	 */
	public static final String CHANEL_TYPE_VIRTUAL_PAY = "0011";

	/**
	 * 银联多渠道实时代付(姓名卡号)
	 */
	public static final String CHANEL_TYPE_MULTI_CHANNEL_NAMECARD_PAY = "0013";

	/**
	 * 通联圈存
	 */
	public static final String CHANEL_TYPE_TL_PBOC_LOAD = "0015";


	/**
	 * 威富通微信支付,未调通。废弃
	 */
	public static final String CHANEL_TYPE_WFT = "0034";

	/**
	 * 实时代收
	 */
	public static final String CHANEL_TYPE_SSDS = "0034";

	/**
	 * 线下批量代收
	 */
	public static final String CHANEL_TYPE_OFFLINEBATCHCOLLECTION = "0055";


	/**
	 * 微信支付（商户扫码主扫）
	 */
	public static final String CHANEL_TYPE_WX_MERCHANT_ACTIVE_SCAN = "0030";

	/**
	 * 微信支付（商户扫码被扫）
	 */
	public static final String CHANEL_TYPE_WX_MERCHANT_INACTIVE_SCAN = "0031";

	/**
	 * 微信支付(总营被扫)
	 */
	public static final String CHANEL_TYPE_WX_ZY_INACTIVE_SCAN = "0053";



	/**
	 * 支付宝支付（总营被扫）
	 */
	public static final String CHANEL_TYPE_ALIPAY_ZY_INACTIVE_SCAN = "0051";



	/**
	 * 支付宝支付（总营主扫）
	 */
	public static final String CHANEL_TYPE_ALIPAY_ZY_ACTIVE_SCAN = "0066";



	/**
	 * 威富通支付宝支付（商户扫码被扫）
	 */
	public static final String CHANEL_TYPE_ALIPAY_WFT_INACTIVE_SCAN = "0052";


	/**
	 * 威富通支付宝支付（商户扫码主扫）
	 */
	public static final String CHANEL_TYPE_ALIPAY_WFT_ACTIVE_SCAN = "0062";

	/**
	 * 微信支付（总营主扫）
	 */
	public static final String CHANEL_TYPE_WX_ZY_ACTIVE_SCAN = "0067";

	/**
	 * 现金支付
	 */
	public static final String CHANEL_TYPE_CASH = "0024";

	/**
	 * 转账
	 */
	public static final String CHANEL_TYPE_TRANSFER = "0035";

	/**
	 * 南京认筹金
	 */
	public static final String CHANEL_TYPE_CONFESS_TO_RAISE_NANJING = "";

	/**
	 * 中金白条
	 */
	public static final String CHANEL_TYPE_IOUS = "";

	/*************************************************************************************************



	/*************************************************************************************************
	/**
	 * 已安装无卡支付插件，并启动插件
	 */
	public static final int PLUGIN_VALID = 0;

	/**
	 * 未安装无卡支付插件
	 */
	public static final int PLUGIN_NOT_INSTALLED = -1;

	/**
	 * 无卡支付插件需要升级
	 */
    public static final int PLUGIN_NEED_UPGRADE = 2;
    /*************************************************************************************************



    /*************************************************************************************************
    /**
     * 无卡支付成功
     */
    public static final String UPMP_PAY_SUCCESS = "success";

    /**
     * 无卡支付失败
     */
    public static final String UPMP_PAY_FAIL = "fail";

    /**
     * 用户取消了无卡支付操作
     */
    public static final String UPMP_PAY_CANCEL = "cancel";
    /*************************************************************************************************/

    /**
     * UPMP插件安装成功
     */
    public static final int PLUGIN_INSTALL_SUCCESS = 0;

    /**
     * UPMP插件安装失败
     */
    public static final int PLUGIN_INSTALL_FAIL = 1;

    /******************************山东城联，收款，需要区分刷卡收款，电子现金，一卡通。payType = 2，则为一卡通*****************/

    public static String PAY_TYPE = "0";

    public static String PARAMS_INFO = "";

    public static String AMOUNT_INFO = "";

    /***********************************************************************************************/

    public static String TRACE_BTN_BACK = "返回";

    public static String TRACE_DROP_HISTORY = "号码历史记录下拉按钮";

    public static String TRACE_AUTO_CHECKBOX = "自动登录选择框";

    public static String TRACE_TEXT_UESER_TYPE = "选择用户登陆类型";//Pad版用到

    /***********************************************************************************************/

    public static final int POST = 0;

    public static final int GET = 1;

    public static final int FILEUPLOAD = 2;

    /**
	 * 快捷支付已实名
	 */
	public static final String HAVE_REAL_NAME = "1";

    /**
     * 插件交易成功
     */
    public static final int PAYMENT_RESULT_SUC = 21;

    /**
     * 插件交易失败
     */
    public static final int PAYMENT_RESULT_FAIL = 22;

    /**
     * 插件交易不确定
     */
    public static final int PAYMENT_RESULT_UNSURE = 23;

    /**
     * 网络异常
     */
    public static final int PAYMENT_NET_ERROR = 24;

    /**
     * 交易中断
     */
    public static final int PAYMENT_OUTAGE = 25;

}
