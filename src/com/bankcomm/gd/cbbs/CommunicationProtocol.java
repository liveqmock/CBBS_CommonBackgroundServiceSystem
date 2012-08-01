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
 * �����߼�
 * @deprecated
 */
public class CommunicationProtocol {

	private static Log log = LogFactory.getLog(CommunicationProtocol.class);

	/**
	 * �����ո�ͨ���ӽ����߼�
	 * @param inputstr ���ձ���
	 * @return ���ر���
	 */
	public String processInput(String inputstr){

		String resultstr = "";
		final int packetHead = 8;
		final int packetLength = packetHead+67;

		//�����ж�λ���Ƿ��㹻
		if(packetLength!=inputstr.length()){
			log.error("���ĳ��Ȳ���ȷ,�����ֶ�Ϊ:["+inputstr+"]");
			return this.ErrMsg("���ĳ��Ȳ���ȷ");
		}

		//���
		String strInUse = inputstr.substring(8,packetLength);
		log.info("������: ["+strInUse+"]");
		String txnCod = strInUse.substring(0,6);
		String encryStatus = strInUse.substring(6,7);//0:����;1:����
		String password = strInUse.substring(7,27).trim();
		String sessionId = strInUse.substring(27,67);
		log.info("������:["+txnCod+"]|�ӽ��ܱ�־:["+encryStatus+"]|����:["+password+"]|SESSION:["+sessionId+"]");

		//ͨ���жϽ�������д���
		if("444555".equals(txnCod)){

			if("0".equals(encryStatus)){//�����0,�߽�������

				try {
					resultstr= EBDES.decryptoDES(password, sessionId);
			    }
			    catch (Exception ex) {
			    	log.trace(ex);
			    	resultstr=this.ErrMsg("����ʧ��");
			    }
			}else if("1".equals(encryStatus)){//�����1,�߼�������

				try {
					resultstr = EBDES.encryptoDES(password, sessionId);
			    }
			    catch (Exception ex) {
			    	log.trace(ex);
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
