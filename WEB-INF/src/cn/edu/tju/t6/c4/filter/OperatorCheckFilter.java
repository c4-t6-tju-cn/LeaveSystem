package cn.edu.tju.t6.c4.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.edu.tju.t6.c4.base.CommonConst;

public class OperatorCheckFilter  implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String method = httpRequest.getMethod();
		String url = httpRequest.getRequestURI();
		String deparament = (String) httpRequest.getAttribute("deparament");
		long userID;
		if(httpRequest.getAttribute("user") == null)	userID = 0;
		else	userID = (long)httpRequest.getAttribute("user");
		if(CommonConst.DEPARATMENT_EMPLOYEE.equals(deparament)){
			// record----  0 < applyID length < 25 
			if(url.contains("/record")){
				String applyID = getFirstMatchSubstring(url.split("/record")[1],"\\d{1,25}");
				if(applyID != null && userID == Long.parseLong(applyID)){
					chain.doFilter(request, response);
					return;
				}
				checkFail(response, "Permission deny! Record permisson.");
				return;
			}
			
			// user----  0 < userID length < 25 
			if(url.contains("/user") && !method.equalsIgnoreCase("DELETE")){
				String user = getFirstMatchSubstring(url.split("/user")[1],"\\d{1,25}");
				if(user != null && userID == Long.parseLong(user)){
					chain.doFilter(request, response);
					return;
				}
			}
			checkFail(response, "Permission deny! User permission.");
		}
		else if(CommonConst.DEPARATMENT_ADMIN.equals(deparament) || CommonConst.DEPARATMENT_MANAGER.equals(deparament))
			chain.doFilter(request, response);
		else
			checkFail(response, "Deparament cann't recognized.");
	}
	
	private List<String> getMatchSubstring(String str, String pattern){
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(str);
		List<String> list = new ArrayList<String>();
		while(matcher.find()){
			String srcStr = matcher.group();
			list.add(srcStr);
		}
		return list;
	}
	
	private String getFirstMatchSubstring(String str, String pattern){
		List<String> list = getMatchSubstring(str, pattern);
		if(list.size() > 0)
			return list.get(0);
		return null;
	}

	private void checkFail(ServletResponse response, String str) throws IOException{
		PrintWriter out = response.getWriter();
		out.write("<h1> "+str+" </h1>");
		out.flush();
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	public static void main(String args[]){
		OperatorCheckFilter ocf = new OperatorCheckFilter();
		String str = ocf.getFirstMatchSubstring("", "\\d{5,15}");
		System.out.println(str);
	}
}
