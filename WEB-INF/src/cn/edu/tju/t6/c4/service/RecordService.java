package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import cn.edu.tju.t6.c4.base.Record;


public interface RecordService {

	public List<Record> get(long applyID) throws SQLException;
	
	public List<Record> getAfter(long applyID, Calendar time) throws SQLException;
	
	public List<Record> getByState(String state) throws SQLException;
	
	public List<Record> getByState(long applyID, String state) throws SQLException;
	
	public boolean delete(int recordID,long applyID) throws SQLException;
	
	public boolean add(Record record) throws SQLException;
	
	public boolean update(Record record) throws SQLException;
}
