package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import cn.edu.tju.t6.c4.base.Approval;
import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.Application;
import cn.edu.tju.t6.c4.dao.ApplicationDao;
import cn.edu.tju.t6.c4.dao.ApprovalDao;
import cn.edu.tju.t6.c4.dao.UserDao;

@Service
public class RecordServiceImpl implements RecordService{
	@Autowired
	ApplicationDao recordDao;
	@Autowired
	UserDao userDao;
	@Autowired
	ApprovalDao approvalDao;
	
	
	@Override
	public List<Application> get(long applyID) 
			throws SQLException {
		return recordDao.getRecordByApplyID(applyID);
	}

	@Override
	public List<Application> getAfter(long applyID, Calendar time) 
			throws SQLException {
		List<Application> list = get(applyID);
		for(int i = 0 ; i < list.size() ; i++){
			Application record = list.get(i);
			if(record.getApply_date()==null) break;
			String times[] = record.getApply_date().split(CommonConst.TIME_SPLIT);
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(times[0]), 
					 Integer.parseInt(times[1]), 
					 Integer.parseInt(times[2]));
			if(cal.compareTo(time) < 0) list.remove(record);
		}
		return list;
	}

	@Override
	public List<Application> getByState(String state) 
			throws SQLException {
		return recordDao.getRecordByStatus(state);
	}

	@Override
	public List<Application> getByState(long applyID, String state) 
			throws SQLException {
		return recordDao.getRecordByApplyIDAndState(applyID, state);
	}

	@Override
	public boolean delete(int recordID,long applyID) 
			throws SQLException {
		if(!recordDao.checkExist(recordID))
			throw new SQLException("SQLException: no record which id ="+recordID);
		Application record = recordDao.getRecordByID(recordID);
		if(record.getApplicant_id()!=applyID)
			throw new SQLException("SQLExcetion: this record is not belong to "+applyID);
		return recordDao.deleteRecordByID(recordID);
	}

	@Override
	public boolean update(Application record) 
			throws SQLException {
		if(record.getLeave_length() <= 0)
			throw new SQLException("SQLException: leaveDays can't be 0 or empty!");
		if(!recordDao.checkExist(record.getApplication_id()))
			throw new SQLException("SQLException: no record which id ="+record.getApplication_id());
		return recordDao.updateRecord(record);
	}

	@Override
	public boolean add(Application record) 
			throws SQLException {
		if(record.getLeave_length() == 0)
			throw new SQLException("SQLException: leaveDays can't be 0 or empty!");
		//get history leave record after this year
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1, 1);
		List<Application> list = getAfter(record.getApplicant_id(),cal);
		
		//get total left days
		int totalLeftDays = 0;
		for(int i = 0 ; i < list.size() ; i++)
			totalLeftDays+=list.get(i).getLeave_length();
		
		//get max leave days
		int totalLeaveDays = userDao.get(record.getApplicant_id()).getTotal_annual_leave();
		int restLeaveDays = totalLeaveDays - totalLeftDays;
		
		//compare and get response
		if(record.getLeave_length() > restLeaveDays)
			throw new SQLException("rest leave day("+restLeaveDays+" day) not enough : \n" +
					"total of left days and wait apply leave days is "+totalLeftDays + "\n" +
					"total leave days is "+totalLeaveDays);
		return recordDao.addRecord(record);
	}

	public Application getById(long applicationID) throws SQLException{
		Application appl = recordDao.getRecordByID(applicationID);
		List<Approval> apprs = approvalDao.getApprovalsByApplication(applicationID);
		if(apprs != null);
			appl.setApprovals(apprs);
		return appl;
	}
}
