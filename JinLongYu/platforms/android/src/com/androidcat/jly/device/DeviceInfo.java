package com.androidcat.jly.device;

import java.io.Serializable;

/**
 * 设备
 * @author Administrator
 *
 */
public class DeviceInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 设备类型
	 * 0:付款设备    1:收款设备
	 */
	private String deviceType;

	/**
	 * 设备型号
	 */
	private String deviceId;

	/**
	 * 设备名称
	 */
	private String deviceName;

	/**
	 * 连接方式
	 * 0：有线    1：蓝牙
	 */
	private String connectionMode;

	/**
	 * 设备蓝牙地址
	 */
	private String deviceBluetoothMac;

	/**
	 * 设备蓝牙名称
	 */
	private String deviceBluetoothName;

	/**
	 * PSAM卡号
	 */
	private String pn;

	/**
	 * SN
	 */
	private String sn;

	/**
	 * 是否为软键盘
	 */
	private boolean isSoftKeyboard = false;

	/**
	 * 图片资源
	 */
	private int img=-1;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isSoftKeyboard() {
		return isSoftKeyboard;
	}

	public void setSoftKeyboard(boolean isSoftKeyboard) {
		this.isSoftKeyboard = isSoftKeyboard;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getDeviceBluetoothName() {
		return deviceBluetoothName;
	}

	public void setDeviceBluetoothName(String deviceBluetoothName) {
		this.deviceBluetoothName = deviceBluetoothName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getConnectionMode() {
		return connectionMode;
	}

	public void setConnectionMode(String connectionMode) {
		this.connectionMode = connectionMode;
	}

	public String getDeviceBluetoothMac() {
		return deviceBluetoothMac;
	}

	public void setDeviceBluetoothMac(String deviceBluetoothMac) {
		this.deviceBluetoothMac = deviceBluetoothMac;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}


}
