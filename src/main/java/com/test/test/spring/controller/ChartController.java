package com.test.test.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.test.spring.service.ChartService;


@Controller
class ChartController {

	@Autowired
	ChartService ChartService;
	
	@RequestMapping(value = "/get_realtime_transaction", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getRealTimeTransaction_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		return ChartService.getRealTimeTransaction_Chart(params);
	}
	
	
	@RequestMapping(value = "/get_daily_usage_transaction", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get_Daily_Usage_Transaction_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		return ChartService.get_Daily_Usage_Transaction_Chart(params);
	}
	
	@RequestMapping(value = "/get_monthly_usage_transaction", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get_Monthly_Usage_Transaction_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		return ChartService.get_Monthly_Usage_Transaction_Chart(params);
	}
	
	
	
	@RequestMapping(value = "/get_poscomply_realtime_transaction", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> get_POSCOMPLY_RealTimeTransaction_Chart(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		return ChartService.get_POSCOMPLY_RealTimeTransaction_Chart(params);
	}
	
}
