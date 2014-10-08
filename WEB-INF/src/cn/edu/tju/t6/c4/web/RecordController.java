package cn.edu.tju.t6.c4.web;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;

import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.Application;
import cn.edu.tju.t6.c4.service.RecordService;

@Controller
@SessionAttributes("user")
@RequestMapping(value="/record")
public class RecordController{
	
	@Autowired
	RecordService recordService;
	/*map sequence:
	 *	record/applyID/{applyID}/status/{status}/time/{time}
	 */
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public @ResponseBody Application getById(@PathVariable String id) throws NumberFormatException, SQLException{
		return recordService.getById(Long.parseLong(id));
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/applyID/{applyID}/status/all/time/all")
	public @ResponseBody List<Application> get(
			@PathVariable String applyID) throws Exception{
		return recordService.get(Long.parseLong(applyID));
	}

	/*@RequestMapping(method=RequestMethod.GET, value="/applyID/{applyID}/status/all/time/{time}")
	public @ResponseBody List<Application> getAfter(
			@PathVariable("applyID") String applyID, 
			@PathVariable("time") String time) throws Exception {
		String times[] = time.split(CommonConst.TIME_SPLIT);
		Calendar date = Calendar.getInstance();
		date.set(Integer.parseInt(times[0]), 
				 Integer.parseInt(times[1]), 
				 Integer.parseInt(times[2]));
		return recordService.getAfter(Long.parseLong(applyID), date);
	}*/

	@RequestMapping(method=RequestMethod.GET, value="/applyID/all/status/{status}/time/all")
	public @ResponseBody List<Application> getByState(
			@PathVariable("status") String status) throws Exception{
		return recordService.getByState(status);
	}

	@RequestMapping(method=RequestMethod.GET, value="/applyID/{applyID}/status/{status}/time/all")
	public @ResponseBody List<Application> getByState(
			@PathVariable("applyID") String applyID, 
			@PathVariable("status") String status) throws Exception{
		return recordService.getByState(Long.parseLong(applyID),status);
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/{recordID}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String recordID) throws Exception{
		recordService.delete(Integer.parseInt(recordID));
	}

	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void post(@RequestBody String body,
					@ModelAttribute("user") long id ) throws Exception{
		Application record = (new Gson()).fromJson(body, Application.class);
		record.setApplicant_id(id);
		record.setStatus(CommonConst.STATE_WAITMANAGER);
		
		recordService.add(record, id);
	}

	@RequestMapping(method=RequestMethod.POST, value="/{ID}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody String body,
					@PathVariable String ID) throws Exception{
		Application record = (new Gson()).fromJson(body, Application.class);
		recordService.update(record);
	}

}
