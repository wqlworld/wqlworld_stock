package com.wqlworld.fszs.fact;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.wqlworld.fszs.model.Account;
import com.wqlworld.fszs.model.AccountDetail;
import com.wqlworld.fszs.model.StockData;
import com.wqlworld.fszs.model.ServiceRule.SellRule;

public class AccountFactory {
	/**
	 * 新建帐户
	 * @param amount
	 * @param huShenZhishu
	 * @return
	 */
	public static AccountWrapper createAccount(double amount,StockData huShenZhishu){
		double serviceFee = amount*RuleFactory.getTianHongRule().getBuyFeeRate();
		Account account = new Account();
		AccountDetail acde = new AccountDetail();
		acde.setHuShenZhishu(huShenZhishu);
		acde.setPrincipal(amount);
		acde.setAddPrincipal(amount);
		acde.setServiceFee(serviceFee);
		acde.setBalance(amount-serviceFee);
		acde.setAddBalance(amount-serviceFee);
		acde.setRate((acde.getBalance()-acde.getPrincipal())/acde.getPrincipal());
		acde.setProfit(acde.getBalance()-acde.getPrincipal());
		double buyCount = acde.getBalance()/1;
	    //buyCount = acde.getBalance()/(account.getBalance()/account.getCount()*(acde));
		acde.setBuyCount(buyCount);
		acde.setCount(acde.getBuyCount());
		acde.setCurCount(buyCount);
		
		account.setPrincipal(amount);
		account.setBalance(acde.getBalance());
		account.setRate((account.getBalance()-account.getPrincipal())/account.getPrincipal());
		account.setPriZhishu(huShenZhishu.getCloseData()/(1+account.getRate()));
		account.setCount(acde.getCount());
		account.setProfit(account.getBalance()-account.getPrincipal());
		account.setCreateDatetime(huShenZhishu.getDate());
		AccountWrapper aw = new AccountWrapper();
		aw.account = account;
		
		aw.getAllDetails().add(acde);
		aw.getQueueDetails().offer(acde);
		aw.getStackDetails().push(acde);
		return aw;
	}
	/**
	 * 给帐户添加资金
	 * @return
	 */
	public static AccountDetail addPrincipal(double amount,StockData huShenZhishu,AccountWrapper accountWrpr){
		if(amount<0){
			throw new RuntimeException("卖入时资金不能小于0.00元！");
		}
		Account account = accountWrpr.account;
		calculateAccount(accountWrpr, huShenZhishu);
		double serviceFee = amount*RuleFactory.getTianHongRule().getBuyFeeRate();
		AccountDetail acde = new AccountDetail();
		acde.setHuShenZhishu(huShenZhishu);
		acde.setPrincipal(account.getPrincipal()+amount);
		acde.setAddPrincipal(amount);
		acde.setServiceFee(serviceFee);
		acde.setBalance(account.getBalance()+amount-serviceFee);
		acde.setAddBalance(amount-serviceFee);
		acde.setRate((acde.getBalance()-acde.getPrincipal())/acde.getPrincipal());
		acde.setProfit(account.getProfit()-serviceFee);
		
		double buyCount = 0.00f,oriPar=00.0f;
		if(account.getCount() != 0){
			oriPar = account.getBalance()/account.getCount();//帐户多少钱每份
		}
		 
		if(oriPar == 0){
			buyCount = acde.getBalance()/1;
		}else{
			buyCount = acde.getAddBalance()/(oriPar);
		}
	    //buyCount = acde.getBalance()/(account.getBalance()/account.getCount()*(acde));
		acde.setBuyCount(buyCount);
		acde.setCount(acde.getBuyCount()+account.getCount());
		acde.setCurCount(buyCount);
		
		account.setPrincipal(acde.getPrincipal());
		account.setBalance(acde.getBalance());
		account.setRate(acde.getRate());
		account.setPriZhishu(huShenZhishu.getCloseData()/(1+account.getRate()));
		account.setCount(acde.getCount());
		account.setProfit(acde.getProfit());
		
		accountWrpr.getAllDetails().add(acde);
		accountWrpr.getQueueDetails().offer(acde);
		accountWrpr.getStackDetails().push(acde);
		
		return acde;
	}
	
	public static void calculateAccount(AccountWrapper accountWrpr,StockData huShen){
		Account account = accountWrpr.account;
		//account.setPrincipal(acde.getPrincipal());
		//double r = (huShen.getCloseData()-(account.getPriZhishu()*(1+account.getRate())))/(account.getPriZhishu()*(1+account.getRate()));
		double nowBalance = Double.isNaN(nowBalance =huShen.getCloseData()*account.getPrincipal()/account.getPriZhishu())?0.00f:nowBalance;/*account.getBalance()*(1-(-r));*/  
		double nowProfit = nowBalance - account.getBalance();
		account.setBalance(nowBalance);
		double rate = 0.00f;
		account.setRate(Double.isNaN((rate =((account.getBalance()-account.getPrincipal())/account.getPrincipal())))?0.00f:rate);
		//account.setPriZhishu(huShen.getCloseData()/(1-account.getRate()));
		//account.setCount(acde.getCount());
		account.setProfit(account.getProfit() + nowProfit);
	}
	
