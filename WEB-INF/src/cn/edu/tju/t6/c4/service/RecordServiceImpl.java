package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;







import cn.edu.tju.t6.c4.base.Approval;
import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.Application;
import cn.edu.tju.t6.c4.base.User;
import cn.edu.tju.t6.c4.dao.ApplicationDao;
import cn.edu.tju.t6.c4.dao.ApprovalDao;
import cn.edu.tju.t6.c4.dao.DepartmentDao;
import cn.edu.tju.t6.c4.dao.UserDao;

@Service
public class RecordServiceImpl implements RecordService{
	@Autowired
	ApplicationDao applicationDao;
	@Autowired
	UserDao userDao;
	@Autowired
	ApprovalDao approvalDao;
	@Autowired
	DepartmentDao departmentDao;
	
	@Override
	public List<Application> get(long applyID){
		return applicationDao.getRecordByApplyID(applyID);
	}

	@Override
	public List<Application> getAfter(long applyID, int year) 
			{
		
		return applicationDao.getRecordForAnnual(applyID, year);
	}

	@Override
	public List<Application> getByState(String state) 
			throws SQLException {
		return applicationDao.getRecordByStatus(state);
	}

	@Override
	public List<Application> getByState(long applyID, String state) 
			throws SQLException {
		return applicationDao.getRecordByApplyIDAndState(applyID, state);
	}

	@Override
	public boolean delete(int recordID) {
		if(!applicationDao.checkExist(recordID))
			System.out.println("SQLException: no record which id ="+recordID);
		return applicationDao.deleteRecordByID(recordID);
	}

	@Override
	public boolean update(Application record) 
			throws SQLException {
		if(record.getLeave_length() <= 0)
			throw new SQLException("SQLException: leaveDays can't be 0 or empty!");
		if(!applicationDao.checkExist(record.getApplication_id()))
			throw new SQLException("SQLException: no record which id ="+record.getApplication_id());
		return applicationDao.updateRecord(record);
	}

	@Override
	public boolean add(Application record, long applicant_id) 
			{
		if(record.getLeave_length() == 0){
			System.out.println("SQLException: leaveDays can't be 0 or empty!");
			return false;
		}
		//get history leave record after this year
		//System.out.println(record.getLeave_type());
		if(record.getLeave_type().equals("annual")){
			Calendar cal = Calendar.getInstance();
			List<Application> list = getAfter(record.getApplicant_id(),cal.YEAR);
			
			//get total left days
			int totalLeftDays = 0;
			for(int i = 0 ; i < list.size() ; i++)
				totalLeftDays+=list.get(i).getLeave_length();
			
			//get max leave days
			int totalLeaveDays = userDao.get(record.getApplicant_id()).getTotal_annual_leave();
			int restLeaveDays = totalLeaveDays - totalLeftDays;
			
			
			//compare and get response
			if(record.getLeave_length() > restLeaveDays){
				System.out.println("Error when apply: rest leave day("+restLeaveDays+" day) not enough : \n" +
						"total of left days and wait apply leave days is "+totalLeftDays + "\n" +
						"total leave days is "+totalLeaveDays);
				return false;
			}
		}
		record.setApplicant_id(applicant_id);
		record.setStatus(CommonConst.STATE_WAITMANAGER);
		record.setApply_date(CommonConst.getCurrentDate());
		return applicationDao.addRecord(record);
	}
	@Override
	public Application getById(long applicationID) throws SQLException{
		Application appl = applicationDao.getRecordByID(applicationID);
		List<Approval> apprs = approvalDao.getApprovalsByApplication(applicationID);
		User applicant = userDao.get(appl.getApplicant_id());
		appl.setApplicant(applicant);
		if(apprs != null);
			appl.setApprovals(apprs);
		
		return appl;
	}

	@Override
	public boolean approve(Approval appr, long auditorId) {
		// TODO Auto-generated method stub
		// check form
		/**
		 * check list:
		 * *. Admin can approve all application without any check and the 
		 * 0. A denied/passed approval can't be approve again
		 * 1. Auditor position is bigger than its needs
		 * 2. Same Department when a "wait department manager"
		 * 3. Approve date is not later than application's leave-start date
		 */
		long application_id = appr.getApplication_id();
		Application appl = applicationDao.getRecordByID(application_id);
		User applicant = userDao.get(appl.getApplicant_id());
		User auditor = userDao.get(auditorId);
		boolean checkOk = true;
		
		if(appl.getStatus().equalsIgnoreCase(CommonConst.STATE_FAIL)
				||appl.getStatus().equalsIgnoreCase(CommonConst.STATE_SUCCESS)
				||CommonConst.notBefore(appl.getLeave_date(), CommonConst.getCurrentDate())){
			checkOk = false;
		}
		else{
			if(auditor.getStaff_position().equalsIgnoreCase(CommonConst.POSITION_AD)){
				if(appr.getAgreed())
					appl.setStatus(CommonConst.STATE_SUCCESS);
				else
					appl.setStatus(CommonConst.STATE_FAIL);
			}
			else if(appl.getStatus().equalsIgnoreCase(CommonConst.STATE_WAITGM)
					&& auditor.getStaff_position().equalsIgnoreCase(CommonConst.POSITION_GM)){
				if(appr.getAgreed())
					appl.setStatus(CommonConst.STATE_SUCCESS);
				else
					appl.setStatus(CommonConst.STATE_FAIL);
			}
			else if(appl.getStatus().equalsIgnoreCase(CommonConst.STATE_WAITGMV)){
				if(auditor.getStaff_position().equalsIgnoreCase(CommonConst.POSITION_GM)){
					if(appr.getAgreed())
						appl.setStatus(CommonConst.STATE_SUCCESS);
					else
						appl.setStatus(CommonConst.STATE_FAIL);
				}
				else if(auditor.getStaff_position().equalsIgnoreCase(CommonConst.POSITION_VM)){
					if(appr.getAgreed())
						appl.setStatus(CommonConst.STATE_WAITGM);
					else
						appl.setStatus(CommonConst.STATE_FAIL);
				}
				else checkOk = false;
			}
			else if(appl.getStatus().equalsIgnoreCase(CommonConst.STATE_WAITMANAGER)){
				if(auditor.getStaff_position().equalsIgnoreCase(CommonConst.POSITION_GM)){
					if(appr.getAgreed())
						appl.setStatus(CommonConst.STATE_SUCCESS);
					else
						appl.setStatus(CommonConst.STATE_FAIL);
				}
				else if(auditor.getStaff_position().equalsIgnoreCase(CommonConst.POSITION_VM)){
					if(appr.getAgreed())
						appl.setStatus(CommonConst.STATE_WAITGM);
					else
						appl.setStatus(CommonConst.STATE_FAIL);
				}
				else if(auditor.getStaff_position().equalsIgnoreCase(CommonConst.POSITION_DM)&&
						auditor.getDepartment_id()==applicant.getDepartment_id()){
					if(appr.getAgreed())
						if(appl.getLeave_length()<=3)
							appl.setStatus(CommonConst.STATE_SUCCESS);
						else appl.setStatus(CommonConst.STATE_WAITGMV);
					else
						appl.setStatus(CommonConst.STATE_FAIL);
				}
				else checkOk = false;
			}
			else checkOk = false;
		}
		
		if(checkOk){
			appr.setApprove_date(CommonConst.getCurrentDate());
			appr.setAuditor_id(auditorId);
			
			checkOk = approvalDao.add(appr);
			System.out.println(checkOk);
			if(checkOk) checkOk = applicationDao.updateRecord(appl);
			return checkOk;
		}
		return false;
	}
	
	
	
}
