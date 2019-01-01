package com.androidcat.jly.device.utils;

/**
 * Created by IntelliJ IDEA.
 * User: fibiger
 * Date: 2009-05-05
 * Time: 10:18:48
 * To change this template use File | Settings | File Templates.
 */

import com.androidcat.jly.utils.log.LogU;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class SecurityUtil {

	/**
	 * 将byte数组转换成16进制组成的字符串 例如 一个byte数组 b[0]=0x07;b[1]=0x10;...b[5]=0xFB;
	 * byte2hex(b); 将返回一个字符串"0710BE8716FB"
	 *
	 * @param bytes
	 *            待转换的byte数组
	 * @return
	 */
	public static String hexEncode(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		StringBuffer buff = new StringBuffer();
		int len = bytes.length;
		for (int j = 0; j < len; j++) {
			if ((bytes[j] & 0xff) < 16) {
				buff.append('0');
			}
			buff.append(Integer.toHexString(bytes[j] & 0xff));
		}
		return buff.toString();
	}

	/**
	 * 将16进制组成的字符串转换成byte数组 例如 hex2Byte("0710BE8716FB"); 将返回一个byte数组
	 * b[0]=0x07;b[1]=0x10;...b[5]=0xFB;
	 *
	 * @param src
	 *            待转换的16进制字符串
	 * @return
	 */
	public static byte[] hexDecode(String src) {
		if (src == null || src.length() == 0 || src.length() % 2 != 0) {
			return null;
		}
		int nSrcLen = src.length();
		byte byteArrayResult[] = new byte[nSrcLen / 2];
		StringBuffer strBufTemp = new StringBuffer(src);
		String strTemp;
		int i = 0;
		while (i < strBufTemp.length() - 1) {
			strTemp = src.substring(i, i + 2);
			byteArrayResult[i / 2] = (byte) Integer.parseInt(strTemp, 16);
			i += 2;
		}
		return byteArrayResult;
	}

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encodeHexString(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		final String hexString = "0123456789abcdef";
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String hexStringToString(String bytes) throws UnsupportedEncodingException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		final String hexString = "0123456789abcdef";
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray(), "UTF-8");
	}

	public static byte[] hexToBytes(String hex) {
		byte[] buffer = new byte[hex.length() / 2];
		String strByte;

		for (int i = 0; i < buffer.length; i++) {
			strByte = hex.substring(i * 2, i * 2 + 2);
			buffer[i] = (byte) Integer.parseInt(strByte, 16);
		}

		return buffer;
	}

	/**
	 * 将整数转换为16进制的n个字节
	 *
	 * @param in
	 *            待转换的16进制字符串
	 * @return
	 */
	public static byte[] integer2Bytes(int in, int n) {
		byte[] bb = new byte[n];
		for (int i = 0; i < n; i++) {
			bb[i] = (byte) ((in >> (8 * (n - 1 - i))) & 0xff);
		}
		return bb;
	}

	/**
	 * 将整数转为16进行数后并以指定长度返回（当实际长度大于指定长度时只返回从末位开始指定长度的值）
	 *
	 * @param val
	 *            待转换整数
	 * @param len
	 *            指定长度
	 * @return String
	 */
	public static String Int2HexStr(int val, int len) {
		String result = Integer.toHexString(val).toUpperCase();
		int r_len = result.length();
		if (r_len > len) {
			return result.substring(r_len - len, r_len);
		}
		if (r_len == len) {
			return result;
		}
		StringBuffer strBuff = new StringBuffer(result);
		for (int i = 0; i < len - r_len; i++) {
			strBuff.insert(0, '0');
		}
		return strBuff.toString();
	}

	/**
	 * 将长整数转为16进行数后并以指定长度返回（当实际长度大于指定长度时只返回从末位开始指定长度的值）
	 *
	 * @param val
	 *            待转换长整数
	 * @param len
	 *            指定长度
	 * @return String
	 */
	public static String Long2HexStr(long val, int len) {
		String result = Long.toHexString(val).toUpperCase();
		int r_len = result.length();
		if (r_len > len) {
			return result.substring(r_len - len, r_len);
		}
		if (r_len == len) {
			return result;
		}
		StringBuffer strBuff = new StringBuffer(result);
		for (int i = 0; i < len - r_len; i++) {
			strBuff.insert(0, '0');
		}
		return strBuff.toString();
	}

	/**
	 * 将整数转换成byte数组 例如: 输入: n = 1000000000 ( n = 0x3B9ACA00) 输出: byte[0]:3b
	 * byte[1]:9a byte[2]:ca byte[3]:00 注意: 输入的整数必须在[-2^32, 2^32-1]的范围内
	 *
	 * @param n
	 *            整型值
	 * @return byte数组
	 */
	public static byte[] int2Bytes(int n) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(n);
		return bb.array();
	}

	/**
	 * 将长整数转换成byte数组 例如: 输入: n = 1000000000 ( n = 0x3B9ACA00) 输出: byte[0]:3b
	 * byte[1]:9a byte[2]:ca byte[3]:00 注意: 输入的长整数必须在[-2^64, 2^64-1]的范围内
	 *
	 * @param l
	 *            长整型值
	 * @return byte数组
	 */
	public static byte[] long2Bytes(long l) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(l);
		return bb.array();
	}

	/**
	 * 将无符号的4个字节的byte数据转换成32bit的长整形
	 *
	 * @param buf
	 *            无符号的4个字节数据
	 * @return 32bit的长整形
	 */
	public static long unsigned4BytesToInt(byte[] buf) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		firstByte = (0x000000FF & ((int) buf[0]));
		secondByte = (0x000000FF & ((int) buf[1]));
		thirdByte = (0x000000FF & ((int) buf[2]));
		fourthByte = (0x000000FF & ((int) buf[3]));
		return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
	}

	/**
	 * 生成len个字节的十六进制随机数字符串 例如:len=8 则可能会产生 DF15F0BDFADE5FAF 这样的字符串
	 *
	 * @param len
	 *            待产生的字节数
	 * @return String
	 */
	public static String yieldHexRand(int len) {
		StringBuffer strBufHexRand = new StringBuffer();
		Random rand = new Random(System.currentTimeMillis());
		int index;
		// 随机数字符
		char charArrayHexNum[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F' };
		for (int i = 0; i < len * 2; i++) {
			index = Math.abs(rand.nextInt()) % 16;
			if (i == 0) {
				while (charArrayHexNum[index] == '0') {
					index = Math.abs(rand.nextInt()) % 16;
				}
			}
			strBufHexRand.append(charArrayHexNum[index]);
		}
		return strBufHexRand.toString();
	}

	/**
	 * 计算字节校验和
	 *
	 * @param input
	 * @return
	 */
	public static byte calcLRC(byte[] input) {
		byte ret = 0;
		for (int i = 0; i < input.length; i++) {
			ret ^= input[i];
		}
		return ret;
	}

	/**
	 * 产生8字节的时间戳
	 *
	 * @param l
	 *            时间
	 * @return 8字节的时间戳
	 */
	public static byte[] generate8bytetime(Long l) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(l);
		return bb.array();
	}

	/**
	 * 按位异或byte数组 (两个byte数组的长度必须一样)
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static String doXOR(String b1, String b2) {
		if (b1.length() != b2.length()) {
			return null;
		}

		byte[] byte1 = hexDecode(b1);
		byte[] byte2 = hexDecode(b2);

		byte[] result = new byte[byte1.length];
		for (int i = 0; i < byte1.length; i++) {
			int temp = (byte1[i] ^ byte2[i]) & 0xff;
			result[i] = (byte) temp;
		}
		return hexEncode(result).toUpperCase();
	}

	/**
	 * 异或
	 *
	 * @param op1
	 * @param op2
	 * @return
	 */
	public static byte[] xor(byte[] op1, byte[] op2) {
		int len = op1.length;
		byte[] out = new byte[len];
		for (int i = 0; i < len; i++) {
			out[i] = (byte) (op1[i] ^ op2[i]);
		}
		return out;
	}

	/**
	 * 按位求反byte数组
	 *
	 * @param b
	 * @return
	 */
	public static String doReverse(String b) {
		byte[] byte1 = hexDecode(b);
		for (int i = 0; i < byte1.length; i++) {
			byte1[i] = (byte) (~byte1[i] & 0xff);
		}
		return hexEncode(byte1).toUpperCase();
	}

	/**
	 * 将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同
	 *
	 * @param key
	 *            待转换密钥(16个字节 由2个8字节密钥组成)
	 * @return
	 */
	public static String key16To24(String key) {
		// 计算加解密密钥，即将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同
		if (key.length() == 32) {
			return key + key.substring(0, 16); // 将key前8个字节复制到keyresult的最后8个字节
		} else {
			return null;
		}
	}

	/**
	 * 填充05数据，如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x05到数据块的右边，直到数据块的长度是8的倍数。
	 *
	 * @param data
	 *            待填充05的数据
	 * @return
	 */
	public static String padding05(String data) {
		int padlen = 8 - (data.length() / 2) % 8;
		if (padlen != 8) {
			String padstr = "";
			for (int i = 0; i < padlen; i++)
				padstr += "05";
			data += padstr;
			return data;
		} else {
			return data;
		}
	}

	/**
	 * 填充00数据，如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x00到数据块的右边，直到数据块的长度是8的倍数。
	 *
	 * @param data
	 *            待填充00的数据
	 * @return
	 */
	public static String padding00(String data) {
		int padlen = 8 - (data.length() / 2) % 8;
		if (padlen != 8) {
			String padstr = "";
			for (int i = 0; i < padlen; i++)
				padstr += "00";
			data += padstr;
			return data;
		} else {
			return data;
		}
	}

	/**
	 * 填充20数据，如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x20到数据块的右边，直到数据块的长度是8的倍数。
	 *
	 * @param data
	 *            待填充20的数据
	 * @return
	 */
	public static String padding20(String data) {
		int padlen = 8 - (data.length() / 2) % 8;
		if (padlen != 8) {
			String padstr = "";
			for (int i = 0; i < padlen; i++)
				padstr += "20";
			data += padstr;
			return data;
		} else {
			return data;
		}
	}

	/**
	 * 填充80数据，首先在数据块的右边追加一个
	 * '80',如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x00到数据块的右边，直到数据块的长度是8的倍数。
	 *
	 * @param data
	 *            待填充80的数据
	 * @return
	 */
	public static String padding80(String data) {
		int padlen = 8 - (data.length() / 2) % 8;
		String padstr = "";
		for (int i = 0; i < padlen - 1; i++)
			padstr += "00";
		data = data + "80" + padstr;
		return data;
	}

	/**
	 * CBC模式中的DES/3DES/TDES算法(数据不需要填充),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与
	 * 先用前8个字节密钥DES解密 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
	 *
	 * @param key
	 *            加解密密钥(8字节:DES算法 16字节:3DES/TDES算法
	 *            24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
	 * @param data
	 *            待加/解密数据(长度必须是8的倍数)
	 * @param icv
	 *            初始链值(8个字节) 一般为8字节的0x00 icv="0000000000000000"
	 * @param mode
	 *            0-加密，1-解密
	 * @return 加解密后的数据 为null表示操作失败
	 */
	public static String descbc(String key, String data, String icv, int mode) {
		try {
			// 判断加密还是解密
			int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

			SecretKey keySpec = null;

			Cipher enc = null;

			// 判断密钥长度
			if (key.length() == 16) {
				// 生成安全密钥
				keySpec = new SecretKeySpec(hexDecode(key), "DES");// key

				// 生成算法
				enc = Cipher.getInstance("DES/CBC/NoPadding");
			} else if (key.length() == 32) {
				// 计算加解密密钥，即将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同,并生成安全密钥
				keySpec = new SecretKeySpec(hexDecode(key + key.substring(0, 16)), "DESede");// 将key前8个字节复制到keycbc的最后8个字节

				// 生成算法
				enc = Cipher.getInstance("DESede/CBC/NoPadding");
			} else if (key.length() == 48) {
				// 生成安全密钥
				keySpec = new SecretKeySpec(hexDecode(key), "DESede");// key

				// 生成算法
				enc = Cipher.getInstance("DESede/CBC/NoPadding");
			} else {
				// logger.info("Key length is error");
				return null;
			}

			// 生成ICV
			IvParameterSpec ivSpec = new IvParameterSpec(hexDecode(icv));// icv

			// 初始化
			enc.init(opmode, keySpec, ivSpec);

			// 返回加解密结果
			return (hexEncode(enc.doFinal(hexDecode(data)))).toUpperCase();// 开始计算
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * CBC模式中的DES/3DES/TDES算法(数据需要填充80),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与
	 * 先用前8个字节密钥DES解密 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
	 *
	 * @param key
	 *            加解密密钥(8字节:DES算法 16字节:3DES/TDES算法
	 *            24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
	 * @param data
	 *            待加/解密数据
	 * @param icv
	 *            初始链值(8个字节) 一般为8字节的0x00 icv="0000000000000000"
	 * @param mode
	 *            0-加密，1-解密
	 * @return 加解密后的数据 为null表示操作失败
	 */
	public static String descbcNeedPadding80(String key, String data, String icv, int mode) {
		return descbc(key, padding80(data), icv, mode);
	}

	/**
	 * ECB模式中的DES/3DES/TDES算法(数据不需要填充),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与
	 * 先用前8个字节密钥DES解密 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
	 *
	 * @param key
	 *            加解密密钥(8字节:DES算法 16字节:3DES/TDES算法
	 *            24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
	 * @param data
	 *            待加/解密数据(长度必须是8的倍数)
	 * @param mode
	 *            0-加密，1-解密
	 * @return 加解密后的数据 为null表示操作失败
	 */
	public static String desecb(String key, String data, int mode) {
		try {

			// 判断加密还是解密
			int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

			SecretKey keySpec = null;

			Cipher enc = null;

			// 判断密钥长度
			if (key.length() == 16) {
				// 生成安全密钥
				keySpec = new SecretKeySpec(hexDecode(key), "DES");// key

				// 生成算法
				enc = Cipher.getInstance("DES/ECB/NoPadding");
			} else if (key.length() == 32) {
				// 计算加解密密钥，即将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同,并生成安全密钥
				keySpec = new SecretKeySpec(hexDecode(key + key.substring(0, 16)), "DESede");// 将key前8个字节复制到keyecb的最后8个字节

				// 生成算法
				enc = Cipher.getInstance("DESede/ECB/NoPadding");
			} else if (key.length() == 48) {
				// 生成安全密钥
				keySpec = new SecretKeySpec(hexDecode(key), "DESede");// key

				// 生成算法
				enc = Cipher.getInstance("DESede/ECB/NoPadding");
			} else {
				// logger.info("Key length is error");
				return null;
			}

			// 初始化
			enc.init(opmode, keySpec);

			// 返回加解密结果
			return (hexEncode(enc.doFinal(hexDecode(data)))).toUpperCase();// 开始计算
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * ECB模式中的DES/3DES/TDES算法(数据需要填充80),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与
	 * 先用前8个字节密钥DES解密 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
	 *
	 * @param key
	 *            加解密密钥(8字节:DES算法 16字节:3DES/TDES算法
	 *            24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
	 * @param data
	 *            待加/解密数据
	 * @param mode
	 *            0-加密，1-解密
	 * @return 加解密后的数据 为null表示操作失败
	 */
	public static String desecbNeedPadding80(String key, String data, int mode) {
		return desecb(key, padding80(data), mode);
	}

	/**
	 * ECB模式中的DES算法(数据不需要填充)
	 *
	 * @param key
	 *            加解密密钥(8个字节)
	 * @param data
	 *            输入:待加/解密数据(长度必须是8的倍数) 输出:加/解密后的数据
	 * @param mode
	 *            0-加密，1-解密
	 * @return
	 */
	public static void des(byte[] key, byte[] data, int mode) {
		try {
			if (key.length == 8) {
				// 判断加密还是解密
				int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

				// 生成安全密钥
				SecretKey keySpec = new SecretKeySpec(key, "DES");// key

				// 生成算法
				Cipher enc = Cipher.getInstance("DES/ECB/NoPadding");

				// 初始化
				enc.init(opmode, keySpec);

				// 加解密结果
				byte[] temp = enc.doFinal(data); // 开始计算

				System.arraycopy(temp, 0, data, 0, temp.length); // 将加解密结果复制一份到data
			}
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
	}

	/**
	 * 产生MAC算法3,即Single DES加上最终的TDES MAC,支持8、16字节的密钥 这也叫Retail
	 * MAC,它是在[ISO9797-1]中定义的MAC算法3，带有输出变换3、没有截断，并且用DES代替块加密
	 *
	 * @param key
	 *            密钥(8字节:CBC/DES算法 16字节:先CBC/DES，再完成3DES/TDES算法)
	 * @param data
	 *            要计算MAC的数据
	 * @param icv
	 *            初始链向量 (8个字节) 一般为8字节的0x00 icv="0000000000000000"
	 * @param padding
	 *            0：填充0x00 (PIM专用) 1：填充0x20 (SIM卡专用 必须输出8个字节) 2：填充0x80
	 *            (GP卡用)3:填充0x05（卡门户应用）
	 * @param outlength
	 *            返回的MAC长度 1：4个字节 2：8个字节
	 * @return
	 */
	public static String generateMAC(String key, String data, String icv, int padding, int outlength) {
		try {
			if (key.length() == 32 || key.length() == 16) {
				byte[] leftKey = new byte[8];
				byte[] rightKey = new byte[8];
				System.arraycopy(hexDecode(key), 0, leftKey, 0, 8); // 将key复制一份到leftKey

				byte[] icvTemp = hexDecode(icv); // 将icv复制一份到icvTemp

				// 实际参与计算的数据
				byte[] dataTemp = null;

				if (padding == 0) {
					dataTemp = hexDecode(padding00(data)); // 填充0x00
				} else if (padding == 1) {
					dataTemp = hexDecode(padding20(data)); // 填充0x20
				} else if (padding == 2) {
					dataTemp = hexDecode(padding80(data)); // 填充0x80
				} else if (padding == 3) {
					dataTemp = hexDecode(padding05(data));
				}// 填充0x05

				int nCount = dataTemp.length / 8;
				for (int i = 0; i < nCount; i++) {
					for (int j = 0; j < 8; j++)
						// 初始链值与输入数据异或
						icvTemp[j] ^= dataTemp[i * 8 + j];
					des(leftKey, icvTemp, 0); // DES加密
				}
				if (key.length() == 32) // 如果key的长度是16个字节
				{
					System.arraycopy(hexDecode(key), 8, rightKey, 0, 8); // 将key复制一份到rightKey
					des(rightKey, icvTemp, 1); // DES解密
					des(leftKey, icvTemp, 0); // DES加密
				}
				String mac = (hexEncode(icvTemp)).toUpperCase();
				return (outlength == 1 && padding != 1) ? mac.substring(0, 8) : mac;// 返回结果
			} else {
				// logger.info("Key length is error");
			}
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 产生MAC算法1,即Full TDES MAC,支持16、24字节的密钥 这也叫完整的TDES
	 * MAC,它是在[ISO9797-1]中定义的MAC算法1，带有输出变换1，没有截断，并且用TDES代替块加密。
	 *
	 * @param key
	 *            密钥(16字节:3DES/TDES算法 24字节:3DES/TDES算法)
	 * @param data
	 *            要计算MAC的数据
	 * @param icv
	 *            初始链向量 (8个字节) 一般为8字节的0x00 icv="0000000000000000"
	 * @param padding
	 *            0：填充0x00 (PIM专用) 1：填充0x20 (SIM卡专用 必须输出8个字节) 2：填充0x80 (GP卡用)
	 * @param outlength
	 *            返回的MAC长度 1：4个字节 2：8个字节
	 * @return
	 */
	public static String generateMACAlg1(String key, String data, String icv, int padding, int outlength) {
		try {
			if (key.length() == 32 || key.length() == 48) {
				byte[] icvTemp = hexDecode(icv); // 将icv复制一份到icvTemp

				// 实际参与计算的数据
				byte[] dataTemp = null;

				if (padding == 0) {
					dataTemp = hexDecode(padding00(data)); // 填充0x00
				} else if (padding == 1) {
					dataTemp = hexDecode(padding20(data)); // 填充0x20
				} else {
					dataTemp = hexDecode(padding80(data)); // 填充0x80
				}

				int nCount = dataTemp.length / 8;
				for (int i = 0; i < nCount; i++) {
					for (int j = 0; j < 8; j++)
						// 初始链值与输入数据异或
						icvTemp[j] ^= dataTemp[i * 8 + j];
					String resulticv = desecb(key, hexEncode(icvTemp), 0); // 3DES/TDES加密
					System.arraycopy(hexDecode(resulticv), 0, icvTemp, 0, 8); // 将icv复制一份到icvTemp
				}
				String mac = (hexEncode(icvTemp)).toUpperCase();
				return (outlength == 1 && padding != 1) ? mac.substring(0, 8) : mac;// 返回结果
			} else {
				// logger.info("Key length is error");
			}
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 产生MAC算法2,即RIPEMD-MAC,支持8、16字节的密钥 这也叫RIPEMD-MAC(RIPEMD-MAC [RIPE 93] +
	 * EMAC (DMAC) [Petrank-Rackoff 98]),
	 * 它是在[ISO9797-1]中定义的MAC算法2，带有输出变换2、没有截断，并且用DES代替块加密
	 *
	 * @param key
	 *            密钥(8字节:CBC/DES算法 16字节:先CBC/DES，再完成3DES/TDES算法)
	 * @param data
	 *            要计算MAC的数据
	 * @param icv
	 *            初始链向量 (8个字节) 一般为8字节的0x00 icv="0000000000000000"
	 * @param padding
	 *            0：填充0x00 (PIM专用) 1：填充0x20 (SIM卡专用 必须输出8个字节) 2：填充0x80 (GP卡用)
	 * @param outlength
	 *            返回的MAC长度 1：4个字节 2：8个字节
	 * @return
	 */
	public static String generateMACAlg2(String key, String data, String icv, int padding, int outlength) {
		try {
			if (key.length() == 32 || key.length() == 16) {
				byte[] leftKey = new byte[8];
				byte[] rightKey = new byte[8];
				System.arraycopy(hexDecode(key), 0, leftKey, 0, 8); // 将key复制一份到leftKey

				byte[] icvTemp = hexDecode(icv); // 将icv复制一份到icvTemp

				// 实际参与计算的数据
				byte[] dataTemp = null;

				if (padding == 0) {
					dataTemp = hexDecode(padding00(data)); // 填充0x00
				} else if (padding == 1) {
					dataTemp = hexDecode(padding20(data)); // 填充0x20
				} else {
					dataTemp = hexDecode(padding80(data)); // 填充0x80
				}

				int nCount = dataTemp.length / 8;
				for (int i = 0; i < nCount; i++) {
					for (int j = 0; j < 8; j++)
						// 初始链值与输入数据异或
						icvTemp[j] ^= dataTemp[i * 8 + j];
					des(leftKey, icvTemp, 0); // DES加密
				}
				if (key.length() == 32) // 如果key的长度是16个字节
				{
					System.arraycopy(hexDecode(key), 8, rightKey, 0, 8); // 将key复制一份到rightKey
					des(rightKey, icvTemp, 0); // DES加密
				}
				String mac = (hexEncode(icvTemp)).toUpperCase();
				return (outlength == 1 && padding != 1) ? mac.substring(0, 8) : mac;// 返回结果
			} else {
				// logger.info("Key length is error");
			}
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 产生MAC算法4,支持16、24字节的密钥
	 * 这也叫Mac-DES,它是在[ISO9797-1]中定义的MAC算法4，带有输出变换4，没有截断，并且用DES代替块加密。
	 *
	 * @param key
	 *            密钥(16字节:3DES/TDES算法 24字节:3DES/TDES算法)
	 * @param data
	 *            要计算MAC的数据
	 * @param icv
	 *            初始链向量 (8个字节) 一般为8字节的0x00 icv="0000000000000000"
	 * @param padding
	 *            0：填充0x00 (PIM专用) 1：填充0x20 (SIM卡专用 必须输出8个字节) 2：填充0x80 (GP卡用)
	 * @param outlength
	 *            返回的MAC长度 1：4个字节 2：8个字节
	 * @return
	 */
	public static String generateMACAlg4(String key, String data, String icv, int padding, int outlength) {
		try {
			byte[] leftKey = new byte[8];
			byte[] middleKey = new byte[8];
			byte[] rightKey = new byte[8];

			if (key.length() == 48) {
				System.arraycopy(hexDecode(key), 16, rightKey, 0, 8); // 将key的最右边8个字节复制一份到rightKey
			} else if (key.length() == 32) {
				System.arraycopy(hexDecode(key), 8, rightKey, 0, 8); // 将key的最右边8个字节复制一份到rightKey
			} else {
				// logger.info("Key length is error");
				return null;
			}

			System.arraycopy(hexDecode(key), 0, leftKey, 0, 8); // 将key的最左边8个字节复制一份到leftKey
			System.arraycopy(hexDecode(key), 8, middleKey, 0, 8); // 将key的中间8个字节复制一份到middleKey

			byte[] icvTemp = hexDecode(icv); // 将icv复制一份到icvTemp

			// 实际参与计算的数据
			byte[] dataTemp = null;

			if (padding == 0) {
				dataTemp = hexDecode(padding00(data)); // 填充0x00
			} else if (padding == 1) {
				dataTemp = hexDecode(padding20(data)); // 填充0x20
			} else {
				dataTemp = hexDecode(padding80(data)); // 填充0x80
			}

			int nCount = dataTemp.length / 8;
			for (int i = 0; i < nCount; i++) {
				for (int j = 0; j < 8; j++)
					// 初始链值与输入数据异或
					icvTemp[j] ^= dataTemp[i * 8 + j];
				des(leftKey, icvTemp, 0); // DES加密
				if (i == 0)
					des(rightKey, icvTemp, 0); // DES加密
				if (i == nCount - 1)
					des(middleKey, icvTemp, 0); // DES加密
			}
			String mac = (hexEncode(icvTemp)).toUpperCase();
			return (outlength == 1 && padding != 1) ? mac.substring(0, 8) : mac;// 返回结果
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 生成CRC32
	 *
	 * @param data
	 *            待处理数据
	 * @param strinitcrc
	 *            长度必须为8
	 * @param lastblock
	 *            1:最后取反
	 * @return 8个字节的CRC
	 */
	public static String generateCRC32(byte[] data, byte[] strinitcrc, int lastblock) {
		long crc32 = unsigned4BytesToInt(strinitcrc);
		for (int i = 0; i < data.length; i++)
			crc32 = lGenCRC32(crc32, data[i]);
		if (lastblock == 1)
			crc32 = ~crc32;
		return Long2HexStr(crc32, 8);
	}

	/**
	 * 生成CRC32
	 *
	 * @param lOldCRC
	 *            the crc32 value
	 * @param ByteVal
	 *            the new data byte
	 * @return
	 */
	public static long lGenCRC32(long lOldCRC, byte ByteVal) {
		long TabVal;
		int j;
		TabVal = ((lOldCRC) ^ ByteVal) & 0xff;
		for (j = 8; j > 0; j--) {
			if ((TabVal & 1) == 1) {
				TabVal = (TabVal >> 1) ^ 0xEDB88320L;
			} else {
				TabVal >>= 1;
			}
		}
		return TabVal ^ (((lOldCRC) >> 8) & 0x00FFFFFFL);
	}

	/**
	 * SHA-1摘要
	 *
	 * @param data
	 *            要计算SHA-1摘要的数据
	 * @return 16进制字符串形式的SHA-1消息摘要，一般为20字节 为null表示操作失败
	 */
	public static String generateSHA1(String data) {
		try {
			// 使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

			// 开始使用算法
			messageDigest.update(hexDecode(data));

			// 输出算法运算结果
			byte[] hashValue = messageDigest.digest(); // 20位字节

			return (hexEncode(hashValue)).toUpperCase();
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	public static byte[] generateSHA1(byte[] data) {
		try {
			// 使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

			// 开始使用算法
			messageDigest.update(data);

			// 输出算法运算结果
			byte[] hashValue = messageDigest.digest(); // 20位字节

			return hashValue;
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * RSA签名
	 *
	 * @param N
	 *            RSA的模modulus
	 * @param E
	 *            RSA公钥的指数exponent
	 * @param D
	 *            RSA私钥的指数exponent
	 * @param data
	 *            输入数据
	 * @param mode
	 *            0-加密，1-解密
	 * @param type
	 *            0-私钥加密，公钥解密 1-公钥加密，私钥解密
	 * @return 签名后的数据 为null表示操作失败
	 */
	public static String generateRSA(String N, String E, String D, String data, int mode, int type) {
		try {
			// 判断加密还是解密
			int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			BigInteger big_N = new BigInteger(N);
			Key key = null;

			if (mode != type) { // 生成公钥
				BigInteger big_E = new BigInteger(E);
				KeySpec keySpec = new RSAPublicKeySpec(big_N, big_E);
				key = keyFactory.generatePublic(keySpec);
			} else { // 生成私钥
				BigInteger big_D = new BigInteger(D);
				KeySpec keySpec = new RSAPrivateKeySpec(big_N, big_D);
				key = keyFactory.generatePrivate(keySpec);
			}

			// 获得一个RSA的Cipher类，使用私钥加密
			Cipher cipher = Cipher.getInstance("RSA"); // RSA/ECB/PKCS1Padding

			// 初始化
			cipher.init(opmode, key);

			// 返回加解密结果
			return (hexEncode(cipher.doFinal(hexDecode(data)))).toUpperCase();// 开始计算
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * RSA签名
	 *
	 * @param key
	 *            RSA的密钥 公钥用X.509编码；私钥用PKCS#8编码
	 * @param data
	 *            输入数据
	 * @param mode
	 *            0-加密，1-解密
	 * @param type
	 *            0-私钥加密，公钥解密 1-公钥加密，私钥解密
	 * @return 签名后的数据 为null表示操作失败
	 */
	public static String generateRSA(String key, String data, int mode, int type) {
		try {
			// 判断加密还是解密
			int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			Key strkey = null;
			if (mode != type) { // 生成公钥
				KeySpec keySpec = new X509EncodedKeySpec(SecurityUtil.hexDecode(key)); // X.509编码
				strkey = keyFactory.generatePublic(keySpec);
			} else { // 生成私钥
				KeySpec keySpec = new PKCS8EncodedKeySpec(SecurityUtil.hexDecode(key)); // PKCS#8编码
				strkey = keyFactory.generatePrivate(keySpec);
			}

			// 获得一个RSA的Cipher类，使用私钥加密
			Cipher cipher = Cipher.getInstance("RSA"); // RSA/ECB/PKCS1Padding

			// 初始化
			cipher.init(opmode, strkey);

			// 返回加解密结果
			return (hexEncode(cipher.doFinal(hexDecode(data)))).toUpperCase();// 开始计算
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 生成带SHA-1摘要的RSA签名
	 *
	 * @param N
	 *            RSA的模modulus
	 * @param D
	 *            RSA私钥的指数exponent
	 * @param data
	 *            输入数据
	 * @return 签名后的数据 为null表示操作失败
	 */
	public static String generateSHA1withRSA(String N, String D, String data) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			BigInteger big_N = new BigInteger(N);
			BigInteger big_D = new BigInteger(D);
			KeySpec keySpec = new RSAPrivateKeySpec(big_N, big_D);
			PrivateKey key = keyFactory.generatePrivate(keySpec);

			// 使用私钥签名
			Signature sig = Signature.getInstance("SHA1WithRSA"); // SHA1WithRSA
			sig.initSign(key);
			sig.update(hexDecode(data));

			// 返回加密结果
			return (hexEncode(sig.sign())).toUpperCase();// 开始计算
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 验证带SHA-1摘要的RSA签名
	 *
	 * @param N
	 *            RSA的模modulus
	 * @param E
	 *            RSA公钥的指数exponent
	 * @param plaindata
	 *            原始数据
	 * @param signdata
	 *            签名数据
	 * @return 验证结果 true 验证成功 false 验证失败
	 */
	public static boolean verifySHA1withRSA(String N, String E, String plaindata, String signdata) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			BigInteger big_N = new BigInteger(N);
			BigInteger big_E = new BigInteger(E);
			KeySpec keySpec = new RSAPublicKeySpec(big_N, big_E);
			PublicKey key = keyFactory.generatePublic(keySpec);

			// 使用公钥验证
			Signature sig = Signature.getInstance("SHA1WithRSA"); // SHA1WithRSA
			sig.initVerify(key);
			sig.update(hexDecode(plaindata));

			// 返回验证结果
			return sig.verify(hexDecode(signdata));
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return false;
	}

	/**
	 * 生成带SHA-1摘要的RSA签名
	 *
	 * @param key
	 *            DAP或者Token私钥 用PKCS#8编码
	 * @param data
	 *            要签名的原始数据
	 * @return 签名后的数据 为null表示操作失败
	 */
	public static String generateSHA1withRSA(String key, String data) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			KeySpec keySpec = new PKCS8EncodedKeySpec(hexDecode(key)); // PKCS#8编码
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

			// 使用私钥签名
			Signature sig = Signature.getInstance("SHA1WithRSA"); // SHA1WithRSA
			sig.initSign(privateKey);
			sig.update(hexDecode(data));

			// 返回加密结果
			return (hexEncode(sig.sign())).toUpperCase();// 开始计算
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 验证带SHA-1摘要的RSA签名
	 *
	 * @param key
	 *            Receipt公钥 用X.509编码
	 * @param plaindata
	 *            原始数据
	 * @param signdata
	 *            签名数据
	 * @return 验证结果 true 验证成功 false 验证失败
	 */
	public static boolean verifySHA1withRSA(String key, String plaindata, String signdata) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			KeySpec keySpec = new X509EncodedKeySpec(SecurityUtil.hexDecode(key)); // X.509编码
			PublicKey publicKey = keyFactory.generatePublic(keySpec);

			// 使用公钥验证
			Signature sig = Signature.getInstance("SHA1WithRSA"); // SHA1WithRSA
			sig.initVerify(publicKey);
			sig.update(hexDecode(plaindata));

			// 返回验证结果
			return sig.verify(hexDecode(signdata));
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return false;
	}

	/**
	 * DAP计算，函数先对LOAD FILE(C4后面的不包括长度)计算hash(调用generateSHA1函数),所得结果20字节，
	 * 这个值可以放入install for load的HASH字段，再用DAP密钥对上一步的HASH内容进行Single DES Plus Final
	 * Triple DES算法(调用generateMAC函数) 或者RSA
	 * SSA-PKCS1-v1_5算法(调用generateSHA1withRSA函数),计算DAP。
	 *
	 * @param key
	 *            算法1：DAP密钥 16字节 算法2：DAP私钥 用PKCS#8编码
	 * @param data
	 *            要计算的数据（C4后面的不包括长度）
	 * @param algorithm
	 *            要进行计算的算法 1：Single DES Plus Final Triple DES MAC(调用generateMAC)
	 *            2：RSA SSA-PKCS1-v1_5(调用generateSHA1withRSA)
	 * @return DAP数据 为null表示操作失败
	 */
	public static String generateDAP(String key, String data, int algorithm) {
		return (algorithm == 2) ? generateSHA1withRSA(key, generateSHA1(data)) : generateMAC(key, generateSHA1(data), "0000000000000000", 2, 2);
	}

	/**
	 * 生成密钥校验值。密钥校验值的计算采用3DES-ECB算法， 以16Bytes密钥对数据“0x00 00 00 00 00 00 00
	 * 00”进行加密操作，并左取计算结果的outlength字节作为密钥校验值
	 *
	 * @param key
	 *            待校验密钥 暂时固定为16字节
	 * @param outlength
	 *            返回的校验值字节长度 一般为3字节
	 * @return 密钥校验值 为null表示操作失败
	 */
	public static String generateKeyCheck(String key, int outlength) {
		try {
			String dataTemp = desecb(key, "0000000000000000", 0);
			if (dataTemp.length() >= outlength * 2) {
				return dataTemp.substring(0, outlength * 2);
			}
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 通过加密机接口获取安全通道密钥
	 *
	 * @param keyVersion
	 *            密钥版本
	 * @param keyIndex
	 *            密钥索引 参见《手机支付系统OTA平台-加密机接口规范》A.5
	 * @param divNum
	 *            密钥分散级数，应大于等于0
	 * @param domainAID
	 *            安全域AID
	 * @param imsi
	 *            卡的IMSI
	 * @return 一组特定类型的密钥 map key：密钥类型 目前有：ENC(1)、MAC(2)、DEK(3)、DAP(4)、TOKEN(5
	 *         暂不支持)、RECEIPT(暂不支持)、KIC(0)、KID(0)、KIK(暂不支持) map
	 *         value：密钥值(暂时固定为16字节) 外部程序通过key获取的值为null表示通过加密机获取该类型的密钥失败
	 */
	public static Map<Integer, String> generateSecureDomainKey(int keyVersion, int keyIndex, int divNum, String domainAID, String imsi) {
		Map<Integer, String> sdKey = new HashMap<Integer, String>();
		try {
			// int algFlag = 0x82; //分散算法
			// int[] keysLen = new int[4];
			// byte[] keys = new byte[64];
			String strKey = "";
			// IMSI左补零补足8Bytes
			int imsiLength = 16 - imsi.length();
			for (int i = 0; i < imsiLength; i++)
				imsi = "0" + imsi;
			// 判断密钥索引
			switch (keyIndex) {
			case 0x41:// 0x41 Kic根密钥

				// KIC:0x0E
				String rightKic = Int2HexStr(keyVersion, 2) + "0E";
				int kicLength = 16 - rightKic.length();
				for (int i = 0; i < kicLength; i++)
					rightKic = "0" + rightKic;
				// 计算 IMSI 异或 （Key Version +Key类型）
				String kic = doReverse(doXOR(imsi, rightKic)); // 先异或再取反
				kicLength = 32 - kic.length();
				for (int i = 0; i < kicLength; i++)
					kic = "0" + kic;
				// 调用加密机接口
				// OTAHSM.OTAAPI.GenerateAndExportSDKey(keyVersion,keyIndex,algFlag,divNum,hexDecode(domainAID+kic),keysLen,keys);
				// strKey = hexEncode(keys);
				switch (keyVersion) {
				case 0x10:
					strKey = "C8C9045CBD795C5303FF2A5089104F3E";
					break;
				case 0x11:
					strKey = "86E9408747FF12E2C47B18F19D5ADD41";
					break;
				case 0x12:
					strKey = "16B96743F7D1E7BA90E4EF447C1D4146";
					break;
				case 0x13:
					strKey = "B2D19983940F5119161BE4289359D2B0";
					break;
				default:
					strKey = "C8C9045CBD795C5303FF2A5089104F3E";
				}
				sdKey.put(0, strKey);

				break;
			case 0x42:// 0x42 Kid根密钥

				// KID:0x0C
				String rightKid = Int2HexStr(keyVersion, 2) + "0C";
				int kidLength = 16 - rightKid.length();
				for (int i = 0; i < kidLength; i++)
					rightKid = "0" + rightKid;
				// 计算 IMSI 异或（Key Version +Key类型）
				String kid = doReverse(doXOR(imsi, rightKid)); // 先异或再取反
				kidLength = 32 - kid.length();
				for (int i = 0; i < kidLength; i++)
					kid = "0" + kid;
				// 调用加密机接口
				// OTAHSM.OTAAPI.GenerateAndExportSDKey(keyVersion,keyIndex,algFlag,divNum,hexDecode(domainAID+kid),keysLen,keys);
				// strKey = hexEncode(keys);
				switch (keyVersion) {
				case 0x10:
					strKey = "855D06477874A68EC145AB8707004597";
					break;
				case 0x11:
					strKey = "B5A4A9B3F81B8C2F888E09B4655CF2D6";
					break;
				case 0x12:
					strKey = "D8FE8115AB0E80E5F115EF69E06C3D1F";
					break;
				case 0x13:
					strKey = "61F825277B859A3827EE0A7071DABBD3";
					break;
				default:
					strKey = "855D06477874A68EC145AB8707004597";
				}
				sdKey.put(0, strKey);

				break;
			case 0x61:// 0x61 RFM根密钥
				break;
			case 0x81:// 0x81 业务下载根密钥
				break;
			case 0x21: // 0x21 安全域根密钥
			case 0x01: // 0x01 应用主控根密钥

				// S-ENC:0x0E
				String rightEnc = Int2HexStr(keyVersion, 2) + "0E";
				int encLength = 16 - rightEnc.length();
				for (int i = 0; i < encLength; i++)
					rightEnc = "0" + rightEnc;
				// 计算 IMSI 异或（Key Version +Key类型）
				String enc = doReverse(doXOR(imsi, rightEnc)); // 先异或再取反
				encLength = 32 - enc.length();
				for (int i = 0; i < encLength; i++)
					enc = "0" + enc;
				// 调用加密机接口
				// OTAHSM.OTAAPI.GenerateAndExportSDKey(keyVersion,keyIndex,algFlag,divNum,hexDecode(domainAID+enc),keysLen,keys);
				// strKey = hexEncode(keys);
				switch (keyVersion) {
				case 0x10:
					strKey = "0C450B7A3A77BF039C92627BE1F684AA";
					break;
				case 0x11:
					strKey = "4B3D365CD5501D2BB2A97B601E4EE2C1";
					break;
				case 0x12:
					strKey = "AF22C8AD892AA964512E4D95F14AFFF7";
					break;
				case 0x13:
					strKey = "940FA3C8BB51C37C97DE4C4DF8C18B47";
					break;
				default:
					strKey = "0C450B7A3A77BF039C92627BE1F684AA";
				}
				sdKey.put(1, strKey);

				// S-MAC:0x0C
				String rightMac = Int2HexStr(keyVersion, 2) + "0C";
				int macLength = 16 - rightMac.length();
				for (int i = 0; i < macLength; i++)
					rightMac = "0" + rightMac;
				// 计算 IMSI 异或（Key Version +Key类型）
				String mac = doReverse(doXOR(imsi, rightMac)); // 先异或再取反
				macLength = 32 - mac.length();
				for (int i = 0; i < macLength; i++)
					mac = "0" + mac;
				// OTAHSM.OTAAPI.GenerateAndExportSDKey(keyVersion,keyIndex,algFlag,divNum,hexDecode(domainAID+mac),keysLen,keys);
				// strKey = hexEncode(keys);
				switch (keyVersion) {
				case 0x10:
					strKey = "978BC1B22678CB97F35840E56125AEAF";
					break;
				case 0x11:
					strKey = "6AE2B35F6A62233FAC30D26A74E32919";
					break;
				case 0x12:
					strKey = "B7089CA8B4B667BD613EDF19F1A3CF42";
					break;
				case 0x13:
					strKey = "ABE9DB98BA738EBC4FBD5B1908D01FC2";
					break;
				default:
					strKey = "978BC1B22678CB97F35840E56125AEAF";
				}
				sdKey.put(2, strKey);

				// DEK:0x0D
				String rightDek = Int2HexStr(keyVersion, 2) + "0D";
				int dekLength = 16 - rightDek.length();
				for (int i = 0; i < dekLength; i++)
					rightDek = "0" + rightDek;
				// 计算 IMSI 异或（Key Version +Key类型）
				String dek = doReverse(doXOR(imsi, rightDek)); // 先异或再取反
				dekLength = 32 - dek.length();
				for (int i = 0; i < dekLength; i++)
					dek = "0" + dek;
				// 调用加密机接口
				// OTAHSM.OTAAPI.GenerateAndExportSDKey(keyVersion,keyIndex,algFlag,divNum,hexDecode(domainAID+dek),keysLen,keys);
				// strKey = hexEncode(keys);
				switch (keyVersion) {
				case 0x10:
					strKey = "73C89A3C8133EFEB053674FE76AB9185";
					break;
				case 0x11:
					strKey = "6DACA3224ACA8EA3E6BE0900854931FB";
					break;
				case 0x12:
					strKey = "515169A69A4F3D893EB9F076C8B75588";
					break;
				case 0x13:
					strKey = "ACF9218C602D8C7F074DC9519289E191";
					break;
				default:
					strKey = "73C89A3C8133EFEB053674FE76AB9185";
				}
				sdKey.put(3, strKey);

				// DAP:0x0A
				String rightDap = Int2HexStr(keyVersion, 2) + "0A";
				int dapLength = 16 - rightDap.length();
				for (int i = 0; i < dapLength; i++)
					rightDap = "0" + rightDap;
				// 计算 IMSI 异或（Key Version +Key类型）
				String dap = doReverse(doXOR(imsi, rightDap)); // 先异或再取反
				dapLength = 32 - dap.length();
				for (int i = 0; i < dapLength; i++)
					dap = "0" + dap;
				// 调用加密机接口
				// OTAHSM.OTAAPI.GenerateAndExportSDKey(keyVersion,keyIndex,algFlag,divNum,hexDecode(domainAID+dap),keysLen,keys);
				// strKey = hexEncode(keys);
				switch (keyVersion) {
				case 0x10:
					strKey = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
					break;
				case 0x11:
					strKey = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
					break;
				case 0x12:
					strKey = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
					break;
				case 0x13:
					strKey = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
					break;
				default:
					strKey = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
				}
				sdKey.put(4, strKey);

				break;
			case 0x02: // 0x02 应用维护根密钥
				break;
			case 0x03: // 0x03 消费根密钥
				break;
			case 0x04: // 0x04 充值根密钥
				break;
			case 0x05: // 0x05 TAC根密钥
				break;
			case 0x06: // 0x06 PIN重装根密钥
				break;
			case 0x07: // 0x07 空中充值根密钥
				break;
			default:
			}
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		return sdKey;
	}

	/**
	 * 生成带MAc的apdu数据域
	 *
	 * @param randomNum
	 *            16字节随机数
	 * @param keyMain
	 *            主密匙
	 * @param appData
	 *            应用数据
	 * @param timeMills
	 *            时间戳
	 * @param keyMac
	 *            Mac密匙
	 * @return 要透传的数据
	 */
	public static String generateTransMac(String randomNum, String keyMain, String command, byte[] timeMillis, String keyMac) {
		String appdata = null;
		String command_head = null;
		if (command.length() < 256) {
			appdata = command.substring(10);
			command_head = command.substring(0, 10);
		} else {
			appdata = command.substring(12);
			command_head = command.substring(0, 12);
		}

		try {

			// 用主密匙keyMask使用3DES算法对16字节的随机数进行加密
			// logger.debug("用主密钥加密随机数："+randomNum);
			String processKey = SecurityUtil.desecb(keyMain, randomNum, 0);// 过程密钥
			// logger.debug("获得过程密钥"+processKey);
			String mdata; // 加密后的密文
			String used_data; // N字节要发送的数据信息
			if (appdata != null)
				used_data = appdata + SecurityUtil.hexEncode(timeMillis);
			else {
				used_data = SecurityUtil.hexEncode(timeMillis);
			}

			// if(used_data.length()%16!=0){
			// 用过程密钥Key对“N字节要发送的数据信息+8字节的时间戳”进行3DES算法加密(填充80)

			// logger.debug("用过程密钥加密数据："+SecurityUtil.padding80(used_data));
			mdata = SecurityUtil.desecb(processKey, SecurityUtil.padding80(used_data), 0);// 过程密钥加密数据
			// logger.debug("获得密文："+mdata);
			// }else{
			// //用过程密钥Key对“N字节要发送的数据信息+8字节的时间戳”进行3DES算法加密(填充80)
			// //logger.debug("用过程密钥加密数据："+used_data);
			// mdata=SecurityUtil.desecb(processKey,used_data,0);//过程密钥加密数据
			// //logger.debug("获得密文："+mdata);
			// }

			// 随机数生成MAC过程密钥
			// logger.debug("生成MAC过程密钥的随机数："+randomNum);
			String processKey_mac = SecurityUtil.desecb(keyMac, randomNum, 0);
			// logger.debug("随机数生成MAC过程密钥为："+processKey_mac);

			String ICV = "0000000000000000";
			// 用MAC密钥计算data的MAC
			String MAC_data = command_head + mdata + randomNum;
			String mac;
			if (MAC_data.length() % 16 != 0) {
				// logger.debug("用mac过程密钥加密数据："+SecurityUtil.padding80(MAC_data));
				mac = SecurityUtil.generateMAC(processKey_mac, SecurityUtil.padding80(MAC_data), ICV, 2, 1);
				// logger.debug("获得4字节mac："+mac);
			} else {
				// logger.debug("用mac过程密钥加密数据："+MAC_data);
				mac = SecurityUtil.generateMAC(processKey_mac, MAC_data, ICV, 2, 1);
				// logger.debug("获得4字节mac："+mac);
			}

			// 返回透传数据
			// logger.debug("返回密文+mac:"+command_head+mdata+randomNum+mac);
			return command_head + mdata + randomNum + mac;

		} catch (Exception e) {
			e.getMessage();
		}

		return null;
	}

	/**
	 * 生成带MAc的apdu数据域（传输数据，length是应用数据+时间戳+16字节随机数长度）
	 *
	 * @param randomNum
	 *            16字节随机数
	 * @param keyMain
	 *            主密匙
	 * @param appData
	 *            应用数据
	 * @param timeMills
	 *            时间戳
	 * @param keyMac
	 *            Mac密匙
	 * @return 要透传的数据
	 */
	public static String generateTransLenMac(String randomNum, String keyMain, String command, byte[] timeMillis, String keyMac) {
		String appdata = null;
		String command_head = null;
		if (command.length() < 256) {
			appdata = command.substring(10);
			command_head = command.substring(0, 10);
		} else {
			appdata = command.substring(12);
			command_head = command.substring(0, 12);
		}

		try {

			// 用主密匙keyMask使用3DES算法对16字节的随机数进行加密
			// logger.debug("用主密钥加密随机数："+randomNum);
			String processKey = SecurityUtil.desecb(keyMain, randomNum, 0);// 过程密钥
			// logger.debug("获得过程密钥"+processKey);
			String mdata; // 加密后的密文
			String used_data; // N字节要发送的数据信息
			if (appdata != null)
				used_data = appdata + SecurityUtil.hexEncode(timeMillis);
			else {
				used_data = SecurityUtil.hexEncode(timeMillis);
			}

			// if(used_data.length()%16!=0){
			// 用过程密钥Key对“N字节要发送的数据信息+8字节的时间戳”进行3DES算法加密(填充80)

			// logger.debug("用过程密钥加密数据："+SecurityUtil.padding80(used_data));
			mdata = SecurityUtil.desecb(processKey, SecurityUtil.padding80(used_data), 0);// 过程密钥加密数据
			// logger.debug("获得密文："+mdata);
			// }else{
			// //用过程密钥Key对“N字节要发送的数据信息+8字节的时间戳”进行3DES算法加密(填充80)
			// //logger.debug("用过程密钥加密数据："+used_data);
			// mdata=SecurityUtil.desecb(processKey,used_data,0);//过程密钥加密数据
			// //logger.debug("获得密文："+mdata);
			// }

			// 随机数生成MAC过程密钥
			// logger.debug("生成MAC过程密钥的随机数："+randomNum);
			String processKey_mac = SecurityUtil.desecb(keyMac, randomNum, 0);
			// logger.debug("随机数生成MAC过程密钥为："+processKey_mac);

			String ICV = "0000000000000000";
			int index = (mdata + randomNum).length() / 2;
			if (command.length() >= 256) {
				byte[] len = new byte[2];
				len[0] = (byte) ((index >> 8) & 0xff);
				len[1] = (byte) (index & 0xff);
				byte[] commandHead = SecurityUtil.hexDecode(command_head);
				System.arraycopy(len, 0, commandHead, 4, 2);
				command_head = SecurityUtil.hexEncode(commandHead);
			} else {
				byte[] len = new byte[1];
				len[0] = (byte) (index & 0xff);
				byte[] commandHead = SecurityUtil.hexDecode(command_head);
				System.arraycopy(len, 0, commandHead, 4, 1);
				command_head = SecurityUtil.hexEncode(commandHead);
			}
			// 用MAC密钥计算data的MAC
			String MAC_data = command_head + mdata + randomNum;
			String mac;
			if (MAC_data.length() % 16 != 0) {
				// logger.debug("用mac过程密钥加密数据："+SecurityUtil.padding80(MAC_data));
				mac = SecurityUtil.generateMAC(processKey_mac, SecurityUtil.padding80(MAC_data), ICV, 2, 1);
				// logger.debug("获得4字节mac："+mac);
			} else {
				// logger.debug("用mac过程密钥加密数据："+MAC_data);
				mac = SecurityUtil.generateMAC(processKey_mac, MAC_data, ICV, 2, 1);
				// logger.debug("获得4字节mac："+mac);
			}
			// 返回透传数据
			// logger.debug("返回密文+mac:"+command_head+mdata+randomNum+mac);
			return command_head + mdata + randomNum + mac;

		} catch (Exception e) {
			e.getMessage();
		}

		return null;
	}

	/**
	 * 生成带Mac的apdu数据域（mac密钥）
	 *
	 * @param randomNum
	 *            16字节随机数
	 * @param keyMain
	 *            主密匙
	 * @param appData
	 *            应用数据
	 * @param timeMills
	 *            时间戳
	 * @param keyMac
	 *            Mac密匙
	 * @return 要透传的数据
	 */
	public static String generateKeyMac(String randomNum, String keyMain, String command, byte[] timeMillis, String keyMac) {
		try {

			// 用主密匙keyMask使用3DES算法对16字节的随机数进行加密
			// logger.debug("用主密钥加密随机数："+randomNum);
			String processKey = SecurityUtil.desecb(keyMain, randomNum, 0);// 过程密钥
			// logger.debug("获得过程密钥"+processKey);

			// 生成SE的数据域
			byte[] seCommandData = MakeCommand_SE(command, (byte) 0x02, keyMain);
			// SE的APDU指令头
			byte[] seCommandHead = new byte[] { (byte) 0xf0, (byte) 0xc0, 0x00, 0x00, 0x40 };
			byte[] seCommand = new byte[42];
			System.arraycopy(seCommandHead, 0, seCommand, 0, 5);
			System.arraycopy(seCommandData, 0, seCommand, 5, 37);

			// 生成带mac值的指令数据
			String dataAddMac = generateTransLenMac(randomNum, keyMain, hexEncode(seCommand), timeMillis, keyMac);

			// 返回透传数据
			// logger.debug("返回Mac密匙密文+mac:"+dataAddMac);
			return dataAddMac;

		} catch (Exception e) {
			e.getMessage();
		}

		return null;
	}

//	/**
//	 * HEX解码 将形如122A01 转换为0x12 0x2A 0x01
//	 *
//	 * @param data
//	 * @return
//	 */
//	public static byte[] hexDecode(String data) {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		for (int i = 0; i < data.length(); i += 2) {
//			String onebyte = data.substring(i, i + 2);
//			int b = Integer.parseInt(onebyte, 16) & 0xff;
//			out.write(b);
//		}
//		return out.toByteArray();
//	}

	/**
	 * 拼装报文 （更新密钥）
	 *
	 * @param code_value
	 *            16字节的新密钥值
	 * @param code_tag
	 *            密钥标签
	 * @param keyMain
	 *            用于加密新密钥的主密钥
	 * @return 拼装好的报文
	 */
	public static byte[] MakeCommand_SE(String code_value, byte code_tag, String keyMain) {
		byte[] SE_command_head = new byte[] { (byte) 0x80, (byte) 0xfb, 0x00, 0x01, 0x20 };
		SE_command_head[2] = code_tag;

		byte[] SE_command_data = new byte[32];
		byte[] SE_command = new byte[37];

		String random = generalStringToAscii(8) + generalStringToAscii(8);
		byte[] random_byte = SecurityUtil.hexDecode(random);
		// logger.debug("生成随机数："+random);

		// 用主密匙keyMask使用3DES算法对16字节的随机数进行加密
		// logger.debug("用主密钥加密随机数："+random);
		String processKey = SecurityUtil.desecb(keyMain, random, 0);// 过程密钥
		// logger.debug("获得过程密钥"+processKey);

		// 用过程密钥processKey使用3DES算法对16字节的新密钥进行加密
		// logger.debug("用过程密钥加密新密钥："+code_value);
		String code_data = SecurityUtil.desecb(processKey, code_value, 0);// 密文
		byte[] code = SecurityUtil.hexDecode(code_data);

		// logger.debug("获得密文"+code_data);

		System.arraycopy(code, 0, SE_command_data, 0, 16);
		System.arraycopy(random_byte, 0, SE_command_data, 16, 16);
		System.arraycopy(SE_command_head, 0, SE_command, 0, 5);
		System.arraycopy(SE_command_data, 0, SE_command, 5, 32);
		// logger.debug("SE更新密钥的报文"+hexEncode(SE_command));
		return SE_command;
	}

	/**
	 * 根据客户端传入的交易报文，来做mac校验
	 *
	 * @author wangzt 20120301
	 * @param appData
	 *            交易报文
	 * @param keyMac
	 *            Mac密钥
	 * @return 校验成功或失败
	 */
	public static boolean volidMac(String appData, String keyMac) {

		String real_mac = appData.substring(appData.length() - 8).toUpperCase();
		// logger.debug("返回数据的MAC为："+real_mac);
		try {
			// 计算mac码
			String randomNum = appData.substring(appData.length() - 40, appData.length() - 8);// 得到16位随机数密文
			// logger.debug("返回数据的随机数为："+randomNum);
			String processKey = SecurityUtil.desecb(keyMac, randomNum, 0);
			// logger.debug("随机数生成MAC过程密钥为："+processKey);

			String ICV = "0000000000000000";
			String MAC_data = appData.substring(0, appData.length() - 8);
			String mac;
			if (MAC_data.length() % 16 != 0) {
				// logger.debug("用MAC密钥计算数据："+SecurityUtil.padding80(MAC_data));
				mac = SecurityUtil.generateMAC(processKey, SecurityUtil.padding80(MAC_data), ICV, 2, 1);
				// logger.debug("返回数据的mac值为："+mac);
			} else {
				// logger.debug("用MAC密钥计算数据："+MAC_data);
				mac = SecurityUtil.generateMAC(processKey, MAC_data, ICV, 2, 1);
				// logger.debug("返回数据的mac值为："+mac);
			}

			// 将计算出来的mac码的左边的4个字节与传入的客户端传入的mac码进行比较，如果一致说明校验成功，否则校验失败
			if (mac.equals(real_mac)) {
				return true;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return false;
	}

	/**
	 * 解密多惠拉客户端传过来的数据
	 *
	 * @author wangzt 20120301
	 * @param appData
	 * @param keyMain
	 * @return
	 */
	public static String desData(String appData, String keyMain) {
		try {

			String randomNum = appData.substring(appData.length() - 40, appData.length() - 8);// 得到16位随机数密文
			// logger.debug("返回数据的随机数为："+randomNum);
			String processKey = SecurityUtil.desecb(keyMain, randomNum, 0);// 得到解密的过程密钥
			// logger.debug("随机数生成过程密钥为："+processKey);

			// logger.debug("用过程密钥解密密文数据："+appData.substring(0,
			// appData.length()-44));
			String redata = SecurityUtil.desecb(processKey, appData.substring(0, appData.length() - 44), 1);
			// logger.debug("解密得到明文数据为："+redata);
			return redata;

		} catch (Exception e) {
			e.getMessage();
		}
		return null;

	}

	// 计算CRC
	public static char calcCRC(byte[] data, int length) throws Exception {
		if (data == null || data.length <= 0 || length <= 0) {
			throw new Exception("bad canshu");
		}

		char wCrc = 0xffff;
		int index = 0;
		byte ch;
		while (index < length) {
			ch = data[index];
			ch = (byte) (ch ^ (byte) (wCrc & 0x00ff));
			ch = (byte) (ch ^ (ch << 4));
			wCrc = (char) ((wCrc >> 8) ^ ((char) (ch & 0xff) << 8) ^ ((char) (ch & 0xff) << 3) ^ ((char) (ch & 0xff) >> 4));

			index++;
		}
		return wCrc;
	}

	// 读取*.apk文件最后50K的数据
	public static byte[] readResFileData(String filePath) {
		FileInputStream fis = null;
		DataInputStream dis = null;
		byte[] data = null;
		int size = 0;
		int length = 0;
		try {
			fis = new FileInputStream(new File(filePath));
			dis = new DataInputStream(fis);

			length = dis.available();

			int offset = 0;

			if (length > 51200) {
				offset = length - 51200;
				size = 51200;

				dis.skip(offset);
			} else {
				size = length;
			}

			data = new byte[size];

			try {
				dis.read(data, 0, size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			dis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	// TODO mmp中3.29更新后存在的代码，在mall中4.1更新后不存在，如果此方法没用可删除。
	// 读取*.apk文件最后50K的数据
	public static byte[] readResFileData(File file) {
		FileInputStream fis = null;
		DataInputStream dis = null;
		byte[] data = null;
		int size = 0;
		int length = 0;
		try {
			fis = new FileInputStream(file);
			dis = new DataInputStream(fis);

			length = dis.available();

			int offset = 0;

			if (length > 51200) {
				offset = length - 51200;
				size = 51200;

				dis.skip(offset);
			} else {
				size = length;
			}

			data = new byte[size];

			try {
				dis.read(data, 0, size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			dis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	public static void main(String[] args) {
		String va = SecurityUtil.desecb("03030303030303030404040404040404", "505152535455565758595A5B5C5D5E5F", 0);
		String pw = SecurityUtil.desData("4B9CE3149E91AD32ED82F2D31DCEDE4F52565648485752535349565556575154986FC3D6", "1234434556689886");
		System.out.println("505152535455565758595A5B5C5D5E5F");
		System.out.println(va);
		System.out.println(pw);
		System.out.println(hexEncode(generate8bytetime(System.currentTimeMillis())));
	}

	// public static void main(String[] args) {
	// // AppManager MAC算法
	// System.out
	// .println("----------------------------------------------AppManager MAC算法----------------------------------------------");
	// // String data =
	// //
	// "6A617661000000000E98681099031392005680021210A0000000770002FFFFFFFF890000030101939CF04F";
	// String data = "6A617661000000000F986810990313920056800301017C5661AF";
	// // String data = "6A617661000000001098681099031392005680030102CC489391";
	// // String data = "6A617661000000001198681099031392005680030101309F8F60";
	// data = "6A61766100000000129868109903139200568003010297DC92C9";
	// // data = "6A617661000000001398681099031392005680030101759E21BD";
	// // data = "6A617661000000001498681099031392005680030102C6D3D7A5";
	// // data = "6A617661000000001598681099031392005680030102B6948B06";
	// data = "6A61766100000000099868109903139200568003010002EAB069";
	// data = "6A617661000000001B9868109903139200564202060483715B01021758BFC9";
	//
	// // MoProtocol mo = new MoProtocol(data);
	// // String resMAC = GPMethods.descbc("0000000000000000",
	// // padding00(data.substring(8, data.length()-8)), "0000000000000000",
	// // 0);同下面等效
	// // String resMAC = GPMethods.generateMAC("0000000000000000", data
	// // .substring(8, data.length() - 8), "0000000000000000", 0, 1);
	// //
	// // System.out.println("data:" + data.substring(8, data.length() - 8));
	// // System.out.println("resMac:" + resMAC);
	// // System.out.println("Mac:" + mo.getMac());
	//
	// if (1 > 0) {
	// return;
	// }
	//
	// // DES信息
	// String keydes = "03030303030303030404040404040404";
	// String keydessingle = "0303030303030303";
	// String icvzero = "0000000000000000";
	// String datades = "505152535455565758595A5B5C5D5E5F";
	// // RSA信息
	// String n =
	// "104068165814066171453454170353038519486775042342492744766680368506290846517340070977945229091544342110906004745320891396630176448761681555736180304398377446761600298166596842949837816541409139121458074646397908554610644543809435287369060932764117559892734836154948380360808172922168093603524471797331116321859";
	// // N
	// // RSA的模modulus
	// String e = "65537"; // E RSA公钥的指数exponent
	// String d =
	// "72752599065218971135563975599963574908922979995478090151063182072481520729335378365282796222869456614634016104057585184369259565992724753308952756859729010539640388269145907721540432443682071900761111456168573670139906530408400434225165523664075502447958747204061635904931973644582923946604654696613953716993";
	// // D
	// // RSA私钥的指数exponent
	// String rsaPrivateKey =
	// "30820136020100300d06092a864886f70d0101010500048201203082011c020100028181009432af4cc3cf98dd44dc1dbc747bd37b43719670abe9c8caf92b4be4e35880d6ee8fe1d2e7dfcfaf2e5f33c0f63454042c88721372037c6a47188140fd0c2c25df2dd35062c84de30f6499b7db90b9ebf5424e9b0f0ba7f9c2a5efd44f374d90a96f44ae3689867780b090bc4928559c2ad5e34f7b23482a319db32887ba6c43020100028180679a6762cb213e44b1f70ff2c79c29646ae9e5ba2ef4e922bcbebff7ebd7db7a669cbfcaa9bf95796a79b3bc9a624aba0048033804e4dafd916658360eb18bca7d0ffc37c7eacfc498012fc7be707cfd0403b368ab2eab195c74252c717d690db87ffe0d2b8ae9d18ae6c034ab2ad32718c60d893d2ba9b2ca10acdf8c8cc301020100020100020100020100020100";
	// // RSA私钥
	// // 用PKCS#8编码
	// String rsaPublicKey =
	// "30819f300d06092a864886f70d010101050003818d00308189028181009432af4cc3cf98dd44dc1dbc747bd37b43719670abe9c8caf92b4be4e35880d6ee8fe1d2e7dfcfaf2e5f33c0f63454042c88721372037c6a47188140fd0c2c25df2dd35062c84de30f6499b7db90b9ebf5424e9b0f0ba7f9c2a5efd44f374d90a96f44ae3689867780b090bc4928559c2ad5e34f7b23482a319db32887ba6c430203010001";
	// // RSA公钥
	// // 用X.509编码
	// // SCP信息
	// String keymac = "05050505050505050606060606060606";
	// String keyenc = "08080808080808080707070707070707";
	// String keydek = "09090909090909090101010101010101";
	// String keydap = "04040404040404040606060606060606";
	// String keytoken = "03030303030303030505050505050505";
	// String hostrand = "1234567812345678";
	// String loadfile =
	// "01001ADECAFFED010204000110A0000000770002FFFFFFFF890000000002001F001A001F00140014005E001A017C00210037000000EE000400020"
	// +
	// "01102010004001402020107A0000000620101010106A000000151000300140110A0000000770002FFFFFFFF8900000300001706001A0080030300"
	// +
	// "0104040004002DFFFF00230037009200ED0106014C07017C000441188C0005191E25290418191E044116048B00147A04308F0016181D1E8C00037"
	// +
	// "A0210188D0004870004780110AD008E010006057A0222198B00072D1A032510FB53321F611C1A0425730010FFA4FFA4000918198B00087008116D"
	// +
	// "008D00097A1F10806B261A042575001B0002FFD8000DFFF2001418198B000A700E18198B000B700718198B000C7A116E008D00097A0522188B000"
	// +
	// "D6108116A818D0009198B000E3B198B00072D1A03106F381A051084381A068D000F1A078B001038071A062541321A1F3D0441327B0011925B387B"
	// +
	// "0011031A1F7B0011928D0012321A041F05435B3819031F8B00137A032018AD00198E020006008901AF0160091908AF018B00137A05218D0015100"
	// +
	// "76A081169858D0009198B00072D1A032510846A081169828D000918198B000E8902AD001A03AF0208418E040006023BAD001A10081A1007258E04"
	// +
	// "0006033B7A0521198B00072D1A032510846B1718198B000E8902AD001A03AF0208418E040006023B1A088D0015381908048B00137A08002100040"
	// +
	// "0020002030003A5010103000E00000000000000000000000000000000000005005E00170200000002000001020000020600000106810304068003"
	// +
	// "000181020003800A0103000080068007010300008203000083030000810380030303800A060680080303800603050000000680100203800A08038"
	// +
	// "003020681030101000000090037000D2A06C10802062C02040A2302040026050F0606070C081C081B070707070804051005100606040E0B0E060A"
	// + "040E050E0F090D0E0707";
	// String scp01Msisdn = "15926348766";
	// String scp02Msisdn = "15926348788";
	// // 0348信息
	// String kic = "0f0a0f01060f0c0c07060b070804030d";
	// String kid = "020805080f0b050400080b0b040d0f00";
	// String kik = "E81378177614AC8BBBD8546729C80939";
	// String spi = "0A091015";
	// String tar = "000000";
	// String counter = "0000000001";
	// String packetMT =
	// "80E602001B10D2760000050002FFFFFFFF8900000202000006EF04C602000000";
	// String bigpacketMT =
	// "80E602006C10D2760000050002FFFFFFFF8900000202000006EF04C60200000010D2760000050002FFFFFFFF8900000202000006EF04C60200000010D2760000050002FFFFFFFF8900000202000006EF04C60200000010D2760000050002FFFFFFFF8900000202000006EF04C602000000";
	// String packetMO = "02710000171200000000000000010000"
	// + generateMACAlg1(kid,
	// "0271000017120000000000000001000001900000", icvzero, 0,
	// 2) + "01900000";
	// System.out
	// .println("----------------------------------------------基本算法----------------------------------------------");
	// // 基本算法
	// System.out.println("DES-CBC算法："
	// + descbc(keydessingle, datades, icvzero, 0));
	// System.out.println("DES-ECB算法：" + desecb(keydessingle, datades, 0));
	// System.out.println("3DES-CBC算法：" + descbc(keydes, datades, icvzero, 0));
	// System.out.println("3DES-ECB算法：" + desecb(keydes, datades, 0));
	// System.out.println("MAC算法3："
	// + generateMAC(keydes, hostrand + datades, icvzero, 2, 2));
	// System.out.println("MAC算法3："
	// + generateMAC(keydes, datades,
	// desecb(keydessingle, hostrand, 0), 2, 2));
	// System.out.println("MAC算法1："
	// + generateMACAlg1(keydes, hostrand + datades, icvzero, 2, 2));
	// System.out.println("MAC算法2："
	// + generateMACAlg2(keydes, hostrand + datades, icvzero, 2, 2));
	// System.out.println("MAC算法4："
	// + generateMACAlg4(keydes, hostrand + datades, icvzero, 2, 2));
	// System.out.println("随机数算法：" + yieldHexRand(16));
	// System.out.println("异或算法：" + doXOR(kic, kid));
	// System.out.println("求反算法：" + doReverse(kic));
	// System.out
	// .println("----------------------------------------------SCP02 Initialize Update命令----------------------------------------------");
	// // SCP02 Initialize Update命令
	// // KeySessionInfo sessionInfo = new KeySessionInfo(); //
	// 保存主机随机数，以便后续命令使用。
	// // sessionInfo.setHostrand(hostrand);
	// // keySessionInfo.put(scp02Msisdn, sessionInfo);
	// System.out.println("SCP02 Initialize Update命令：" + "8050010008"
	// + hostrand);
	// System.out
	// .println("----------------------------------------------计算SCP02卡片密文----------------------------------------------");
	// // 计算SCP02卡片密文
	// String derivationstr = "0182" + "1135" + "000000000000000000000000";//
	// 过程密钥常量0182
	// String senc = descbc(keyenc, derivationstr, icvzero, 0);
	// System.out.println("SCP02过程密钥SENC：" + senc);
	// String cardTempstr = hostrand + "1135" + "123456123456";
	// String testTempCardcryp = descbcNeedPadding80(senc, cardTempstr,
	// icvzero, 0);
	// String testCardcryp = testTempCardcryp.substring(testTempCardcryp
	// .length() - 16);
	// System.out.println("SCP02卡片密文：" + testCardcryp);
	// System.out.println("SCP02卡片密文："
	// + generateMACAlg1(senc, cardTempstr, icvzero, 2, 2));
	// System.out
	// .println("----------------------------------------------SCP02 External Authenticate命令----------------------------------------------");
	// // SCP02 External Authenticate命令
	// // String initresp = "1234567890123456789000021135123456123456"
	// // + testCardcryp;
	// // System.out.println("SCP02 External Authenticate命令："
	// // + externalAuthenticate(scp02Msisdn, initresp, keymac, keyenc,
	// // keydek, icvzero, "0215", 3));
	// // System.out.println("SCP02下行包命令："
	// // + generateSCPPacket(scp02Msisdn, packetMT, ""));
	// // System.out
	// //
	// .println("----------------------------------------------SCP01 Initialize Update命令----------------------------------------------");
	// // // SCP01 Initialize Update命令
	// // KeySessionInfo sessionInfo1 = new KeySessionInfo(); //
	// 保存主机随机数，以便后续命令使用。
	// // sessionInfo1.setHostrand(hostrand);
	// // keySessionInfo.put(scp01Msisdn, sessionInfo1);
	// // System.out.println("SCP01 Initialize Update命令：" + "8050010008"
	// // + hostrand);
	// // System.out
	// //
	// .println("----------------------------------------------计算SCP01卡片密文----------------------------------------------");
	// // 计算SCP01卡片密文
	// String sRandCard = "1135123456123456";
	// String derivationstr1 = sRandCard.substring(8)
	// + hostrand.substring(0, 8) + sRandCard.substring(0, 8)
	// + hostrand.substring(8);
	// String senc1 = desecb(keyenc, derivationstr1, 0);
	// System.out.println("SCP01过程密钥SENC：" + senc1);
	// String cardTempstr1 = hostrand + sRandCard;
	// String testTempCardcryp1 = descbcNeedPadding80(senc1, cardTempstr1,
	// icvzero, 0);
	// String testCardcryp1 = testTempCardcryp1.substring(testTempCardcryp1
	// .length() - 16);
	// System.out.println("SCP01卡片密文：" + testCardcryp1);
	// System.out.println("SCP01卡片密文："
	// + generateMACAlg1(senc1, cardTempstr1, icvzero, 2, 2));
	// System.out
	// .println("----------------------------------------------SCP01 External Authenticate命令----------------------------------------------");
	// // SCP01 External Authenticate命令
	// String initresp1 = "1234567890123456789000021135123456123456"
	// + testCardcryp1;
	// // System.out.println("SCP01 External Authenticate命令："
	// // + externalAuthenticate(scp01Msisdn, initresp1, keymac, keyenc,
	// // keydek, icvzero, "0105", 1));
	// // System.out.println("SCP01下行包命令："
	// // + generateSCPPacket(scp01Msisdn, packetMT, ""));
	// // System.out
	// //
	// .println("----------------------------------------------0348封装解析----------------------------------------------");
	// // 0348封装
	// /*System.out.println("0348下行包命令："
	// + generate0348Packet(2, tar, counter, spi, kic, kid, packetMT));
	// System.out.println("0348下行BIG包命令："
	// + generate0348Packet(2, tar, counter, spi, kic, kid,
	// bigpacketMT));*/
	// System.out.println("0348上行包命令：" + packetMO);
	// // Response0348 response0348 = analyse0348ResponsePacket(2, spi, kic,
	// kid,
	// // packetMO);
	// // System.out.println("0348上行包解析：TAR->" + response0348.getTar()
	// // + ",CNTR->" + response0348.getCounter() + ",PCNTR->"
	// // + response0348.getPPCNTR() + ",STATUS->"
	// // + response0348.getPStatus() + ",DATA->"
	// // + response0348.getPlainData());
	// // System.out.println("0348密钥加密：" + desecb(kik, kic, 0));
	// // System.out
	// //
	// .println("----------------------------------------------敏感数据加解密、生成密钥校验值、消息摘要、DAP、Token----------------------------------------------");
	// // 敏感数据加解密、生成密钥校验值、消息摘要、DAP、Token
	// // System.out.println("敏感数据加密："
	// // + encryptOrDecryptKey(scp02Msisdn, keyenc, 1, 0));
	// System.out.println("生成密钥校验值：" + generateKeyCheck(keyenc, 3));
	// String strhash = generateSHA1(loadfile);
	// System.out.println("SHA-1消息摘要：" + strhash);
	// String strtoken = "02003610A0000000770002FFFFFFFF89000000000014"
	// + strhash + "0EEF0CC6020000C7020000C8020000";
	// System.out.println("Token数据：" + strtoken);
	// // System.out.println("Token命令："
	// // + generateTokenPacket(keytoken, strtoken, 1, "E6", scp02Msisdn,
	// // "00"));
	// System.out
	// .println("DAP：" + generateMAC(keydap, strhash, icvzero, 2, 2));
	// System.out.println("DAP：" + generateDAP(keydap, loadfile, 1));
	// System.out
	// .println("----------------------------------------------RSA算法----------------------------------------------");
	// // RSA算法
	// String tokenRSA = generateRSA(n, e, d, strtoken, 0, 0);
	// System.out.println("RSA算法私钥加密：" + tokenRSA);
	// System.out.println("RSA算法公钥解密：" + generateRSA(n, e, d, tokenRSA, 1, 0));
	// String tokenRSA1 = generateRSA(n, e, d, strtoken, 0, 1);
	// System.out.println("RSA算法公钥加密：" + tokenRSA1);
	// System.out
	// .println("RSA算法私钥解密：" + generateRSA(n, e, d, tokenRSA1, 1, 1));
	// String tokenSHA1RSA = generateSHA1withRSA(n, d, strtoken);
	// System.out.println("SHA1withRSA算法加密：" + tokenSHA1RSA);
	// System.out.println("SHA1withRSA算法验证："
	// + verifySHA1withRSA(n, e, strtoken, tokenSHA1RSA));
	// String tokenRSA2 = generateRSA(rsaPrivateKey, strtoken, 0, 0);
	// System.out.println("RSA算法私钥加密：" + tokenRSA2);
	// System.out.println("RSA算法公钥解密："
	// + generateRSA(rsaPublicKey, tokenRSA2, 1, 0));
	// String tokenSHA1RSA1 = generateSHA1withRSA(rsaPrivateKey, strtoken);
	// System.out.println("SHA1withRSA算法加密：" + tokenSHA1RSA1);
	// System.out.println("SHA1withRSA算法验证："
	// + verifySHA1withRSA(rsaPublicKey, strtoken, tokenSHA1RSA1));
	//
	// }

	/**
	 * 该函数具有两个功能： 1.产生一个具有length长度的随机数 2.将该随机数转化为ascii的形式
	 *
	 * @param length
	 * @return ascii字符串
	 */
	public static String generalStringToAscii(int length) {

		int num = 1;
		String strRandom;

		for (int i = 0; i < length; i++) {
			num *= 10;
		}

		try {
			Thread.sleep(10);
			Random rand = new Random((new Date()).getTime());
			strRandom = Integer.toString(rand.nextInt(num));

			int len = strRandom.length();
			for (int i = 0; i < length - len; i++) {
				strRandom = "0" + strRandom;
			}

		} catch (InterruptedException e) {
			strRandom = "00000000";
		}

		StringBuffer sbu = new StringBuffer();
		char[] chars = strRandom.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			sbu.append((int) chars[i]);
		}
		return sbu.toString();
	}

	/**
	 * 解密数据
	 *
	 * @param key
	 * @param actionInfo
	 *            actionInfo密文
	 * @return 明文
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String key, String actionInfo) throws UnsupportedEncodingException {
		LogU.e("key", key);
		LogU.e("actionInfo", actionInfo);
		// 获取随机数
		String randData = actionInfo.substring(0, 32);
		// 获取应用密文
		String singData = actionInfo.substring(32, actionInfo.length());
		// 获取过程密钥
		String processKey = SecurityUtil.desecb(key, randData, 0);
		// 解密singData
		String actionInfoString = SecurityUtil.desecb(processKey, singData, 1);
		// 将16进制数字解码成字符串,适用于所有字符（包括中文）
		actionInfoString = SecurityUtil.hexStringToString(actionInfoString);
		// 最后一个'80'出现的位置
		int num = actionInfoString.lastIndexOf("80");
		// 截取actionInfoString
		if (num != -1) {
			actionInfoString = actionInfoString.substring(0, num);
		}
		// actionInfoString转换为字符串
		actionInfoString = new String(hexDecode(actionInfoString), "UTF-8");

		// System.out.println("完成解密，json\n"+actionInfoString);
		LogU.e("完成解密  actionInfo", actionInfoString);
		return actionInfoString;
	}

	/**
	 * 加密数据
	 *
	 * @param key
	 * @param actionInfo
	 *            actionInfo明文
	 * @return actionInfo密文
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String key, String actionInfo) throws UnsupportedEncodingException {
		LogU.e("key", key);
		LogU.e("actionInfo", actionInfo);
		// 生成随机数
		String randData = generalStringToAscii(8) + generalStringToAscii(8);
		// 获取过程密钥
		String processKey = SecurityUtil.desecb(key, randData, 0);
		// 将actionInfo转换16进制后，补80
		actionInfo = SecurityUtil.padding80(SecurityUtil.hexEncode(actionInfo.getBytes("UTF-8")));
		// 将字符串编码成16进制数字,适用于所有字符（包括中文）
		actionInfo = SecurityUtil.encodeHexString(actionInfo);
		// 加密
		actionInfo = SecurityUtil.desecb(processKey, actionInfo, 0);
		// 最终生成密文
		String end = randData + actionInfo;

		// System.out.println("完成加密，json\n"+actionInfo);
		LogU.e("完成加密 actionInfo", actionInfo);
		return end;
	}

	/**
	 * 脚本算MAC
	 * key 密钥
	 * data 数据
	 * V 初始向量
	 */
	public static String clacMac(String key,String data,String V){
		byte[] vb=hexDecode(V);

		byte[] kma=hexDecode(key.substring(0,16));
		byte[] kmb=hexDecode(key.substring(16));
		String mac=null;
		StringBuffer src=new StringBuffer(data);
		if(src.length()%16==0){
			src.append("8000000000000000");
		}else{
			src.append("80");
			while(src.length()%16!=0){
				src.append('0');
			}
		}

		int partCount=src.length()/16;//共多少组

//		System.out.println("补齐后>>>"+src.toString());

		byte[][] depart=new byte[partCount][];

		for(int i=0;i<partCount;i++){
			depart[i]=hexDecode(src.substring(i*16, (i+1)*16));
		}
		byte[] I=xor(vb, depart[0]);

//		System.out.println(Utils.hexEncode(vb)+" XOR "+Utils.hexEncode(depart[0])+"  =  "+Utils.hexEncode(I));

		String O=null;
		for(int i=1;i<partCount;i++){
			O=desecb(hexEncode(kma),hexEncode(I),0);
//			System.out.println("对"+Utils.hexEncode(I)+"做 des ,key = "+Utils.hexEncode(kma)+" ,结果为："+O);
			I=xor(hexDecode(O), depart[i]);
//			System.out.println(O+" XOR "+Utils.hexEncode(depart[i])+"  =  "+Utils.hexEncode(I));
		}
		O=desecb(hexEncode(kma),hexEncode(I),0);
//		System.out.println("对"+Utils.hexEncode(I)+"做 des ,key = "+Utils.hexEncode(kma)+" ,结果为："+O);
//		String tmpo=new String(O);
//		O=Tool.desecb(Utils.hexEncode(kmb),O,1);
//		System.out.println("对"+tmpo+"做 des解密 ,key = "+Utils.hexEncode(kmb)+" ,结果为："+O);
//		tmpo=new String(O);
//		O=Tool.desecb(Utils.hexEncode(kma),O,0);
//		System.out.println("对"+tmpo+"做 des ,key = "+Utils.hexEncode(kma)+" ,结果为："+O);

		mac=O.substring(0, 8);
		return mac;
	}

	 /**
     * Decodes a TYPE_BCD-encoded number as a String.
     *
     * @param buf The byte buffer containing the TYPE_BCD data.
     */
    public static String Bcd2String(byte[] buf) throws IndexOutOfBoundsException {
        int length = buf.length;
        char[] digits = new char[length * 2];
        int start = 0;

        for (int i = 0; i < length; i++) {
            digits[start++] = (char) ((((buf[i] & 0x00f0) >> 4) > 9) ?
                    ((buf[i] & 0x00f0) >> 4) + 55 : ((buf[i] & 0x00f0) >> 4) + 48);
            digits[start++] = (char) (((buf[i] & 0x000f) > 9) ?
                    (buf[i] & 0x000f) + 55 : (buf[i] & 0x000f) + 48);
        }

        return new String(digits);
    }
}
