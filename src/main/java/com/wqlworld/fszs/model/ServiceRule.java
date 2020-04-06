package com.wqlworld.fszs.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceRule {
	private double buyFeeRate;
	private List<SellRule> sellRule = new ArrayList<SellRule>();
	
	
	public double getBuyFeeRate() {
		return buyFeeRate;
	}


	public void setBuyFeeRate(double buyFeeRate) {
		this.buyFeeRate = buyFeeRate;
	}


	public List<SellRule> getSellRule() {
		return sellRule;
	}


	public void setSellRule(List<SellRule> sellRule) {
		this.sellRule = sellRule;
	}


	public static class SellRule{
		private int startDate;
		private int endDate;
		private double rate;
		public int getStartDate() {
			return startDate;
		}
		public void setStartDate(int startDate) {
			this.startDate = startDate;
		}
		public int getEndDate() {
			return endDate;
		}
		public void setEndDate(int endDate) {
			this.endDate = endDate;
		}
		public double getRate() {
			return rate;
		}
		public void setRate(double f) {
			this.rate = f;
		}
		
	}
}
