package cn.edu.tju.t6.c4.web;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;

import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.Record;
import cn.edu.tju.t6.c4.service.RecordService;

@Controller
@RequestMapping(value="/record")
public class RecordController{
	
	@Autowired
	RecordService recordService;
	/*map sequence:
	 *	record/applyID/{applyID}/status/{status}/time/{time}
	 */
	@RequestMapping(method=RequestMethod.GET, value="/applyID/{applyID}/status/all/time/all")
	public @ResponseBody List<Record> get(
			@PathVariable String applyID) throws Exception{
		return recordService.get(Long.parseLong(applyID));
	}

	@RequestMapping(method=RequestMethod.GET, value="/applyID/{applyID}/status/all/time/{time}")
	public @ResponseBody List<Record> getAfter(
			@PathVariable("applyID") String applyID, 
			@PathVariable("time") String time) throws Exception {
		String times[] = time.split(CommonConst.TIME_SPLIT);
		Calendar date = Calendar.getInstance();
		date.set(Integer.parseInt(times[0]), 
				 Integer.parseInt(times[1]), 
				 Integer.parseInt(times[2]));
		return recordService.getAfter(Long.parseLong(applyID), date);
	}

	@RequestMapping(method=RequestMethod.GET, value="/applyID/all/status/{status}/time/all")
	public @ResponseBody List<Record> getByState(
			@PathVariable String state) throws Exception{
		return recordService.getByState(state);
	}

	@RequestMapping(method=RequestMethod.GET, value="/applyID/{applyID}/status/{status}/time/all")
	public @ResponseBody List<Record> getByState(
			@PathVariable("applyID") String applyID, 
			@PathVariable("status") String state) throws Exception{
		return recordService.getByState(Long.parseLong(applyID),state);
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/{recordID}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String recordID,
					@PathVariable String applyID) throws Exception{
		recordService.delete(Integer.parseInt(recordID),Long.parseLong(applyID));
	}

	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void post(@RequestBody String body,
					@PathVariable String applyID) throws Exception{
		Record record = (new Gson()).fromJson(body, Record.class);
		if(record.getApplyID()!=Long.parseLong(applyID))
			throw new Exception("Update Exception: add record permission deny!");
		recordService.add(record);
	}

	@RequestMapping(method=RequestMethod.POST, value="/{applyID}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody String body,
					@PathVariable String applyID) throws Exception{
		Record record = (new Gson()).fromJson(body, Record.class);
		if(record.getApplyID()!=Long.parseLong(applyID))
			throw new Exception("Update Exception: update record permission deny!");
		recordService.update(record);
	}

}
