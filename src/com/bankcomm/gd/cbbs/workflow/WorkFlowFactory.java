package com.bankcomm.gd.cbbs.workflow;
/**
 * ��������Ĺ�����
 * @author Administrator
 *
 */
public class WorkFlowFactory {

	/**
	 * ���չ��������ͷ��ض�Ӧ�Ĺ���������
	 * @param workFlowType ����������
	 * @return ����������
	 */
	public static WorkFlow getWorkFlowObject(String workFlowType){

		if("YCTLOG".equals(workFlowType)){
			return new YctWorkFlow();
		}else{
			return null;
		}

	}
}
