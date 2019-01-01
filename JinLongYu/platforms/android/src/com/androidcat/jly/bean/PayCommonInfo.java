package com.androidcat.jly.bean;

import android.content.ContentValues;

import com.androidcat.jly.application.AppConstants;
import com.androidcat.jly.utils.log.LogU;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @ClassName PayCommonInfo
 * @Description 通讯相关全局变量类
 * @author xuxiang
 * @date 2014-08-18
 */
public class PayCommonInfo implements Serializable{

	public static String referCode="";  //交易参考号

	public static String transDate="";  //交易日期

	public static String certnum="";  //交易凭证号

	public static String respCode="";  //交易撤销的应答码

	public static String rspCode="";  //POS签到的返回码

	public static String numberDataFor21 = "";//21号文

	public static void setNumberDataFor21(String numberDataFor21) {
		PayCommonInfo.numberDataFor21 = numberDataFor21;
	}

	public static void setRspCode(String rspCode) {
	    PayCommonInfo.rspCode = rspCode;
	}

    public static void setReferCode(String referCode) {
    	PayCommonInfo.referCode = referCode;
    }

    public static void setTransDate(String transDate) {
    	PayCommonInfo.transDate = transDate;
    }

    public static void setCertnum(String certnum) {
    	PayCommonInfo.certnum = certnum;
    }

    public static void setRespCode(String respCode) {
    	PayCommonInfo.respCode = respCode;
    }

	private static final long serialVersionUID = -5488574343810095175L;

	/**
	 * 插件入口时间戳
	 */
	public static String pluginKeyTime = "";

	/**
	 * 插件入口签名
	 */
	public static String pluginKeySign = "";

	/**
	 * 会话ID
	 */
	public static String sessionId = "";

	/**
	 * 持卡人姓名
	 */
	public static String cardHolder = "";

	/**
	 * 用户真名
	 */
	public static String userRealName="";
	/**
	 * 用户手机号
	 */
	public static String userPhone="";
	/**
	 * 用户密码
	 */
	public static String userPsd="";
	/**
	 * 登录账号
	 */
	public static String account="";
	/**
	 * 用户证件类型
	 */
	public static String certType="";
	/**
	 * 用户身份证号 证件号码
	 */
	public static String certNo="";

	/**
	 * 地区代码[N6]
	 */
	public static String areaCode = "";

	/**
	 * 终端信息[AN..255(LLVAR)]
	 */
	public static String terminalInfo = "";

	/**
	 * 设备类型[N4]
	 */
	public static String deviceType = "";

	/**
	 * 商户可支持的设备类型
	 */
	public static ArrayList<String> deviceTypeList = new ArrayList<String>();

	/**
	 * 商户通道可支持的设备类型
	 */
	public static ArrayList<String> channelDeviceTypeList = new ArrayList<String>();

	/**
	 * 设备SN[AN16]
	 */
	public static String sn = "";

	/**
	 * 设备PN[AN..20(LLVAR)]
	 */
	public static String pn = "";

	/**
	 * 用户ID[N16]
	 */
	public static String userId = "";

	/**
	 * 批次号[N8]
	 */
	public static String batchNo = "";

	/**
	 * IC卡数据域[AN..999(LLLVAR)]
	 */
	public static String icCardData = "";

	/**
	 * 商户ID[AN16]
	 */
	public static String merchantId = "";

	public static String tempMerId = "";

	/**
	 * 商户号
	 */
	public static String merchantCode = "";

	/**
	 * 商户名称
	 */
	public static String merchantName = "";

	/**
	 * 分销商ID，商户切换时可能用到
	 */
	public static String subMerchantId = "";

	/**
	 * 商城ID[AN16]
	 */
	public static String mallId = "";

	/**
	 * 自定义域[AN..999(LLLVAR)]
	 */
	public static String customDomin60 = "";

	/**
	 * 刷卡数据 刷卡得到的所有数据放入48域
	 */
	public static String swipeResult = "";

