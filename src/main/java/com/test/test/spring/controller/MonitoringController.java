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

import com.test.test.spring.service.MenualDBConnectionService;
import com.test.test.spring.service.MonitoringService;

@Controller
class MonitoringController {
	@Autowired
	MonitoringService monitoringService;
	
	@Autowired
	MenualDBConnectionService menualDBService;
	
//	@Value("${server.running.flag}")
//	private String server_running_flag = "";
//	
//	@Value("${local.search_engine}")
//	private String local_elasticsearch = "";
	
	@RequestMapping(value = "/commonCodeList", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> commonCodeList(@RequestBody Map<String, String> params)	{
		//params : cdTpTp
		//SERVER_CONF_CD 서버환경
		//CHAIN_CODE 체인코드
		//LOG_LEVEL 로그레벨
		return monitoringService.commonCodeList(params);
	}
	
	@RequestMapping(value = "/findFeederServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findFeederServerLog(@RequestBody Map<String, Object> params)	{
		return monitoringService.findFeederServerLog(params);
	}
	
	@RequestMapping(value = "/findAIServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findAIServerLog(@RequestBody Map<String, Object> params)	{
		return monitoringService.findAIServerLog(params);
	}
	
	@RequestMapping(value = "/findWasServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findWasServerLog(@RequestBody Map<String, Object> params)	{
//		params.put("server_running_flag", this.server_running_flag);
//		params.put("local_search_engine", this.local_elasticsearch);
		return monitoringService.findWasServerLog(params);
	}
	
	@RequestMapping(value = "/findQueueServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findQueueServerLog(@RequestBody Map<String, Object> params)	{
		return monitoringService.findQueueServerLog(params);
	}
	
	@RequestMapping(value = "/findFeedServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findLogListServerLog(@RequestBody Map<String, Object> params)	{
		return monitoringService.findFeedServerLog(params);
	}
	
	@RequestMapping(value = "/findEcmServerLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findEcmServerLog(@RequestBody Map<String, Object> params)	{
		return monitoringService.findEcmServerLog(params);
	}
	
	@RequestMapping(value = "/selectSqlList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectSqlList(@RequestBody Map<String, Object> params)	{
		return monitoringService.selectSqlList(params);
	}
	
	@RequestMapping(value = "/saveSql", method = RequestMethod.POST)
	@ResponseBody
	public void saveSql(@RequestBody Map<String, Object> params)	{
		monitoringService.saveSql(params);
	}
	
	@RequestMapping(value = "/updateSql", method = RequestMethod.POST)
	@ResponseBody
	public void updateSql(@RequestBody Map<String, Object> params)	{
		monitoringService.updateSql(params);
	}
	
	@RequestMapping(value = "/deleteSql", method = RequestMethod.POST)
	@ResponseBody
	public void deleteSql(@RequestBody Map<String, Object> params)	{
		monitoringService.deleteSql(params);
	}
	
	@RequestMapping(value = "/selectSql", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectSql(@RequestBody Map<String, Object> params)	{
		return monitoringService.selectSql(params);
	}
	
	@RequestMapping(value = "/excuteSql", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> excuteSql(@RequestBody Map<String, Object> params)	{
		return menualDBService.excuteSql(params);
	}
	
	@RequestMapping(value = "/callProvideIF", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> callProvideIF(@RequestBody Map<String, Object> params)	{
		return monitoringService.callProvideIF(params);
	}
	
	@RequestMapping(value = "/callTextIF", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> callTextIF(@RequestBody Map<String, Object> params)	{
		System.out.println("callTextIF");
		return monitoringService.callTextIF(params);
	}
	
	
}
