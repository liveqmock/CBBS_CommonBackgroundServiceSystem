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

import com.bankcomm.gd.cbbs.PropertiesLoader;
import com.bankcomm.gd.util.Code;

/**
 * ���ͨǩ��������
 * @author Cruiser
 *
 */
public class YctWorkFlow implements WorkFlow {

	private final int PORT;
	private final String SERVER;
	/** ����ͨѶsocket */
	private static Map<String, Socket> sockets = new HashMap<String, Socket>();
	/** ����ͨѶsocket������ʱ�� */
	private static Map<String, Long> sockets_timeout = new HashMap<String, Long>();
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * ���캯������ʼ��./ini/ServerConfig.ini�ļ���������������Ϣ
	 *
	 */
	public YctWorkFlow(){
		PropertiesLoader propLoader = new PropertiesLoader("../ini/ServerConfig.ini");
		SERVER = propLoader.getByName("YCT_SERVER");
		PORT = Integer.valueOf(null==propLoader.getByName("YCT_PORT")?"0":propLoader.getByName("YCT_PORT"));
		log.info("==========================================");
		if(0==PORT||null==SERVER){
			log.error("Server ip or post is null");
			System.exit(1);
		}else{
			log.info("Initialization successed. STServer IP is ["+SERVER+"], POST is ["+PORT+"].");
		}
	}

	/**
	 * ���ͨǩ��
	 * @param inputstr ���ձ���
	 * @return ���ر���
	 * @Override
	 */
	public String execute(String inputstr){
		log.info(YctWorkFlow.sockets);

		//�ӱ��ĵõ�ǩ���׶α�־
		String Loglvl = inputstr.substring(0,1);
		log.info("Login type is "+Loglvl);
		//�ӱ��ĵõ�Socket ID
		String ScktID = inputstr.substring(1, 8);
		log.info("Socket ID is "+ScktID);
		//�ӱ��ĵõ��ⷢ����
		String ReqDat = inputstr.substring(8, 280);
		log.info("Request message is "+ReqDat);

		//���Socket�Ƿ�ʱ(��ʱʱ��Ϊ5����),��ʱ�ر�socket�����sockets��sockets_timeout
		this.socketTimeOutCheck();
		
		if("1".equals(Loglvl)){//��һ�׶δ���
			//log.info("����ǩ����һ�׶δ���...");
			log.info("YCT Login 1 processing...");
			try {
				//���Map���Ƿ��Ѿ���ͬһID��socket,��������ͷŲ�ɾ��
				if(null!=sockets.get(ScktID)){
					if(!sockets.get(ScktID).isClosed()){
						sockets.get(ScktID).close();
					}
					sockets.remove(ScktID);
					sockets_timeout.remove(ScktID);
				}
				//����Socket����ӵ�Map��
				Socket socket = new Socket(this.SERVER,this.PORT);
				sockets.put(ScktID, socket);
				sockets_timeout.put(ScktID, new Date().getTime());

				//�ⷢ���Ľ���ͨѶ������
				return this.Stream_Send(socket, ReqDat);

			} catch (IOException e) {
				log.error("IO error:"+e.getMessage());
				log.trace(e);
				//ͨѶ���󷵻�:272��9
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}finally{//����ǩ��������Ҫʹ��ͬһ��socket���ⷢ���α��ģ���˲��ͷ�socket��Դ
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

			log.info("YCT Login 2 processing...");

			//�ж�Socket��Ϊ��
			Socket socket = sockets.get(ScktID);

			if(socket==null){
				log.error("socket null");
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}
			if(socket.isClosed()){
				log.error("socket closed");
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}
			
			//�ⷢ���Ľ���ͨѶ������
			try {
				return this.Stream_Send(socket, ReqDat);
				
			}catch (IOException e) {
				log.error("IO error:"+e.getMessage());
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
			    	log.error("release resource error:"+e.getMessage());
			    }
		    }
			
		}else{//�����ڵ�����
			log.error("unknow login type");
			return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
		}

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
	private void socketTimeOutCheck() {
		for(String oneScktID:sockets_timeout.keySet()){

			log.info("Checking socket["+oneScktID+"] is timeout or not.");
			
			if(null!=sockets_timeout.get(oneScktID)){
				long timePass = new Date().getTime()-sockets_timeout.get(oneScktID);
				long timePassMinute = timePass/(1000l*60l);
				//����5���Ӿ��ͷ�socket,�����sockets��sockets_timeout
				if(timePassMinute>5l){
					if(null!=sockets.get(oneScktID)){//sockets��Ϊ��,���sockets��sockets_timeout
						try{
							sockets.get(oneScktID).close();
						}catch(IOException e){
							log.error("Close socket error:"+oneScktID);
							log.trace(e);
						}finally{
							sockets.remove(oneScktID);
							sockets_timeout.remove(oneScktID);
						}
					}else{//socketsΪ��,���sockets_timeout
						sockets_timeout.remove(oneScktID);
					}
				}
			}else{//Socket��ʱʱ��Ϊ�գ�Socket��ID��Ϊ��
				sockets_timeout.remove(oneScktID);
			}
		}
	}


}
