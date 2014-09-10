package cn.edu.tju.t6.c4.base;

import java.io.Serializable;


public class Record implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int id;
	protected int leaveDays;
	protected long applyID;
	protected long approveID;
	protected String applyTime;
	protected String approveTime;
	protected String state;
	

	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}

	public int getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}
	

	public long getApplyID() {
		return applyID;
	}
	public void setApplyID(long applyID) {
		this.applyID = applyID;
	}

	public long getApproveID() {
		return approveID;
	}
	public void setApproveID(long approveID) {
		this.approveID = approveID;
	}
	

	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
