package com.test.test.spring.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.test.test.spring.service.ServermanageService;


@Controller
class ServermanageController {
	@Autowired
	ServermanageService servermanageService;
	
	@Value("${feed_db_xml.server.path}")
	private String feedServerPath;
	
	@Value("${feed_db_xml.server.backup.path}")
	private String feedBackupPath;
	
	@RequestMapping(value = "/getCodeList", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> getCodeList(@RequestBody Map<String, Object> params)	{
		return servermanageService.getCodeList(params);
	}	
	
	@RequestMapping(value = "/findServerList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findServerList(@RequestBody Map<String, Object> params)	{
		return servermanageService.findServerList(params);
	}
	
	@RequestMapping(value = "/insertServerInfo", method = RequestMethod.POST)
	@ResponseBody
	public String insertServerInfo(@RequestBody Map<String, Object> params)	{
		return servermanageService.insertServerInfo(params);
	}
	
	@RequestMapping(value = "/deleteServerInfo", method = RequestMethod.POST)
	@ResponseBody
	public String deleteServerInfo(@RequestBody Map<String, Object> params)	{
		return servermanageService.deleteServerInfo(params);
	}
	
	@RequestMapping(value = "/getConfigData", method = RequestMethod.POST)
	@ResponseBody
	public String getConfigData(@RequestBody Map<String, Object> params)	{
		return servermanageService.getConfigData(params);
	}
	
	@RequestMapping(value = "/getConfigDataAsXml", method = RequestMethod.POST)
	@ResponseBody
	public String getConfigDataAsXml(@RequestBody Map<String, Object> params)	{

		String file_path = params.get("file_path").toString();
		return servermanageService.getConfigData(file_path);
	}
	
	@RequestMapping(value = "/getConfigDataAsJson", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getConfigDataAsJson(@RequestBody Map<String, Object> params)	{
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String confData = servermanageService.getConfigData(feedServerPath);
			
			JSONObject jObject = XML.toJSONObject(confData);
		    ObjectMapper mapper = new ObjectMapper();
		    mapper.enable(SerializationFeature.INDENT_OUTPUT);
		    Object json = mapper.readValue(jObject.toString(), Object.class);
			
			result.put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "/findCronJob", method = RequestMethod.POST)
	@ResponseBody

	public Map<String, Object> findCronJob(@RequestBody Map<String, Object> params)	{
		return servermanageService.findCronJob(params);
	}
	 
	@RequestMapping(value = "/backupCronJob", method = RequestMethod.POST)
	@ResponseBody
	public String backupCronJob(@RequestBody Map<String, Object> params)	{
		return servermanageService.backupCronJob(params);
	}
	
	@RequestMapping(value = "/saveCronJob", method = RequestMethod.POST)
	@ResponseBody
	public String saveCronJob(@RequestBody Map<String, Object> params)	{
		return servermanageService.saveCronJob(params);
	}
	
	@RequestMapping(value = "/runJob", method = RequestMethod.POST)
	@ResponseBody
	public String runJob(@RequestBody Map<String, Object> params)	{
		return servermanageService.runJob(params);
	}
	

	@RequestMapping("/filexmlUpload")
	@ResponseBody
	public Map<String,Object> fileUpload(HttpServletRequest req, HttpServletResponse rep
) throws FileNotFoundException, IOException {
		MultipartHttpServletRequest mReq = (MultipartHttpServletRequest) req;
		Map<String,Object> resultMap = servermanageService.filexmlUpload(mReq, feedServerPath, feedBackupPath);
		return resultMap;
	}

	@RequestMapping(value = "/getXmlFileInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getXmlFileInfo(@RequestBody Map<String, Object> params)	{
		return servermanageService.getXmlFileInfo(feedServerPath, params);
	}
	

	@RequestMapping(value = "/setXmlFileUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> setXmlFileUpdate(@RequestBody Map<String, Object> params)	{
		return servermanageService.setXmlFileUpdate(feedServerPath, params);
	}

	@RequestMapping(value = "/setXmlFileInsert", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> setXmlFileInsert(@RequestBody Map<String, Object> params)	{
		return servermanageService.setXmlFileInsert(feedServerPath, params);
	}

	@RequestMapping(value = "/setXmlFileDelete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> setXmlFileDelete(@RequestBody Map<String, Object> params)	{
		return servermanageService.setXmlFileDelete(feedServerPath, params);
	}
	
}
