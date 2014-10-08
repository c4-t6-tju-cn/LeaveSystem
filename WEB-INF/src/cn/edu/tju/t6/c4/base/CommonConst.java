package cn.edu.tju.t6.c4.base;

import java.util.Calendar;
import java.util.Date;

public class CommonConst {

	   public static final String TIME_SPLIT = "-";

	   public static final String STATE_WAITMANAGER = "WAITDM";
	   
	   public static final String STATE_WAITGMV = "WAITGMV";
	   
	   public static final String STATE_WAITGM = "WAITGM";

	   public static final String STATE_FAIL = "DENIED";

	   public static final String STATE_SUCCESS = "PASSED";
	   
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
		   m=cal.get(Calendar.MONTH)+1;    
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
	   public static Date getDate(String datestr){
		   Date date = new Date();
		   try {
			   int y = Integer.parseInt(datestr.split(TIME_SPLIT)[0]);
			   int m = Integer.parseInt(datestr.split(TIME_SPLIT)[1]);
			   int d = Integer.parseInt(datestr.split(TIME_SPLIT)[2]);
			   date.setYear(y);
			   date.setMonth(m-1);
			   date.setDate(d);
		   }catch(Exception e){
			   System.out.println("Date is not right!");
			   e.printStackTrace();
		   }
		   return date;
	   }
	   public static boolean notBefore(String d1, String d2){
		   if(getDate(d1).after(getDate(d2))) return false;
		   else return true;
	   }
}
