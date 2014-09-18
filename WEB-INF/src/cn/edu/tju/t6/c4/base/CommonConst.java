package cn.edu.tju.t6.c4.base;

import java.util.Calendar;

public class CommonConst {

	   public static final String TIME_SPLIT = "-";

	   public static final String STATE_WAITMANAGER = "WAITDM";
	   
	   public static final String STATE_WAITGMV = "WAITGMV";
	   
	   public static final String STATE_WATIGM = "WAITGM";

	   public static final String STATE_FAIL = "DENY";

	   public static final String STATE_SUCCESS = "PASS";
	   
	   public static final String POSITION_EMPLOYEE = "EMPLOYEE";
	   
	   public static final String POSITION_GM = "GENERAL MANAGER";/*General Manager*/
	   
	   public static final String POSITION_DM = "MANAGER";/*Department Manager*/
	   
	   public static final String POSITION_VM = "VICE GENERAL MANAGER";/*Vice General Manager*/
	   
	   public static final String POSITION_AD = "ADMIN";
	   
	   public static final String CHECK_USER_ATTRIBUTE = "CHECK_USER_ATTRIBUTE";
	   
	   public static String getCurrentDate(){
		   int y,m,d;    
		   Calendar cal=Calendar.getInstance();    
		   y=cal.get(Calendar.YEAR);    
		   m=cal.get(Calendar.MONTH);    
		   d=cal.get(Calendar.DATE);
		   String date = y + TIME_SPLIT + m + TIME_SPLIT + d;
		   return date;
	   }
	   
	   public static String getCurrentTime(){
		   int h,m,s;    
		   Calendar cal=Calendar.getInstance();    
		   h=cal.get(Calendar.HOUR);    
		   m=cal.get(Calendar.MINUTE);    
		   s=cal.get(Calendar.SECOND);
		   String time = h + ":" + m + ":" + s;
		   return getCurrentDate() + "/" + time;
	   }
}
