package cn.edu.tju.t6.c4.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.tju.t6.c4.base.Department;
import cn.edu.tju.t6.c4.service.UserService;
@Controller
@RequestMapping(value="/department")
public class DepartmentController {
	@Autowired
	private UserService userService;
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody List<Department> getAll()
			throws Exception{
		return userService.getDepartments();
	}
}