	/**
	 * 交易信息载体类型[N2]
	 * 磁条卡    00
	 * IC卡	  01
	 */
	public static String vectorType = "";

	/**
	 * 终端应用代码[N6]
	 */
	public static String appCode = "";

	/**
	 * 交易代码[N10]
	 */
	public static String transCode = "";

	/**
	 * 通道类型[AN4]
	 */
	public static String channelType = "";

	/**
	 * 交易金额[N12] 单位分
	 */
	public static String transMoney = "";

	/**
	 * 随机数 16位
	 */
	public static String randomNumber = "";

	/**
	 * 交易卡类型，2个字节的ASC码
	 * 00  ->  不区分
	 * 01  ->  借记卡
	 * 02  ->  贷记卡
	 * 03  ->  准贷记卡
	 * 04  ->  预付费卡
	 */
	public static String cardType = AppConstants.CARD_ALL;

	/**
	 * 第三方订单号[N20]
	 */
	public static String thirdOrderNo = "";

	/**
	 * 订单编号，插件中微信支付查询订单状态时使用
	 */
	public static String orderId = "";

	/**
	 * 插件中支付成功通知url地址
	 */
	public static String callBackUrl = "";

	/**
	 * 交易时间[n14(yyyyMMDDHHmmss)]
	 */
	public static String transTime = "";

	/**
	 * 主账号[N..19(LLVAR)]
	 */
	public static String mainAccount = "";

	/**
	 * 其它金额(手续费)，12个字节的ASC码
	 */
	public static String otherMoney = "0";

	/**
	 * 计算手续费之前的交易金额,每次计算手续费都用这个变量
	 */
	public static String transMoneyBeforeCal = "";

	/**
	 * 银行卡密码[N..19(LLVAR)]
	 */
	public static String bankcardPassword = "";

	/**
	 * 附加数据[AN..999(LLLVAR)]
	 */
	public static String additionData = "";

	/**
	 * 订单号[N8]
	 */
	public static String orderNo = "";

	/**
	 * 原始交易时间[n14(yyyyMMDDHHmmss)]
	 */
	public static String originalTransTime = "";

	/**
	 * 交易凭证号[N..20(LLVAR)]
	 */
	public static String certifNo = "";

	/**
	 * 原始交易流水号[N20]
	 */
	public static String originalTransNo = "";

	/**
	 * 原始订单号
	 */
	public static String origQryId ="";

	/**
	 * 管理员账号[AN..40(LLVAR)]
	 */
	public static String adminAccount = "";

	/**
	 * 管理员密码[AN..40(LLVAR)]
	 */
	public static String adminPassword = "";

	/**
	 * 设置商户类型
	 * 0 表示绑卡支付商户； 1 表示刷卡支付商户
	 */
	public static String payType = "";

	/**
	 * 绑卡支付商户
	 */
	private static final String BINDCARD_PAY_TYPE = "0";

	/**
	 * 刷卡支付商户
	 */
	private static final String SWIPECARD_PAY_TYPE = "1";

	/**
	 * 是否签名 或者 打印
	 * 10 -> 需要打印或者签名
	 * 00 -> 不需要
	 * 默认需要打印或者签名
	 */
	public static String voucherType = "00";

	/**
	 * 不需要打印、不需要签名
	 */
	private static final String PRINT_NO_SIGN_NO = "00";

	/**
	 * 需要打印或者签名
	 */
	private static final String PRINT_OR_SIGN = "10";

	/**
	 * 支付前签名
	 */
	private static final String SIGN_BEFORE_PAY = "20";

	/**
	 * 通用签名（JS调起）
	 */
	private static final String GENERAL_SIGN = "30";

	/**
	 * 只存在签名
	 */
	private static final String SIGN_ONLY = "40";

	/**
	 * 只存在打印
	 */
	private static final String PRINT_ONLY = "50";

	/**
	 * 插件调起的通用签名
	 */
	private static final String PLUGIN_GENERAL_SIGN = "60";

