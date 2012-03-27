package com.bankcomm.gd.cbbs;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertiesLoader {

	private final String propertyFile = "./ini/ServerConfig.ini";
	private static Properties props = new Properties();
	private Log log = LogFactory.getLog(PropertiesLoader.class);

	public PropertiesLoader() throws Exception{
		try{
			props.load(new FileInputStream(propertyFile));
		}catch(FileNotFoundException e){
			log.error("初始化PropertiesLoader对象失败:"+e.getMessage());
			props = null;
		}catch(IOException e){
			log.error("初始化PropertiesLoader对象失败:"+e.getMessage());
			props = null;
		}
		throw new Exception();
	}

	public static String getByName(String propName){
		if(null == props){
			return null;
		}
		return props.getProperty(propName);
	}
}
