package com.wqlworld.fszs.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class AccountDetail {
	private int id;
	private int acId;
	private StockData huShenZhishu;//当前沪深
	private double priZhishu;//成本指数
	private double principal;//累计本金=帐户原本金+addPrincipal-serviceFee(如果是支出，成本=principal-((实际取得款+服务费)/(balance/principal)))
	private double balance;//累计余额=帐户原余额+addPrincipal-serviceFee(如果是支出，余额=余额-实际取得款-服务费)          (实际取得款+服务费)=(-addBalance/(1-费率))
	private double addPrincipal;//新加/支出资金(如果是支出，支出成本=-((实际取得款+服务费)/(balance/principal)))
	private double addBalance;//新加余额(如果是支出，支出余额=-实际取得款) 
	private double serviceFee;//服务费(如果支出，服务费=((-addBalance/(1-费率))*费率)
	private double profit;//累计赢利(不被新加款，支出款影响)
	private double rate;//当前赢亏比率(加款影响，支出不影响)
	private double buyCount;//卖入时份额(首次建仓时随便定义)支出时为负
	private double curCount;//当前持有份额(首次建仓时随便定义)支出时为0
	private double count;//累计份额(首次建仓时随便定义)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAcId() {
		return acId;
	}
	public void setAcId(int acId) {
		this.acId = acId;
	}
	public StockData getHuShenZhishu() {
		return huShenZhishu;
	}
	public void setHuShenZhishu(StockData huShenZhishu) {
		this.huShenZhishu = huShenZhishu;
	}
	
	public double getPriZhishu() {
		return priZhishu;
	}
	public void setPriZhishu(double priZhishu) {
		this.priZhishu = priZhishu;
	}
	public double getPrincipal() {
		return principal;
	}
	public void setPrincipal(double principal) {
		this.principal = principal;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getAddPrincipal() {
		return addPrincipal;
	}
	public void setAddPrincipal(double addPrincipal) {
		this.addPrincipal = addPrincipal;
	}
	
	public double getAddBalance() {
		return addBalance;
	}
	public void setAddBalance(double addBalance) {
		this.addBalance = addBalance;
	}
	public double getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(double serviceFee) {
		this.serviceFee = serviceFee;
	}
	
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public double getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(double buyCount) {
		this.buyCount = buyCount;
	}
	
	public double getCurCount() {
		return curCount;
	}
	public void setCurCount(double curCount) {
		this.curCount = curCount;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	
	
}
