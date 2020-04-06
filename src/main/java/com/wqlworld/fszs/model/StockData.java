package com.wqlworld.fszs.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "FSZS.STOCK_DATA")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
//@GenericGenerator(name = "system-uuid", strategy = "uuid")
public class StockData {
	@Id
	private Long ID;
	@Column(name = "TRADE_DATE")
	private Date date;//日期
	@Column(name = "YEDAY_DATA")
	private Double yedayData;//昨收盘数据
	@Column(name = "OPEN_DATA")
	private Double openData;//开盘数据
	@Column(name = "CLOSE_DATA")
	private Double closeData;//收盘数据
	@Column(name = "RATE")
	private Double rate;
	public StockData(){
		
	}
	public StockData(Date date,double yedayData,double openData,double closeData){
		this.date = date;
		this.yedayData = yedayData;
		this.openData = openData;
		this.closeData = closeData;
		this.rate = (closeData - yedayData)/yedayData;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getOpenData() {
		return openData;
	}
	public void setOpenData(double openData) {
		this.openData = openData;
	}
	
	public double getYedayData() {
		return yedayData;
	}
	public void setYedayData(double yedayData) {
		this.yedayData = yedayData;
	}
	public void setCloseData(double closeData) {
		this.closeData = closeData;
	}
	public double getCloseData() {
		return closeData;
	}
	public void setCloseDate(double closeDate) {
		this.closeData = closeDate;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
}
