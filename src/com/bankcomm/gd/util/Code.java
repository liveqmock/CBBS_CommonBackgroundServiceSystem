package com.bankcomm.gd.util;

/**
 * �����ֽ��롢ascii��Ȳ�����ͨ����
 * @author ������
 *
 */
public class Code {

	/**
	  * ��16�����ֽ��룬ת����ascii��ʽ
	  * �磺byte[]{0x2B, 0x44, 0xEF, 0xD9} --> "2B44EFD9" 
	  * @param b byte[] 16�����ֽ���
	  * @return String ascii�ַ���
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
	  * ��ָ��ascii�ַ�������ÿ�����ַ�Ϊ��λת��Ϊ16������ʽ
	  * �磺"2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	  * @param src String ascii�ַ���
	  * @return byte[] 16�����ֽ���
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
	  * ������ASCII�ַ��ϳ�һ���ֽڣ�
	  * �磺"EF"--> 0xEF
	  * @param src0 byte ��4λ
	  * @param src1 byte ��4λ
	  * @return byte ���ֽ�
	  */
	public static byte uniteBytes(byte src0, byte src1) {
	   byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
	   _b0 = (byte)(_b0 << 4);
	   byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
	   byte ret = (byte)(_b0 ^ _b1);
	   return ret;
	}
	

}
