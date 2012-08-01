package com.bankcomm.gd.cbbs;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertiesLoader {

	private final String propertyFile;
	private static Properties props = new Properties();
	private Log log = LogFactory.getLog(PropertiesLoader.class);

	/**
	 * 使用对应配置文件构造实例
	 * @param configFile
	 */
	public PropertiesLoader(String configFile){

		this.propertyFile=configFile;
		if(null==this.propertyFile){
			log.error("Config file is null.");
		}else{
			try{
				props.load(new FileInputStream(this.propertyFile));
			}catch(FileNotFoundException e){
				log.error("Initialization PropertiesLoader object error:"+e.getMessage());
				props = null;
			}catch(IOException e){
				log.error("Initialization PropertiesLoader object error:"+e.getMessage());
				props = null;
			}
		}
	}

	/**
	 * 不允许无参数实例化
	 *
	 */
	private PropertiesLoader(){
		this.propertyFile=null;
	}

	/**
	 * 得到配置值,<b>返回值可能为null,需要对返回值进行判断</b>
	 * @param propName 配置项名称
	 * @return 配置值
	 */
	public String getByName(String propName){
		if(null == props){
			return null;
		}else{
			return props.getProperty(propName);
		}
	}

	public static void main (String args[]){
		PropertiesLoader tester = new PropertiesLoader("./ini/ServerConfig.ini");
		System.out.println("PORT_NUMBER:"+tester.getByName("PORT"));
		System.out.println("NOTHING:"+tester.getByName("NOTHING"));
	}
}
