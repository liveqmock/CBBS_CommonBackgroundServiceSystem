package com.bankcomm.gd.util;

/**
 * 处理字节码、ascii码等操作的通用类
 * @author 顾启明
 *
 */
public class Code {

	/**
	  * 将16进制字节码，转换成ascii形式
	  * 如：byte[]{0x2B, 0x44, 0xEF, 0xD9} --> "2B44EFD9" 
	  * @param b byte[] 16进制字节码
	  * @return String ascii字符串
	  */
	public static String Bytes2HexString(byte[] b) {
	   String ret = "";
	   for (int i = 0; i < b.length; i++) {
	     String hex = Integer.toHexString(b[i] & 0xFF);
	     if (hex.length() == 1) {
	       hex = '0' + hex;
	     }
	     ret += hex.toUpperCase();
	   }
	   return ret;
	}

	/**
	  * 将指定ascii字符串，以每两个字符为单位转换为16进制形式
	  * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	  * @param src String ascii字符串
	  * @return byte[] 16进制字节码
	  */
	public static byte[] HexString2Bytes(String src){
		int strLength = src.length()/2;
	   byte[] ret = new byte[strLength];
	   byte[] tmp = src.getBytes();
	   for(int i=0; i<strLength; i++){
	     ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);
	   }
	   return ret;
	}

	/**
	  * 将两个ASCII字符合成一个字节；
	  * 如："EF"--> 0xEF
	  * @param src0 byte 高4位
	  * @param src1 byte 低4位
	  * @return byte 新字节
	  */
	public static byte uniteBytes(byte src0, byte src1) {
	   byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
	   _b0 = (byte)(_b0 << 4);
	   byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
	   byte ret = (byte)(_b0 ^ _b1);
	   return ret;
	}
	

}
