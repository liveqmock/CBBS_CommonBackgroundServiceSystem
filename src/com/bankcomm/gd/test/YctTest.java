package com.bankcomm.gd.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class YctTest {

	private static Log log = LogFactory.getLog(YctTest.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Stream();
		//Writer_Reader();

	}

	public static void Writer_Reader() throws IOException{
		PrintWriter pw = null;
		BufferedReader br = null;
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("10.240.13.201", 5003));
		pw = new PrintWriter(socket.getOutputStream(),true);
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		log.info("sending msg...");
		String ReqDat = "FE01000183800080491510CAF24FB6CA491510CAF24FB6CAF66D057B9F83F809A92852DFCB1CB40E841178CFDA40445F491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA";
		byte[] bit_msg = YctTest.HexString2Bytes(ReqDat);
		pw.println(bit_msg);
		log.info("receiving msg...");
		String content=null;
		while((content=br.readLine())!=null){
			//content = Bytes2HexString(strFromIcs.toCharArray());
			log.info(content);
			
			
		}

		log.info("sending msg...");
		String ReqDat2 = "FE01000183800080491510CAF24FB6CA491510CAF24FB6CAF66D057B9F83F809A92852DFCB1CB40E841178CFDA40445F491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA";
		byte[] bit_msg2 = YctTest.HexString2Bytes(ReqDat2);
		pw.println(bit_msg);
		log.info("receiving msg...");
		//String content=null;
		/*while((byte[] content2=br.readLine())!=null){
			//content = Bytes2HexString(strFromIcs.toCharArray());
			log.info(content);
			
			
		}*/
	}
	
	public static String Stream_Send(Socket socket, String ReqDat) throws IOException{
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		
		try{
			log.info("sending msg...");
			log.info(ReqDat);
			byte[] bit_msg = YctTest.HexString2Bytes(ReqDat);
			os.write(bit_msg);
			log.info("receiving msg...");
			int hasRead =0;
			int totleRead=0;
			byte[] bbuf = new byte[1024];
			if((hasRead=is.read(bbuf))>0){
				totleRead+=hasRead;
				log.info(1);
			}
			byte[] _bbuf=new byte[totleRead];
			for(int i=0; i<totleRead;i++){
				_bbuf[i]=bbuf[i];
			}
			String content = Bytes2HexString(_bbuf);
			log.info(content);
			return content;
		}finally{
	    	/*if(null!=os){
	    		os=null;
	    	}
	    	if(null!=is){
	    		is.close();
	    	}*/
		}

	}
	
	public static void Stream() throws IOException{
		//Socket socket = new Socket("10.240.13.201", 5003);
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("10.240.13.201", 5003));

		//String ReqDat = "FE01000183800080491510CAF24FB6CA491510CAF24FB6CAF66D057B9F83F809A92852DFCB1CB40E841178CFDA40445F491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA";
		//String ReqDat = "FE0300018380008077B2131B8F170CDD77B2131B8F170CDDAA2542638008A38675987E61E511ECB1F5510B8DFAA673853C91DC71C76D168C77B2131B8F170CDD77B2131B8F170CDD77B2131B8F170CDD77B2131B8F170CDDA85DEE21F300888A491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA";
		//String ReqDat = "AA630001028000986B2A3DE0B49A138222402C0DB56665C3315A766677B05DDC006AF679B9A906F4F5C9722F6210806D08757FF6FF2EB247BE2CE80DBF1EA20CEC90C13ADE5ADA00252C4EA060D6D82C6C4B3CE5E3C55BD059C75C22640F8DB55A21BEEAAF282540A9FCDD5C5938F2338BC26E086C7616FCE33BCA50A62E62AD8F27269FA37757E322ABD769CA0B6CA744AA1282F87B34ACAC0A0E3A441C9576";
		//ǩ��1
		Stream_Send(socket, "FE01000183800080491510CAF24FB6CA491510CAF24FB6CAF66D057B9F83F809A92852DFCB1CB40E841178CFDA40445F491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA");
		if(socket.isClosed()){
			log.info("socket closed");
		}else{
			log.info("socket opened");
		}
		if(socket.isConnected()){
			log.info("socket Connected");
		}else{
			log.info("socket not Connected");
		}
		if(socket.isInputShutdown()){
			log.info("socket InputShutdown");
		}else{
			log.info("socket not InputShutdown");
		}
		if(socket.isOutputShutdown()){
			log.info("socket OutputShutdown");
		}else{
			log.info("socket not OutputShutdown");
		}
		//ǩ��2
		Stream_Send(socket, "FE0300018380008077B2131B8F170CDD77B2131B8F170CDDAA2542638008A38675987E61E511ECB1F5510B8DFAA673853C91DC71C76D168C77B2131B8F170CDD77B2131B8F170CDD77B2131B8F170CDD77B2131B8F170CDDA85DEE21F300888A491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA");
		/*Socket socket2 = new Socket();
		socket2.connect(new InetSocketAddress("10.240.13.201", 5003));
		Stream_Send(socket2, "FE01000183800080491510CAF24FB6CA491510CAF24FB6CAF66D057B9F83F809A92852DFCB1CB40E841178CFDA40445F491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA491510CAF24FB6CA");*/
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
		log.info("Decimal : " + temp.toString());

		return sb.toString();
	}

	/**
	  * ������ASCII�ַ��ϳ�һ���ֽڣ�
	  * �磺"EF"--> 0xEF
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
	  * ��ָ���ַ���src����ÿ�����ַ��ָ�ת��Ϊ16������ʽ
	  * �磺"2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
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
	  * ��ָ��byte������16���Ƶ���ʽ��ӡ������̨
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
	   log.info("");
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