	/**
	 * 打印方式 ： 0 -> 热敏打印  1 -> 针式打印 2 -> 一体机打印
	 */
	public static String printType = "2";

	/**
	 * 网点ID
	 */
	public static String serviceId = "";

	/**
	 * 网点名称
	 */
	public static String serviceName = "";

	/**
	 * 设备名称
	 */
	public static String deviceName = "";

	/**
	 * 设备连接方式
	 * 0：有线    1：蓝牙
	 */
	public static String connectMode = "";

	/**
	 * 设备蓝牙地址
	 */
	public static String bluetoothMac = "";

	/**
	 * 设备蓝牙名称
	 */
	public static String bluetoothName = "";

	/**
	 * <Strong><font color="red">48域的数据，由于48域是附加数据域，数据结构，数据类型不确定，所以用ContentValues格式。
	 * cv48 48域的键值,如下：</font></Strong><p/>
	 * cityCode ：城市代码。
	 * cardNo: 卡号  <br/>
	 * cardType:卡类型 <br/>
	 * terminalNo：终端机编号(11位手机+8)<br/>
	 * dealNum：交易金额,单位分 <br/>
	 * cardDealNo：卡交易序号 <br/>
	 * cardBalance：卡片消费前余额,单位分 <br/>
	 * dealTime：交易日期时间 (yyyyMMddHHmmss)<br/>
	 * random：随机数 <br/>
	 * @see ContentValues
	 */
	public static ContentValues cv48 = new ContentValues();

	/**
	 * 设备是否为软键盘
	 */
	public static boolean isSoftKeyboard = false;

	/**
	 * 消费 还是 撤销
	 * 0 消费
	 * 1 撤销
	 */
	public static String payORDiscard = "";

	/**
	 * webview入口url
	 */
	public static String entryUrl = "";

	/**
	 * sp
	 */
	public static String sp = "";

	/**
	 * 是否用户承担手续费
	 * 1 -> 用户承担手续费
	 * 0 -> 商户承担手续费
	 * 默认商户承担手续费
	 */
	public static String isUserPayFee = "0";

	/**
	 * 用户承担手续费
	 */
	public static final String USER_PAY_FEE = "1";

	/**
	 * 商户承担手续费
	 */
	public static final String MERCHANT_PAY_FEE = "0";

	/**
	 * 区分借贷记卡
	 */
	public static final String DIFF_CARD = "0";

	/**
	 * 是否区分借贷记卡
	 * 非0 -> 不区分
	 * 0 -> 区分
	 * 默认不区分
	 */
	public static String isDiffCard = "1";

	/**
	 * 业务平台进入底座的操作类型
	 * 0 -> 余额查询
	 * 1 -> 付款
	 * 2 -> 补打印
	 */
	public static int operaType = -1;

	/**
	 * 启动银联正式环境
	 */
	public static final String UPMP = "00";

	/**
	 * 启动银联测试环境
	 */
	public static final String UPMP_TEST = "01";
	/**
	 * 银联环境  默认测试环境
	 * 00 -> 银联正式环境
	 * 01 -> 银联测试环境
	 */
	public static String upmpMode = UPMP_TEST;

	/**
	 * POP代扣—用户发起代扣 签约ID
	 */
	public static String signID = "";

	/**
	 * POP代扣—用户发起代扣  手机验证码
	 */
	public static String phoneValidCode = "";

	/**
	 * 通联卡卡转账—付款人账号
	 */
	public static String payAccount = "";

	/**
	 * 通联卡卡转账—收款人账号
	 */
	public static String receiveNo = "";

	/**
	 * 通联卡卡转账—收款人姓名
	 */
	public static String receiveName = "";

	/**
	 * 银行卡卡片有效期
	 */
	public static String cardLife = "";

	/**
	 * 银行卡卡片序列号
	 */
	public static String cardSerialNumber = "";

	/**
	 * 白名单银行卡列表[AN..ffff(LLLLVAR)]
	 */
	public static String bankcardWhiteList = "";

