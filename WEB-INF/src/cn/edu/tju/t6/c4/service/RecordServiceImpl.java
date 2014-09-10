package cn.edu.tju.t6.c4.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.Record;
import cn.edu.tju.t6.c4.dao.RecordDao;
import cn.edu.tju.t6.c4.dao.UserDao;

@Service
public class RecordServiceImpl implements RecordService{

	@Autowired
	RecordDao recordDao;
	@Autowired
	UserDao userDao;
	
	@Override
	public List<Record> get(long applyID) 
			throws SQLException {
		return recordDao.getRecordByApplyID(applyID);
	}

	@Override
	public List<Record> getAfter(long applyID, Calendar time) 
			throws SQLException {
		List<Record> list = get(applyID);
		for(int i = 0 ; i < list.size() ; i++){
			Record record = list.get(i);
			if(record.getApplyTime()==null) break;
			String times[] = record.getApplyTime().split(CommonConst.TIME_SPLIT);
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(times[0]), 
					 Integer.parseInt(times[1]), 
					 Integer.parseInt(times[2]));
			if(cal.compareTo(time) < 0) list.remove(record);
		}
		return list;
	}

	@Override
	public List<Record> getByState(String state) 
			throws SQLException {
		return recordDao.getRecordByState(state);
	}

	@Override
	public List<Record> getByState(long applyID, String state) 
			throws SQLException {
		return recordDao.getRecordByApplyIDAndState(applyID, state);
	}

	@Override
	public boolean delete(int recordID,long applyID) 
			throws SQLException {
		if(!recordDao.checkExist(recordID))
			throw new SQLException("SQLException: no record which id ="+recordID);
		Record record = recordDao.getRecordByID(recordID);
		if(record.getApplyID()!=applyID)
			throw new SQLException("SQLExcetion: this record is not belong to "+applyID);
		return recordDao.deleteRecordByID(recordID);
	}

	@Override
	public boolean update(Record record) 
			throws SQLException {
		if(record.getID() == 0)
			throw new SQLException("SQLException: recordID cann't be 0 or empty!");
		if(record.getLeaveDays() == 0)
			throw new SQLException("SQLException: leaveDays cann't be 0 or empty!");
		if(!recordDao.checkExist(record.getID()))
			throw new SQLException("SQLException: no record which id ="+record.getID());
		return recordDao.updateRecord(record);
	}

	@Override
	public boolean add(Record record) 
			throws SQLException {
		if(record.getLeaveDays() == 0)
			throw new SQLException("SQLException: leaveDays cann't be 0 or empty!");
		//get history leave record after this year
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1, 1);
		List<Record> list = getAfter(record.getApplyID(),cal);
		
		//get total left days
		int totalLeftDays = 0;
		for(int i = 0 ; i < list.size() ; i++)
			totalLeftDays+=list.get(i).getLeaveDays();
		
		//get max leave days
		int totalLeaveDays = userDao.get(record.getApplyID()).getTotalLeaveDays();
		int restLeaveDays = totalLeaveDays - totalLeftDays;
		
		//compare and get response
		if(record.getLeaveDays() > restLeaveDays)
			throw new SQLException("rest leave day("+restLeaveDays+" day) not enough : \n" +
					"total of left days and wait apply leave days is "+totalLeftDays + "\n" +
					"total leave days is "+totalLeaveDays);
		return recordDao.addRecord(record);
	}

}
