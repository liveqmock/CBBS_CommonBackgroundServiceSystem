package com.bankcomm.gd.test;

import com.bankcomm.gd.cbbs.CommunicationProtocol;

public class CpTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String tradeLenth = "00000067";
		String txnCod = "444555";
		String sessionId = "HDEKESFHJTIDHTDGGPJCAFJMCVGAHGDYDADEAMEL";
		
		//��������
		String encryStatus = "1";//0:����;1:����
		String password = "222222              ";
		StringBuffer inputStr = new StringBuffer();
		inputStr.append(tradeLenth).append(txnCod).append(encryStatus).append(password).append(sessionId);
		System.out.println("inputStr:"+inputStr.toString());
		System.out.println(new CommunicationProtocol().processInput(inputStr.toString()));

		//��������
		encryStatus = "0";//0:����;1:����
		password = "RrozK15jQM0=        ";
		inputStr = new StringBuffer();
		inputStr.append(tradeLenth).append(txnCod).append(encryStatus).append(password).append(sessionId);
		System.out.println("inputStr:"+inputStr.toString());
		System.out.println(new CommunicationProtocol().processInput(inputStr.toString()));

	}

}
