package cn.edu.tju.t6.c4.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import cn.edu.tju.t6.c4.base.DBOperator;
import cn.edu.tju.t6.c4.base.Department;

@Repository
public class DepartmentDao {
	private final String SELETC_DEPARTMENT = 
			"SELECT * FROM department;";
	private DBOperator dbOperator = new DBOperator();
	public ArrayList<Department> getDepartments(){
		ResultSet rs = dbOperator.select(SELETC_DEPARTMENT);
		ArrayList<Department> deps = new ArrayList<Department>();
		if(rs!=null){
			try {
				while(rs.next()){
					Department d = new Department();
					d.setDepartment_id(rs.getInt("department_id"));
					d.setDepartment_name(rs.getString("department_name"));
					deps.add(d);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return deps;
			
	}
}
