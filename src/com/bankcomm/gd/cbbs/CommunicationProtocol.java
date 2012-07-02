package com.bankcomm.gd.cbbs;

import java.util.Arrays;
import java.util.Date;
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
 * 处理逻辑
 *
 */
public class CommunicationProtocol {

	/** 保存通讯socket */
	private static Map<String, Socket> sockets = new HashMap<String, Socket>();
	/** 保存通讯socket创建的时间 */
	private static Map<String, Long> sockets_timeout = new HashMap<String, Long>();
	private static Log log = LogFactory.getLog(CommunicationProtocol.class);

	/**
	 * 羊城通签到
	 * @param inputstr 接收报文
	 * @return 返回报文
	 */
	public static String YctLogin(String inputstr){
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

	/**
	 * 处理收付通宝加解密逻辑
	 * @param inputstr 接收报文
	 * @return 返回报文
	 */
	public String processInput(String inputstr){

		String resultstr = "";
		final int packetHead = 8;
		final int packetLength = packetHead+67;

		//首先判断位数是否足够
		if(packetLength!=inputstr.length()){
			log.error("报文长度不正确,接收字段为:["+inputstr+"]");
			return this.ErrMsg("报文长度不正确");
		}

		//拆包
		String strInUse = inputstr.substring(8,packetLength);
		log.info("报文体: ["+strInUse+"]");
		String txnCod = strInUse.substring(0,6);
		String encryStatus = strInUse.substring(6,7);//0:解密;1:加密
		String password = strInUse.substring(7,27).trim();
		String sessionId = strInUse.substring(27,67);
		log.info("交易码:["+txnCod+"]|加解密标志:["+encryStatus+"]|密码:["+password+"]|SESSION:["+sessionId+"]");

		//通过判断交易码进行处理
		if("444555".equals(txnCod)){

			if("0".equals(encryStatus)){//如果是0,走解密流程

				try {
					resultstr= EBDES.decryptoDES(password, sessionId);
			    }
			    catch (Exception ex) {
			    	log.trace(ex);
			    	resultstr=this.ErrMsg("解密失败");
			    }
			}else if("1".equals(encryStatus)){//如果是1,走加密流程

				try {
					resultstr = EBDES.encryptoDES(password, sessionId);
			    }
			    catch (Exception ex) {
			    	log.trace(ex);
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
