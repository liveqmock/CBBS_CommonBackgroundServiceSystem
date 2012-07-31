package com.bankcomm.gd.cbbs.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bankcomm.gd.cbbs.CommunicationProtocol;
import com.bankcomm.gd.util.Code;

public class YctWorkFlow implements WorkFlow {

	/** ����ͨѶsocket */
	private static Map<String, Socket> sockets = new HashMap<String, Socket>();
	/** ����ͨѶsocket������ʱ�� */
	private static Map<String, Long> sockets_timeout = new HashMap<String, Long>();
	private static Log log = LogFactory.getLog(CommunicationProtocol.class);

	/**
	 * ���ͨǩ��
	 * @param inputstr ���ձ���
	 * @return ���ر���
	 * @Override
	 */
	public String execute(String inputstr){
		log.info(sockets);

		//�ӱ��ĵõ�ǩ���׶α�־
		String Loglvl = inputstr.substring(0,1);
		log.info(Loglvl);
		//�ӱ��ĵõ�Socket ID
		String ScktID = inputstr.substring(1, 8);
		log.info(ScktID);
		//�ӱ��ĵõ��ⷢ����
		String ReqDat = inputstr.substring(8, 280);
		log.info(ReqDat);

		//���Socket�Ƿ�ʱ(��ʱʱ��Ϊ5����),��ʱ�ر�socket�����sockets��sockets_timeout
		socketTimeOutCheck();
		
		if("1".equals(Loglvl)){//��һ�׶δ���
			//log.info("����ǩ����һ�׶δ���...");
			log.info("pross 1...");
			try {
				//���Map���Ƿ��Ѿ���ͬһID��socket,��������ͷŲ�ɾ��
				if(null!=sockets.get(ScktID)){
					if(!sockets.get(ScktID).isClosed()){
						sockets.get(ScktID).close();
					}
					sockets.remove(ScktID);
				}
				//����Socket����ӵ�Map��
				Socket socket = new Socket("10.240.13.201",5003);
				//log.info(socket);
				sockets.put(ScktID, socket);
				sockets_timeout.put(ScktID, new Date().getTime());
				//log.info(sockets.get(ScktID));
				//�ⷢ���Ľ���ͨѶ������
				return this.Stream_Send(socket, ReqDat);

			} catch (IOException e) {
				log.error("IO����:"+e.getMessage());
				log.trace(e);
				//ͨѶ���󷵻�:272��9
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}finally{
			    /*try{
			    	if(null!=osInside){
			    		osInside=null;
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
			if(socket==null){
				return "socket null";
			}
			if(socket.isClosed()){
				return "socket closed";
			}
			//�ⷢ���Ľ���ͨѶ������

			try {
				return this.Stream_Send(socket, ReqDat);
				
			}  catch (IOException e) {
				log.error("IO����:"+e.getMessage());
				log.trace(e);
				//ͨѶ���󷵻�:272��9
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}finally{
			    try{
					OutputStream osInside = socket.getOutputStream();
					InputStream isInside = socket.getInputStream();
			    	if(null!=osInside){
			    		osInside.close();
			    	}
			    	if(null!=isInside){
			    		isInside.close();
			    	}
			    	if(null!=socket){
			    		socket.close();
			    	}
			    	sockets.remove(ScktID);
			    	sockets_timeout.remove(ScktID);
			    }catch(IOException e){
			    	log.error("�ͷ���Դ����:"+e.getMessage());
			    }
		    }
			
		}else{//�����ڵ�����
			return "δ֪����";
		}

		//TODO �ж�Socket�Ƿ�ʱ,����������ͷ�Socket
	}

	private String Stream_Send(Socket socket, String ReqDat) throws IOException{
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		
		try{
			log.info("sending msg...");
			log.info(ReqDat);
			byte[] bit_msg = Code.HexString2Bytes(ReqDat);
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
			String content = Code.Bytes2HexString(_bbuf);
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

	
	/**
	 * ���Socket�Ƿ�ʱ(��ʱʱ��Ϊ5����),��ʱ�ر�socket�����sockets��sockets_timeout
	 */
	private static void socketTimeOutCheck() {
		for(String oneScktID:sockets_timeout.keySet()){
			log.info("���socket["+oneScktID+"]�Ƿ�ʱ");
			if(null!=sockets_timeout.get(oneScktID)){
				long timePass = new Date().getTime()-sockets_timeout.get(oneScktID);
				long timePassMinute = timePass/(1000l*60l);
				//����5���Ӿ��ͷ�socket,�����sockets��sockets_timeout
				if(timePassMinute>5l){
					if(null!=sockets.get(oneScktID)){//sockets��Ϊ��,���sockets��sockets_timeout
						try{
							sockets.get(oneScktID).close();
						}catch(IOException e){
							log.error("�ر�socketʧ��:"+oneScktID);
							log.trace(e);
						}finally{
							sockets.remove(oneScktID);
							sockets_timeout.remove(oneScktID);
						}
					}else{//socketsΪ��,���sockets_timeout
						sockets_timeout.remove(oneScktID);
					}
				}
			}
		}
	}


}
