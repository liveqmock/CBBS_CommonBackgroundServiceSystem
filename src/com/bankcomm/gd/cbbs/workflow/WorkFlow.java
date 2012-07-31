package com.bankcomm.gd.cbbs.workflow;
/**
 * 工作流接口,本接口定义工作流对象所有的操作
 * @author gu.qm
 *
 */
public interface WorkFlow {

	/**
	 * 定义工作流操作方式
	 * @param msgBody 请求报文体
	 * @return 返回报文
	 */
	public String execute(String msgBody);
}
