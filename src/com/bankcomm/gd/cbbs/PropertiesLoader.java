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
	 * ʹ�ö�Ӧ�����ļ�����ʵ��
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
	 * �������޲���ʵ����
	 *
	 */
	private PropertiesLoader(){
		this.propertyFile=null;
	}

	/**
	 * �õ�����ֵ,<b>����ֵ����Ϊnull,��Ҫ�Է���ֵ�����ж�</b>
	 * @param propName ����������
	 * @return ����ֵ
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
