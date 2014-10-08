package cn.edu.tju.t6.c4.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;

import cn.edu.tju.t6.c4.base.Approval;
import cn.edu.tju.t6.c4.base.User;
import cn.edu.tju.t6.c4.service.RecordService;

@Controller
@RequestMapping(value="/approval")
@SessionAttributes("user")
public class ApprovalController {
	@Autowired
	private RecordService recordService;
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public void add(@RequestBody String body,
			@ModelAttribute("user") long id ){
		Approval appr = (new Gson()).fromJson(body, Approval.class);
		
		recordService.approve(appr,id);
	}
}
