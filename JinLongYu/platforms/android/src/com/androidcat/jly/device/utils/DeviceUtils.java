package com.androidcat.jly.device.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.androidcat.jly.bean.PayCommonInfo;
import com.androidcat.jly.device.DeviceData;
import com.androidcat.jly.device.DeviceInfo;
import com.androidcat.jly.utils.AppUtils;
import com.androidcat.jly.utils.log.LogU;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 设备工具类
 * @author wj
 *
 */
public class DeviceUtils {

	public static boolean hasDevice = false;// 是否插入设备

	public static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	/**
	 * 开启蓝牙适配
	 */
	public static void openBluetoothAdapter() {
		try{
			if(!bluetoothAdapter.isEnabled()){
				bluetoothAdapter.enable();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static List<HashMap<String, Object>> getDeviceList(Context mContext, List<String> list) {
		List<HashMap<String, Object>> equipmentList = new ArrayList<HashMap<String, Object>>();
		for(int i = 0 ; i < list.size() ; i++){
			HashMap<String, Object> equipmentMap = new HashMap<String, Object>();
			/*if(list.get(i).equals(DeviceData.EQUI_DOVILA)) {
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.tianyu_dovila));
				equipmentMap.put("IMG", R.drawable.equi_dovila);
				equipmentMap.put("SOFT_KEYBOARD", true);
				equipmentMap.put("CONNECTION_MODE", DeviceData.AUDIO_EQUIOMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_AC)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.itron_ac));
				equipmentMap.put("IMG", R.drawable.equi_ac);
				equipmentMap.put("SOFT_KEYBOARD", true);
				equipmentMap.put("CONNECTION_MODE", DeviceData.AUDIO_EQUIOMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_GOLD_AC)){

			} else if(list.get(i).equals(DeviceData.EQUI_BBPOS)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.bbpos));
				equipmentMap.put("IMG", R.drawable.equi_bbpos);
				equipmentMap.put("SOFT_KEYBOARD", true);
				equipmentMap.put("CONNECTION_MODE", DeviceData.AUDIO_EQUIOMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_ME10)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.newland_lakala));
				equipmentMap.put("IMG", R.drawable.equi_lakala);
				equipmentMap.put("SOFT_KEYBOARD", true);
				equipmentMap.put("CONNECTION_MODE", DeviceData.AUDIO_EQUIOMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_BLACK_DOVILA)){

			} else if(list.get(i).equals(DeviceData.EQUI_SUPPERPAY)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.tianyu_supperpay));
				equipmentMap.put("IMG", R.drawable.equi_supperpay);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.AUDIO_EQUIOMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_DFB)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.itron_dianfubao));
				equipmentMap.put("IMG", R.drawable.equi_itron);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.AUDIO_EQUIOMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_ME30)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.newland_me30));
				equipmentMap.put("IMG", R.drawable.equi_me30);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.BLUETOOTH_EQUIPMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_YITIJI)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.itron_yitiji));
				equipmentMap.put("IMG", R.drawable.equi_yitiji);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.AUDIO_EQUIOMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_ME31)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.newland_me31));
				equipmentMap.put("IMG", R.drawable.equi_me31);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.BLUETOOTH_EQUIPMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_LANDI_M35)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.landi_m35));
				equipmentMap.put("IMG", R.drawable.equi_landi_m35);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.BLUETOOTH_EQUIPMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_LANDI_M36)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.landi_m36));
				equipmentMap.put("IMG", R.drawable.equi_landi_m36);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.BLUETOOTH_EQUIPMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_NFC)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.nfc));
				equipmentMap.put("IMG", R.drawable.equi_landi_nfc);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.NORMAL_EQUIPMENT);
			} else if(list.get(i).equals(DeviceData.EQUI_71241)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.tianyu_71241));
				equipmentMap.put("IMG", R.drawable.equi_suppay_2);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.BLUETOOTH_EQUIPMENT);
			}  else if(list.get(i).equals(DeviceData.EQUI_PAX_D180)){
				equipmentMap.put("NUM", list.get(i));
				equipmentMap.put("NAME", mContext.getResources().getString(R.string.pax_d180));
				equipmentMap.put("IMG", R.drawable.equi_d180);
				equipmentMap.put("SOFT_KEYBOARD", false);
				equipmentMap.put("CONNECTION_MODE", DeviceData.BLUETOOTH_EQUIPMENT);
			} */

			String num = (String)equipmentMap.get("NUM");
			if (!TextUtils.isEmpty(num)){
				equipmentList.add(equipmentMap);
			}

		}
		return equipmentList;
	}

	public static DeviceInfo getEuipmentInfo(Context mContext, String deviceId, String connectMode, String name, String bluetoothMAC){
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setDeviceId(deviceId);
		if(!TextUtils.isEmpty(connectMode)) {
			deviceInfo.setConnectionMode(connectMode);
		}

		if(!TextUtils.isEmpty(name)) {
			deviceInfo.setDeviceName(name);
		}

		if(!TextUtils.isEmpty(bluetoothMAC)) {
			deviceInfo.setDeviceBluetoothMac(bluetoothMAC);
		}

		/*if(deviceId.equals(DeviceData.EQUI_DOVILA)) {
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.tianyu_dovila));
		} else if(deviceId.equals(DeviceData.EQUI_AC)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.itron_ac));
		} else if(deviceId.equals(DeviceData.EQUI_GOLD_AC)){

		} else if(deviceId.equals(DeviceData.EQUI_BBPOS)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.bbpos));
		} else if(deviceId.equals(DeviceData.EQUI_ME10)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.newland_lakala));
		} else if(deviceId.equals(DeviceData.EQUI_BLACK_DOVILA)){

		} else if(deviceId.equals(DeviceData.EQUI_SUPPERPAY)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.tianyu_supperpay));
		} else if(deviceId.equals(DeviceData.EQUI_DFB)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.itron_dianfubao));
		} else if(deviceId.equals(DeviceData.EQUI_ME30)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.newland_me30));
		} else if(deviceId.equals(DeviceData.EQUI_YITIJI)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.itron_yitiji));
		} else if(deviceId.equals(DeviceData.EQUI_ME31)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.newland_me31));
		} else if(deviceId.equals(DeviceData.EQUI_LANDI_M35)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.landi_m35));
		} else if(deviceId.equals(DeviceData.EQUI_LANDI_M36)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.landi_m36));
		} else if(deviceId.equals(DeviceData.EQUI_71241)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.tianyu_71241));
		} else if(deviceId.equals(DeviceData.EQUI_PAX_D180)){
			deviceInfo.setDeviceName(mContext.getResources().getString(R.string.pax_d180));
		} */
		return deviceInfo;
	}

	/**
	 * 填充卡数据+设备数据
	 * @param deviceType 设备类型
	 * @param cardInfo 卡数据
	 * @return
	 */
	public static void buildPayCommonInfoFromDevice(Context mContext, String deviceType, String random, JSONObject cardInfo){
		String mData = "";
		String transCardType = "";
		mData = cardInfo.optString("mData");
        transCardType = cardInfo.optString("type");

        if(!TextUtils.isEmpty(transCardType)){
        	PayCommonInfo.setVectorType(transCardType);
        	if(transCardType.equals(DeviceData.MAGNETIC_CARD)){
    			PayCommonInfo.setSwipeResult(mData);
            }else if(transCardType.equals(DeviceData.IC_CARD)){
            	PayCommonInfo.setIcCardData(mData);
            }
        }else {
        	if(!TextUtils.isEmpty(PayCommonInfo.vectorType)) {
        		if(PayCommonInfo.vectorType.equals(DeviceData.MAGNETIC_CARD)){
        			PayCommonInfo.setSwipeResult(mData);
                }else if(PayCommonInfo.vectorType.equals(DeviceData.IC_CARD)){
                	PayCommonInfo.setIcCardData(mData);
                }
        	}
        }

		PayCommonInfo.setDeviceType(deviceType);
		PayCommonInfo.setRandomNumber(random);

		String sn = DeviceUtils.getCSwiperSN(cardInfo.optString("sn"));
		String pn = cardInfo.optString("pn");

		if(TextUtils.isEmpty(pn)){
			pn = sn;
		}

		PayCommonInfo.setPN(pn);
		PayCommonInfo.setSN(sn);

		DeviceUtils.saveSnAndPn(mContext, sn, pn);
	}



	/**
	 * 写入DCData到下位机 -- IC卡
	 * @param result 银联返回报文
	 * @return true: 校验成功  false:校验失败
	 */
	public static void inutDcData(final Context mContext, String result, final String deviceId){
		if(DeviceData.IC_CARD.equals(PayCommonInfo.vectorType)){
			final String responseCode = ToolUtils.getValueKey("<responseCode>(.*?)</responseCode>", result);
			final String outDCData = ToolUtils.getValueKey("<DCData>(.*?)</DCData>", result);

			/*new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						if(DeviceData.EQUI_ME31.equals(deviceId) || DeviceData.EQUI_ME30.equals(deviceId)){
							NewlandDeviceManager.inputDCData(NewlandControllerImpl.getInstance(), outDCData, responseCode);
						} else if(DeviceData.EQUI_DFB.equals(deviceId) || DeviceData.EQUI_YITIJI.equals(deviceId)) {
							ItronControllerImpl.getInstance(mContext).inputDCData(outDCData, responseCode);
						} else if(DeviceData.EQUI_SUPPERPAY.equals(deviceId)) {
							ReadCardSupPay.getInstance(mContext).inputDCData(outDCData, responseCode);
						} else if(DeviceData.EQUI_LANDI_M35.equals(deviceId) || DeviceData.EQUI_LANDI_M36.equals(deviceId)) {
							CardUtilsLandi.inputDCData(LandiMPOSBlockImpl.getInstance(mContext), outDCData, responseCode);
							String result = CardUtilsLandi.PBOCStop(LandiMPOSBlockImpl.getInstance(mContext));
							if(!result.contentEquals("9000")){
				                LandiMPOSBlockImpl.getInstance(mContext).cancelTrade();
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						TraceLogUtil.logException(e, TraceLog.ActionLevel.WARNING, AppConfig.currentToken);
						LogU.d("readCard", "IC卡回写异常");
					}
				}
			}).start();	*/
		}
	}

	/**
 	 * 设备复位
 	 * @param deviceId
 	 */
 	public static void resetDevice(final Context mContext, final String deviceId) {
 		/*new Thread(new Runnable() {
			@Override
			public void run() {
				String apiOrder = "取消操作:";
				if(DeviceData.EQUI_ME31.equals(deviceId) || DeviceData.EQUI_ME30.equals(deviceId)){
					if(ConnUtils.getDeviceManager().getDevice() != null){
						apiOrder = apiOrder + "reset()";
						NewlandControllerImpl.getInstance().reset();
					}
				} else if(DeviceData.EQUI_DFB.equals(deviceId) || DeviceData.EQUI_YITIJI.equals(deviceId)) {
					ItronControllerImpl.getInstance(mContext).reset();
					ItronControllerImpl.getInstance(mContext).exit();
					apiOrder = apiOrder + "reset(),exit()";
				} else if(DeviceData.EQUI_SUPPERPAY.equals(deviceId)) {
					ReadCardSupPay.getInstance(mContext).exit();

					apiOrder = apiOrder + "exit()";

				} else if(DeviceData.EQUI_AC.equals(deviceId)) {

				} else if(DeviceData.EQUI_DOVILA.equals(deviceId)) {

				} else if(DeviceData.EQUI_BBPOS.equals(deviceId)) {

				} else if(DeviceData.EQUI_BLACK_DOVILA.equals(deviceId)) {

				} else if(DeviceData.EQUI_GOLD_AC.equals(deviceId)) {

				} else if(DeviceData.EQUI_ME10.equals(deviceId)) {

				}else if(DeviceData.EQUI_LANDI_M35.equals(deviceId) || DeviceData.EQUI_LANDI_M36.equals(deviceId)){
					if(LandiMPOSBlockImpl.getInstance(mContext) != null)
					LandiMPOSBlockImpl.getInstance(mContext).cancelTrade();
					apiOrder = apiOrder + "cancleTrade()";
				}

				TraceLogUtil.logDeviceBehaviour(PayCommonInfo.deviceType, apiOrder,
						null, null, AppConfig.currentToken);
		   }
		}).start();*/
 	}

 	/**
 	 * 释放音频
 	 */
 	public static void releaseAudio(final Context mContext, final String deviceId) {
 		/*new Thread(new Runnable() {
			@Override
			public void run() {
				String apiOrder = "释放音频:";
				if(DeviceData.EQUI_DFB.equals(deviceId) || DeviceData.EQUI_YITIJI.equals(deviceId)) {

				} else if(DeviceData.EQUI_SUPPERPAY.equals(deviceId)) {
					ReadCardSupPay.getInstance(mContext).destroy();
					apiOrder = apiOrder + "DovilaSDKInterface.handleHeadSet(mContext, false)";
				} else if(DeviceData.EQUI_DFB.equals(deviceId) || DeviceData.EQUI_YITIJI.equals(deviceId)) {
					ItronControllerImpl.getInstance(mContext).stopCSwiper();
					apiOrder = apiOrder + "ReleaseDevice";
				} else if(DeviceData.EQUI_AC.equals(deviceId)) {

				} else if(DeviceData.EQUI_DOVILA.equals(deviceId)) {

				} else if(DeviceData.EQUI_BBPOS.equals(deviceId)) {

				} else if(DeviceData.EQUI_BLACK_DOVILA.equals(deviceId)) {

				} else if(DeviceData.EQUI_GOLD_AC.equals(deviceId)) {

				} else if(DeviceData.EQUI_ME10.equals(deviceId)) {

				}
				TraceLogUtil.logDeviceBehaviour(PayCommonInfo.deviceType, apiOrder,
						null, null, AppConfig.currentToken);
		   }
		}).start();*/
 	}

	private static DovilaHeadsetReceiver mDovilaReceiver = new DovilaHeadsetReceiver();

	public static void register(Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
		intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

		context.registerReceiver(mDovilaReceiver, intentFilter);
	}

	public static void unregister(Context context) {
		context.unregisterReceiver(mDovilaReceiver);
	}

	private static class DovilaHeadsetReceiver extends BroadcastReceiver {
		/**
		 * 上次插入时间
		 */
		private long lastPlug = 0l;
		/**
		 * 插拔延时时间，兼容软开关设备
		 */
		private long Delay = 500l;

		@Override
		public void onReceive(final Context context, Intent intent) {
			if ("android.intent.action.HEADSET_PLUG".equals(intent.getAction())) {
				long now = System.currentTimeMillis();
				if (now - lastPlug < Delay) {
					return;
				}
				lastPlug = now;
				int state = intent.getIntExtra("state", -1);
				if (state == 2 || state == 1) {// 设备插入
					LogU.d("设备状态-----", "设备插入");
					hasDevice = true;

				} else if (state == 0) {// 设备拔出
					LogU.d("设备状态-----", "设备拔出");
					hasDevice = false;
				}

				if(null != notifier){
					notifier.onDeviceChanged(hasDevice);
				}
			}
		}
	}

	public interface DeviceChangeListener {
		public void onDeviceChanged(boolean hasDevice);
	}

	private static DeviceChangeListener notifier = null;

	public static void setDeviceChangeListener(DeviceChangeListener li) {
		notifier = li;
	}

	/**
	 * 获取交易类型
	 * 消费/查余额
	 * @param cash
	 * @return
	 */
	/*public static String getTransType (String cash) {
		String type = null;
		if(!TextUtils.isEmpty(cash)){
			if(cash.equals("0")){
				type = ReadCardSupPay.BALANCE;
			}else{
				type = ReadCardSupPay.CONSUMPTION;
			}
		}else{
			type = ReadCardSupPay.BALANCE;
		}
		return type;
	}*/

	/**
	 * 保存设备信息
	 * @param mContext
	 * @param deviceId 设备型号
	 * @param deviceName 设备名称
	 * @param connectMode 连接方式 0：有线    1：蓝牙
	 * @param bluetoothMac 设备蓝牙地址
	 * @param isSoftKeyboard 是否为软键盘
	 */
	public static void saveDeviceInfo (Context mContext, String deviceId, String deviceName, String connectMode, String bluetoothMac,
			String bluetoothName, Boolean isSoftKeyboard) {
		SharedPreferences.Editor editor;
		SharedPreferences prefernces;
		prefernces = mContext.getSharedPreferences(AppUtils.getPackageName(mContext).packageName, 0);
		editor = prefernces.edit();
		editor.putString("DEVICE_ID", deviceId);
		editor.putString("DEVICE_NAME", deviceName);
		editor.putString("CONNECT_MODE", connectMode);
		editor.putString("BLUETOOTH_MAC", bluetoothMac);
		editor.putString("BLUETOOTH_NAME", bluetoothName);
		editor.putBoolean("SOFT_KEYBOARD", isSoftKeyboard);
		editor.commit();
	}

	/**
	 * 保存设备SN/PN
	 * @param mContext
	 * @param sn
	 * @param pn
	 */
	public static void saveSnAndPn(Context mContext, String sn, String pn) {
		SharedPreferences.Editor editor;
		SharedPreferences prefernces;
		prefernces = mContext.getSharedPreferences(AppUtils.getPackageName(mContext).packageName, 0);
		editor = prefernces.edit();
		editor.putString("SN", sn);
		editor.putString("PN", pn);
		editor.commit();
	}



	/**
	 * 清除当前设备连接信息
	 * @param mContext
	 * @return
	 */
	public static boolean clearDeviceInfo(Context mContext) {
		try{
			PayCommonInfo.deviceType = "";
			//ElectronicCashUtil.getInstance().clearDeviceConnectInfo();
			SharedPreferences.Editor editor;
			SharedPreferences prefernces;
			prefernces = mContext.getSharedPreferences(AppUtils.getPackageName(mContext).packageName, 0);
			editor = prefernces.edit();
			editor.putString("DEVICE_ID", "");
			editor.putString("DEVICE_NAME", "");
			editor.putString("CONNECT_MODE", "");
			editor.putString("BLUETOOTH_MAC", "");
			editor.putString("BLUETOOTH_NAME", "");
			editor.putBoolean("SOFT_KEYBOARD", false);
			editor.commit();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * 获取蓝牙名称
	 * 比较是否是同一蓝牙设备时使用
	 * @param mContext
	 * @return
	 */
	public static String getBluetoothName(Context mContext){
		String bluetoothName = "";
		SharedPreferences prefernces = mContext.getSharedPreferences(AppUtils.getPackageName(mContext).packageName, 0);
		bluetoothName = prefernces.getString("BLUETOOTH_NAME", null);
		return bluetoothName;
	}

	/**
	 * 截取银联新设备前16位SN
	 */
	public static String getCSwiperSN(String oldSN){
		String newSN = "";
		if (oldSN.length() > 16){
			newSN = oldSN.substring(0, 16);
		}else{
			newSN = oldSN;
		}
		return newSN;
	}
}
