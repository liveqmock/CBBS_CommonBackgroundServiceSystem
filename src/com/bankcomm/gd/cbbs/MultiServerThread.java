package com.bankcomm.gd.cbbs;

import java.net.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bankcomm.gd.cbbs.workflow.WorkFlowFactory;
import com.bankcomm.gd.cbbs.workflow.WorkFlow;

/**
 * 守护进程
 *
 */
public class MultiServerThread extends Thread {
	private Socket socket = null;
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 使用接收的Socket对程序进行初始化化
	 * @param socket
	 */
	public MultiServerThread(Socket socket){
		super("MultiServerThread");
		this.socket=socket;
	}

	/**
	 * 不允许使用无参数式初始化
	 *
	 */
	private MultiServerThread(){}

	/**
	 * 通过socket接收ICS请求，并返回，与ICS使用同步短连接进行通讯
	 */
	public void run(){
		PrintWriter pw = null;
		BufferedReader br = null;
		//new the input and output streams and prepare the read and write operations
		try {
			pw = new PrintWriter(socket.getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// to give this outputLine a default value.
			String serverFeedback="999999";
			
			// using the printWriter to write the message from the serversockts to client sockets
			String strFromIcs=null;
			while((strFromIcs=br.readLine())!=null){
				log.info("SERVER RECEIVES THE DATA FRMO ICS: ["+strFromIcs+"]");
				// 获得工作流类型
				String workFlowType = strFromIcs.substring(0,6);
				// 获得报文体
				String msgBody = strFromIcs.substring(6);
				// 工作流处理
				WorkFlow workFlowObject = WorkFlowFactory.getWorkFlowObject(workFlowType);
				serverFeedback=workFlowObject.execute(msgBody);
				// 报文返回
				log.info("SERVER RESPONSE THE DATA TO ICS: ["+serverFeedback+"]");
				pw.println(serverFeedback);
				
			}
			log.info("The work flow has completed.");
			
		} catch (IOException e) {
			log.error("IO error:"+e.getMessage());
			log.trace(e);
		}finally{
		    try{
		    	if(null!=pw){
			    	pw.close();
		    	}
		    	if(null!=br){
		    		br.close();
		    	}
		    	if(null!=this.socket){
		    		this.socket.close();
		    	}

		    }catch(IOException e){
		    	log.error("release resource error:"+e.getMessage());
		    }
	    	
	    }
	}
}
