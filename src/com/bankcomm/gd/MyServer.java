package com.bankcomm.gd;

import java.net.*;
import java.io.*;


public class MyServer {

	public static void main (String args[]) throws IOException{
		//Server needs a ServerSockets Objects as its parameter
		ServerSocket serverSocket = null;
		//Server would like to utilize the multiple threads to deal with the communication
		boolean listening = true;
		int socketPost = 3894; 
		
		try{
			serverSocket = new ServerSocket(socketPost);
		}catch(IOException ioe){
			System.err.println("Cannot listen the port "+socketPost+". Maybe this port is in use");
			System.exit(1);
		}
		
		while(listening){
			System.out.println("The ServerSocket["+socketPost+"] is now listening for the request");
			new MultiServerThread(serverSocket.accept()).start();				
		}
		serverSocket.close();
	}
}
