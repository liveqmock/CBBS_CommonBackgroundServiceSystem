package com.bankcomm.gd.cbbs.util;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class PropertiesLoader {

	private String propertyFile = null;
	private static Properties props = new Properties();
	private Log log = LogFactory.getLog(PropertiesLoader.class);

	public PropertiesLoader(){

		this.init();
		if(null==this.propertyFile){
			log.error("配置文件为空");
		}else{
			try{
				props.load(new FileInputStream(this.propertyFile));
			}catch(FileNotFoundException e){
				log.error("初始化PropertiesLoader对象失败:"+e.getMessage());
				props = null;
			}catch(IOException e){
				log.error("初始化PropertiesLoader对象失败:"+e.getMessage());
				props = null;
			}
		}
	}

	/**
	 * 用于初始化配置文件
	 *
	 */
	protected abstract void init();
	
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

	/*public static void main (String args[]){
		System.out.println(new PropertiesLoader().getByName("PORT_NUMBER"));
	}*/
}
