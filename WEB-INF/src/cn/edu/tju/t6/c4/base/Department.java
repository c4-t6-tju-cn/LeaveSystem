package cn.edu.tju.t6.c4.base;

import java.io.Serializable;

public class Department implements Serializable{
	private int department_id;
	private String department_name;
	private User department_manager;
	
	public void setDepartment_id(int id){
		department_id = id;
	}
	
	public int getDepartment_id(){
		return department_id;
	}
	
	public void setDepartment_name(String name){
		department_name = name;
	}
	
	public String getDepartment_name(){
		return department_name;
	}
	
	public void setDepartment_manager(User u){
		department_manager = u;
	}
	
	public User getDepartment_manager(){
		return department_manager;
	}
}

