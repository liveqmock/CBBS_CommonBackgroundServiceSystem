package com.bankcomm.gd.cbbs;

import java.util.Arrays;
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
import java.net.InetAddress;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bankcomm.gd.test.YctTest;
import com.bocom.eb.des.EBDES;

/**
 * �����߼�
 * @author gu.qm
 *
 */
public class CommunicationProtocol {

	private static Map<String, Socket> sockets = new HashMap<String, Socket>();
	private static Log log = LogFactory.getLog(CommunicationProtocol.class);

	/**
	 * ���ͨǩ��
	 * @param inputstr ���ձ���
	 * @return ���ر���
	 */
	public static String YctLogin(String inputstr){
		//log.info(sockets);

		//�ӱ��ĵõ�ǩ���׶α�־
		String Loglvl = inputstr.substring(0,1);
		log.info(Loglvl);
		//�ӱ��ĵõ�Socket ID
		String ScktID = inputstr.substring(1, 8);
		log.info(ScktID);
		//�ӱ��ĵõ��ⷢ����
		String ReqDat = inputstr.substring(8, 280);
		log.info(ReqDat);
		if("1".equals(Loglvl)){//��һ�׶δ���
			//log.info("����ǩ����һ�׶δ���...");
			log.info("pross 1...");
			try {
				//����Socket����ӵ�Map��
				Socket socket = new Socket("10.240.13.201",5003);
				//log.info(socket);
				sockets.put(ScktID, socket);
				//log.info(sockets.get(ScktID));
				//�ⷢ���Ľ���ͨѶ������
				return YctTest.Stream_Send(socket, ReqDat);

			} catch (IOException e) {
				log.error("IO����:"+e.getMessage());
				log.trace(e);
				//ͨѶ���󷵻�:272��9
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}finally{
			    /*try{
			    	if(null!=osInside){
			    		osInside.close();
			    	}
			    	if(null!=isInside){
			    		isInside.close();
			    	}
			    }catch(IOException e){
			    	log.error("�ͷ���Դ����:"+e.getMessage());
			    }*/
		    }
			
		}else if("2".equals(Loglvl)){//�ڶ��׶δ���
			log.info("pross 2...");
			//�ж�Socket��Ϊ��
			Socket socket = sockets.get(ScktID);

			//log.info(socket);
			if(socket==null||socket.isClosed()){
				return "socket null";
			}
			//�ⷢ���Ľ���ͨѶ������
			try {

				//�ⷢ���Ľ���ͨѶ������
				osInside = socket.getOutputStream();
				isInside = socket.getInputStream();
				log.info("sending msg...");
				osInside.write(YctTest.HexString2Bytes(ReqDat));

				log.info("receiving msg...");
				byte[] msgInputOutside = new byte[1024];
				int totleRead = 0;
				int hasRead =0;
				//String content="";
				while((hasRead=isInside.read(msgInputOutside))>0){
					//fos.write(bbuf, 0, hasRead);
					//fos.flush();
					totleRead+=hasRead;
				}
				byte[] bit_msg = new byte[totleRead];
				for(int i=0;i<totleRead;i++){
					bit_msg[i]=msgInputOutside[i];
				}
				log.info(YctTest.Bytes2HexString(bit_msg));
				return YctTest.Bytes2HexString(bit_msg);
				
			}  catch (IOException e) {
				log.error("IO����:"+e.getMessage());
				log.trace(e);
				//ͨѶ���󷵻�:272��9
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}/*finally{
			    try{
			    	if(null!=osInside){
			    		osInside.close();
			    	}
			    	if(null!=isInside){
			    		isInside.close();
			    	}
			    }catch(IOException e){
			    	log.error("�ͷ���Դ����:"+e.getMessage());
			    }
		    }*/
		    


			
		}else{//�����ڵ�����
			return "δ֪����";
		}
		
		//TODO �ж�Socket�Ƿ�ʱ,����������ͷ�Socket
	}

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
