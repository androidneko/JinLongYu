package com.androidcat.jly.device;

public class DeviceData {

	public static final String SUCCESS = "9000";


	//* * * * * * * * * * * device * * * * * * * * * * * *//
	/**
	 * 大多惠拉
	 */
	public static final String EQUI_DOVILA = "0001";

	/**
	 * 艾创 -- 小盒子
	 */
	public static final String EQUI_AC = "0002";

	/**
	 * 金脚艾创
	 */
	public static final String EQUI_GOLD_AC = "0003";

	/**
	 * BBPOS
	 */
	public static final String EQUI_BBPOS = "0004";

	/**
	 * ME10
	 */
	public static final String EQUI_ME10 = "0005";

	/**
	 * NFC设备
	 */
	public static final String EQUI_NFC = "0006";



	/**
	 * 收款多惠拉（非接）
	 */
	public static final String EQUI_BLACK_DOVILA = "1001";

	/**
	 * 天喻--supperPay
	 */
	public static final String EQUI_SUPPERPAY = "1002";

	/**
	 * 艾创--点付宝
	 */
	public static final String EQUI_DFB = "1003";

	/**
	 * 新大陆--ME30
	 */
	public static final String EQUI_ME30 = "1004";

	/**
	 * 艾创--一体机
	 */
	public static final String EQUI_YITIJI = "1005";

	/**
	 * 新大陆--ME31
	 */
	public static final String EQUI_ME31 = "1006";

	/**
	 * 联迪--M35
	 */
	public static final String EQUI_LANDI_M35 = "1007";

	/**
	 * 联迪--M36
	 */
	public static final String EQUI_LANDI_M36 = "1008";

	/**
	 * 天喻--71241
	 */
	public static final String EQUI_71241 = "1009";

	/**
	 * 百富--D180
	 */
	public static final String EQUI_PAX_D180 = "1010";

	/**
	 * 联迪APOS A8
	 */
	public static final String EQUI_LANDI_A8 = "1011";


	/**
	 * 新大陆N900
	 */
	public static final String EQUI_N900 = "1101";

	/**
	 * 拉卡拉APOS A8
	 */
	public static final String EQUI_LAKALA_A8 = "1012";

	/**
	 * 银联云POS A8
	 */
	public static final String EQUI_UNIONPAY_CLOUD_A8 = "1013";

	/**
	 * 升腾云POS
	 */
	public static final String EQUI_V8 = "1014";

	/**
	 * 银联云POS V8
	 */
	public static final String EQUI_UNIONPAY_CLOUD_V8 = "1015";

	/**
	 * 商米P1
	 */
	public static final String EQUI_SUNMI_P1 = "1016";

	//* * * * * * * * * * card type * * * * * * * * * * //
	/**
	 * 磁条卡
	 */
	public static final String MAGNETIC_CARD = "00";

	/**
	 * IC卡
	 */
	public static final String IC_CARD = "01";

	/**
	 * RF非接卡
	 */
	public static final String RF_CARD = "02";


	//* * * * * * * * * * check device * * * * * * * * * * //
	/**
	 * 设备检测成功
	 */
	public static final int CHECK_SUCCESS = 0;

	/**
	 * 设备检测失败
	 */
	public static final int CHECK_FAILURE = 1;


	//* * * * * * * * * * read card * * * * * * * * * * //
	/**
	 * 读卡操作成功
	 */
	public static final int READ_CARD_SUCCESS = 0;

	/**
	 * 读卡操作失败
	 */
	public static final int READ_CARD_FAILURE = 1;

	/**
	 * 重新读卡
	 */
	public static final int RESTART = 2;

	/**
	 * 延迟查询
	 */
	public static final int QUERYORDER_DELAYED = 11;

	/**
	 * 获取SN(PN)成功
	 */
	public static final int GET_SN_SUCCESS = 3;

	//* * * * * * * * * * abnormal operation * * * * * * * * * * //
	/**
	 * 取消
	 */
	public static final int CANCEL = 4;

	/**
	 * 移除设备
	 */
	public static final int REMOVE = 5;


	//* * * * * * * * * * check in * * * * * * * * * * //
	/**
	 * 签到成功
	 */
	public static final int CHECK_IN_SUCCESS = 6;

	/**
	 * 签到失败
	 */
	public static final int CHECK_IN_ERR = 7;

	//* * * * * * * * * * transfor * * * * * * * * * * //
	/**
	 * 交易成功
	 */
	public static final int TRANS_SUCCESS = 8;

	/**
	 * 交易失败
	 */
	public static final int TRANS_FAIL = 9;

	/**
	 * 设备连接失败，重新选择设备
	 */
	public static final int SELECT_AGAIN = 10;

	/**
	 * 网络连接失败
	 */
	public static final int NET_CONNECTION_FAIL = 12;


	/**
	 * 取消
	 */
	public static final int CANCELPBOC = 13;

	//* * * * * * * * * transaction type * * * * * * * * * //
	/**
	 * 消费
	 */
	public static final int TYPE_TRANSACTION = 0;

	/**
	 * 余额查询
	 */
	public static final int TYPE_BALANCE = 1;

	/**
	 * 刷卡取号
	 */
	public static final int TYPE_GET_CARD_NO = 2;

	/**
	 * 撤销交易操作
	 */
	public static final int TYPE_UNDO = 3;

	/**
	 * 第三方订单支付
	 */
	public static final int TYPE_ORDER_PAY = 4;

	/**
	 * 信用卡还款
	 */
	public static final int TYPE_CREDIT_PAY = 5;

	/**
	 * 多渠道水费
	 */
	public static final int TYPE_MULTICHANNEL_WATER = 6;

	/**
	 * 多渠道电费
	 */
	public static final int TYPE_MULTICHANNEL_ELECTRICITY = 7;


	//* * * * * * * * * * connect mode * * * * * * * * * * //
	/**
	 * 音频设备
	 */
	public static final int AUDIO_EQUIOMENT = 0;

	/**
	 * 蓝牙设备
	 */
	public static final int BLUETOOTH_EQUIPMENT = 1;

	/**
	 * 复合设备（有线/蓝牙）
	 */
	public static final int COMPOSITE_EQUIPMENT = 2;

	/**
	 * NFC既不是音频设备也不是蓝牙设备
	 */
	public static final int NORMAL_EQUIPMENT = 3;
}
