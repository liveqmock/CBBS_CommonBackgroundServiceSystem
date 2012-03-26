package com.bankcomm.gd.service;

import java.util.List;
import java.util.ArrayList;
import com.bankcomm.gd.service.CommonServer;

/**
 * 本类用于管理所有的服务器
 * @author 顾启明
 *
 */
public final class ServerManager {

	/** 服务器列表 */
	private List<CommonServer> serverList = new ArrayList<CommonServer>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询服务器状态
	 * @param serverName 服务名称:<br/>
	 *                     ALL:为
	 * @return
	 */
	public boolean serverStatus(String serverName){
		return false;
	}
}