	/**
	 * 验签数据
	 */
	public static String verifySign = "";

	/**
	 * 扩展字段
	 */
	public static String extendField = "";

	/**
	 * 江苏九州通扩展数据
	 */
	public static String JZTExtendData = "";

	/**
	 * 通用打印数据（JS调起）
	 */
	public static String generalPrintString = "";

	/**
	 * 快捷支付是否账户实名[N1]
	 * 0 -> 未实名
	 * 1 -> 已实名
	 */
	public static String accountRealName = "0";

	/**
	 * 登录时从bsss拿到，协议接口中的一个参数
	 */
	public static String spDev = "07";

	/**
	 * 区分收款或付款（插件用到）
	 */
	public static String orderType = "";

	/**
	 * 用户自定义字段（插件用到）
	 */
	public static String customData = "";


	/**
	 * 设置OrderId
	 * @param orderId
	 */
	public static void setOrderId(String orderId){
		PayCommonInfo.orderId = orderId;
	}

	/**
	 * 设置地区代码
	 * @param areaCode
	 */
	public static void setAreaCode(String areaCode){
		PayCommonInfo.areaCode = areaCode;
	}

	/**
	 * 设置终端信息
	 * @param terminalInfo
	 */
	public static void setTerminalInfo(String terminalInfo){
		PayCommonInfo.terminalInfo = terminalInfo;
	}
	/**
	 * 设置登录账号
	 * @param account
	 */
	public static void setAccount(String account) {
		PayCommonInfo.account = account;
	}

	/**
	 * 设置设备类型
	 * @param deviceType
	 */
	public static void setDeviceType(String deviceType){
		PayCommonInfo.deviceType = deviceType;
	}

	/**
	 * 设备SN[AN16]
	 * @param sn
	 */
	public static void setSN(String sn){
		PayCommonInfo.sn = sn;
	}

	/**
	 * 设备PN[AN..20(LLVAR)]
	 * @param pn
	 */
	public static void setPN(String pn){
		PayCommonInfo.pn = pn;
	}

	/**
	 * 用户ID[N16]
	 * @param userId
	 */
	public static void setUserId(String userId){
		PayCommonInfo.userId = userId;
	}

	/**
	 * 批次号[N8]
	 * @param batchNo
	 */
	public static void setBatchNo(String batchNo){
		PayCommonInfo.batchNo = batchNo;
		LogU.d("PayCommonInfo", batchNo);
	}

	/**
	 * IC卡数据域[AN..999(LLLVAR)]
	 * @param icCardData
	 */
	public static void setIcCardData(String icCardData){
		PayCommonInfo.icCardData = icCardData;
	}

	/**
	 * 商户ID[AN16]
	 * @param merchantId
	 */
	public static void setMerchantId(String merchantId){
		PayCommonInfo.merchantId = merchantId;
	}

	/**
	 * 商户名称
	 * @param merchantName
	 */
	public static void setMerchantName(String merchantName){
		PayCommonInfo.merchantName = merchantName;
	}

	/**
	 * 设置分销商ID
	 * @param subMerchantId
	 */
	public static void setSubMerchantId(String subMerchantId){
		PayCommonInfo.subMerchantId = subMerchantId;
	}

	/**
	 * 商城ID[AN16]
	 * @param mallId
	 */
	public static void setMallId(String mallId){
		PayCommonInfo.mallId = mallId;
	}

	/**
	 * 自定义域[AN..999(LLLVAR)]
	 * @param customDomin60
	 */
	public static void setCustomDomin60(String customDomin60){
		PayCommonInfo.customDomin60 = customDomin60;
	}

	/**
	 * 刷卡数据 刷卡得到的所有数据放入48域
	 * @param swipeResult
	 */
	public static void setSwipeResult(String swipeResult){
		PayCommonInfo.swipeResult = swipeResult;
	}

	/**
	 * 交易信息载体类型[N2]
	 * @param vectorType
	 */
	public static void setVectorType(String vectorType){
		PayCommonInfo.vectorType = vectorType;
	}

