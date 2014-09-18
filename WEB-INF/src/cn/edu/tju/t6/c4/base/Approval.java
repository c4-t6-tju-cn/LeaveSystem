package cn.edu.tju.t6.c4.base;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="approval")
public class Approval implements Serializable{
	protected int approval_id;
	protected int application_id;
	protected long auditor_id;
	protected String approve_date;
	protected String approve_opinion;
	protected boolean agreed;
	
	public int getApproval_id() {
		return approval_id;
	}
	public void setApproval_id(int approval_id) {
		this.approval_id = approval_id;
	}
	
	public int getApplication_id(){
		return application_id;
	}
	
	public void setApplication_id(int application_id){
		this.application_id = application_id;
	}
	public long getAuditor_id() {
		return auditor_id;
	}
	public void setAuditor_id(long l) {
		this.auditor_id = l;
	}
	
	public String getApprove_date() {
		return approve_date;
	}
	public void setApprove_date(String approve_date) {
		this.approve_date = approve_date;
	}
	
	public String getApprove_opinion() {
		return approve_opinion;
	}
	public void setApprove_opinion(String approve_opinion) {
		this.approve_opinion = approve_opinion;
	}
	
	public boolean getAgreed() {
		return agreed;
	}
	public void setAgreed(boolean agreed) {
		this.agreed = agreed;
	}
}
