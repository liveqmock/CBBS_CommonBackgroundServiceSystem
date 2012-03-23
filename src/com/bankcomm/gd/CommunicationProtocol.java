package com.bankcomm.gd;

import java.util.Arrays;
import com.bocom.eb.des.EBDES;

public class CommunicationProtocol {

	public String  processInput(String inputstr){

		String resultstr = "";
		final int packetHead = 8;
		int packetLength = packetHead+67;

		//System.out.println(packetLength);
		//�����ж�λ���Ƿ��㹻
		if(packetLength!=inputstr.length()){
			System.out.println(inputstr.length());
			return this.ErrMsg("���ĳ��Ȳ���ȷ");
		}

		//���
		String strInUse = inputstr.substring(8,packetLength);
		System.out.println(strInUse);
		String txnCod = strInUse.substring(0,6);
		String encryStatus = strInUse.substring(6,7);//0:����;1:����
		String password = strInUse.substring(7,27).trim();
		String sessionId = strInUse.substring(27,67);
		System.out.println(txnCod+"|"+encryStatus+"|"+password+"|"+sessionId);

		//ͨ���жϽ�������д���
		if("444555".equals(txnCod)){

			if("0".equals(encryStatus)){//�����0,�߽�������

				try {
			      String clear = EBDES.decryptoDES(password, sessionId);
			      resultstr = clear;
			    }
			    catch (Exception ex) {
			      System.out.println(ex);
			      resultstr=this.ErrMsg("����ʧ��");
			    }
			}else if("1".equals(encryStatus)){//�����1,�߼�������

				try {
			      String crypter = EBDES.encryptoDES(password, sessionId);
			      resultstr = crypter;
			    }
			    catch (Exception ex) {
			      System.out.println(ex);
			      resultstr=this.ErrMsg("����ʧ��");
			    }
			}else{//���ͱ�־λ�쳣
				resultstr=this.ErrMsg("�ӽ��ܱ�־����");
			}

		}else{//�޴˽�����
			resultstr=this.ErrMsg("���������");
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
