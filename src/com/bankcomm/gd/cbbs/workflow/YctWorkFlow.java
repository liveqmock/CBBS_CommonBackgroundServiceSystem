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
 * 羊城通签到工作流
 * @author Cruiser
 *
 */
public class YctWorkFlow implements WorkFlow {

	private final int PORT;
	private final String SERVER;
	/** 保存通讯socket */
	private static Map<String, Socket> sockets = new HashMap<String, Socket>();
	/** 保存通讯socket创建的时间 */
	private static Map<String, Long> sockets_timeout = new HashMap<String, Long>();
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 构造函数，初始化./ini/ServerConfig.ini文件里面的相关配置信息
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
	 * 羊城通签到
	 * @param inputstr 接收报文
	 * @return 返回报文
	 * @Override
	 */
	public String execute(String inputstr){
		log.info(YctWorkFlow.sockets);

		//从报文得到签到阶段标志
		String Loglvl = inputstr.substring(0,1);
		log.info("Login type is "+Loglvl);
		//从报文得到Socket ID
		String ScktID = inputstr.substring(1, 8);
		log.info("Socket ID is "+ScktID);
		//从报文得到外发报文
		String ReqDat = inputstr.substring(8, 280);
		log.info("Request message is "+ReqDat);

		//检查Socket是否超时(超时时间为5分钟),超时关闭socket并清空sockets和sockets_timeout
		this.socketTimeOutCheck();
		
		if("1".equals(Loglvl)){//第一阶段处理
			//log.info("进入签到第一阶段处理...");
			log.info("YCT Login 1 processing...");
			try {
				//检查Map中是否已经有同一ID的socket,如果有先释放并删除
				if(null!=sockets.get(ScktID)){
					if(!sockets.get(ScktID).isClosed()){
						sockets.get(ScktID).close();
					}
					sockets.remove(ScktID);
					sockets_timeout.remove(ScktID);
				}
				//创建Socket并添加到Map中
				Socket socket = new Socket(this.SERVER,this.PORT);
				sockets.put(ScktID, socket);
				sockets_timeout.put(ScktID, new Date().getTime());

				//外发报文进行通讯并返回
				return this.Stream_Send(socket, ReqDat);

			} catch (IOException e) {
				log.error("IO error:"+e.getMessage());
				log.trace(e);
				//通讯错误返回:272个9
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}finally{//由于签到交易需要使用同一个socket，外发两次报文，因此不释放socket资源
			    /*try{
			    	if(null!=osInside){
			    		osInside=null;
			    	}
			    	if(null!=isInside){
			    		isInside.close();
			    	}
			    }catch(IOException e){
			    	log.error("释放资源错误:"+e.getMessage());
			    }*/
		    }
			
		}else if("2".equals(Loglvl)){//第二阶段处理

			log.info("YCT Login 2 processing...");

			//判断Socket不为空
			Socket socket = sockets.get(ScktID);

			if(socket==null){
				log.error("socket null");
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}
			if(socket.isClosed()){
				log.error("socket closed");
				return "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
			}
			
			//外发报文进行通讯并返回
			try {
				return this.Stream_Send(socket, ReqDat);
				
			}catch (IOException e) {
				log.error("IO error:"+e.getMessage());
				log.trace(e);
				//通讯错误返回:272个9
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
			
		}else{//不存在的数据
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
	 * 检查Socket是否超时(超时时间为5分钟),超时关闭socket并清空sockets和sockets_timeout
	 */
	private void socketTimeOutCheck() {
		for(String oneScktID:sockets_timeout.keySet()){

			log.info("Checking socket["+oneScktID+"] is timeout or not.");
			
			if(null!=sockets_timeout.get(oneScktID)){
				long timePass = new Date().getTime()-sockets_timeout.get(oneScktID);
				long timePassMinute = timePass/(1000l*60l);
				//超过5分钟就释放socket,并清空sockets和sockets_timeout
				if(timePassMinute>5l){
					if(null!=sockets.get(oneScktID)){//sockets不为空,清空sockets和sockets_timeout
						try{
							sockets.get(oneScktID).close();
						}catch(IOException e){
							log.error("Close socket error:"+oneScktID);
							log.trace(e);
						}finally{
							sockets.remove(oneScktID);
							sockets_timeout.remove(oneScktID);
						}
					}else{//sockets为空,清空sockets_timeout
						sockets_timeout.remove(oneScktID);
					}
				}
			}else{//Socket超时时间为空，Socket的ID不为空
				sockets_timeout.remove(oneScktID);
			}
		}
	}


}
