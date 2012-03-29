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
			log.error("��������ַ��˿�Ϊ��");
		}else{
			log.info("==========================================");
			log.info("��ʼ���ɹ�:��������ַΪ["+SERVER+"],�˿�Ϊ["+PORT+"]");
		}
	}

	public static void main (String args[]){

		new ServerVisitor().visitorWorking();
	}

	/**
	 * ͨ�������ַ��Գ�����в���
	 */
	public void visitorWorking(){
		Socket socket = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		
		//��ʼ��Socket,����װ���������
		try{
			socket = new Socket();
			socket.connect(new InetSocketAddress(this.SERVER, this.PORT), this.TIMEOUT);
			//���ó�ʱʱ��
			socket.setSoTimeout(10000);
			pw = new PrintWriter(socket.getOutputStream(),true);  //true means automatically flush()! this is crucial part!
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(UnknownHostException ukhe){
			log.error("�޷��ҵ�����:["+SERVER+"]���߶˿�:["+PORT+"]");
			System.exit(-1);
		}catch(IOException ioe){
			log.error("�޷���������");
			System.exit(-1);
	    }

		//��תϵͳ����
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

	    String userInputStr=null;
	    String fromServerStr=null;
	    try{
		    while ((userInputStr=userInput.readLine())!=null){
				log.info("�ͻ�������: "+userInputStr);
				if("exit".equalsIgnoreCase(userInputStr)||"quit".equalsIgnoreCase(userInputStr)){
					break;
				}
				pw.println(userInputStr);
			
				if((fromServerStr=br.readLine())!=null){
					log.info("����������: " + fromServerStr);
					if("exit".equalsIgnoreCase(fromServerStr)){
						break;
					}
				}
				System.out.print("������: ");
			}
	    	
	    }catch(SocketTimeoutException e){
	    	log.error("���ӳ�ʱ:"+e.getMessage());
	    }catch(IOException e){
	    	log.error("��ȡ����:"+e.getMessage());
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
		    	log.error("�ͷ���Դ����:"+e.getMessage());
		    }
	    	
	    }

	    //��ȫ�˳�
	    log.info("�ͻ��˰�ȫ�˳�.");

	}
}
