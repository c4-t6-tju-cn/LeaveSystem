package cn.edu.tju.t6.c4.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Repository;

import cn.edu.tju.t6.c4.base.DBOperator;
import cn.edu.tju.t6.c4.base.User;

@Repository
public class UserDao {
	
	private DBOperator dbOperator = new DBOperator();

	private final String SELETC_USER_BY_ID = 
			"SELECT U.user_id, U.user_name, U.department_id, D.department_name, U.total_annual_leave, U.staff_position "
			+ " FROM user U, department D "
			+ " WHERE D.department_id=U.department_id and U.user_id = %d;";
	private final String LIST_ALL_USER = "SELECT U.user_id, U.user_name, U.department_id, D.department_abbr, U.total_annual_leave, U.staff_position "
			+ "FROM user U, department D WHERE D.department_id=U.department_id;";
	private final String DELETE_USER_BY_ID = "DELETE FROM user WHERE user_id = %d;";
	private final String ADD_USER = "INSERT user (user_id, user_name, pwd, department_id, staff_position, total_annual_leave) " +
									"values (%d, '%s', '%s', %s, %s, %d);";
	
	private final String UPDATE_USER = "UPDATE user SET %s='%s' " +
									"WHERE user_id = %d;";
	
	private final String UPDATE_USER_BY_ANNUALLEAVE = "UPDATE user SET total_annual_leave=%d where user_id=%d;";
	
	private final String SELECT_PWD = "select pwd from user where user_id=%d;";
	
	private final String SELECT_DEPARTMENT_MANAGER = 
			"select U.user_id, U.user_name, U.department_id,  U.total_annual_leave, U.staff_position"
			+ " from user U where U.department_id='%d' and U.staff_position='manager';";
	
	public boolean checkExist(long id){
		if(get(id)!= null)	return true;
		return false;
	}
	
	
	
	public User get(long id){
		List<User> list = selectBySQL(String.format(SELETC_USER_BY_ID, id));
		if(list.size() == 0)	return null;
		else if(list.size() == 1)	return list.get(0);
		else
			System.out.println("multi user by "+id);
		return null;
	}
	
	public List<User> getAll(){
		return selectBySQL(LIST_ALL_USER);
	}
	
	
	
	public boolean add(User user){
		return dbOperator.add(String.format(ADD_USER, 
				user.getUser_id(),
				user.getUser_name(),
				user.getPwd(),
				user.getDepartment_id() > 0?user.getDepartment_id():null,
				user.getStaff_position() != null? "'"+user.getStaff_position()+"'":null,
				user.getTotal_annual_leave() !=0? user.getTotal_annual_leave():null));
	}

	public boolean update(User user){
		
		if(user.getUser_name()!=null&&user.getUser_name()!=""){
			dbOperator.update(String.format(UPDATE_USER,"user_name",user.getUser_name(),user.getUser_id()));
		}
		if(user.getDepartment_id()>0){
			dbOperator.update(String.format(UPDATE_USER,"department_id",user.getDepartment_id(),user.getUser_id()));
		}
		if(user.getStaff_position()!=null&& user.getStaff_position()!=""){
			dbOperator.update(String.format(UPDATE_USER,"staff_position",user.getStaff_position(),user.getUser_id()));
		}
		if(user.getPwd()!=null&&user.getPwd()!=""){
			dbOperator.update(String.format(UPDATE_USER,"pwd",user.getPwd(),user.getUser_id()));
		}
		return true;
	}
	
	public boolean updateByAnnualleave(User user){
		return dbOperator.update(String.format(UPDATE_USER_BY_ANNUALLEAVE,
									user.getTotal_annual_leave(), user.getUser_id()));
	}
	
	public boolean delete(long id){
		return dbOperator.delete(String.format(DELETE_USER_BY_ID, id));
	}
	
	
	private List<User> selectBySQL(String sql){
		List<User> res = new ArrayList<User>();
		ResultSet rs = dbOperator.select(sql);
		try {
			while(rs.next()){
				res.add(setInfoToCreateUser(rs));
			}
			dbOperator.close();
		} catch (SQLException e) {
			e.printStackTrace();;
		}
		return res;
	}
	
	private User setInfoToCreateUser(ResultSet rs){
		User user = new User();
		user.setUser_id(Long.parseLong(dbOperator.getItemResult(rs, "U.user_id")));
		user.setUser_name(dbOperator.getItemResult(rs, "U.user_name"));
		user.setStaff_position(dbOperator.getItemResult(rs,"U.staff_position"));
		user.setDepartment_id(Integer.parseInt(dbOperator.getItemResult(rs,"U.department_id")));
		user.setTotal_annual_leave(Integer.parseInt(dbOperator.getItemResult(rs,"U.total_annual_leave")));
		user.setDepartment_abbr(dbOperator.getItemResult(rs,"D.department_abbr"));
		user.setDepartment_name(dbOperator.getItemResult(rs,"D.department_name"));
		return user;
	}
	
	public String getPwd(long id){
		ResultSet rs = dbOperator.select(String.format(SELECT_PWD,id));
		Random rand = new Random();
		int pwdi = rand.nextInt();
		String pwd=pwdi+"";
		try {
			if(rs.next()){
				pwd=rs.getString("pwd");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pwd;
	}
	
	public User getDepartmentManager(int dep_id){
		ResultSet rs = dbOperator.select(String.format(SELECT_DEPARTMENT_MANAGER,dep_id));
		//String pwd=pwdi+"";
		try {
			if(rs.next()){
				return setInfoToCreateUser(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) throws SQLException{
		UserDao ud = new UserDao();
		System.out.println(ud.get(3011218145l).getUser_name());
	}
}
