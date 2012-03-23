package com.bankcomm.gd;

import java.util.Arrays;
import com.bocom.eb.des.EBDES;

public class CommunicationProtocol {

	public String  processInput(String inputstr){

		String resultstr = "";
		final int packetHead = 8;
		int packetLength = packetHead+67;

		//System.out.println(packetLength);
		//首先判断位数是否足够
		if(packetLength!=inputstr.length()){
			System.out.println(inputstr.length());
			return this.ErrMsg("报文长度不正确");
		}

		//拆包
		String strInUse = inputstr.substring(8,packetLength);
		System.out.println(strInUse);
		String txnCod = strInUse.substring(0,6);
		String encryStatus = strInUse.substring(6,7);//0:解密;1:加密
		String password = strInUse.substring(7,27).trim();
		String sessionId = strInUse.substring(27,67);
		System.out.println(txnCod+"|"+encryStatus+"|"+password+"|"+sessionId);

		//通过判断交易码进行处理
		if("444555".equals(txnCod)){

			if("0".equals(encryStatus)){//如果是0,走解密流程

				try {
			      String clear = EBDES.decryptoDES(password, sessionId);
			      resultstr = clear;
			    }
			    catch (Exception ex) {
			      System.out.println(ex);
			      resultstr=this.ErrMsg("解密失败");
			    }
			}else if("1".equals(encryStatus)){//如果是1,走加密流程

				try {
			      String crypter = EBDES.encryptoDES(password, sessionId);
			      resultstr = crypter;
			    }
			    catch (Exception ex) {
			      System.out.println(ex);
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
		System.out.println(inputMsg);
		return ERRCODE+inputMsg;

	}
	private String NormalMsg(String msg){
		final String ERRCODE = "000000";
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
		System.out.println(inputMsg);
		return ERRCODE+inputMsg;

	}
}
