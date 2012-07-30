package com.bankcomm.gd.cbbs.workflow;

public class WorkFlowFactory {
	
	public static final int YCTLOG = 1;

	public static WorkFlow getWorkFlowObject(String serverType){
		if("YCTLOG".equals(serverType)){
			return new YctWorkFlow();
		}else{
			return null;
		}

	}
}
