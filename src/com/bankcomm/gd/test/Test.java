package com.bankcomm.gd.test;

import java.util.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  /*long a=new Date().getTime();
		  //long c=1000;
		  long b=1000l*60*60*24*356;
		  System.out.println(a/b);*/
		Map<String, Long> sockets_timeout = new HashMap<String, Long>();
		sockets_timeout.put("1",111l);
		System.out.println(sockets_timeout.get("2"));

	}

}
