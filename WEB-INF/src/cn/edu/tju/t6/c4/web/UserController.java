package cn.edu.tju.t6.c4.web;

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

import cn.edu.tju.t6.c4.base.User;
import cn.edu.tju.t6.c4.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody List<User> getAll()
			throws Exception{
		return userService.getAll();
	}

	@RequestMapping(method=RequestMethod.GET, value="/{userID}")
	public @ResponseBody User get(@PathVariable String userID) 
			throws Exception{
		return userService.get(Long.parseLong(userID));
	}

	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void add(@RequestBody String userBody)
			throws Exception{
		User user = (new Gson()).fromJson(userBody, User.class);
		userService.add(user);
	}

	@RequestMapping(method=RequestMethod.POST, value="/{userID}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody String userBody,
					@PathVariable String userID)
			throws Exception{
		User user = (new Gson()).fromJson(userBody, User.class);
		if(user.getUser_id()!=Long.parseLong(userID))
			throw new Exception("Update Exception: update userID must be right!");
		userService.update(user);
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/{userID}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String userID)
			throws Exception{
		userService.delete(Long.parseLong(userID));
	}
}
