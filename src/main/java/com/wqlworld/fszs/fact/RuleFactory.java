package com.wqlworld.fszs.fact;

import com.wqlworld.fszs.model.ServiceRule;

public class RuleFactory {
	private static ServiceRule tianHongRule = null;

	public static synchronized ServiceRule getTianHongRule() {
		if (tianHongRule == null) {
			tianHongRule = new ServiceRule();
			tianHongRule.setBuyFeeRate(0.001f);
			ServiceRule.SellRule sr = new ServiceRule.SellRule();
			sr.setStartDate(0);
			sr.setEndDate(6);
			sr.setRate(0.015f);
			tianHongRule.getSellRule().add(sr);
			sr = new ServiceRule.SellRule();
			sr.setStartDate(7);
			sr.setEndDate(-1);
			sr.setRate(0.0005f);
			tianHongRule.getSellRule().add(sr);
		}
		return tianHongRule;
	}
}
