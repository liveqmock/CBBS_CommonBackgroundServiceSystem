package com.bankcomm.gd.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;

public class YctTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String ReqDat = "FE01000183800080491510CAF24FB6CA491510CAF24FB6CAF66D057B9F83F809A92852DFCB1CB40E841178CFDA40445F491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA";

		Socket socket = new Socket("10.240.13.201", 5003);
		// 外发报文进行通讯并返回
		PrintWriter pw = null;
		BufferedReader br = null;
		//pw = new PrintWriter(socket.getOutputStream(), true);
		//br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		/*
		 * if(!(socket.isConnected())){ log.info("socket is not connect");
		 * }else{ log.info("socket is connect"); }
		 */
		byte[] bit_msg = YctTest.HexString2Bytes(ReqDat);
		//char[] char_msg = new char[bit_msg.length];
		/*for(int i=0;i<bit_msg.length;i++){
			char_msg[i]=(char)bit_msg[i];
		}*/
		
		System.out.println("sending msg...");
		//pw.println(String.valueOf(char_msg));
		/*FileWriter fw = new FileWriter("tmp");
		fw.write(String.valueOf(char_msg));*/
		FileOutputStream fos = new FileOutputStream("tmp2");
		//fos.write(bit_msg);
		os.write(bit_msg);
		System.out.println("receiving msg...");
		String content = null;
		/*
		 * for(int count=0;count<10000;count++){
		 * if((content=br.readLine())!=null){ return content; } }
		 */
		/*while ((content = br.readLine()) != null) {
			// log.info(arg0)
		}*/
		byte[] bbuf = new byte[136];
		int hasRead =0;
		while((hasRead=is.read(bbuf))>0){
			fos.write(bbuf, 0, hasRead);
			fos.flush();
		}
		fos.close();
		content = Bytes2HexString(bbuf);
		System.out.println(content);

	}

	public static String convertStringToHex(String str) {

		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		return hex.toString();
	}

	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}

	/**
	  * 将两个ASCII字符合成一个字节；
	  * 如："EF"--> 0xEF
	  * @param src0 byte
	  * @param src1 byte
	  * @return byte
	  */
	public static byte uniteBytes(byte src0, byte src1) {
	   byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
	   _b0 = (byte)(_b0 << 4);
	   byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
	   byte ret = (byte)(_b0 ^ _b1);
	   return ret;
	}
	/**
	  * 将指定字符串src，以每两个字符分割转换为16进制形式
	  * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	  * @param src String
	  * @return byte[]
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
	  * 将指定byte数组以16进制的形式打印到控制台
	  * @param hint String
	  * @param b byte[]
	  * @return void
	  */
	public static void printHexString(String hint, byte[] b) {
	   System.out.print(hint);
	   for (int i = 0; i < b.length; i++) {
	     String hex = Integer.toHexString(b[i] & 0xFF);
	     if (hex.length() == 1) {
	       hex = '0' + hex;
	     }
	     System.out.print(hex.toUpperCase() + " ");
	   }
	   System.out.println("");
	}
	/**
	  *
	  * @param b byte[]
	  * @return String
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
}
