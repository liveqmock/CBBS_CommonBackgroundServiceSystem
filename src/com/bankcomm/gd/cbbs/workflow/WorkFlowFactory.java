package com.bankcomm.gd.cbbs.workflow;
/**
 * 工作流类的工厂类
 * @author Administrator
 *
 */
public class WorkFlowFactory {

	/**
	 * 按照工作流类型返回对应的工作流对象
	 * @param workFlowType 工作流类型
	 * @return 工作流对象
	 */
	public static WorkFlow getWorkFlowObject(String workFlowType){

		if("YCTLOG".equals(workFlowType)){
			return new YctWorkFlow();
		}else{
			return null;
		}

	}
}
