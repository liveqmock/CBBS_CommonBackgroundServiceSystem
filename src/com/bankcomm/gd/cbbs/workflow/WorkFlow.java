package com.bankcomm.gd.cbbs.workflow;
/**
 * �������ӿ�,���ӿڶ��幤�����������еĲ���
 * @author gu.qm
 *
 */
public interface WorkFlow {

	/**
	 * ���幤����������ʽ
	 * @param msgBody ��������
	 * @return ���ر���
	 */
	public String execute(String msgBody);
}
