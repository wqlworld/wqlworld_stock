package com.wqlworld.fszs.jpa.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wqlworld.fszs.model.StockData;

public interface StockDataRepo extends JpaRepository<StockData, String>{
	@Query(value="select * "+
				  "from STOCK_DATA da "+
				 "inner join (select ?1 startDate, ?2 endDate from dual) param "+
				    "on (1 = 1) "+
				 "where (param.startDate is null or "+
			       "da.trade_date >= to_date(param.startDate, 'YYYY-MM-DD')) "+
			       "and "+
			       "(param.endDate is null or "+
			       "da.trade_date <= to_date(param.endDate,'YYYY-MM-DD')) "+
			       "order by da.trade_date ASC",nativeQuery = true)
	public List<StockData> findByStockDate(String startDate,String endDate);
}
