package cn.edu.tju.t6.c4.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.tju.t6.c4.base.Approval;
import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.DBOperator;

public class ApprovalDao {
	
	private DBOperator dbOperator = new DBOperator();
	
	private final String GET_APPROVALS_BY_ID = "SELECT * FROM approve where application_id = %d;";
	private final String GET_APPROVALS_BY_AUD = "SELECT * FROM approve where auditor_id = %d;";
	private final String ADD_APPROVAL = "INSERT approval (auditor_id, application_id, approve_date, approve_opinion, agreed)"
			+ " values(%d, %d, '%s', %s, %d);";
	
	public List<Approval> getApprovalsByApplication(long applicationID) throws SQLException{
		return dealSelectSQL(String.format(GET_APPROVALS_BY_ID, applicationID));
		
	}
	
	public List<Approval> getApprovalsByAud (long audId)throws SQLException{
		return dealSelectSQL(String.format(GET_APPROVALS_BY_AUD,audId));
	}
	
	
	public List<Approval> dealSelectSQL(String SQL) throws SQLException{
		List<Approval> list = new ArrayList<Approval>();
		ResultSet rs = dbOperator.select(SQL);
		try {
			while(rs.next()){
				list.add(setInfoToCreateApproval(rs));
			}
			dbOperator.close();
			return list;
		} catch (SQLException e) {
			throw new SQLException("Cann't get resultset!");
		}
	}
	
	private Approval setInfoToCreateApproval(ResultSet rs) throws SQLException{
		Approval approval = new Approval();
		approval.setAgreed(rs.getString("agreed")=="1");
		approval.setApproval_id(rs.getInt("approval_id"));
		approval.setApprove_date(rs.getString("approve_date"));
		approval.setAuditor_id(rs.getInt("auditor_id"));
		approval.setApprove_opinion(rs.getString("approve_opinion"));
		approval.setApplication_id(rs.getInt("application_id"));
		return approval;
	}
	
	public boolean add(Approval ap) throws SQLException{
		boolean agreed = ap.getAgreed();
		String approve_date = CommonConst.getCurrentDate();
		int auditor_id = ap.getAuditor_id();
		String approve_opinion = ap.getApprove_opinion();
		int application_id = ap.getApplication_id();
		
		String sql = String.format(ADD_APPROVAL, auditor_id, application_id, approve_date, approve_opinion, agreed);
		return dbOperator.add(sql);
	}
	
}