	/**
	 * 终端应用代码[N6]
	 * @param appCode
	 */
	public static void setAppCode(String appCode){
		PayCommonInfo.appCode = appCode;
	}

	/**
	 * 交易代码[N10]
	 * @param transCode
	 */
	public static void setTransCode(String transCode){
		PayCommonInfo.transCode = transCode;
	}

	/**
	 * 通道类型[AN4]
	 * 0000 付款绑卡 0001付款刷卡 0002收款刷卡
	 * @param channelType
	 */
	public static void setChannelType(String channelType){
		PayCommonInfo.channelType = channelType;
	}

	/**
	 * 随机数 16位
	 * @param randomNumber
	 */
	public static void setRandomNumber(String randomNumber){
		PayCommonInfo.randomNumber = randomNumber;
	}

	/**
	 * 交易卡类型，2个字节的ASC码
	 * 00 ： 借记卡
	 * 01  ： 贷记卡
	 * 02：准贷记卡
	 * 03：预付费卡
	 * @param cardType
	 */
	public static void setCardType(String cardType){
		PayCommonInfo.cardType = cardType;
	}

	/**
	 * 交易金额[N12]
	 * @param transMoney
	 */
	public static void setTransMoney(String transMoney){
		PayCommonInfo.transMoney = transMoney;
	}

	/**
	 * 第三方订单号[N20]
	 * @param thirdOrderNo
	 */
	public static void setThirdOrderNo(String thirdOrderNo){
		PayCommonInfo.thirdOrderNo = thirdOrderNo;
	}

	/**
	 * 交易时间[n14(yyyyMMDDHHmmss)]
	 * @param transTime
	 */
	public static void setTransTime(String transTime){
		PayCommonInfo.transTime = transTime;
	}

	/**
	 * 主账号[N..19(LLVAR)]
	 * @param mainAccount
	 */
	public static void setMainAccount(String mainAccount){
		PayCommonInfo.mainAccount = mainAccount;
	}

	/**
	 * 设置其它金额(手续费)，12个字节的ASC码
	 * @param otherMoney
	 */
	public static void setOtherMoney(String otherMoney){
		PayCommonInfo.otherMoney = otherMoney;
	}

	/**
	 * 银行卡密码[N..19(LLVAR)]
	 * @param bankcardPassword
	 */
	public static void setBankcardPassword(String bankcardPassword){
		PayCommonInfo.bankcardPassword = bankcardPassword;
	}

	/**
	 * 附加数据[AN..999(LLLVAR)]
	 * @param additionData
	 */
	public static void setAdditionData(String additionData){
		PayCommonInfo.additionData = additionData;
	}

	/**
	 * 订单号[N8]
	 * @param orderNo
	 */
	public static void setOrderNo(String orderNo){
		PayCommonInfo.orderNo = orderNo;
	}

	/**
	 * 原始交易时间[n14(yyyyMMDDHHmmss)]
	 * @param originalTransTime
	 */
	public static void setOriginalTransTime(String originalTransTime){
		PayCommonInfo.originalTransTime = originalTransTime;
	}

	/**
	 * 交易凭证号[N..20(LLVAR)]
	 * @param certifNo
	 */
	public static void setCertifNo(String certifNo){
		PayCommonInfo.certifNo = certifNo;
	}

	/**
	 * 原始交易流水号[N20]
	 * @param originalTransNo
	 */
	public static void setOriginalTransNo(String originalTransNo){
		PayCommonInfo.originalTransNo = originalTransNo;
	}

	/**
	 * 管理员账号[AN..40(LLVAR)]
	 * @param adminAccount
	 */
	public static void setAdminAccount(String adminAccount){
		PayCommonInfo.adminAccount = adminAccount;
	}

	/**
	 * 管理员密码[AN..40(LLVAR)]
	 * @param adminPassword
	 */
	public static void setAdminPassword(String adminPassword){
		PayCommonInfo.adminPassword = adminPassword;
	}

