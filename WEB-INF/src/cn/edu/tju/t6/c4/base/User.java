package cn.edu.tju.t6.c4.base;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	
	protected long user_id;
	protected String user_name;
	protected int department_id;
	protected String staff_position;
	protected int total_annual_leave;
	protected String department_name;
	protected String department_abbr;
	protected String pwd;
	
	public String getDepartment_name(){
		return department_name;
	}
	
	public void setDepartment_name(String department_name){
		this.department_name = department_name;
	}
	
	public String getDepartment_abbr(){
		return department_abbr;
	}
	
	public void setDepartment_abbr(String department_abbr){
		this.department_abbr = department_abbr;
	}
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String  getStaff_position() {
		return staff_position;
	}
	public void setStaff_position(String staff_position) {
		this.staff_position = staff_position;
	}
	
	public int getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}
	
	public int getTotal_annual_leave() {
		return total_annual_leave;
	}
	public void setTotal_annual_leave(int total_annual_leave) {
		this.total_annual_leave = total_annual_leave;
	}

	public String getPwd(){
		return pwd;
	}
	public void setPwd(String pwd){
		this.pwd = pwd;
	}
}
