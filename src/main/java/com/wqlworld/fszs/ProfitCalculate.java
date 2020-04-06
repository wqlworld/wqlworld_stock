package com.wqlworld.fszs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.wqlworld.fszs.fact.AccountFactory;
import com.wqlworld.fszs.fact.AccountFactory.AccountWrapper;
import com.wqlworld.fszs.model.AccountDetail;
import com.wqlworld.fszs.model.StockData;

@EnableTransactionManagement//开启事务
@SpringBootApplication
@EntityScan(basePackages ={ "com.wqlworld.fszs.model"})
@ComponentScan("com.wqlworld.fszs")
public class ProfitCalculate {
	public static double RATE = 0.02f; 
	
	public static StockData zhishus[]=new StockData[]{new StockData(getDate(2020,3,4),4091.36f,4078.50f,4115.05f),
			new StockData(getDate(2020,3,5),4115.05f,4150.21f,4206.73f),
			new StockData(getDate(2020,3,6),4206.73f,4158.20f,4138.51f),
			new StockData(getDate(2020,3,9),4138.51f,4063.08f,3997.13f),
			new StockData(getDate(2020,3,10),3997.13f,3975.23f,4082.73f),
			new StockData(getDate(2020,3,11),4082.73f,4090.93f,4028.43f),
			new StockData(getDate(2020,3,12),4028.43f,3976.11f,3950.91f),
			new StockData(getDate(2020,3,13),3950.91f,3771.01f,3895.31f),
			new StockData(getDate(2020,3,16),3895.31f,3899.86f,3727.84f),
			new StockData(getDate(2020,3,17),3727.84f,3739.78f,3709.68f),
			new StockData(getDate(2020,3,18),3709.68f,3729.69f,3636.26f),
			new StockData(getDate(2020,3,19),3636.26f,3623.79f,3589.09f),
			new StockData(getDate(2020,3,20),3589.09f,3629.51f,3653.22f)};
	
	public static Date getDate(int year,int month,int date){
		Calendar cal =Calendar.getInstance();
		cal.set(year, month-1, date);
		return cal.getTime();
	}
	public static void main(String[] args){
		SpringApplication.run(ProfitCalculate.class, args);
	}
	public static void mainw(String[] args){
		
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		AccountWrapper aw = AccountFactory.createAccount(6000, zhishus[0]);
		for(StockData zhi:zhishus){
			if(aw.getStackDetails().size()!=0){
				AccountDetail last = aw.getStackDetails().peek();
				double rate = (zhi.getCloseData() - last.getHuShenZhishu().getCloseData())/last.getHuShenZhishu().getCloseData();
				if(rate<-RATE){
					AccountFactory.calculateAccount(aw, zhi);
					System.out.println("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
							+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP));
					AccountFactory.addPrincipal(2000, zhi, aw);
					System.out.println("今日："+fm.format(zhi.getDate())+","+fm.format(last.getHuShenZhishu().getDate())+"为基跌"+(rate*100)+"%,买入2000.00"+",累计成本："+aw.getAccount().getPrincipal()+",手续费："+aw.getStackDetails().peek().getServiceFee());
					System.out.println("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
							+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)+"\n\n");
				}
				if(rate>RATE){
					AccountFactory.calculateAccount(aw, zhi);
					System.out.println("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
							+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP));
					//double count = last.getCurCount();
					double count = ((zhi.getCloseData()/last.getHuShenZhishu().getCloseData())*last.getAddBalance())/(aw.getAccount().getBalance()/aw.getAccount().getCount());//((aw.getAccount().getBalance()/aw.getAccount().getPrincipal())*2000)/(aw.getAccount().getBalance()/aw.getAccount().getCount());
					AccountFactory.sellCount(count , zhi, aw);
					AccountDetail sellDetail = aw.getAllDetails().get(aw.getAllDetails().size()-1);
					
						aw.getStackDetails().pop();
				
					System.out.println("今日："+fm.format(zhi.getDate())+","+fm.format(last.getHuShenZhishu().getDate())+"为基涨"+(rate*100)+"%,卖出"+(-sellDetail.getBuyCount())+"份,赢利："+(-sellDetail.getAddBalance()-last.getAddPrincipal())
							+",累计赢利："+aw.getAccount().getProfit()+",余额："+sellDetail.getBalance()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)+""
									+ ",手续费："+sellDetail.getServiceFee()+"\n\n");
				}
				/*if(rate>RATE){
					double amount = last.getAddBalance()*(1+rate);
					AccountFactory.sellCount(amount , zhi, aw);
					aw.getStackDetails().pop();
					System.out.println("今日："+fm.format(zhi.getDate())+","+fm.format(last.getHuShenZhishu().getDate())+"为基涨"+rate+"%,卖出"+(amount)+",赢利："+(last.getAddBalance()*(1+rate)-2000)
							+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+aw.getAccount().getPrincipal());
				}*/
			}else{
				
					AccountFactory.addPrincipal(2000, zhi, aw);
					System.out.println("今日："+fm.format(zhi.getDate())+",因为空仓，"+fm.format(aw.getAccount().getCreateDatetime())+"为基跌"+(((zhi.getCloseData()-aw.getAccount().getPriZhishu())/aw.getAccount().getPrincipal())*100)+"%,买入2000.00"+",累计成本："+aw.getAccount().getPrincipal()+",手续费："+aw.getStackDetails().peek().getServiceFee()+"\n\n");
				
			}
			
		}
		AccountFactory.calculateAccount(aw, zhishus[zhishus.length-1]);
		System.out.println("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
				+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP));
		
		/*double a = 4000f;
		int i = 0;
		while(a>2000f){
			a = a*0.90f;
			i++;
		}
		System.out.println(i);
		
		HuShenZhishu startZs = zhishus[0];
		Stack<HuShenZhishu> ruzhiStack = new Stack();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		double chenBen = 0.0f;
		double profit = 0.0f;
		for(HuShenZhishu hszs : zhishus){
			if((hszs.getCloseData()-startZs.getCloseData())/startZs.getCloseData()<=-RATE){
				ruzhiStack.push(hszs);
				BigDecimal rate = new BigDecimal(((hszs.getCloseData()-startZs.getCloseData())/startZs.getCloseData())*100).setScale(4,RoundingMode.HALF_UP);
				chenBen = chenBen + 2000.00f;
				System.out.println("今日："+fm.format(hszs.getDate())+","+fm.format(startZs.getDate())+"为基跌"+rate+"%,买入2000.00"+",累计成本："+chenBen);
				startZs = hszs;
			}
			if(ruzhiStack.size()!=0 &&(hszs.getCloseData()-ruzhiStack.peek().getCloseData())/ruzhiStack.peek().getCloseData()>=RATE){
				HuShenZhishu machuZs = ruzhiStack.pop();
				BigDecimal rate = new BigDecimal(((hszs.getCloseData()-machuZs.getCloseData())/machuZs.getCloseData())*100).setScale(4,RoundingMode.HALF_UP);
				profit = profit + 2000.00f*rate.doubleValue()/100;
				chenBen = chenBen - 2000.00f;
				System.out.println("今日："+fm.format(hszs.getDate())+","+fm.format(machuZs.getDate())+"为基涨"+rate+"%,卖出"+(2000.00f+2000.00f*rate.doubleValue()/100)+",赢利："+2000.00f*rate.doubleValue()/100
						+",累计赢利："+profit+",累计成本："+chenBen);
			}
			
		}*/
		
		
	}
}

