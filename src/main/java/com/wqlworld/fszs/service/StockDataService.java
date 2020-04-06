package com.wqlworld.fszs.service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wqlworld.fszs.fact.AccountFactory;
import com.wqlworld.fszs.fact.AccountFactory.AccountWrapper;
import com.wqlworld.fszs.fact.CalculateTool;
import com.wqlworld.fszs.jpa.repo.StockDataRepo;
import com.wqlworld.fszs.model.AccountDetail;
import com.wqlworld.fszs.model.StockData;

@Transactional
@Service
public class StockDataService implements InitializingBean {
	
	public static double RATE = 0.04f; 
	
	
	@Autowired
	private StockDataRepo huShenZhishuRepo;
	
	
	/**
	 * 做T分析
	 * @param startDate
	 * @param endDate
	 * @param out
	 * @throws IOException
	 */
	public void caculateMethod1(Date startDate, Date endDate,OutputStream out) throws IOException{
		 double RATE = 0.04f;
		double maxPrincipal = 0.00f,maxProfit = 0.00f,minProfit = 0.00f;
		double step = 2000.00f;
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		List<StockData> zhishus = this.huShenZhishuRepo.findByStockDate(fm.format(startDate), fm.format(endDate));
		
		AccountWrapper aw = AccountFactory.createAccount(2000, zhishus.get(0));
		for(StockData zhi:zhishus){
			if(aw.getStackDetails().size()!=0){
				AccountDetail last = aw.getStackDetails().peek();
				double rate = (zhi.getCloseData() - last.getHuShenZhishu().getCloseData())/last.getHuShenZhishu().getCloseData();
				if(rate<(-RATE)){
					//AccountFactory.calculateAccount(aw, zhi);
					//System.out.write(("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
					//		+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)+"\n").getBytes("utf-8"));
					AccountFactory.addPrincipal(step, zhi, aw);
					out.write(("今日："+fm.format(zhi.getDate())+","+fm.format(last.getHuShenZhishu().getDate())+"为基跌"
					+CalculateTool.scale2(rate*100)+"%,买入"+step+",累计成本："+CalculateTool.scale2(aw.getAccount().getPrincipal())+",手续费："
							+CalculateTool.scale2(aw.getStackDetails().peek().getServiceFee())+"\n").getBytes("utf-8"));
					out.write(("总仓情况:利亏率"+CalculateTool.scale2(aw.getAccount().getRate()*100)+"%,合计"
							+CalculateTool.scale2(aw.getAccount().getCount())+"份,余额："+CalculateTool.scale2(aw.getAccount().getBalance())
							+",累计赢利："+CalculateTool.scale2(aw.getAccount().getProfit())+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)+"\n\n").getBytes("utf-8"));
				}
				if(rate>RATE){
					AccountFactory.calculateAccount(aw, zhi);
					//System.out.write(("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
					//		+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)+"\n").getBytes());
					//double count = last.getCurCount();
					double count = ((zhi.getCloseData()/last.getHuShenZhishu().getCloseData())*last.getAddBalance())/(aw.getAccount().getBalance()/aw.getAccount().getCount());//((aw.getAccount().getBalance()/aw.getAccount().getPrincipal())*2000)/(aw.getAccount().getBalance()/aw.getAccount().getCount());
					AccountFactory.sellCount(count , zhi, aw);
					AccountDetail sellDetail = aw.getAllDetails().get(aw.getAllDetails().size()-1);
					
						aw.getStackDetails().pop();
				
						out.write(("今日："+fm.format(zhi.getDate())+","+fm.format(last.getHuShenZhishu().getDate())
						+"为基涨"+CalculateTool.scale2(rate*100)+"%,卖出"+CalculateTool.scale2(-sellDetail.getBuyCount())+"份,赢利："+CalculateTool.scale2(-sellDetail.getAddBalance()-last.getAddPrincipal())
							+",累计赢利："+CalculateTool.scale2(aw.getAccount().getProfit())+",余额："+CalculateTool.scale2(sellDetail.getBalance())+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)+""
									+ ",手续费："+CalculateTool.scale2(sellDetail.getServiceFee())+"\n\n").getBytes());
				}
				
			}else{
				
					AccountFactory.addPrincipal(6000, zhi, aw);
					out.write(("今日："+fm.format(zhi.getDate())+",因为空仓，"+fm.format(aw.getAccount().getCreateDatetime())
					+"为基跌"+CalculateTool.scale2(((zhi.getCloseData()-aw.getAccount().getPriZhishu())/aw.getAccount().getPrincipal())*100)+"%,买入"+step+",累计成本："
					+CalculateTool.scale2(aw.getAccount().getPrincipal())+",手续费："+CalculateTool.scale2(aw.getStackDetails().peek().getServiceFee())+"\n\n").getBytes());
				
			}
			AccountFactory.calculateAccount(aw, zhi);
			if(CalculateTool.compareToOne(aw.getAccount().getPrincipal(), maxPrincipal)>0){
				maxPrincipal = aw.getAccount().getPrincipal();
			}
			if(CalculateTool.compareToOne(aw.getAccount().getProfit(), maxProfit)>0){
				maxProfit = aw.getAccount().getProfit();
			}
			if(CalculateTool.compareToOne(aw.getAccount().getProfit(), minProfit)<0){
				minProfit = aw.getAccount().getProfit();
			}
			out.flush();
		}
		System.out.println("finished\n");
		AccountFactory.calculateAccount(aw, zhishus.get(zhishus.size()-1));
		out.write(("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
				+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)
						+ "最大成本："+maxPrincipal+ "最大赢利："+maxProfit+ "最大亏损："+minProfit
						+"赢利率："+CalculateTool.scale2(aw.getAccount().getProfit()/maxPrincipal)+"\n").getBytes());
		out.flush();
		out.close();
	}
	
	public void calculateMethod2(Date startDate, Date endDate,OutputStream out) throws IOException{
		double RATE = 0.01f;
		double maxPrincipal = 0.00f,maxProfit = 0.00f,minProfit = 0.00f;
		double step = 2000.00f;
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		List<StockData> zhishus = this.huShenZhishuRepo.findByStockDate(fm.format(startDate), fm.format(endDate));
		
		AccountWrapper aw = AccountFactory.createAccount(2000, zhishus.get(0));
		for(StockData zhi:zhishus){
			if(aw.getStackDetails().size()!=0){
				AccountDetail last = aw.getStackDetails().peek();
				double rate = (zhi.getCloseData() - last.getHuShenZhishu().getCloseData())/last.getHuShenZhishu().getCloseData();
				if(rate<(-RATE)){
					
					AccountFactory.addPrincipal(step, zhi, aw);
					out.write(("今日："+fm.format(zhi.getDate())+","+fm.format(last.getHuShenZhishu().getDate())+"为基跌"
					+(rate*100)+"%,买入"+step+",累计成本："+aw.getAccount().getPrincipal()+",手续费："+aw.getStackDetails().peek().getServiceFee()+"\n").getBytes("utf-8"));
					out.write(("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
							+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)+"\n\n").getBytes("utf-8"));
				}
				if(rate>RATE*4){
					AccountFactory.calculateAccount(aw, zhi);
					
					double count = ((zhi.getCloseData()/last.getHuShenZhishu().getCloseData())*last.getAddBalance())/(aw.getAccount().getBalance()/aw.getAccount().getCount());//((aw.getAccount().getBalance()/aw.getAccount().getPrincipal())*2000)/(aw.getAccount().getBalance()/aw.getAccount().getCount());
					AccountFactory.sellCount(count , zhi, aw);
					AccountDetail sellDetail = aw.getAllDetails().get(aw.getAllDetails().size()-1);
					
						aw.getStackDetails().pop();
				
						out.write(("今日："+fm.format(zhi.getDate())+","+fm.format(last.getHuShenZhishu().getDate())+"为基涨"+(rate*100)+"%,卖出"+(-sellDetail.getBuyCount())+"份,赢利："+(-sellDetail.getAddBalance()-last.getAddPrincipal())
							+",累计赢利："+aw.getAccount().getProfit()+",余额："+sellDetail.getBalance()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)+""
									+ ",手续费："+sellDetail.getServiceFee()+"\n\n").getBytes());
				}
				
			}else{
				
					AccountFactory.addPrincipal(2000, zhi, aw);
					out.write(("今日："+fm.format(zhi.getDate())+",因为空仓，"+fm.format(aw.getAccount().getCreateDatetime())+"为基跌"+(((zhi.getCloseData()-aw.getAccount().getPriZhishu())/aw.getAccount().getPrincipal())*100)+"%,买入"+step+",累计成本："
					+aw.getAccount().getPrincipal()+",手续费："+aw.getStackDetails().peek().getServiceFee()+"\n\n").getBytes());
				
			}
			AccountFactory.calculateAccount(aw, zhi);
			if(CalculateTool.compareToOne(aw.getAccount().getPrincipal(), maxPrincipal)>0){
				maxPrincipal = aw.getAccount().getPrincipal();
			}
			if(CalculateTool.compareToOne(aw.getAccount().getProfit(), maxProfit)>0){
				maxProfit = aw.getAccount().getProfit();
			}
			if(CalculateTool.compareToOne(aw.getAccount().getProfit(), minProfit)<0){
				minProfit = aw.getAccount().getProfit();
			}
			out.flush();
		}
		System.out.println("finished\n");
		AccountFactory.calculateAccount(aw, zhishus.get(zhishus.size()-1));
		out.write(("总仓情况:利亏率"+(aw.getAccount().getRate()*100)+"%,合计"+aw.getAccount().getCount()+"份,余额："+aw.getAccount().getBalance()
				+",累计赢利："+aw.getAccount().getProfit()+",累计成本："+new BigDecimal(aw.getAccount().getPrincipal()).setScale(2,RoundingMode.HALF_UP)
						+ "最大成本："+maxPrincipal+ "最大赢利："+maxProfit+ "最大亏损："+minProfit+"\n").getBytes());
		out.flush();
		out.close();
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		/*List<StockData> datas = this.huShenZhishuRepo.findAll();
		for(StockData data: datas){
			System.out.println(data.getCloseData());
		}*/
	}
	

}
