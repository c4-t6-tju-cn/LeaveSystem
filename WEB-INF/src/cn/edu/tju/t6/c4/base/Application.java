package cn.edu.tju.t6.c4.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="application")
public class Application implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected int application_id;
	protected long applicant_id;
	protected String leave_date;
	protected int leave_length;
	protected String leave_reason;
	protected String leave_type;
	@OneToMany(cascade = { CascadeType.ALL },fetch = FetchType.EAGER)
	@JoinColumn(name = "application_id")	
	protected List<Approval> approvals;
	protected String apply_date;
	protected String status;

	public int getApplication_id() {
		return application_id;
	}
	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}
	
	public long getApplicant_id() {
		return applicant_id;
	}
	public void setApplicant_id(long applicant_id) {
		this.applicant_id = applicant_id;
	}
	
	
	public String getLeave_date() {
		return leave_date;
	}
	public void setLeave_date(String leave_date) {
		this.leave_date = leave_date;
	}
	
	
	
	
	public int getLeave_length() {
		return leave_length;
	}
	public void setLeave_length(int leave_length) {
		this.leave_length = leave_length;
	}
	
	public String getLeave_reason() {
		return leave_reason;
	}
	public void setLeave_reason(String leave_reason) {
		this.leave_reason = leave_reason;
	}
	
	public void setLeave_type(String leave_type){
		this.leave_type = leave_type;
	}
	public String getLeave_type(){
		return leave_type;
	}

	public List<Approval> getApprovals() {
		return approvals;
	}
	public void setApprovals(List<Approval> approvals) {
		this.approvals = approvals;
	}
	

	public String getApply_date() {
		return apply_date;
	}
	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
