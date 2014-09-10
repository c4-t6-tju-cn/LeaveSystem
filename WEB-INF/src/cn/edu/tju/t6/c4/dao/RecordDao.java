package cn.edu.tju.t6.c4.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.tju.t6.c4.base.DBOperator;
import cn.edu.tju.t6.c4.base.Record;

@Repository
public class RecordDao {
	
	private DBOperator dbOperater = new DBOperator();
	
	private final String CHECK_EXIST_RECORD = "SELECT * FROM record WHERE id = %d;";
	private final String GET_RECORD_BY_APPLYID = "SELECT * FROM record WHERE apply_id = %d;";
	private final String GET_RECORD_BY_STATE = "SELECT * FROM record WHERE state = '%s';";
	private final String GET_RECORD_BY_APPLYID_AND_STATE = "SELECT * FROM record WHERE apply_id = %d and state = '%s';";
	private final String GET_RECORD_BY_ID = "SELECT * FROM record WHERE id = %d;";
	private final String DELETE_RECORD_BY_RECORDID = "DELETE FROM record WHERE id = %d;";
	private final String ADD_RECORD = "INSERT record (leave_day, apply_id, approve_id, apply_time, approve_time, state) " +
										"values (%d, %d, %d, %s, %s, %s);";
	private final String UPDATE_RECORD = "UPDATE record " +
										"SET leave_day = %d, apply_id = %d, approve_id = %d, " +
											"apply_time = %s, approve_time= %s, state = %s " +
										"WHERE id = %d;";
	   
	public List<Record> getRecordByApplyID(long applyID) 
			throws SQLException{
		return selectBySQL(String.format(GET_RECORD_BY_APPLYID, applyID));
	}
	
	public List<Record> getRecordByState(String state) 
			throws SQLException{
		return selectBySQL(String.format(GET_RECORD_BY_STATE, state));
	}
	
	public List<Record> getRecordByApplyIDAndState(long applyID, String state) 
			throws SQLException{
		return selectBySQL(String.format(GET_RECORD_BY_APPLYID_AND_STATE, applyID, state));
	}
	
	public Record getRecordByID(int id) 
			throws SQLException{
		return selectBySQL(String.format(GET_RECORD_BY_ID, id)).get(0);
	}
	
	public boolean deleteRecordByID(long id) 
			throws SQLException{
		return dbOperater.delete(String.format(DELETE_RECORD_BY_RECORDID, id));
	}
	
	public boolean addRecord(Record record) 
			throws SQLException{
		
		return dbOperater.add(String.format(ADD_RECORD,
				record.getLeaveDays(),
				record.getApplyID()	!= 0 ? record.getApplyID():null,
				record.getApproveID() != 0 ? record.getApproveID():null,
				record.getApplyTime() != null ? "'"+record.getApplyTime()+"'":null,
				record.getApproveTime() != null ? "'"+record.getApproveTime()+"'":null,
				record.getState() != null ? "'"+record.getState()+"'":null));
	}
	
	public boolean checkExist(int id) 
			throws SQLException{
		boolean res = false;
		ResultSet rs = dbOperater.select(String.format(CHECK_EXIST_RECORD, id));
		res = rs.first();
		dbOperater.close();
		return res;
	}
	
	public boolean updateRecord(Record record) 
			throws SQLException{
		
		return dbOperater.update(String.format(UPDATE_RECORD,
				record.getLeaveDays(),
				record.getApplyID()	!= 0 ? record.getApplyID():null,
				record.getApproveID() != 0 ? record.getApproveID():null,
				record.getApplyTime() != null ? "'"+record.getApplyTime()+"'":null,
				record.getApproveTime() != null ? "'"+record.getApproveTime()+"'":null,
				record.getState() != null ? "'"+record.getState()+"'":null,
				record.getID()));
	}
	
	private List<Record> selectBySQL(String sql) throws SQLException{
		List<Record> res = new ArrayList<Record>();
		ResultSet rs = dbOperater.select(sql);
		try {
			while(rs.next()){
				res.add(setInfoToCreateRecord(rs));
			}
			dbOperater.close();
			return res;
		} catch (SQLException e) {
			throw new SQLException("Cann't get resultset!");
		}
	}
	
	private Record setInfoToCreateRecord(ResultSet rs) 
			throws NumberFormatException, SQLException{
		Record record = new Record();
		record.setID(rs.getInt("id"));
		record.setLeaveDays(rs.getInt("leave_day"));
		
		if(rs.getString("apply_id")!=null)
			record.setApplyID(Long.parseLong(rs.getString("apply_id")));
		if(rs.getString("approve_id")!=null)
			record.setApproveID(Long.parseLong(rs.getString("approve_id")));

		if(rs.getString("apply_time")!=null)
			record.setApplyTime(rs.getString("apply_time"));
		if(rs.getString("approve_time")!=null)
			record.setApproveTime(rs.getString("approve_time"));
		
		if(rs.getString("state")!=null)
			record.setState(rs.getString("state"));
		
		return record;
	}
}
