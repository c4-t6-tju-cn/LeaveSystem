package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.List;

import cn.edu.tju.t6.c4.base.Department;
import cn.edu.tju.t6.c4.base.User;

public interface UserService {
	
	public List<User> getAll() throws SQLException;
	
	public User get(long id) throws SQLException;
	
	public boolean add(User user) throws SQLException;
	
	public boolean delete(long id) throws SQLException;
	
	public boolean update(User user) throws SQLException;
	
	public List<Department>  getDepartments() ;
}
