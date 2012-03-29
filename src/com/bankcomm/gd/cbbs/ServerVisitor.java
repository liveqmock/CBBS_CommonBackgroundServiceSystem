package com.bankcomm.gd.cbbs;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerVisitor {

	private final int PORT;
	private final String SERVER;
	private final int TIMEOUT=10000;
	private Log log = LogFactory.getLog(this.getClass());

	public ServerVisitor(){
		PropertiesLoader propLoader = new PropertiesLoader("./ini/ServerConfig.ini");
		SERVER = propLoader.getByName("SERVER");
		PORT = Integer.valueOf(null==propLoader.getByName("PORT")?"0":propLoader.getByName("PORT"));
		if(0==PORT||null==SERVER){
			log.error("服务器地址或端口为空");
		}else{
			log.info("==========================================");
			log.info("初始化成功:服务器地址为["+SERVER+"],端口为["+PORT+"]");
		}
	}

	public static void main (String args[]){

		new ServerVisitor().visitorWorking();
	}

	/**
	 * 通过输入字符对程序进行操作
	 */
	public void visitorWorking(){
		Socket socket = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		
		//初始化Socket,并封装输入输出流
		try{
			socket = new Socket();
			socket.connect(new InetSocketAddress(this.SERVER, this.PORT), this.TIMEOUT);
			//设置超时时间
			socket.setSoTimeout(10000);
			pw = new PrintWriter(socket.getOutputStream(),true);  //true means automatically flush()! this is crucial part!
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(UnknownHostException ukhe){
			log.error("无法找到主机:["+SERVER+"]或者端口:["+PORT+"]");
			System.exit(-1);
		}catch(IOException ioe){
			log.error("无法创建连接");
			System.exit(-1);
	    }

		//封转系统输入
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

	    String userInputStr=null;
	    String fromServerStr=null;
	    try{
		    while ((userInputStr=userInput.readLine())!=null){
				log.info("客户端输入: "+userInputStr);
				if("exit".equalsIgnoreCase(userInputStr)||"quit".equalsIgnoreCase(userInputStr)){
					break;
				}
				pw.println(userInputStr);
			
				if((fromServerStr=br.readLine())!=null){
					log.info("服务器返回: " + fromServerStr);
					if("exit".equalsIgnoreCase(fromServerStr)){
						break;
					}
				}
				System.out.print("请输入: ");
			}
	    	
	    }catch(SocketTimeoutException e){
	    	log.error("连接超时:"+e.getMessage());
	    }catch(IOException e){
	    	log.error("读取错误:"+e.getMessage());
	    }finally{
		    try{
		    	if(null!=userInput){
		    		userInput.close();
		    	}
		    	if(null!=pw){
			    	pw.close();
		    	}
		    	if(null!=br){
		    		br.close();
		    	}
		    	if(null!=socket){
		    		socket.close();
		    	}

		    }catch(IOException e){
		    	log.error("释放资源错误:"+e.getMessage());
		    }
	    	
	    }

	    //安全退出
	    log.info("客户端安全退出.");

	}
}
