package com.androidcat.jly.device.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.androidcat.jly.utils.log.LogU;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ToolUtils {

	/**
	 * 银行卡号值
	 */
	private static final HashMap<String, String> BANK_NUMBER_MAP = new HashMap<String, String>();

	/**
     * 对字符串进行MD5加密。
     */
    public static String encryptMD5(String strInput) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strInput.getBytes("UTF-8"));
            byte b[] = md.digest();
            buf = new StringBuffer(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                if (((int) b[i] & 0xff) < 0x10) { /* & 0xff转换无符号整型 */
                    buf.append("0");
                }
                buf.append(Long.toHexString((int) b[i] & 0xff)); /* 转换16进制,下方法同 */
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buf.toString();
    }

	/**
	 * 判断银行卡号是否合格
	 */
	public static boolean isBCNUnValid(final String bcn) {
		try {
			Pattern pattern = Pattern.compile(("^\\d{13,19}$"));
			Matcher matcher = pattern.matcher(bcn);
			return matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断手机号是否合格
	 *
	 * @param phonenumber
	 * @return
	 */
	public static boolean isPhoneValid(String phonenumber) {
//		try {
//			if (!phonenumber.matches("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$")) {
//				return false;
//			}
//			return true;
//		} catch (Exception e) {
//			TraceLogUtil.logException(e, TraceLog.ActionLevel.ERROR, AppConfig.currentToken);
//			return false;
//		}
		return phonenumber.length() == 11 && phonenumber.startsWith("1");
	}

	/**
	 * 判断账号是否合格
	 * @param account
	 * @return
	 */
	public static boolean isAccount(String account) {
//		return account.matches("[a-zA-z]\\S+") || account.matches("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		return !TextUtils.isEmpty(account);
	}

	/**
	 * 判断密码是否合格
	 */
	public static boolean isPwdUnValid(final String pwd) {
		try {
			if (pwd.length() != 6) {// pwd.length() < 6 || pwd.length() > 20
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断身份证是否合格
	 */
	public static boolean isCertificateIdValid(final String certificateid) {
		if (TextUtils.isEmpty(certificateid)) {
			return false;
		}
		return certificateid.matches("^\\d{14}([0-9[xX]])$|^\\d{17}([0-9[xX]])$");
	}

	/**
	 * 获取随机数
	 *
	 * @param byteLength
	 *            字节长度
	 * @return
	 */
	public static byte[] getRandom(int byteLength) {
		Random rand = null;
		byte[] random = new byte[byteLength];
		for (int i = 0; i < byteLength; i++) {
			rand = new Random();
			random[i] = (byte) rand.nextInt(100);
		}
		return random;
	}

	public static String getRandomStr(int length){
		StringBuffer sb = new StringBuffer();
		int temp = 0;
		Random rand;
		for(int i = 0 ; i < length; i++){
			rand = new Random();
			temp = rand.nextInt(100);
			sb.append("" + temp);
		}
		return sb.toString();
	}

	/**
     * 加载Assert文本文件，转换成String类型
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String loadAssetsText(Context context, String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName, Context.MODE_PRIVATE);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0) {
            byteStream.write(bytes, 0, len);
        }

        return new String(byteStream.toByteArray(), "UTF-8");
    }

	/**
     * 金额格式化（由分转为￥1.23元格式）
     */
    public static String moneyFormatFromCent(String money) {
    	if(TextUtils.isEmpty(money)) {
    		return "0.00";
    	}
    	double amount = 0.00;
		amount = Double.parseDouble(money)/100.00;
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(amount);
    }

    /**
     * 金额格式化（由分转为￥1.23元格式）
     * 如果money为null，返回"0"
     */
    public static String moneyFormatCent(String money) {
    	if(TextUtils.isEmpty(money)) {
    		return "0";
    	}

    	double amount = Double.parseDouble(money)/100.00;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(amount);
    }

    /**
	 * @Title yuanToFen
	 * @Description 金钱格式转换 将字符串1元 转换成 字符串100分
	 * @param @param yuan
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String yuanToFen(String yuan) {
		Double m = Double.parseDouble(yuan);
		DecimalFormat df = new DecimalFormat("0.00");
		yuan = df.format(m);
		String fen = (long) (m * 100 + 0.05) + "";
		return fen;
	}

	/**
	 * 银行卡号星号显示转换 6222002202882165->622200 *** *** 2165
	 *
	 * @param no
	 * @return
	 */
	public static String getBankNoWithStar(String no) {
		if (no == null)
			return null;
		int len = no.length();
		if (len < 8)
			return no;

		String tail = "";
		String head = "";
		String center = "";
		StringBuffer temp = new StringBuffer();
		char[] source = no.toCharArray();
		for (int i = 0; i < 6; i++) {// 取头
			temp.append(source[i]);
		}
		head = temp.toString();

		temp = new StringBuffer();
		for (int j = len - 4; j < len; j++) {// 取尾
			temp.append(source[j]);
		}
		tail = temp.toString();

		temp = new StringBuffer();// 取中间
		temp.append("*** ***");
		center = temp.toString();

		temp = null;
		temp = new StringBuffer();// 连接
		temp.append(head).append(" ").append(center).append(" ").append(tail);
		return temp.toString();
	}

	/**
	 * 手机号星号显示转换 13627213688->136*****688
	 *
	 * @param no
	 * @return
	 */
	public static String getPhoneNumberWithStar(String no) {
		if (no == null)
			return null;
		int len = no.length();
		if (len < 11)
			return no;

		String tail = "";
		String head = "";
		String center = "";
		StringBuffer temp = new StringBuffer();
		char[] source = no.toCharArray();
		for (int i = 0; i < 3; i++) {// 取头
			temp.append(source[i]);
		}
		head = temp.toString();

		temp = new StringBuffer();
		for (int j = len - 3; j < len; j++) {// 取尾
			temp.append(source[j]);
		}
		tail = temp.toString();

		temp = new StringBuffer();// 取中间
		temp.append("*****");
		center = temp.toString();

		temp = null;
		temp = new StringBuffer();// 连接
		temp.append(head).append(" ").append(center).append(" ").append(tail);
		return temp.toString();
	}

	/**
	 * 金额格式化为12位长度,不足补0
	 */
	/**
	 * @param total
	 */
	public static String amountFormat(String total) {
		if(TextUtils.isEmpty(total))
			total = "0";
		return String.format(Locale.CHINA, "%012d", Long.valueOf(total));
	}

    /**
	 * 将金额转换为6字节
	 * @param cash
	 * @return
	 */
   public static byte[] strToBCD(String cash) {
	  byte[] bcd = null;
	  if(!TextUtils.isEmpty(cash)){
		  double f = 0;
		  f = Double.parseDouble(cash);
		  long aaa = Math.round(f * 100);
		  bcd = new byte[6];
		  int temp = 0;
		  for (int i = 0; i < 6; i++) {
			temp = (int) (aaa % 100);
			bcd[5 - i] = intToBCD(temp);
			aaa /= 100;
		  }
	  }
	  return bcd;
   }

   public static byte intToBCD(int i){
		int t = i%100;
		int h = t/10;		//十位
		int l = t%10;		//个位

		return (byte)(h * 16 + l);
	}

   /**
    * 终端时间戳
    * @return
    */
   public static String getSubmitTime() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		StringBuffer buffer = new StringBuffer();
		buffer.append(c.get(Calendar.YEAR));
		int t = c.get(Calendar.MONTH) + 1;
		if (t < 10) {
			buffer.append('0');
		}
		buffer.append(t);

		t = c.get(Calendar.DAY_OF_MONTH);
		if (t < 10) {
			buffer.append('0');
		}
		buffer.append(t);
		t = c.get(Calendar.HOUR_OF_DAY);
		if (t < 10) {
			buffer.append('0');
		}
		buffer.append(t);
		t = c.get(Calendar.MINUTE);
		if (t < 10) {
			buffer.append('0');
		}
		buffer.append(t);
		t = c.get(Calendar.SECOND);
		if (t < 10) {
			buffer.append('0');
		}
		buffer.append(t);

		String traceTime = buffer.toString();
		return traceTime;
	}

	public static String changeSubmitTime(String time){
		String cTime = null;
		if(!TextUtils.isEmpty(time)){
			cTime = Calendar.getInstance().get(Calendar.YEAR) + time.substring(2, time.length() - 2);
		}
		return cTime;
	}

	/**
	 * 将bitmap转成字节
	 *
	 * @param bitmap
	 * @return
	 */
	public static byte[] getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * 转小写,去首尾空格
	 *
	 * @param str
	 * @return
	 */
	public static String cap2Min(String str) {
		return str.toLowerCase(Locale.CHINA).trim();
	}

	/**
	 * 在数据串中获取对应TLV数据
	 *
	 * @param src
	 * @param tag
	 * @return
	 */
	public static String getTLV(String src, String tag) {
		String value = null;
		int index = src.indexOf(tag);
		if (index != -1) {
			int start=index+tag.length()+2;
			String lenStr = src.substring(index + tag.length(), index+ tag.length() + 2);
			int len = 0;
			try {
				len = Integer.parseInt(lenStr, 16);
			} catch (NumberFormatException e) {
			}
			int end=index+ tag.length() + 2 + len * 2;
			if (len != 0 && src.length() >= end) {
				try {
					value = src.substring(start, end);
				} catch (Exception e) {
					LogU.e(tag, src, e);
					e.printStackTrace();
				}
			}
		}

		return value;
	}

	public static String getValueKey(String regInput, String content) {
		if(!TextUtils.isEmpty(content)){
			Pattern pattern = Pattern.compile(regInput);
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				return matcher.group(1);
			}
		}
		return "";
	}

	/**
     * 数字字符串转ASCII码字符串
     *
     * @param content
     *            字符串
     * @return ASCII字符串
     */
    public static String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
    }

    /**
     * 挂起
     * @param time
     */
    public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    /**
	 * 发送消息队列
	 * @param handler 队列
	 * @param msgStr 设备类型
	 * @param flag
	 * @param
	 */
	public static void sendHandler(Handler handler, String msgStr, int flag) {
		Message msg = handler.obtainMessage();
		msg.what = flag;
		if(!TextUtils.isEmpty(msgStr)){
			msg.obj = msgStr;
		}
		handler.sendMessage(msg);
	}

	/**
	 * 延迟发送消息队列
	 * @param handler
	 * @param msgStr
	 * @param flag
	 * @param delayMillis
	 */
	public static void sendHandlerDelayed(Handler handler, String msgStr, int flag, long delayMillis) {
		Message msg = handler.obtainMessage();
		msg.what = flag;
		if(!TextUtils.isEmpty(msgStr)){
			msg.obj = msgStr;
		}
		handler.sendMessageDelayed(msg, delayMillis);
	}

	/**
	 * @Title getBankName
	 * @Description 根据前6位银行卡号 获取对应 银行卡名
	 * @param @param bankNumber
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getBankName(String bankNumber) {
		if (TextUtils.isEmpty(bankNumber) || bankNumber.length() < 6) {
			return "UPAY";
		} else {
			bankNumber = bankNumber.replace(" ", "");
			bankNumber = bankNumber.substring(0, 6);
			String bankName = BANK_NUMBER_MAP.get(bankNumber);
			if (bankName == null) {
				bankName = "UPAY";
			}
			return bankName;
		}
	}

	/**
	 * @Title StringToDate
	 * @Description "20130110144444" -> 2013/01/10
	 * @param @param text
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String StringToDate(String text, String spliter) {
		String date = "";
		if (!TextUtils.isEmpty(text) && !text.equals("null")) {
			StringBuffer sb = new StringBuffer();
			sb.append(text.substring(0, 4));
			sb.append(spliter);
			sb.append(text.substring(4, 6));
			sb.append(spliter);
			sb.append(text.substring(6, 8));
			date = sb.toString();
		}
		return date;
	}

	/**
	 * @Title StringToTime
	 * @Description "20130110144444" -> 2013/01/10 14:44:44
	 * @param @param text
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String StringToTime(String text) {
		String date = "";
		try {
			if (!TextUtils.isEmpty(text) && !text.equals("null")) {
				StringBuffer sb = new StringBuffer();
				sb.append(text.substring(0, 4));
				sb.append("/");
				sb.append(text.substring(4, 6));
				sb.append("/");
				sb.append(text.substring(6, 8));
				sb.append(" ");
				sb.append(text.substring(8, 10));
				sb.append(":");
				sb.append(text.substring(10, 12));
				sb.append(":");
				sb.append(text.substring(12, 14));
				date = sb.toString();
			}
		} catch (Exception e){
			date = "";
		}
		return date;
	}

	/**
	 * 判断是否为整数
	 * @param str 传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	  public static boolean isInteger(String str) {
		  Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		    return pattern.matcher(str).matches();
	  }

	  public static boolean isZero(String str) {
		  if(!TextUtils.isEmpty(str)) {
			  if(0 == Integer.parseInt(str)){
				  return true;
			  }
		  }
		  return false;
	  }
}
