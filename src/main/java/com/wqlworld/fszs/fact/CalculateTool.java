package com.wqlworld.fszs.fact;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.wqlworld.fszs.model.AccountDetail;

public class CalculateTool {
	/**
	 * 计算某个明细里份额的持有天数
	 * @param acde
	 * @param date
	 * @return
	 */
	public static int calculateDayOfCount(AccountDetail acde,Date date){
		Calendar ndate = disposeDate(date);
		Calendar adate = disposeDate(acde.getHuShenZhishu().getDate());
		long day = (ndate.getTime().getTime()-adate.getTime().getTime())/(1000*60*60*24);
		return (int) day;
		
	}
	
	public static Calendar disposeDate(Date date){
		Calendar cdate = Calendar.getInstance();
		cdate.setTime(date);
		cdate.set(Calendar.HOUR_OF_DAY, 0);
		cdate.set(Calendar.MINUTE, 0);
		cdate.set(Calendar.SECOND, 0);
		cdate.set(Calendar.MILLISECOND, 0);
		return cdate;
	}
	
	public static int compareToOne(double data,double one){
		BigDecimal bData = new BigDecimal(data).setScale(2,RoundingMode.HALF_UP);
		BigDecimal bOne = new BigDecimal(one).setScale(2,RoundingMode.HALF_UP);
		return bData.compareTo(bOne);
	}
	
	public static double scale2(double data){
		return new BigDecimal(data).setScale(2,RoundingMode.HALF_UP).doubleValue();
	}

}
