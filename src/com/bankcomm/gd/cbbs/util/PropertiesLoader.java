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
			log.error("�����ļ�Ϊ��");
		}else{
			try{
				props.load(new FileInputStream(this.propertyFile));
			}catch(FileNotFoundException e){
				log.error("��ʼ��PropertiesLoader����ʧ��:"+e.getMessage());
				props = null;
			}catch(IOException e){
				log.error("��ʼ��PropertiesLoader����ʧ��:"+e.getMessage());
				props = null;
			}
		}
	}

	/**
	 * ���ڳ�ʼ�������ļ�
	 *
	 */
	protected abstract void init();
	
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

	/*public static void main (String args[]){
		System.out.println(new PropertiesLoader().getByName("PORT_NUMBER"));
	}*/
}
