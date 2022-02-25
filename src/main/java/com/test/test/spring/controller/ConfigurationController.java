package com.test.test.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.test.spring.service.ConfigurationService;

@Controller
class ConfigurationController {
	@Autowired
	ConfigurationService configurtaionService;

	@RequestMapping(value = "/saveConfiguration", method = RequestMethod.POST)
	@ResponseBody
	public void saveConfiguration(@RequestBody Map<String, Object> params)	{
		configurtaionService.saveConfiguration(params);
	}
	
	@RequestMapping(value = "/selectESAuthDB", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectESAuthDB(@RequestBody Map<String, Object> params)	{
		System.out.println("~~~controller selectESAuthDB start~~");
		return configurtaionService.selectESAuthDB(params);
	}
	
	@RequestMapping(value = "/selectCommonCode", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectCommonCode(@RequestBody Map<String, String> params)	{
		return configurtaionService.selectCommonCode(params);
	}
	
	@RequestMapping(value = "/selectOrgMember", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> selectOrgMember(@RequestBody Map<String, String> params)	{
		return configurtaionService.selectOrgMember(params);
	}
	
	@RequestMapping(value = "/insertMultiESAuthDB", method = RequestMethod.POST)
	@ResponseBody
	public void insertMultiESAuthDB(@RequestBody Map<String, Object> params)	{
		configurtaionService.insertMultiESAuthDB(params);
	}
	
	@RequestMapping(value = "/disabledESAuthDB", method = RequestMethod.POST)
	@ResponseBody
	public void disabledESAuthDB(@RequestBody Map<String, Object> params)	{
		configurtaionService.disabledESAuthDB(params);
	}
	
	@RequestMapping(value = "/mergeESAuthDB", method = RequestMethod.POST)
	@ResponseBody
	public void mergeESAuthDB(@RequestBody Map<String, Object> params)	{
		configurtaionService.mergeESAuthDB(params);
	}
	
}
