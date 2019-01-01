package com.androidcat.jly.device.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 中信银行卡号状态判断工具类
 *
 * @author pxw
 *
 */
public class BankcardUtils {
	public static final String CNBC_SP="17";

	private static final List<String> cnbcList = new ArrayList<String>();

	static {
		cnbcList.add("376966");
		cnbcList.add("376968");
		cnbcList.add("376969");
		cnbcList.add("400360");
		cnbcList.add("403391");
		cnbcList.add("403392");
		cnbcList.add("403393");
		cnbcList.add("404157");
		cnbcList.add("404158");
		cnbcList.add("404159");
		cnbcList.add("404171");
		cnbcList.add("404172");
		cnbcList.add("404173");
		cnbcList.add("404174");
		cnbcList.add("433666");
		cnbcList.add("433667");
		cnbcList.add("433668");
		cnbcList.add("433669");
		cnbcList.add("514906");
		cnbcList.add("518212");
		cnbcList.add("520108");
		cnbcList.add("556617");
		cnbcList.add("558916");
		cnbcList.add("622916");
		cnbcList.add("622918");
		cnbcList.add("622919");
		cnbcList.add("622680");
		cnbcList.add("622688");
		cnbcList.add("622689");
		cnbcList.add("622690");
		cnbcList.add("628206");
		cnbcList.add("628208");
		cnbcList.add("628209");
	}

	/**
	 * 在更新完当前卡列表状态后，可查询单个卡号是否中信银行卡
	 *
	 * @param number
	 *            卡号
	 * @return
	 */
	public static boolean isCNCBNumber(String number) {
		if (TextUtils.isEmpty(number) || number.length() < 6) {
			return false;
		}
		try {
			String temp = number.substring(0, 6);
			if (cnbcList.contains(temp)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
