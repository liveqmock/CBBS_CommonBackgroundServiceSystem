package com.bankcomm.gd.cbbs;

import java.net.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �ػ�����
 * @author gu.qm
 *
 */
public class MultiServerThread extends Thread {
	private Socket socket = null;
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * ʹ�ý��յ�Socket�Գ�����г�ʼ����
	 * @param socket
	 */
	public MultiServerThread(Socket socket){
		super("ServerSocketTest");
		this.socket=socket;
	}

	/**
	 * ������ʹ���޲���ʽ��ʼ��
	 *
	 */
	private MultiServerThread(){}

	/**
	 * ͨ��socket����ICS���󣬲�����
	 */
	public void run(){
		PrintWriter pw = null;
		BufferedReader br = null;
		//new the input and output streams and prepare the read and write operations
		try {
			pw = new PrintWriter(socket.getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// to give this outputLine a default value.
			String serverFeedback="This is the predefined statment.";
			
			// using the printWriter to write the message from the serversockts to client sockets
			String strFromIcs=null;
			while((strFromIcs=br.readLine())!=null){
				log.info("SERVER RECEIVES THE DATA FRMO ICS: ["+strFromIcs+"]");
				//new a protocol object
				CommunicationProtocol cp = new CommunicationProtocol();
				serverFeedback=cp.processInput(strFromIcs);
				log.info("SERVER RESPONSE THE DATA TO ICS: ["+serverFeedback+"]");
				pw.println(serverFeedback);
				
			}
			log.info("The SERVER has been shuted down.");
			
		} catch (IOException e) {
			log.error("IO����:"+e.getMessage());
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
		    	log.error("�ͷ���Դ����:"+e.getMessage());
		    }
	    	
	    }
	}
}
