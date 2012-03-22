package com.bankcomm.gd;

import com.bocom.eb.des.EBDES;

public class CommunicationProtocol {

	public String  processInput(String str){

		final String ERRSTR = "999999            "; 
		String tradeCode = str.substring(0,6);
		if("480000".equals(tradeCode)){
			if("0".equals(str.substring(6,7))){
				String password = str.substring(,);
			    String sessionID = "IEIMIYBZDKEPEZEOIADFESIMETIFEUJKIHGTCHBD";
			    try {
			      String crypter = EBDES.encryptoDES(password, sessionID);
			      System.out.println(crypter);
			      String clear = EBDES.decryptoDES(crypter, sessionID);
			      System.out.println(clear);
			    }
			    catch (Exception ex) {
			      System.out.println(ex);
			    }
			}else if("1".equals(str.substring(6,7))){
				
			}else{
				str=ERRSTR;
			}
		}else{
			str=ERRSTR;
		}
		return str;
	}
}
