package com.bankcomm.gd;

import java.net.*;
import java.io.*;



public class MyClient {
	public static void main (String args[]) throws IOException{
		Socket socket = null;
		PrintWriter pw = null;
		BufferedReader br = null;
		
		final int PORT_NUMBER = 8800;
		final String SERVER_NAME = "localhost";
		
		
		try{
			socket = new Socket(SERVER_NAME, PORT_NUMBER);
			pw = new PrintWriter(socket.getOutputStream(),true);  //true means automatically flush()! this is crucial part!
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}catch(UnknownHostException ukhe){
				   System.err.println("Cannot find the Host:"+SERVER_NAME+"or the port"+PORT_NUMBER);
				   System.exit(1);
			}catch(IOException ioe){
				   System.err.println("Could not get the I/O function for the communication");
				   System.exit(1);
	    }
		
		
        BufferedReader userType = new BufferedReader(new InputStreamReader(System.in));
	    
	    String userTypeStr=null;
	    String fromServerStr=null;
	    
	   while ((userTypeStr=userType.readLine())!=null){
		   System.out.println("THE CLIENT USER TYPED: "+userTypeStr);
		   pw.println(userTypeStr);
		   if(userTypeStr.equalsIgnoreCase("exit")){
			   break;
		   }
		   
		   if((fromServerStr=br.readLine())!=null){
			   System.out.println("FEEDBACK FROM SERVER:" + fromServerStr);
			   if(fromServerStr.equalsIgnoreCase("exit")){
				   break;
			   }
		   }
	   }
		System.out.println("THE CLIENT SOCKET SHUTED DOWN ALSO.");
	    pw.close();
	    br.close();
	    userType.close();
	    socket.close();
		
	}
	
	    

}