	/**
	 *赎回份额 
	 * @param count
	 * @param huShenZhishu
	 * @param account
	 */
	public static void sellCount(double count,StockData huShenZhishu,AccountWrapper accountWrpr){
		Account account = accountWrpr.account;
		if(new BigDecimal(account.getCount()).setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal(count).setScale(2, RoundingMode.HALF_UP))<0){
			throw new RuntimeException("最多只能赎回"+account.getCount()+"份，赎过份额过大！");
		}
		calculateAccount(accountWrpr, huShenZhishu);
		double readyCount =0.00f;
		List<AccountDetail> useDetails = new ArrayList<AccountDetail>();
		//计算份数总额
		//当前每份多少钱
		double per = account.getBalance()/account.getCount();
		double serviceFee = 0.00f;
		while(CalculateTool.compareToOne(readyCount, count)<0){
			AccountDetail acde = accountWrpr.getQueueDetails().peek();
			double needCount = 0.00f;
			if(acde != null){
				if(CalculateTool.compareToOne(acde.getCurCount(), count-readyCount)>0){
					needCount = count-readyCount;
					acde.setCurCount(acde.getCurCount()-needCount);
					readyCount = readyCount+needCount;
					useDetails.add(acde);
				}else if(CalculateTool.compareToOne(acde.getCurCount(), count-readyCount)==0){
					needCount = count-readyCount;
					readyCount = count;
					acde.setCurCount(0);
					accountWrpr.getQueueDetails().poll();
					useDetails.add(acde);
				}else{
					needCount = acde.getCurCount();
					readyCount = readyCount+acde.getCurCount();
					acde.setCurCount(0);
					accountWrpr.getQueueDetails().poll();
					useDetails.add(acde);
				}
				if(CalculateTool.compareToOne(needCount, 0.00f) != 0){
					//计算手续费
					int day = CalculateTool.calculateDayOfCount(acde, huShenZhishu.getDate());
					SellRule sellRule = null;
					for(SellRule sr: RuleFactory.getTianHongRule().getSellRule()){
						if(sr.getStartDate()<=day && (day<=sr.getEndDate()||sr.getEndDate() == -1)){
							sellRule = sr;
							break;
						}
					}
					if(sellRule != null){
						serviceFee = serviceFee + needCount*per*sellRule.getRate();
					}else{
						//无规则默认不收手续费
					}
				}
			}
		}
		
		double amount = per * readyCount;//赎回金额（含手续费）
		double addBalance = amount - serviceFee;
		
		double addPrincipal = amount/(account.getBalance()/account.getPrincipal());
		AccountDetail acde = new AccountDetail();
		acde.setHuShenZhishu(huShenZhishu);
		acde.setPrincipal(account.getPrincipal()-addPrincipal);
		acde.setAddPrincipal(-addPrincipal);
		acde.setServiceFee(serviceFee);
		acde.setBalance(account.getBalance()-amount);
		acde.setAddBalance(-addBalance);
		double r =0.00f;
		acde.setRate(Double.isNaN((r=((acde.getBalance()-acde.getPrincipal()))/acde.getPrincipal()))?0.00f:r);
		acde.setProfit(account.getProfit()-serviceFee);
		
		acde.setBuyCount(-count);
		acde.setCount(account.getCount()-count);
		
		
		account.setPrincipal(acde.getPrincipal());
		account.setBalance(acde.getBalance());
		account.setRate(acde.getRate());
		account.setPriZhishu(Double.isNaN(r=huShenZhishu.getCloseData()/(1+account.getRate()))?0.00f:r);
		account.setCount(acde.getCount());
		account.setProfit(acde.getProfit());
		
		accountWrpr.getAllDetails().add(acde);
		//accountWrpr.getQueueDetails().offer(acde);
		//accountWrpr.getStackDetails().push(acde);
		
	}

	public static class AccountWrapper{
		private  Account account;
		private  List<AccountDetail> allDetails = new ArrayList<AccountDetail>(); 
		private  Queue<AccountDetail> queueDetails = new LinkedList<AccountDetail>();
		private  Stack<AccountDetail> stackDetails = new Stack<AccountDetail>();
		public Account getAccount() {
			return account;
		}
		public void setAccount(Account account) {
			this.account = account;
		}
		public List<AccountDetail> getAllDetails() {
			return allDetails;
		}
		public void setAllDetails(List<AccountDetail> allDetails) {
			this.allDetails = allDetails;
		}
		public Queue<AccountDetail> getQueueDetails() {
			return queueDetails;
		}
		public void setQueueDetails(Queue<AccountDetail> queueDetails) {
			this.queueDetails = queueDetails;
		}
		public Stack<AccountDetail> getStackDetails() {
			return stackDetails;
		}
		public void setStackDetails(Stack<AccountDetail> stackDetails) {
			this.stackDetails = stackDetails;
		}
		
	}
}