	/**
	 * 设置商户类型 	默认绑卡支付商户
	 * 0 表示绑卡支付商户； 1 表示刷卡支付商户
	 * @param payType
	 */
	public static void setPayType(String payType){
		PayCommonInfo.payType = payType;
	}

	/**
	 * 设置用户姓名
	 * @param realName
	 */
	public static void setUserRealName(String realName) {
		PayCommonInfo.userRealName = realName;
	}

	/**
	 * 设置用户手机号码
	 * @param phone
	 */
	public static void setUserPhone(String phone) {
		PayCommonInfo.userPhone = phone;
	}

	/**
	 * 设置用户登录密码
	 * @param psd
	 */
	public static void setUserPsd(String psd) {
		PayCommonInfo.userPsd = psd;
	}

	/**
	 * 设置是否需要签名 或者 打印
	 * 10 -> 需要打印或者签名
	 * 00 -> 不需要
	 * 30 -> 通用电子签名
	 * 20 -> 支付前签名
	 * @param voucherType
	 */
	public static void setVoucherType(String voucherType){
		PayCommonInfo.voucherType = voucherType;
	}

	/**
	 * 设置打印方式
	 * 0 -> 热敏打印
	 * 1 -> 针式打印
	 * 2 -> 一体机打印
	 * @param printType 打印方式
	 */
	public static void setPrintType(String printType){
		PayCommonInfo.printType = printType;
	}

	/**
	 * 商户可支持的余额查询设备类型
	 * @param deviceTypeList
	 */
	public static void setDeviceTypeList(ArrayList<String> deviceTypeList){
		PayCommonInfo.deviceTypeList = deviceTypeList;
	}

	/**
	 * 商户通道可支持的设备类型
	 * @param channelDeviceTypeList
	 */
	public static void setChannelDeviceTypeList(ArrayList<String> channelDeviceTypeList){
		PayCommonInfo.channelDeviceTypeList = channelDeviceTypeList;
	}

	/**
	 * 判断商户是否是绑卡支付商户
	 * @return
	 */
	public static boolean isBindcardPay(){
		return BINDCARD_PAY_TYPE.equals(PayCommonInfo.payType);
	}

	/**
	 * 判断商户是否是刷卡支付商户
	 * @return
	 */
	public static boolean isSwipecardPay(){
		return SWIPECARD_PAY_TYPE.equals(PayCommonInfo.payType);
	}

	/**
	 * 只存在签名
	 * @return
	 */
	public static boolean isSignOnly() {
		return SIGN_ONLY.equals(voucherType);
	}

	/**
	 * 只存在打印
	 * @return
	 */
	public static boolean isPrintOnly() {
		return PRINT_ONLY.equals(voucherType);
	}

	/**
	 * 不需要打印、不需要签名
	 */
	public static boolean isPrintnoSignno(){
		return PRINT_NO_SIGN_NO.equals(voucherType);
	}

	/**
	 * 需要打印或者签名
	 */
	public static boolean isPrintORSign(){
		return PRINT_OR_SIGN.equals(voucherType);
	}

	/**
	 * 支付前签名
	 */
	public static boolean isSignBeforePay(){
		return SIGN_BEFORE_PAY.equals(voucherType);
	}

	/**
	 * 是否是通用电子签名（JS调起）
	 * @return
	 */
	public static boolean isGeneralSign() {
		return GENERAL_SIGN.equals(voucherType);
	}

	/**
	 * 是否是插件调起的通用签名（插件微信、插件支付宝交易）
	 * @return
	 */
	public static boolean isPluginGeneralSign(){
		return PLUGIN_GENERAL_SIGN.equals(voucherType);
	}

	/**
	 * 设置网点ID
	 * @param serviceId 网点ID
	 */
	public static void setServiceId(String serviceId){
		PayCommonInfo.serviceId = serviceId;
	}

