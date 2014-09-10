package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.tju.t6.c4.base.User;
import cn.edu.tju.t6.c4.dao.UserDao;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao userDao;
	
	@Override
	public List<User> getAll() throws SQLException {
		return userDao.getAll();
	}

	@Override
	public User get(long id) throws SQLException {
		return userDao.get(id);
	}

	@Override
	public boolean add(User user) throws SQLException{
		if(user.getID() == 0)
			throw new SQLException("SQLException: cann't add user which user id is 0!");
		if(user.getName() == null)
			throw new SQLException("SQLException: cann't add user which user name is null!");
		if(user.getPwd() == null)
			throw new SQLException("SQLException: cann't add user which user pwd is empty!");
		return userDao.add(user);
	}

	@Override
	public boolean delete(long id) throws SQLException{
		if(id == 0 || !userDao.checkExist(id))
			throw new SQLException("SQLException: no user which id ="+id);
		return userDao.delete(id);
	}

	@Override
	public boolean update(User user) throws SQLException{
		if(user.getName() == null)
			throw new SQLException("SQLException: user name cann't be null");
		if(user.getID() == 0 ||!userDao.checkExist(user.getID()))
			throw new SQLException("SQLException: no user which id ="+user.getID());
		return userDao.update(user);
	}
}
