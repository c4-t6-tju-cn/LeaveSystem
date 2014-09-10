package cn.edu.tju.t6.c4.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.tju.t6.c4.base.DBOperator;
import cn.edu.tju.t6.c4.base.User;

@Repository
public class UserDao {
	
	private DBOperator dbOperater = new DBOperator();

	private final String SELETC_USER_BY_ID = "SELECT * FROM user WHERE id = %d;";
	private final String LIST_ALL_USER = "SELECT * FROM user;";
	private final String DELETE_USER_BY_ID = "DELETE FROM user WHERE id = %d;";
	private final String ADD_USER = "INSERT user (id, name, pwd, department, totalLeaveDays) " +
									"values (%d, '%s', '%s', %s, %d);";
	private final String UPDATE_USER = "UPDATE user SET name = '%s', department= %s, totalLeaveDays = %d " +
									"WHERE id = %d;";
	
	
	public boolean checkExist(long id) 
			throws SQLException{
		if(get(id)!= null)	return true;
		return false;
	}
	
	public User get(long id) 
			throws SQLException{
		List<User> list = selectBySQL(String.format(SELETC_USER_BY_ID, id));
		if(list.size() == 0)	return null;
		else if(list.size() == 1)	return list.get(0);
		else
			throw new SQLException("muti user by "+id);
	}
	
	public List<User> getAll() 
			throws SQLException{
		return selectBySQL(LIST_ALL_USER);
	}
	
	public boolean add(User user) 
			throws SQLException{
		return dbOperater.add(String.format(ADD_USER, 
				user.getID(),
				user.getName(),
				user.getPwd(),
				user.getDepartment() != null? "'"+user.getDepartment()+"'":null,
				user.getTotalLeaveDays() !=0? user.getTotalLeaveDays():null));
	}

	public boolean update(User user) 
			throws SQLException{
		return dbOperater.update(String.format(UPDATE_USER, 
				user.getName(),
				user.getDepartment() != null? "'"+user.getDepartment()+"'":null,
				user.getTotalLeaveDays() !=0? user.getTotalLeaveDays():null,
				user.getID()));
	}
	
	public boolean delete(long id) 
			throws SQLException{
		return dbOperater.delete(String.format(DELETE_USER_BY_ID, id));
	}
	
	
	private List<User> selectBySQL(String sql) throws SQLException{
		List<User> res = new ArrayList<User>();
		ResultSet rs = dbOperater.select(sql);
		try {
			while(rs.next()){
				res.add(setInfoToCreateUser(rs));
			}
			dbOperater.close();
			return res;
		} catch (SQLException e) {
			throw new SQLException("Cann't get resultset!");
		}
	}
	
	private User setInfoToCreateUser(ResultSet rs) throws SQLException{
		User user = new User();
		user.setID(rs.getLong("id"));
		user.setName(rs.getString("name"));
		user.setPwd(rs.getString("pwd"));
		if(rs.getString("department")!=null)
			user.setDepartment(rs.getString("department"));
		user.setTotalLeaveDays(rs.getInt("totalLeaveDays"));
		return user;
	}
	
	public String getPwd(long id) throws SQLException{
		return get(id).getPwd();
	}

	public static void main(String args[]) throws SQLException{
		UserDao ud = new UserDao();
		System.out.println(ud.get(3011218145l).getName());
	}
}
