package com.wqlworld.fszs.model;

import java.util.Date;

public class Account {
	private int id;
	private double balance;//余额，单位：元
	private double principal;//金，单位：元
	private double rate;//利亏比率
	private double priZhishu;//成本指数
	private double count;//份数
	private double profit;//赢利
	private Date createDatetime;
	private Date updateDatetime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getPrincipal() {
		return principal;
	}
	public void setPrincipal(double principal) {
		this.principal = principal;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getPriZhishu() {
		return priZhishu;
	}
	public void setPriZhishu(double priZhishu) {
		this.priZhishu = priZhishu;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public Date getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}	

}
