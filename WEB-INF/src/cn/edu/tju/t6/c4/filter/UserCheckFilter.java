package cn.edu.tju.t6.c4.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.edu.tju.t6.c4.base.CommonConst;
import cn.edu.tju.t6.c4.base.User;
import cn.edu.tju.t6.c4.dao.UserDao;

public class UserCheckFilter implements Filter{

	UserDao userDao = new UserDao();
	@Override
	public void destroy() {
	
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if(httpRequest.getRequestURI().contains("/login")
				&& httpRequest.getMethod().equalsIgnoreCase("get")){
			/**
			 * Log in action
			 */
			String user = request.getParameter("user");
			String pwd = request.getParameter("pwd");
			if(user == null || pwd == null){
				checkFail(response);
				return;
			}
			else {
				try {
					User userGet = userDao.get(Long.parseLong(user));
					
					if(userGet != null && userDao.getPwd(Long.parseLong(user)).equals(pwd)){
						
						httpRequest.getSession().setAttribute(CommonConst.CHECK_USER_ATTRIBUTE, Boolean.TRUE);
						httpRequest.getSession().setAttribute("user", Long.parseLong(user));
						httpRequest.getSession().setAttribute("position", userGet.getStaff_position());
						httpRequest.getSession().setAttribute("department_id",userGet.getDepartment_id());
						//System.out.println("userGet.getDepartment()"+userGet.getDepartment());
						//chain.doFilter(request, response);
						response.getWriter().print("{\"name\":\"" + userGet.getUser_name() 
								+ "\",\"position\":\"" + userGet.getStaff_position() 
								+ "\",\"id\":\"" + user + "\",\"department_id\":\"" +userGet.getDepartment_id()  + "\"}");
						return;
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					checkFail(response);
				}
			}
		}
		else if (request != null && 
				httpRequest.getSession().getAttribute(CommonConst.CHECK_USER_ATTRIBUTE)!=null) {
			/**
			 * Other action
			 */
			chain.doFilter(request, response);
		} 
		else{
			/**
			 * Not Login
			 */
			checkFail(response);
		}
	}
	
	private void checkFail(ServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		out.write("[{ \"response\":\"Login Error - No this user or password is wrong!\" }]");
		out.flush();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
