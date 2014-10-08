package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.tju.t6.c4.base.Department;
import cn.edu.tju.t6.c4.base.User;
import cn.edu.tju.t6.c4.dao.DepartmentDao;
import cn.edu.tju.t6.c4.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao userDao;
	
	@Override
	public List<User> getAll() {
		return userDao.getAll();
	}

	@Override
	public User get(long id) {
		return userDao.get(id);
	}

	@Override
	public boolean add(User user){
		/*if(user.getUser_id() == 0)
			throw new SQLException("SQLException: cann't add user which user id is 0!");
		if(user.getUser_name() == null)
			throw new SQLException("SQLException: cann't add user which user name is null!");
		if(user.getPwd() == null)
			throw new SQLException("SQLException: cann't add user which user pwd is empty!");*/
		return userDao.add(user);
	}

	@Override
	public boolean delete(long id){
		/*if(id == 0 || !userDao.checkExist(id))
			throw new SQLException("SQLException: no user which id ="+id);*/
		return userDao.delete(id);
	}

	@Override
	public boolean update(User user, long current_user){
		/*if(user.getUser_name() == null)
			throw new SQLException("SQLException: user name cann't be null");
		if(user.getUser_id() == 0 ||!userDao.checkExist(user.getUser_id()))
			throw new SQLException("SQLException: no user which id ="+user.getUser_id());*/
		User current = userDao.get(current_user);
		User update_user = new User();
		long update_id = user.getUser_id();
		
		if(current.getStaff_position().equalsIgnoreCase("admin")){
			update_user = user;
		}
		else{
			if(update_id == current_user){
				update_user.setDepartment_name(user.getUser_name());
				update_user.setPwd(user.getPwd());
			}
			if(current.getDepartment_id()==2){
				update_user.setTotal_annual_leave(user.getTotal_annual_leave());
			}
			if(current.getStaff_position().contains("MANAGER")){
				update_user.setDepartment_id(user.getDepartment_id());
			}
			update_user.setUser_id(update_id);
		}
		
		
		return userDao.update(update_user);
	}

	@Override
	public List<Department> getDepartments() {
		// TODO Auto-generated method stub
		DepartmentDao dd = new DepartmentDao();
		ArrayList<Department> deps = dd.getDepartments();
		for(int i = 0 ; i < deps.size(); i ++){
			Department d = deps.get(i);
			User manager = userDao.getDepartmentManager(d.getDepartment_id());
			d.setDepartment_manager(manager);
		}
		return deps;
	}
}
