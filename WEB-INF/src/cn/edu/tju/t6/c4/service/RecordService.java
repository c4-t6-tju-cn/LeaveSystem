package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import cn.edu.tju.t6.c4.base.Application;


public interface RecordService {
	
	public Application getById(long applicationID) throws SQLException;
	
	public List<Application> get(long applyID) throws SQLException;
	
	public List<Application> getAfter(long applyID, Calendar time) throws SQLException;
	
	public List<Application> getByState(String state) throws SQLException;
	
	public List<Application> getByState(long applyID, String state) throws SQLException;
	
	public boolean delete(int recordID,long applyID) throws SQLException;
	
	public boolean add(Application record) throws SQLException;
	
	public boolean update(Application record) throws SQLException;
}
