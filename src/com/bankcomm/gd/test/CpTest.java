package com.bankcomm.gd.test;

import com.bankcomm.gd.CommunicationProtocol;

public class CpTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String txnCod = "444555";
		String sessionId = "IEIMIYBZDKEPEZEOIADFESIMETIFEUJKIHGTCHBD";
		
		//加密流程
		String encryStatus = "1";//0:解密;1:加密
		String password = "222222              ";
		StringBuffer inputStr = new StringBuffer();
		inputStr.append(txnCod).append(encryStatus).append(password).append(sessionId);
		System.out.println("inputStr:"+inputStr.toString());
		System.out.println(new CommunicationProtocol().processInput(inputStr.toString()));

		//解密流程
		encryStatus = "0";//0:解密;1:加密
		password = "HACH6e5sdDc=        ";
		inputStr = new StringBuffer();
		inputStr.append(txnCod).append(encryStatus).append(password).append(sessionId);
		System.out.println("inputStr:"+inputStr.toString());
		System.out.println(new CommunicationProtocol().processInput(inputStr.toString()));

	}

}
