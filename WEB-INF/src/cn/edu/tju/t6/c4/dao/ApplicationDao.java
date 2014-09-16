package cn.edu.tju.t6.c4.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.tju.t6.c4.base.Approval;
import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.DBOperator;
import cn.edu.tju.t6.c4.base.Application;

@Repository
public class ApplicationDao {
	
	private DBOperator dbOperater = new DBOperator();
	
	private final String CHECK_EXIST_RECORD = "SELECT * FROM application WHERE application_id = %d;";
	private final String GET_RECORD_BY_APPLYID = "SELECT * FROM application WHERE applicant_id = %d;";
	private final String GET_RECORD_BY_STATE = "SELECT * FROM application WHERE status = '%s';";
	private final String GET_RECORD_BY_APPLYID_AND_STATE = "SELECT * FROM application WHERE applicant_id = %d and status = '%s';";
	private final String GET_RECORD_BY_ID = "SELECT * FROM application WHERE application_id = %d;";
	private final String DELETE_RECORD_BY_RECORDID = "DELETE FROM application WHERE application_id = %d;";
	private final String ADD_RECORD = "INSERT application "
			+ 		"(leave_date, applicant_id, leave_length, leave_reason, leave_type, apply_date, status) " 
			+ "values (%s, %d, %d, %s, %s, %s, %s);";
	private final String UPDATE_RECORD = "UPDATE application " +
										"SET %s %s %s %s %s %s where application_id=%d ;";
	
	
	   
	public List<Application> getRecordByApplyID(long applyID) 
			throws SQLException{
		return selectBySQL(String.format(GET_RECORD_BY_APPLYID, applyID));
	}
	
	
	
	public List<Application> getRecordByStatus(String state) 
			throws SQLException{
		return selectBySQL(String.format(GET_RECORD_BY_STATE, state));
	}
	
	public List<Application> getRecordByApplyIDAndState(long applyID, String state) 
			throws SQLException{
		return selectBySQL(String.format(GET_RECORD_BY_APPLYID_AND_STATE, applyID, state));
	}
	
	public Application getRecordByID(int id) 
			throws SQLException{
		return selectBySQL(String.format(GET_RECORD_BY_ID, id)).get(0);
	}
	
	public boolean deleteRecordByID(long id) 
			throws SQLException{
		return dbOperater.delete(String.format(DELETE_RECORD_BY_RECORDID, id));
	}
	
	public boolean addRecord(Application record) 
			throws SQLException{
		
		return dbOperater.add(String.format(ADD_RECORD,
				record.getLeave_date(),
				record.getApplicant_id(),
				record.getLeave_length(),
				record.getLeave_reason() != null ? "'"+record.getLeave_reason()+"'":null,
				record.getLeave_type(),
				record.getApply_date(),
				record.getStatus()));
	}
	
	public boolean checkExist(int id) 
			throws SQLException{
		boolean res = false;
		ResultSet rs = dbOperater.select(String.format(CHECK_EXIST_RECORD, id));
		res = rs.first();
		dbOperater.close();
		return res;
	}
	
	public boolean updateRecord(Application record) 
			throws SQLException{
		
		
		return dbOperater.update(String.format(UPDATE_RECORD,
				record.getLeave_date()!=null?"leave_date='" + record.getLeave_date()+"',":"",
				record.getLeave_length() > 0 ? "leave_length="+record.getLeave_length()+",":"",
				record.getLeave_reason() != null ? "leave_reason='"+record.getLeave_reason()+"',":"",
				record.getLeave_type() != null ? "leave_type='"+record.getLeave_type()+"',":"",
				record.getApply_date() != null? "apply_date='"+record.getApply_date()+"',":"",
				"status='"+record.getStatus()+"'",
				record.getApplicant_id()));
	}
	
	private List<Application> selectBySQL(String sql) throws SQLException{
		List<Application> res = new ArrayList<Application>();
		ResultSet rs = dbOperater.select(sql);
		try {
			while(rs.next()){
				res.add(setInfoToCreateRecord(rs));
			}
			dbOperater.close();
			return res;
		} catch (SQLException e) {
			throw new SQLException("Can't get resultset!");
		}
	}
	
	private Application setInfoToCreateRecord(ResultSet rs) 
			throws NumberFormatException{
		Application record = new Application();
		try{
			record.setApplication_id(rs.getInt("application_id"));
			record.setStatus(rs.getString("status"));
			
			if(rs.getString("leave_length")!=null)
				record.setLeave_length(Integer.parseInt(rs.getString("leave_length")));
			if(rs.getString("leave_type")!=null)
				record.setLeave_type(rs.getString("leave_type"));
	
			if(rs.getString("apply_date")!=null)
				record.setApply_date(rs.getString("apply_date"));
			
			
			
			if(rs.getString("applicant_id")!=null)
				record.setApplicant_id(Long.parseLong(rs.getString("applicant_id")));
			
			if(rs.getString("leave_reason")!=null)
				record.setLeave_reason(rs.getString("leave_reason"));
			
			if(rs.getString("leave_date")!=null)
				record.setLeave_date(rs.getString("leave_date"));
			
		}catch(SQLException e){
			System.out.println("err when set application msg:" + e.getMessage() + "\nTrace:\n");
			e.printStackTrace();
		}
		return record;
		
	}
}
