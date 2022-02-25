package com.test.test.spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.test.spring.repository.ConfigurationMapper;

@Service
public class ConfigurationService {
	@Autowired
	ConfigurationMapper configurationMapper;
	
	private static Logger logger = Logger.getLogger(MonitoringService.class);
		
	public void saveConfiguration(Map<String, Object> params){
		try {
			if(params.containsKey("ELASTIC_IP")) {
				configurationMapper.saveConfiguration(params);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		
	}

	public List<Map<String, String>> selectCommonCode(Map<String, String> params){
		return configurationMapper.selectCommonCode(params);
	}
	
	public List<Map<String, String>> selectOrgMember(Map<String, String> params){
		return configurationMapper.selectOrgMember(params);
	}
	
	public Map<String, Object> selectESAuthDB(Map<String, Object> params){
		logger.info("~~~start selectESAuthDB~~~~!!!!");
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length") + (Integer) params.get("start"));
		params.put("start", (Integer) params.get("start"));
		params.put("SYSTEM_ID", (String) params.get("P_SYSTEM_ID"));
		params.put("AUTH_ID", (String) params.get("P_AUTH_ID"));
		params.put("FULL_NAME", (String) params.get("P_FULL_NAME"));
		params.put("COMPANY_CODE", (String) params.get("P_COMPANY_CODE"));
		params.put("USEYN", (String) params.get("P_USEYN"));		
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		List<Map<String, String>> list = configurationMapper.selectESAuthDB(params);
		results.put("draw", params.get("draw"));
		if(list.size()>0) {
			results.put("recordsTotal", list.get(0).get("TOTCNT"));
			results.put("recordsFiltered",list.get(0).get("TOTCNT"));
		} else {
			results.put("recordsTotal", 0);
			results.put("recordsFiltered",0);
		}
		results.put("data", list);
		
		return results;
	}
	
	@Transactional
	public void insertMultiESAuthDB(Map<String, Object> params){
		configurationMapper.insertMultiESAuthDB(params);
	}
	
	@Transactional
	public void disabledESAuthDB(Map<String, Object> params){
		configurationMapper.disabledESAuthDB(params);
	}
	
	@Transactional
	public void mergeESAuthDB(Map<String, Object> params){
		
		System.out.println("mergeESAuthDB:params="+params.toString());
		configurationMapper.mergeESAuthDB(params);
	}
}













