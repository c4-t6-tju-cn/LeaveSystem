package cn.edu.tju.t6.c4.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;

import cn.edu.tju.t6.c4.base.Approval;
import cn.edu.tju.t6.c4.service.RecordService;


@SessionAttributes("user")
@RequestMapping(value="/approval")
@Controller
public class ApprovalController {
	@Autowired
	private RecordService recordService;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String add(@RequestBody String body,
			@ModelAttribute("user") long id ){
		Approval appr = (new Gson()).fromJson(body, Approval.class);
		
		return recordService.approve(appr,id);
		
		
	}
}
