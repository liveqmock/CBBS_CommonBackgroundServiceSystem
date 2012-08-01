package com.bankcomm.gd.cbbs;

import java.util.Arrays;
/*import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetAddress;*/
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.bankcomm.gd.test.YctTest;
import com.bocom.eb.des.EBDES;

/**
 * 处理逻辑
 * @deprecated
 */
public class CommunicationProtocol {

	private static Log log = LogFactory.getLog(CommunicationProtocol.class);

	/**
	 * 处理收付通宝加解密逻辑
	 * @param inputstr 接收报文
	 * @return 返回报文
	 */
	public String processInput(String inputstr){

		String resultstr = "";
		final int packetHead = 8;
		final int packetLength = packetHead+67;

		//首先判断位数是否足够
		if(packetLength!=inputstr.length()){
			log.error("报文长度不正确,接收字段为:["+inputstr+"]");
			return this.ErrMsg("报文长度不正确");
		}

		//拆包
		String strInUse = inputstr.substring(8,packetLength);
		log.info("报文体: ["+strInUse+"]");
		String txnCod = strInUse.substring(0,6);
		String encryStatus = strInUse.substring(6,7);//0:解密;1:加密
		String password = strInUse.substring(7,27).trim();
		String sessionId = strInUse.substring(27,67);
		log.info("交易码:["+txnCod+"]|加解密标志:["+encryStatus+"]|密码:["+password+"]|SESSION:["+sessionId+"]");

		//通过判断交易码进行处理
		if("444555".equals(txnCod)){

			if("0".equals(encryStatus)){//如果是0,走解密流程

				try {
					resultstr= EBDES.decryptoDES(password, sessionId);
			    }
			    catch (Exception ex) {
			    	log.trace(ex);
			    	resultstr=this.ErrMsg("解密失败");
			    }
			}else if("1".equals(encryStatus)){//如果是1,走加密流程

				try {
					resultstr = EBDES.encryptoDES(password, sessionId);
			    }
			    catch (Exception ex) {
			    	log.trace(ex);
			    	resultstr=this.ErrMsg("加密失败");
			    }
			}else{//传送标志位异常
				resultstr=this.ErrMsg("加解密标志出错");
			}

		}else{//无此交易码
			resultstr=this.ErrMsg("交易码错误");
		}

		return "00000026"+this.NormalMsg(resultstr);
	}

	private String ErrMsg(String msg){
		final String ERRCODE = "999999";
		final int msgLength = 20;
		String inputMsg = "";
		if(msgLength < msg.length()){
			inputMsg = msg.substring(0, 20);
		}else if(msgLength > msg.length()){
			byte   b[]   =   new   byte[msgLength-msg.length()]; 
			Arrays.fill(b, (byte)' '); 
			inputMsg = msg + new String(b); 
		}else{
			inputMsg = msg;
		}
		return ERRCODE+inputMsg;

	}

	private String NormalMsg(String msg){
		final String NORMAL_CODE = "000000";
		final int msgLength = 20;
		String inputMsg = "";
		if(msgLength < msg.length()){
			inputMsg = msg.substring(0, 20);
		}else if(msgLength > msg.length()){
			byte   b[]   =   new   byte[msgLength-msg.length()]; 
			Arrays.fill(b, (byte)' '); 
			inputMsg = msg + new String(b); 
		}else{
			inputMsg = msg;
		}
		return NORMAL_CODE+inputMsg;

	}
}
