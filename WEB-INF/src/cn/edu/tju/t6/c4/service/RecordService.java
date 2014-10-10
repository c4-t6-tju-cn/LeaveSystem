package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import cn.edu.tju.t6.c4.base.Application;
import cn.edu.tju.t6.c4.base.Approval;


public interface RecordService {
	
	public Application getById(long applicationID);
	
	public List<Application> get(long applyID);
	
	public List<Application> getAfter(long applyID, int year);
	
	public List<Application> getByState(String state);
	
	public List<Application> getByState(long applyID, String state);
	
	public boolean delete(int recordID, long applicantid);
	
	public boolean add(Application record, long applicant_id);
	
	public boolean update(Application record);
	
	public boolean approve(Approval appr, long auditor);
}
