package cn.edu.tju.t6.c4.service;

import java.util.List;

import cn.edu.tju.t6.c4.base.Department;
import cn.edu.tju.t6.c4.base.User;

public interface UserService {
	
	public List<User> getAll();
	
	public User get(long id);
	
	public boolean add(User user);
	
	public boolean delete(long id);
	
	public boolean update(User user , long current_user);
	
	public List<Department>  getDepartments() ;
}
