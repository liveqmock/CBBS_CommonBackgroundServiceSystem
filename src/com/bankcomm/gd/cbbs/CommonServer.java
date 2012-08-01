package com.bankcomm.gd.cbbs;

import java.net.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 通用后台系统主类，用于守护相关程序端口
 *
 */
public class CommonServer {

	private final int PORT;
	private final String SERVER;
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 构造函数，初始化./ini/ServerConfig.ini文件里面的相关配置信息
	 *
	 */
	public CommonServer(){
		PropertiesLoader propLoader = new PropertiesLoader("../ini/ServerConfig.ini");
		SERVER = propLoader.getByName("SERVER");
		PORT = Integer.valueOf(null==propLoader.getByName("PORT")?"0":propLoader.getByName("PORT"));
		log.info("==========================================");
		if(0==PORT||null==SERVER){
			log.error("Server ip or post is null");
			System.exit(1);
		}else{
			log.info("Initialization successed. CTServer IP is ["+SERVER+"], POST is ["+PORT+"].");
		}
	}

	/**
	 * 程序入口
	 * @param args 参数无意义
	 * @throws IOException
	 */
	public static void main (String args[]) throws IOException{
		new CommonServer().serverStart();
	}

	private void serverStart() throws IOException{
		//Server needs a ServerSockets Objects as its parameter
		ServerSocket serverSocket = null;
		//Server would like to utilize the multiple threads to deal with the communication
		boolean listening = true;
		
		try{
			serverSocket = new ServerSocket(this.PORT);
		}catch(IOException ioe){
			log.error("Cannot listen the port "+this.PORT+". Maybe this port is in use");
			System.exit(1);
		}
		
		while(listening){
			log.info("The ServerSocket["+this.PORT+"] is now listening for the request");
			new MultiServerThread(serverSocket.accept()).start();				
		}
		serverSocket.close();
	}
}