	/**
	 * 设置网点名称
	 * @param serviceName 网点名称
	 */
	public static void setServiceName(String serviceName){
		PayCommonInfo.serviceName = serviceName;
	}

	/**
	 * 设置设备名称
	 * @param deviceName 设备名称
	 */
	public static void setDeviceName(String deviceName){
		PayCommonInfo.deviceName = deviceName;
	}

	/**
	 * 设置设备连接方式
	 * @param connectMode 设备连接方式 0：有线    1：蓝牙
	 */
	public static void setConnectMode(String connectMode){
		PayCommonInfo.connectMode = connectMode;
	}

	/**
	 * 设置设备蓝牙地址
	 * @param bluetoothMac 设备蓝牙地址
	 */
	public static void setBluetoothMac(String bluetoothMac){
		PayCommonInfo.bluetoothMac = bluetoothMac;
	}

	/**
	 * 设置设备蓝牙名称
	 * @param bluetoothName 设备蓝牙名称
	 */
	public static void setBluetoothName(String bluetoothName){
		PayCommonInfo.bluetoothName = bluetoothName;
	}

	/**
	 * 设置设备是否为软键盘
	 * @param isSoftKeyboard 设备是否为软键盘
	 */
	public static void setIsSoftKeyboard(boolean isSoftKeyboard){
		PayCommonInfo.isSoftKeyboard = isSoftKeyboard;
	}

	/**
	 * 设置消费 还是 撤销
	 * @param payORDiscard 0 消费   1 撤销
	 */
	public static void setPayORDiscard(String payORDiscard){
		PayCommonInfo.payORDiscard = payORDiscard;
	}

	/**
	 * webview入口url
	 * @param entryUrl
	 */
	public static void setEntryUrl(String entryUrl){
		PayCommonInfo.entryUrl = entryUrl;
	}

	/**
	 * 设置用户证件类型
	 * @param certType  用户证件类型
	 */
	public static void setCertType(String certType){
		PayCommonInfo.certType = certType;
	}

	/**
	 * 设置用户身份证号 证件号码
	 * @param certNo  用户身份证号  证件号码
	 */
	public static void setCertNo(String certNo){
		PayCommonInfo.certNo = certNo;
	}

	/**
	 * 设置sp
	 * @param sp
	 */
	public static void setSp(String sp){
		PayCommonInfo.sp = sp;
	}

	/**
	 * 设置由谁承担手续费
	 * @param isUserPayFee
	 */
	public static void setIsUserPayFee(String isUserPayFee){
		PayCommonInfo.isUserPayFee = isUserPayFee;
	}

	/**
	 * 获取是否用户承担手续费
	 * @return
	 */
	public static boolean isUserPayFee(){
		return PayCommonInfo.USER_PAY_FEE.equals(PayCommonInfo.isUserPayFee);
	}

	/**
	 * 获取是否商户承担手续费
	 * @return
	 */
	public static boolean isMerchantPayFee(){
		return PayCommonInfo.MERCHANT_PAY_FEE.equals(PayCommonInfo.isUserPayFee);
	}

	/**
	 * 设置是否区分借贷记卡
	 * @param isDiffCard
	 */
	public static void setIsDiffCard(String isDiffCard){
		PayCommonInfo.isDiffCard = isDiffCard;
	}

	/**
	 * 区分借贷记卡
	 * @return
	 */
	public static boolean isDiffCard(){
		return PayCommonInfo.DIFF_CARD.equals(PayCommonInfo.isDiffCard);
	}

	/**
	 * 设置业务平台进入底座的操作类型
	 * 0 -> 余额查询
	 * 1 -> 付款
	 * 2 -> 补打印
	 * @param operaType
	 */
	public static void setOperaType(int operaType){
		PayCommonInfo.operaType = operaType;
	}

	/**
	 * 设置银联环境
	 * 在Application类onCreate()方法中设置
	 * 00 -> 银联正式环境
	 * 01 -> 银联测试环境
	 * @param upmpMode 银联环境
	 */
	public static void setUpmpMode(String upmpMode){
		PayCommonInfo.upmpMode = upmpMode;
	}

