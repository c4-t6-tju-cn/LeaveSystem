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
		response.setCharacterEncoding("UTF-8");  
		String method = httpRequest.getMethod();
		String url = httpRequest.getRequestURI();
		String position = (String) httpRequest.getSession().getAttribute("position");
		long userID;
		if(httpRequest.getSession().getAttribute("user") == null)	{
			userID = 0;
			/*if(url.contains("/login") && method.equalsIgnoreCase("POST")){
				response.getWriter().print("{}");
			}*/
		}
		else{
			userID = (long)httpRequest.getSession().getAttribute("user");
		}
		if(CommonConst.POSITION_EMPLOYEE.equalsIgnoreCase(position)){
			// record----  0 < applyID length < 25 
			if(url.contains("/record/")){
				String applyID = getFirstMatchSubstring(url.split("/record/")[1],"\\d{1,25}");
				if(applyID != null && userID == Long.parseLong(applyID)){
					chain.doFilter(request, response);
					return;
				}
				checkFail(response, "Permission deny! Record permisson.");
				return;
			}
			
			// user----  0 < userID length < 25 
			if(url.contains("/user/") && !method.equalsIgnoreCase("DELETE")){
				String user = getFirstMatchSubstring(url.split("/user/")[1],"\\d{1,25}");
				System.out.println("user "+ user+ "  "+ userID);
				if(user != null && userID == Long.parseLong(user)){
					chain.doFilter(request, response);
					return;
				}
			}
			
			checkFail(response, "Permission deny! User permission.");
		}
		else if(CommonConst.POSITION_AD.equalsIgnoreCase(position) 
				|| CommonConst.POSITION_DM.equalsIgnoreCase(position)
				|| position.equalsIgnoreCase(CommonConst.POSITION_GM)
				|| position.equalsIgnoreCase(CommonConst.POSITION_VM))
			chain.doFilter(request, response);
		else
			checkFail(response, "Department cann't recognized.");
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
		out.write("[{\"response\":\""+str+"\"}]");
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
