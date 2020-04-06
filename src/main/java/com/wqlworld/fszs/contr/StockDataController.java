package com.wqlworld.fszs.contr;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wqlworld.fszs.service.StockDataService;

@Controller
@RequestMapping("/stock")
public class StockDataController {
	@Autowired
	private StockDataService stockDataService;
	
	@RequestMapping("test")
	public void test(HttpServletRequest request,HttpServletResponse rep) throws IOException, ParseException{
		OutputStream ot = rep.getOutputStream();
		SimpleDateFormat spdf = new SimpleDateFormat("yyyy-MM-dd");
		rep.addHeader("content-type", "text/plain;charset=utf-8");
		stockDataService.caculateMethod1(spdf.parse("2009-11-25"), new Date(), ot);
	}

}
