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
import com.bankcomm.gd.test.YctTest;

public class YctWorkFlow implements WorkFlow {

	/** 保存通讯socket */
	private static Map<String, Socket> sockets = new HashMap<String, Socket>();
	/** 保存通讯socket创建的时间 */
	private static Map<String, Long> sockets_timeout = new HashMap<String, Long>();
	private static Log log = LogFactory.getLog(CommunicationProtocol.class);

	/**
	 * 羊城通签到
	 * @param inputstr 接收报文
	 * @return 返回报文
	 * @Override
	 */
	public String execute(String inputstr){
		log.info(sockets);

		//从报文得到签到阶段标志
		String Loglvl = inputstr.substring(0,1);
		log.info(Loglvl);
		//从报文得到Socket ID
		String ScktID = inputstr.substring(1, 8);
		log.info(ScktID);
		//从报文得到外发报文
		String ReqDat = inputstr.substring(8, 280);
		log.info(ReqDat);

		//检查Socket是否超时(超时时间为5分钟),超时关闭socket并清空sockets和sockets_timeout
		socketTimeOutCheck();
		
		if("1".equals(Loglvl)){//第一阶段处理
			//log.info("进入签到第一阶段处理...");
			log.info("pross 1...");
			try {
				//检查Map中是否已经有同一ID的socket,如果有先释放并删除
				if(null!=sockets.get(ScktID)){
					if(!sockets.get(ScktID).isClosed()){
						sockets.get(ScktID).close();
					}
					sockets.remove(ScktID);
				}
				//创建Socket并添加到Map中
				Socket socket = new Socket("10.240.13.201",5003);
				//log.info(socket);
				sockets.put(ScktID, socket);
				sockets_timeout.put(ScktID, new Date().getTime());
				//log.info(sockets.get(ScktID));
				//外发报文进行通讯并返回
				return YctTest.Stream_Send(socket, ReqDat);

			} catch (IOException e) {
				log.error("IO错误:"+e.getMessage());
				log.trace(e);
				//通讯错误返回:272个9
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
			    	log.error("释放资源错误:"+e.getMessage());
			    }*/
		    }
			
		}else if("2".equals(Loglvl)){//第二阶段处理
			log.info("pross 2...");
			//判断Socket不为空
			Socket socket = sockets.get(ScktID);

			//log.info(socket);
			if(socket==null){
				return "socket null";
			}
			if(socket.isClosed()){
				return "socket closed";
			}
			//外发报文进行通讯并返回

			try {
				return YctTest.Stream_Send(socket, ReqDat);
				
			}  catch (IOException e) {
				log.error("IO错误:"+e.getMessage());
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
			    	log.error("释放资源错误:"+e.getMessage());
			    }
		    }
			
		}else{//不存在的数据
			return "未知类型";
		}

		//TODO 判断Socket是否超时,如果是立即释放Socket
	}

	/**
	 * 检查Socket是否超时(超时时间为5分钟),超时关闭socket并清空sockets和sockets_timeout
	 */
	private static void socketTimeOutCheck() {
		for(String oneScktID:sockets_timeout.keySet()){
			log.info("检查socket["+oneScktID+"]是否超时");
			if(null!=sockets_timeout.get(oneScktID)){
				long timePass = new Date().getTime()-sockets_timeout.get(oneScktID);
				long timePassMinute = timePass/(1000l*60l);
				//超过5分钟就释放socket,并清空sockets和sockets_timeout
				if(timePassMinute>5l){
					if(null!=sockets.get(oneScktID)){//sockets不为空,清空sockets和sockets_timeout
						try{
							sockets.get(oneScktID).close();
						}catch(IOException e){
							log.error("关闭socket失败:"+oneScktID);
							log.trace(e);
						}finally{
							sockets.remove(oneScktID);
							sockets_timeout.remove(oneScktID);
						}
					}else{//sockets为空,清空sockets_timeout
						sockets_timeout.remove(oneScktID);
					}
				}
			}
		}
	}


}
