package com.bankcomm.gd;

import java.net.*;
import java.io.*;


public class MultiServerThread extends Thread {
	private Socket socket = null;
	
	MultiServerThread(Socket socket){
		super("ServerSocketTest");
		this.socket=socket;
	}
	
	public void run(){
		//new the input and output streams and prepare the read and write operations
		try {
			PrintWriter  pw = new PrintWriter(socket.getOutputStream(),true);
			BufferedReader  br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String userInput=null;
			String serverFeedback="This is the predefined statment.";
			
			//new a protocol object
			CommunicationProtocol cp = new CommunicationProtocol();
			// to give this outputLine a default value.
			
			// using the printWriter to write the message from the serversockts to client sockets
			
			while((userInput=br.readLine())!=null){
				System.out.println("SERVER RECEIVES THE DATA FRMO CLIENT SIDE: "+userInput);
				serverFeedback=cp.processInput(userInput);
				System.out.println("SERVER RESPONSE THE DATA TO CLIENT SIDE: "+serverFeedback);
				pw.println(serverFeedback);
				if(serverFeedback.equalsIgnoreCase("exit")){
					break;
				}
				
			}
			System.out.println("The SERVER has been shuted down.");
			pw.close();
			br.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
