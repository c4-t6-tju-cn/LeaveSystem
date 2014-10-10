package cn.edu.tju.t6.c4.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.tju.t6.c4.base.Approval;
import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.DBOperator;

@Repository
public class ApprovalDao {
	private DBOperator dbOperator = new DBOperator();
	private final String GET_APPROVALS_BY_ID = "SELECT * FROM approval where application_id = %d;";
	private final String GET_APPROVALS_BY_AUD = "SELECT * FROM approval where auditor_id = %d;";
	private final String ADD_APPROVAL = "INSERT approval (auditor_id, application_id, approve_date, approve_opinion, agreed)"
			+ " values(%d, %d, '%s', %s, %s);";
	
	public List<Approval> getApprovalsByApplication(long applicationID){
		return dealSelectSQL(String.format(GET_APPROVALS_BY_ID, applicationID));
	}
	
	public List<Approval> getApprovalsByAud (long audId)throws SQLException{
		return dealSelectSQL(String.format(GET_APPROVALS_BY_AUD,audId));
	}
	
	public List<Approval> dealSelectSQL(String SQL){
		List<Approval> list = new ArrayList<Approval>();
		try {
			ResultSet rs = dbOperator.select(SQL);
			while(rs.next()){
				list.add(setInfoToCreateApproval(rs));
			}
			dbOperator.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private Approval setInfoToCreateApproval(ResultSet rs) throws SQLException{
		Approval approval = new Approval();
		approval.setAgreed(rs.getString("agreed")=="1");
		approval.setApproval_id(rs.getInt("approval_id"));
		approval.setApprove_date(rs.getString("approve_date"));
		approval.setAuditor_id(Long.parseLong(rs.getString("auditor_id")));
		approval.setApprove_opinion(rs.getString("approve_opinion"));
		approval.setApplication_id(rs.getInt("application_id"));
		return approval;
	}
	
	public boolean add(Approval ap){
		boolean agreed = ap.getAgreed();
		String approve_date = CommonConst.getCurrentDate();
		long auditor_id = ap.getAuditor_id();
		String approve_opinion = ap.getApprove_opinion();
		int application_id = ap.getApplication_id();
		String sql = String.format(ADD_APPROVAL, auditor_id, application_id, approve_date, 
				(approve_opinion.equals(""))?"null":"'" + approve_opinion+ "'", 
				agreed?"true":"false");
		return dbOperator.add(sql);
	}
	
}