	/**
	 * 设置UPOP代扣—用户发起代扣 签约ID
	 * @param signID POP代扣—用户发起代扣 签约ID
	 */
	public static void setSignID(String signID){
		PayCommonInfo.signID = signID;
	}

	/**
	 * 设置UPOP代扣—用户发起代扣  手机验证码
	 * @param phoneValidCode POP代扣—用户发起代扣  手机验证码
	 */
	public static void setPhoneValidCode(String phoneValidCode){
		PayCommonInfo.phoneValidCode = phoneValidCode;
	}

	/**
	 * 设置通联卡卡转账—付款人账号
	 * @param payAccount 通联卡卡转账—付款人账号
	 */
	public static void setPayAccount(String payAccount){
		PayCommonInfo.payAccount = payAccount;
	}

	/**
	 * 设置通联卡卡转账—收款人账号
	 * @param receiveNo 通联卡卡转账—收款人账号
	 */
	public static void setReceiveNo(String receiveNo){
		PayCommonInfo.receiveNo = receiveNo;
	}

	/**
	 * 设置通联卡卡转账—收款人姓名
	 * @param receiveName 通联卡卡转账—收款人姓名
	 */
	public static void setReceiveName(String receiveName){
		PayCommonInfo.receiveName = receiveName;
	}

	/**
	 * 设置银行卡卡片有效期
	 * @param cardLife 银行卡卡片有效期
	 */
	public static void setCardLife(String cardLife){
		PayCommonInfo.cardLife = cardLife;
	}

	/**
	 * 设置银行卡卡片序列号
	 * @param cardSerialNumber 银行卡卡片序列号
	 */
	public static void setCardSerialNumber(String cardSerialNumber){
		PayCommonInfo.cardSerialNumber = cardSerialNumber;
	}

	/**
	 * 设置白名单银行卡列表
	 * @param bankcardWhiteList 白名单银行卡列表 [AN..ffff(LLLLVAR)]
	 */
	public static void setBankcardWhiteList(String bankcardWhiteList){
		PayCommonInfo.bankcardWhiteList = bankcardWhiteList;
	}

	/**
	 * 设置验签数据
	 * @param verifySign
	 */
	public static void setVerifySign(String verifySign) {
		PayCommonInfo.verifySign = verifySign;
	}

	/**
	 * 设置扩展字段
	 * @param extendField
	 */
	public static void setextendField(String extendField) {
		PayCommonInfo.extendField = extendField;
	}

	public static void setSessionId(String sessionId) {
		PayCommonInfo.sessionId = sessionId;
	}

	/**
	 * 账户是否实名
	 * @param accountRealName [N1]
	 */
	public static void setAccountRealName(String accountRealName) {
		PayCommonInfo.accountRealName = accountRealName;
	}

	public static void setMerchantCode(String merchantCode) {
		PayCommonInfo.merchantCode = merchantCode;
	}

	public static void setJZTExtendData(String jZTExtendData) {
		JZTExtendData = jZTExtendData;
	}

	public static void setOrigQryId(String origQryId) {
		PayCommonInfo.origQryId = origQryId;
	}

	/**
	 * 插件中支付成功通知url地址
	 * @param callBackUrl
	 */
	public static void setCallBackUrl(String callBackUrl) {
		PayCommonInfo.callBackUrl = callBackUrl;
	}

	/**
	 * 通用打印数据（JS调起）
	 * @param generalPrintString
	 */
	public static void setGeneralPrintString(String generalPrintString) {
		PayCommonInfo.generalPrintString = generalPrintString;
	}
	/**
	 * 区分收款或付款（插件用到）
	 */
	public static void setOrderType(String orderType) {
		PayCommonInfo.orderType = orderType;
	}

	/**
	 * 用户自定义字段（插件用到）
	 * @param customData
	 */
	public static void setCustomData(String customData) {
		PayCommonInfo.customData = customData;
	}

}
