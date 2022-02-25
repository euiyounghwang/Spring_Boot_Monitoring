package com.test.test.spring.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.test.test.spring.service.AnalysisService;
import com.test.test.spring.repository.MonitoringMapper;
import com.test.test.spring.util.AesUtil;
import com.test.test.spring.util.Base64Utils;
import com.test.test.spring.util.ESUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class MonitoringService {
	@Autowired
	MonitoringMapper monitoringMapper;
	
	@Autowired
	ElasearchService elasearchService;
	
	@Autowired
	CodeService codeService;
	
	@Autowired
	AnalysisService analysisService;
	
	private String search_engine_ip = "";
	
	@Value("${server.running.flag}")
	private String server_running_flag = "";
	
//	@Value("${dev.search_engine}")
//	private String local_elasticsearch = "";
	
	@Value("${log.search_engine}")
	private String log_elasticsearch = "";
		
	@Value("${prd.search_engine}")
	private String prd_elasticsearch = "";
	
	@Value("${spring.aesutil.keyval}")
	private String aesutil_keyval;
	
	@Value("${spring.aesutil.bsalt}")
	private String aesutil_bsalt;
	
	
//	private static Boolean isPolling = false;
	
	private static int[] CPU = new int[10000];
	private static int[] JVM = new int[10000];
	
	private static Logger logger = Logger.getLogger(MonitoringService.class);
	
	public List<Map<String, String>> commonCodeList(Map<String, String> params){
		return monitoringMapper.commonCodeList(params);
	}
	
	public Map<String, Object> getSearchEngineIndicesList(Map<String, Object> params)
	{
//		logger.info("getSearchEngineIndicesList >> " + params);
//		logger.info("getSearchEngineIndicesList >> " + params.get("start"));
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where = search.get("value");
//		if(search.get("value") != null && !"".equals(search.get("value")))
//			where=search.get("value");
//			params.put("where", where);
			
//		params.put("start", (Integer) params.get("start"));
		
		String key ="";
		String value="";
		Map<String, Object> results = new HashMap<>();
		
//		{recordsFiltered=3, data=[{JVM=7%, STATUS=yellow, SHARDS=7, LOAD=0.09, NODE=ES_DATA_17,10.132.57.80:9300, CPU=0%, DISK=2.7 TB}, {JVM=7%, STATUS=green, SHARDS=7, LOAD=0.09, NODE=ES_DATA_18,10.132.57.81:9300, CPU=0%, DISK=2.7 TB}, {JVM=7%, STATUS=yellow, SHARDS=7, LOAD=0.09, NODE=ES_DATA_17,10.132.57.80:9300, CPU=0%, DISK=2.7 TB}], recordsTotal=3}
		
		if (params.get("version_params") != null) {
			if (params.get("version_params").equals("7.9.1")) {
				search_engine_ip = this.log_elasticsearch;
			}
			else {
				search_engine_ip = this.prd_elasticsearch;
			}
		}
		else {
			return null;
		}
		
		// MASTER 여부 확인
		String result="";
		params.put("elaIP", "http://" + search_engine_ip + "/_cat/indices?format=txt");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
				result= elasearchService.handleElaCommonString(params);
				String[] getIndicesResults = result.split("\n");
//				for(int i=0; i < getIndicesResults.length; i++) {
//					String[] values = getIndicesResults[i].split(" ");
//					for(int h=0; h < values.length; h++)
//						if (values[h] != " " && values[h] != null && !values[h].isEmpty())
//							System.out.println(h+ "," + values[h]);
//				}
				results.put("recordsTotal", getIndicesResults.length);
				results.put("recordsFiltered",getIndicesResults.length);
				String[] results_str = new String[100];
				for(int i=(int)params.get("start"); i < getIndicesResults.length; i++) {
					Map<String, Object> map = new HashMap<>();
					String[] values = getIndicesResults[i].split(" ");
					int k=0;
					for(int h=0; h < values.length; h++)
						if (values[h] != " " && values[h] != null && !values[h].isEmpty()) {
							results_str[k++] = values[h];
//							System.out.println(h+ "," + values[h]);
						}
					if(where != "" && where != null) {
						if(getIndicesResults[i].split(" ")[2].startsWith(where)) {
//							System.out.println("OK");
							map.put("STATUS", results_str[0]);
							map.put("INDICES", results_str[2]);
							map.put("UID", results_str[3]);
							map.put("PR_RE_SET", results_str[4] + '/' + results_str[5]);
							map.put("TOTAL_COUNT", results_str[6]);
//							map.put("PR_RE_SET", getIndicesResults[i].split(" ")[4] + '/' + getIndicesResults[i].split(" ")[5]);
							map.put("DELETED", results_str[7]);
							map.put("STORED", results_str[8].toUpperCase());
							list.add(map);
						}
					}
					else {
//						System.out.println("FAIL");
						map.put("STATUS", results_str[0]);
						map.put("INDICES", results_str[2]);
						map.put("UID", results_str[3]);
						map.put("PR_RE_SET", results_str[4] + '/' + results_str[5]);
						map.put("TOTAL_COUNT", results_str[6]);
						map.put("DELETED", results_str[7]);
						map.put("STORED", results_str[8].toUpperCase());
						list.add(map);
					}
				}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		if (list.size() > 0)
			results.put("data", list);

		logger.info("getSearchEngineIndicesList >> " + results.toString());
		return results;
	}
	
	public Map<String, Object> getSearchEngineNodeList(Map<String, Object> params)
	{
//		isPolling = false;
		
		String key ="";
		String value="";
		Map<String, Object> results = new HashMap<>();
		
//		System.out.println(isPolling);
//		{recordsFiltered=3, data=[{JVM=7%, STATUS=yellow, SHARDS=7, LOAD=0.09, NODE=ES_DATA_17,10.132.57.80:9300, CPU=0%, DISK=2.7 TB}, {JVM=7%, STATUS=green, SHARDS=7, LOAD=0.09, NODE=ES_DATA_18,10.132.57.81:9300, CPU=0%, DISK=2.7 TB}, {JVM=7%, STATUS=yellow, SHARDS=7, LOAD=0.09, NODE=ES_DATA_17,10.132.57.80:9300, CPU=0%, DISK=2.7 TB}], recordsTotal=3}
		
		
		if (params.get("version_params") != null) {
			if (params.get("version_params").equals("7.9.1")) {
				search_engine_ip = this.log_elasticsearch;
			}
			else {
				search_engine_ip = this.prd_elasticsearch;
			}
		}
		else {
			return null;
		}
		
		
		// MASTER 여부 확인
		String isMasterInfo = "";
		params.put("elaIP", "http://" + search_engine_ip + "/_cat/master?format=txt");
		try {
				String result= elasearchService.handleElaCommonString(params);
				isMasterInfo = result.split(" ")[1];
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}

		params.put("elaIP", "http://"+search_engine_ip+"/_nodes/stats?human&pretty");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
    	try
	    {
    			JSONObject jsonObj  = elasearchService.handleElaCommon(params);
	    		ObjectMapper mapper = new ObjectMapper();
	    		Object json = mapper.readValue(jsonObj.toString(), Object.class);
	    		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
;
	    		JSONObject jsonObjSub = JSONObject.fromObject(jsonObj.getString("nodes"));
	    		results.put("recordsTotal", jsonObjSub.size());
    			results.put("recordsFiltered", jsonObjSub.size());
    			    
    			String[] node_key = new String[jsonObjSub.size()];
    			String[] node_ip = new String[jsonObjSub.size()];

    			Iterator iter = jsonObjSub.keys();
    			int i=0;
				while (iter.hasNext()) {
					key = (String) iter.next();
//					node_key[i] = key;
					value = jsonObjSub.getString(key);
					JSONObject jsonObjdetail = JSONObject.fromObject(value);
					node_ip[i++] = jsonObjdetail.getString("host").toString() + "," + key;
				}
				
				Arrays.sort(node_ip);
				for (int j = 0; j < node_ip.length; j++)  {
					node_key[j] = node_ip[j].split(",")[1];
//					System.out.println(node_ip[j]);
				}
				
				for (int j = 0; j < node_key.length; j++) 
				{
					value = jsonObjSub.getString(node_key[j]);
					JSONObject jsonObjdetail = JSONObject.fromObject(value);
					Iterator iter1 = jsonObjdetail.keys();
					while (iter1.hasNext()) {
						Map<String, Object> map = new HashMap<>();
						key = (String) iter1.next();
						value = jsonObjdetail.getString(key);
						if (key.equals("name")) {
							map.put("NODE",jsonObjdetail.getString("name") + "," + jsonObjdetail.getString("transport_address"));
							JSONArray orderOne = (JSONArray) jsonObjdetail.get("roles");
							String node_roles = "";
							for (int k = 0; k < orderOne.size(); k++) {
								node_roles += orderOne.getString(k).toString().toUpperCase();
								if (k != orderOne.size()-1) {
									node_roles += ",";
								}
							}
							
							for (int k = 0; k < orderOne.size(); k++) {
								if (orderOne.getString(k).toString().toUpperCase().equals("MASTER")) {
									if (jsonObjdetail.getString("host").equals(isMasterInfo)) {
										map.put("MASTER", "★");
									}
									else {
										map.put("MASTER", "☆");
									}
									break;
								} else {
									map.put("MASTER", "");
								}
							}
							map.put("ROLES", node_roles);
							
//							System.out.println(JSONObject.fromObject(jsonObjdetail.getJSONObject("indices").getJSONObject("docs")).getString("count"));
							map.put("INDICES", JSONObject.fromObject(jsonObjdetail.getJSONObject("indices").getJSONObject("docs")).getString("count") + "<br />"
											+ "(deleted:" + JSONObject.fromObject(jsonObjdetail.getJSONObject("indices").getJSONObject("docs")).getString("deleted")
											+ ")"
									);
							
							JSONObject obj_cpu = JSONObject.fromObject(jsonObjdetail.getJSONObject("os").getString("cpu"));
							if ( CPU[j] < Integer.parseInt(obj_cpu.getString("percent").toString())) {
								map.put("CPU_STATUS", "↑");
							}
							else if ( CPU[j] > Integer.parseInt(obj_cpu.getString("percent").toString())) {
								map.put("CPU_STATUS", "↓");
							}
							else {
								map.put("CPU_STATUS", "");
							}
							CPU[j] = Integer.parseInt(obj_cpu.getString("percent").toString());
							map.put("CPU", obj_cpu.getString("percent").toString() + "%");
							map.put("LOAD", JSONObject.fromObject(jsonObjdetail.getJSONObject("os").getJSONObject("cpu").getJSONObject("load_average")).getString("1m") + "<br />"
									+ "(5m:" + JSONObject.fromObject(jsonObjdetail.getJSONObject("os").getJSONObject("cpu").getJSONObject("load_average")).getString("5m")
									+ "->15m:" + JSONObject.fromObject(jsonObjdetail.getJSONObject("os").getJSONObject("cpu").getJSONObject("load_average")).getString("15m")
									+ ")"
									
									);
							JSONObject obj_jvm = JSONObject.fromObject(jsonObjdetail.getJSONObject("jvm").getString("mem"));
							
							String JVM_STATUS = "";
							if ( JVM[j] < Integer.parseInt(obj_jvm.getString("heap_used_percent").toString())) {
								JVM_STATUS = "↑";
							}
							else if ( JVM[j] > Integer.parseInt(obj_jvm.getString("heap_used_percent").toString())) {
								JVM_STATUS = "↓";
							}
							else {
								JVM_STATUS = "";
							}
							JVM[j] = Integer.parseInt(obj_jvm.getString("heap_used_percent").toString());
							
							map.put("JVM", "<b>" + obj_jvm.getString("heap_used_percent").toString() + "% " + JVM_STATUS + "</b><br />("
											+ obj_jvm.getString("heap_used").toString().toUpperCase() + " / "
											+ String.format("%.2f",(Double.parseDouble(obj_jvm.getString("heap_max_in_bytes").toString())/ (1024 * 1024 * 1024)))
											+ "GB)");
							
							JSONObject obj_fs = JSONObject.fromObject(jsonObjdetail.getJSONObject("fs").getString("total"));
							map.put("FS", String.format("%.2f",(Double.parseDouble(obj_fs.getString("available_in_bytes").toString()) / Double.parseDouble(obj_fs.getString("total_in_bytes").toString()) * 100.0))
									+ "% <br />(" + obj_fs.getString("free").toString().toUpperCase() + " / "
									+ obj_fs.getString("total").toString().toUpperCase() + ")");
							list.add(map);
	
						}
					}
					results.put("data", list);
				}
	    			
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	
//    	logger.info("getSearchEngineNodeList >> " + results.toString());
		return results;
	}
	
	 //pretty print a map
    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() 
				+ " Value : " + entry.getValue());
        }
    }
	
	public Map<String, Object> getSearchEngineStatus(Map<String, Object> params)
	{
//		isPolling = false;
		
		String key ="";
		String value="";
		Map<String, Object> results = new HashMap<>();
		
		//GET ACTIVE ELASTIC IP 
		
		if (params.get("version_params") != null) {
			if (params.get("version_params").equals("7.9.1")) {
				search_engine_ip = this.log_elasticsearch;
			}
			else {
				search_engine_ip = this.prd_elasticsearch;
			}
		}
		else {
			return null;
		}
		

		  params.put("elaIP", "http://"+search_engine_ip+"/_cluster/health");
    	try
	    	{
	    		JSONObject jsonObj  = elasearchService.handleElaCommon(params);
	    		ObjectMapper mapper = new ObjectMapper();
	    		Object json = mapper.readValue(jsonObj.toString(), Object.class);
	    		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//	    		System.out.println(indented);
//	    		System.out.println(jsonObj.get("cluster_name").toString());
	    		Map<String,String> map = new HashMap<String,String>();
	    		Iterator iter = jsonObj.keys();
	    		while(iter.hasNext()) {
	    		   key = (String)iter.next();
	    		   value = jsonObj.getString(key);
	    		   results.put(key,value);
	    		}
//	    		System.out.println("############################################");
//	    		System.out.println("cluster_health >> " + results.toString());
//	    		System.out.println("############################################");
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	
    	params.put("elaIP", "http://"+search_engine_ip+"/_cluster/stats?human&pretty");
    	try
	    	{
	    		JSONObject jsonObj  = elasearchService.handleElaCommon(params);
	    		ObjectMapper mapper = new ObjectMapper();
	    		Object json = mapper.readValue(jsonObj.toString(), Object.class);
	    		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//	    		System.out.println(indented);
//	    		System.out.println(jsonObj.get("jvm").get.toString());
	    		Map<String,String> map = new HashMap<String,String>();
	    		JSONObject jsonObjSub = JSONObject.fromObject(jsonObj.getString("indices"));
	    		Iterator iter = jsonObjSub.keys();
	    		while(iter.hasNext()) {
	    		   key = (String)iter.next();
	    		   value = jsonObjSub.getString(key);
//	    		   System.out.println("key " + key + ", value " + value);
	    		   results.put(key,value);
	    			}
	    		
	    		JSONObject jsonObj_nodes = JSONObject.fromObject(jsonObj.getString("nodes"));
	    		iter = jsonObj_nodes.keys();
	    		while(iter.hasNext()) {
	    		   key = (String)iter.next();
	    		   value = jsonObj_nodes.getString(key);
//	    		   System.out.println("key " + key + ", value " + value);
	    		   if (key.equals("count") ) {
	    		   	    	  results.put("nodes"+key,value);
	    		   	      }else {
	    		   	    	results.put(key,value);
	    		   	      }
	    		   }
	    			
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	
//    	results.put("data", "elastic");
//    	logger.info("getSearchEngineStatus >> " + results.toString());
    	return results;
	}
	
	public Map<String, Object> findFeederServerLog(Map<String, Object> params)
	{
		
		
		System.out.println("$$$params.get" + params);
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));				
		params.put("system_id", params.get("P_SYSTEM_ID"));
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		
	//  ********************************************************
			//GET ACTIVE ELASTIC IP 
			Map<String, Object> elaParam = new HashMap<>();
			elaParam.put("FK_CD_TP", "ELASTIC_IP");
			elaParam.put("ACTIVE_FLAG", "Y");
			
			if (this.server_running_flag.toUpperCase().equals("Y")) {
				List<Map<String, Object>> result = codeService.selectTable102(elaParam);
				search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
			} else {
				search_engine_ip = this.log_elasticsearch;
			}
		     //  ********************************************************
			

		
		
		String feeder_server_log_all = "feeder-sync-server-log-*";	
		
		params.put("elaIP", "http://"+ search_engine_ip +"/" + feeder_server_log_all + "/_search");
		params.put("indexId", feeder_server_log_all);

		System.out.println("this.local_elasticsearch >> " + search_engine_ip);
		System.out.println("@param" + params);
		
   	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type=""; //type
    		String id=""; //ID
    		String timestamp="";
    		String path = "";
    		String emp_no = "";
    		String handler = "";
    		String host=""; //호스트명
    		String logType="";  //로그타입
    		String logLevel="";  //로그레벨
    		String login_id =""; //메일아이디
    		String message=""; //메시지
    		String highlight=""; //검색출처

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_LOGIN_ID")){
    				login_id = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_LOGIN_ID");
    			} else { 
    				login_id = "-";
    				}
    			
    			host = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST"); //호스트명
    			logType = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TYPE"); //로그타입
    				
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("PATH")){
    				path = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PATH");
    			} else { 
    				path = "-";
    			}
    			
    			//LOG_LEVEL 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("LOG_LEVEL")){
    				logLevel = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_LEVEL");
    			} else { //LOG_LEVEL 없을때는 초기화
    				logLevel = "";
    			}

    		
    		try{
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONArray("LOG_MESSAGE").get(0);
				}catch(Exception ee){
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_MESSAGE");
    			}
//    			highlight = jArr.getJSONObject(i).getString("highlight");
    			timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("INPUTDATE");

    			map.put("INDEX", index);
    			map.put("ID", id);
    			map.put("LOG_PATH", path);
    			map.put("HOST", host);
    			map.put("LOG_LEVEL", logLevel);
    			map.put("TIMESTAMP", timestamp);
    			map.put("MESSAGE", message);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		
			if (list.size() > 0) {
				// if(jsonObj.getJSONObject("hits").getJSONObject("total") instanceof
				// JSONObject){
				// results.put("recordsTotal",
				// Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				// results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				// }
				// else {
				// results.put("recordsTotal",
				// Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
				// results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
				// }
				results.put("recordsTotal",
						Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				results.put("recordsFiltered",
						Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
			} else {
				results.put("recordsTotal", 0);
				results.put("recordsFiltered", 0);
			}
    		results.put("data", list);
    		System.out.println("feeder_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> findAIServerLog(Map<String, Object> params)
	{
//		isPolling = false;
		System.out.println("$$$params.get" + params);
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		params.put("system_id", params.get("P_SYSTEM_ID"));
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		
	//  ********************************************************
			//GET ACTIVE ELASTIC IP 
			Map<String, Object> elaParam = new HashMap<>();
			elaParam.put("FK_CD_TP", "ELASTIC_IP");
			elaParam.put("ACTIVE_FLAG", "Y");
			
			if (this.server_running_flag.toUpperCase().equals("Y")) {
				List<Map<String, Object>> result = codeService.selectTable102(elaParam);
				search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
			} else {
				search_engine_ip = this.log_elasticsearch;
			}
		     //  ********************************************************
			
		
		String ai_server_log_all = "ai-jean-server-log*,ai-server-log-*,ai-ict-contract-server-log-*";
		System.out.println("this.local_elasticsearch >> " + search_engine_ip);
		params.put("elaIP", "http://"+ search_engine_ip +"/" + ai_server_log_all + "/_search");
		params.put("indexId", ai_server_log_all);
		
		System.out.println("@param" + params);
		
   	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type=""; //type
    		String id=""; //ID
    		String timestamp="";
    		String path = "";
    		String emp_no = "";
    		String handler = "";
    		String host=""; //호스트명
    		String logType="";  //로그타입
    		String logLevel="";  //로그레벨
    		String login_id =""; //메일아이디
    		String message=""; //메시지
    		String highlight=""; //검색출처
    		String system_id="";

    		for(int i=0; i<jArr.size() ;i++)
    			{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_LOGIN_ID")){
    				login_id = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_LOGIN_ID");
    			} else { 
    				login_id = "-";
    				}
    			
    			host = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST"); //호스트명
    			logType = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TYPE"); //로그타입
    				
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("PATH")){
    				path = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PATH");
    			} else { 
    				path = "-";
    				}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("TYPE")){
    				system_id = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TYPE");
    			} else { 
    				system_id = "-";
    				}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("HANDLER")){
    				handler = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HANDLER");
    			} else {
    				handler = "-";
    				}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SSO_EMP_NO")){
    				emp_no = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SSO_EMP_NO");
    			} else {
    				emp_no = "-";
    				}
    			
    			//LOG_LEVEL 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("LOG_LEVEL")){
    				logLevel = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_LEVEL");
    			} else { //LOG_LEVEL 없을때는 초기화
    				logLevel = "";
    			}

    		
    		try{
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONArray("LOG_TEXT").get(0);
				}catch(Exception ee){
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_TEXT");
    			}
//    			highlight = jArr.getJSONObject(i).getString("highlight");
    			timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");

    			map.put("INDEX", index);
    			map.put("ID", id);
    			map.put("LOG_PATH", path);
    			map.put("SYSTEM_ID", system_id);
    			map.put("SSO_EMP_NO", emp_no);
    			map.put("SSO_LOGIN_ID", login_id);
    			map.put("HOST", host);
    			map.put("HANDLER", handler);
    			map.put("LOG_LEVEL", logLevel);
    			map.put("TIMESTAMP", timestamp);
//    			map.put("HIGHLIGHT", highlight);
    			map.put("MESSAGE", message);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		
    		if(list.size()>0) {
//	    			if(jsonObj.getJSONObject("hits").getJSONObject("total") instanceof JSONObject){
//	    				 results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//	    				 results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//	    			   }
//	    		 else {
//	    				 results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//	    				 results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//	    				}
    		 results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				 results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		results.put("data", list);
    		System.out.println("was_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	public Map<String, Object> findWasServerLog(Map<String, Object> params)
	{
//		isPolling = false;
		
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
        //  ********************************************************
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
		if (this.server_running_flag.toUpperCase().equals("Y")) {
			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
		} else {
			search_engine_ip = this.log_elasticsearch;
		}
	     //  ********************************************************
		
		
		System.out.println("this.local_elasticsearch >> " + search_engine_ip);
		params.put("elaIP", "http://"+ search_engine_ip +"/was-server-log*/_search");
//		params.put("elaIP", "http://"+ search_engine_ip +"/was-server-log*/_search?track_total_hits=true&rest_total_hits_as_int=true");
		params.put("indexId", "was-server-log*");
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
    		String timestamp="";
    		String logPath = ""; //로그패스
    		String host=""; //호스트명
    		String chainCode=""; //체인코드
    		String logType="";  //로그타입
    		String systemId="";  //시스템
    		String logLevel="";  //로그레벨
    		String mailId =""; //메일아이디
    		String handler="";  //핸들러
    		String message=""; //메시지
    		String highlight=""; //검색출처

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID
    			logPath = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PATH"); //로그패스

    			host = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST"); //호스트명

    			chainCode = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("CHAIN_CODE"); //체인코드
    			logType = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TYPE"); //로그타입

    			//SYSTEM_ID 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SYSTEM_ID")){
    				systemId = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SYSTEM_ID");
    			} else { //SYSTEM_ID 없을때는 초기화
    				systemId = "";
    			}

    			//LOG_LEVEL 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("LOG_LEVEL")){
    				logLevel = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("LOG_LEVEL");
    			} else { //LOG_LEVEL 없을때는 초기화
    				logLevel = "";
    			}

    			//HANDLER 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("HANDLER")){
    				handler = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HANDLER");
    			} else{ //HANDLER 없을때는 초기화
    				handler = "";
    			}

    			//MAIL_ID 존재 체크
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("MAIL_ID")){
    				mailId = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("MAIL_ID");
    			}else{ //MAIL_ID 없을때는 초기화
    				mailId = "";
    			}

    			try{
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONArray("MESSAGE").get(0);
				}catch(Exception ee){
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("MESSAGE");
    			}
    			highlight = jArr.getJSONObject(i).getString("highlight");
    			timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");

    			map.put("INDEX", index);
    			map.put("ID", id);
    			map.put("LOG_PATH", logPath);
//    			map.put("LOG_TYPE", logType);
    			map.put("CHAIN_CODE", chainCode);
    			map.put("TIMESTAMP", timestamp);
    			map.put("HOST", host);
    			map.put("SYSTEM_ID", systemId);
    			map.put("LOG_LEVEL", logLevel);
    			map.put("MAIL_ID", mailId);
    			map.put("HANDLER", handler);
    			map.put("HIGHLIGHT", highlight);
    			map.put("MESSAGE", message);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		
    		if(list.size()>0) {
//	    			if(jsonObj.getJSONObject("hits").getJSONObject("total") instanceof JSONObject){
//	    				 results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//	    				 results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//	    			   }
//	    		 else {
//	    				 results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//	    				 results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//	    				}
    		 results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				 results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		results.put("data", list);
    		System.out.println("was_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> findQueueServerLog(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
//		params.put("elaIP", "http://"+result.get(0).get("CD_TP_MEANING")+"/queue-management-server-log*/_search");
		if (this.server_running_flag.toUpperCase().equals("Y")) {
			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
		} else {
			search_engine_ip = this.log_elasticsearch;
		}
		
		System.out.println("this.elasticsearch >> " + search_engine_ip);
		params.put("elaIP", "http://"+ search_engine_ip +"/queue-management-server-log*/_search");
		params.put("indexId", "queue_management-server-log*");
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type="";
    		String company=""; //회사
    		String action=""; //action
    		String id=""; //ID
    		String q_id=""; //Q_ID
    		String timestamp="";
    		String status_code = "";
    		String updateTimestamp="";
    		String host="";
    		String resultCode="";
    		String systemId="";
    		String message="";
    		String highlight="";

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); //ID

    			host = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST"); //호스트명
    			company = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("COMPANY_CODE"); //회사
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("KEY")){
    				q_id = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("KEY"); //결과값
    			} else { 
    				q_id = "";
    				}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SYSTEM_ID")){
    				systemId = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SYSTEM_ID"); //결과값
    			} else { 
    				systemId = "";
    				}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("ACTION")){
    				action = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("ACTION"); //action
    			} else { 
    				action = "";
    				}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("STATUS_CODE")){
    				status_code = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("STATUS_CODE"); //action
    			} else { 
    				status_code = "";
    			}
    			
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("RESULT")){
    				resultCode = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("RESULT"); //결과값
    			} else { 
    				resultCode = "";
    			}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("CREATE_DATE")){
    				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("CREATE_DATE");
    			} else { 
    				timestamp = "";
    			}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("QUEUE_INSERT_DATE")){
    				updateTimestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("QUEUE_INSERT_DATE");
    			} else { 
    				updateTimestamp = "";
    			}
    			
    			if( jArr.getJSONObject(i).has("highlight")){
    				highlight = (String) jArr.getJSONObject(i).getString("highlight");
    				}
    			else {
    				highlight = "";
    				}
    				
//    			highlight = jArr.getJSONObject(i).getString("highlight");

    			map.put("COMPANY_CODE", company);
    			map.put("INDEX", index);
    			map.put("SYSTEM_ID", systemId);
    			map.put("ID", id);
    			map.put("QUEUE_ID", q_id);
    			map.put("ACTION", action);
    			map.put("S_CODE", status_code);
    			map.put("CREATE_DATE", timestamp);
    			map.put("QUEUE_INSERT_DATE", updateTimestamp);
    			map.put("RESULT_CODE", resultCode);
    			map.put("HIGHLIGHT", highlight);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		
			if (list.size() > 0) {
				// if(jsonObj.getJSONObject("hits").getJSONObject("total") instanceof
				// JSONObject){
				// results.put("recordsTotal",
				// Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				// results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				// }
				// else {
				// results.put("recordsTotal",
				// Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
				// results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
				// }
				results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
			} else {
				results.put("recordsTotal", 0);
				results.put("recordsFiltered", 0);
			}
    		
    		results.put("data", list);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> findFeedServerLog(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
		if (this.server_running_flag.toUpperCase().equals("Y")) {
			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
		} else {
			search_engine_ip = this.log_elasticsearch;
		}
		
		System.out.println("this.local_elasticsearch >> " + search_engine_ip);
		params.put("elaIP", "http://"+ search_engine_ip +"/contents-feeder-server-log*/_search");
//		params.put("elaIP", "http://"+result.get(0).get("CD_TP_MEANING")+"/contents-feeder-server-log*/_search");
		params.put("indexId", "contents-feeder-server-log*");

		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		

    		String id=""; //ID
    		String start_date="";
    		String end_date="";
    		String test1 = "";
    		String systemId=""; 
    		String host=""; 
    		String host_ip="";
    		String test3="";
    		String action="";
    		String test4 ="";
    		String test5 ="";
    		String highlight ="";
    		String message = "";

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			id = jArr.getJSONObject(i).getString("_id"); //ID
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("START_DATE")){
    				start_date = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("START_DATE");
    			} else { 
    				start_date = "";
    			}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("END_DATE")){
    				end_date = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("END_DATE");
    			} else { 
    				end_date = "";
    			}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("SYSTEM_ID")){
    				systemId = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SYSTEM_ID"); 
    			} else { 
    				systemId = "";
    			}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("HOST")){
    				host = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST");
    			} else { 
    				host = "";
    			}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("HOST_IP")){
    				host_ip = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST_IP");
    			} else { 
    				host_ip = "";
    			}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("ACTION")){
    				action = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("ACTION"); 
    			} else { 
    				action = "";
    			}
    			
    			// 임시로 들어간 변수들 나중에 넣는다고해서 가짜 변수만 만들어놓음.
    			test1 = "";
    			test3 = "";
    			test4 = "";
    			test5 = ""; 

    			try{
    				if(jArr.getJSONObject(i).getJSONObject("_source").getJSONArray("RAW_JSON")!= null) {
    					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONArray("RAW_JSON").get(0);
    				}
    				else {
    					message = "";
    				}
    					
				}catch(Exception ee){
//					if (jArr.getJSONObject(i).getJSONObject("_source").getString("RAW_JSON") != null) {
//						message = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("RAW_JSON");
//					}
//					else {
//						message = "";
//					}
					message = "";
    			}
    			highlight = jArr.getJSONObject(i).getString("highlight");

    			map.put("START_DATE", start_date);
    			map.put("END_DATE", end_date);
    			map.put("TEST1", test1);
    			map.put("SYSTEM_ID", systemId);
    			map.put("HOST", host);
    			map.put("HOST_IP", host_ip);
    			map.put("TEST3", test3);
    			map.put("ACTION", action);
    			map.put("TEST4", test4);
    			map.put("ID", id);
    			map.put("TEST5", test5);
    			map.put("HIGHLIGHT", highlight);
    			map.put("MESSAGE", message);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		   
			if (list.size() > 0) {
				// if(jsonObj.getJSONObject("hits").getJSONObject("total") instanceof
				// JSONObject){
				// results.put("recordsTotal",
				// Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				// results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				// }
				// else {
				// results.put("recordsTotal",
				// Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
				// results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
				// }
				results.put("recordsTotal",
						Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				results.put("recordsFiltered",
						Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
			} else {
				results.put("recordsTotal", 0);
				results.put("recordsFiltered", 0);
			}

    		results.put("data", list);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> findEcmServerLog(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
				
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
		if (this.server_running_flag.toUpperCase().equals("Y")) {
			List<Map<String, Object>> result = codeService.selectTable102(elaParam);
			search_engine_ip = result.get(0).get("CD_TP_MEANING").toString();
		} else {
			search_engine_ip = this.log_elasticsearch;
		}
		
		System.out.println("this.local_elasticsearch >> " + search_engine_ip);
		params.put("elaIP", "http://"+ search_engine_ip +"/ecm-auth-server-log*/_search");
//		params.put("elaIP", "http://"+result.get(0).get("CD_TP_MEANING")+"/ecm-auth-server-log*/_search");
		params.put("indexId", "ecm-auth-server-log*");
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String id=""; //ID
    		String logPath = ""; //로그패스
    		String timestamp=""; //체인코드
    		String host=""; //호스트명
    		String highlight=""; //검색출처
    		String message=""; //메시지

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			id = jArr.getJSONObject(i).getString("_id"); //ID
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("PATH")){
    				logPath = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PATH"); //로그패스
    			} else { 
    				logPath = "";
    			}
    			
    			if( jArr.getJSONObject(i).getJSONObject("_source").has("HOST")){
    				host = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST"); //호스트명
    			} else { 
    				host = "";
    			}
    			
    			try{
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getJSONArray("MESSAGE").get(0);
				}catch(Exception ee){
					message = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("MESSAGE");
    			}
    			highlight = jArr.getJSONObject(i).getString("highlight");
    			timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");

    			map.put("INDEX", index);
    			map.put("ID", id);
    			map.put("LOG_PATH", logPath);
    			map.put("TIMESTAMP", timestamp);
    			map.put("HOST", host);
    			map.put("HIGHLIGHT", highlight);
    			map.put("MESSAGE", message);

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		
			if (list.size() > 0) {
				// if(jsonObj.getJSONObject("hits").getJSONObject("total") instanceof
				// JSONObject){
				// results.put("recordsTotal",
				// Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				// results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				// }
				// else {
				// results.put("recordsTotal",
				// Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
				// results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
				// }
				results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				results.put("recordsFiltered", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
			} else {
				results.put("recordsTotal", 0);
				results.put("recordsFiltered", 0);
			}
    		results.put("data", list);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> selectSqlList(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length") + (Integer) params.get("start"));
		params.put("start", (Integer) params.get("start"));
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");

		String dir = (String) order.get(0).get("dir");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		String orderBy=columns.get(column).get("data") +" "+ dir;
		params.put("orderBy", orderBy);
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		if(where != null && !"".equals(where))
			params.put("where", where);
		
		List<Map<String, String>> list = monitoringMapper.selectSqlList(params);
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
	
	public void saveSql(Map<String, Object> params){
		Map<String, String> OBJECT_ID = monitoringMapper.selectMaxSqlObjectId();
		params.put("OBJECT_ID", OBJECT_ID.get("OBJECT_ID"));
		monitoringMapper.saveSql(params);
		if(params.containsKey("PARAMS")) {
			JSONArray jsonArray = getJsonObjectOrJsonArray(params.get("PARAMS"));
			for(int i=0; i<jsonArray.size(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				jsonObj.put("OBJECT_ID", OBJECT_ID.get("OBJECT_ID"));
				jsonObj.put("SEQ", i+1);
			}
			params.put("PARAMS", jsonArray);
			monitoringMapper.saveSqlParams(params);
			
		}
	}
	
	public void updateSql(Map<String, Object> params){
		deleteSql(params);
		monitoringMapper.saveSql(params);
		if(params.containsKey("PARAMS")) {
			JSONArray jsonArray = getJsonObjectOrJsonArray(params.get("PARAMS"));
			for(int i=0; i<jsonArray.size(); i++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				jsonObj.put("OBJECT_ID", params.get("OBJECT_ID"));
				jsonObj.put("SEQ", i+1);
			}
			params.put("PARAMS", jsonArray);
			monitoringMapper.saveSqlParams(params);
		}
	}
	
	public void deleteSql(Map<String, Object> params){
		monitoringMapper.deleteSql(params);
		monitoringMapper.deleteSqlParams(params);
	}
	
	public Map<String, Object> selectSql(Map<String, Object> params){
		Map<String, Object> result = new HashMap<>();
		result.put("sql", monitoringMapper.selectSql(params));
		result.put("params", monitoringMapper.selectSqlDetail(params));
		return result;
	}
	
	public JSONArray getJsonObjectOrJsonArray(Object object){
	    JSONArray jsonArray = new JSONArray();
	    if (object instanceof Map){
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.putAll((Map)object);
	        jsonArray.add(jsonObject);
	    }
	    else if (object instanceof List){
	        jsonArray.addAll((List)object);
	    }
	    return jsonArray;
	}
	
	public Map<String, Object> excuteSql(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		String sSqlSource = params.get("QUERY").toString();
		String sErrMsg = null;
		String queryType = sSqlSource.substring(0, 6);
		if("header".equals(params.get("header"))) {
			try {
				List<Map<String, Object>> selectResult = monitoringMapper.excuteSqlHeaderSelect(params);
				for(Map<String,Object> map : selectResult){
				      if(map.containsKey("CREATION_TIMESTAMP")) {
				         if(map.get("CREATION_TIMESTAMP") != null) {					        	 
				        	 if("oracle.sql.TIMESTAMP".equals(map.get("CREATION_TIMESTAMP").getClass().getName())){
								 oracle.sql.TIMESTAMP ts = (oracle.sql.TIMESTAMP) map.get("CREATION_TIMESTAMP");
								 java.sql.Timestamp ts2 = ts.timestampValue();
								 String ts3 = new SimpleDateFormat("yyyy-MM-dd").format(ts2);
				               map.put("CREATION_TIMESTAMP", ts3); 
				            }
				         } 
				         if(map.get("LAST_UPDATE_TIMESTAMP") != null) {
				           if("oracle.sql.TIMESTAMP".equals(map.get("LAST_UPDATE_TIMESTAMP").getClass().getName())){
				            	oracle.sql.TIMESTAMP ts = (oracle.sql.TIMESTAMP) map.get("LAST_UPDATE_TIMESTAMP");
								 java.sql.Timestamp ts2 = ts.timestampValue();
								 String ts3 = new SimpleDateFormat("yyyy-MM-dd").format(ts2);
								 map.put("LAST_UPDATE_TIMESTAMP", ts3);
				            }
				         } 
			        }
				}
				result.put("data", selectResult);
			} catch (Exception e) {
				sErrMsg = e.getMessage();
				result.put("errMsg", sErrMsg );
			}
		}else if("SELECT".equals(queryType.toUpperCase())){
			params.put("length", (Integer) params.get("length") + (Integer) params.get("start"));
			params.put("start", "0");
			try {
				List<Map<String, Object>> selectResult = monitoringMapper.excuteSqlDataTableSelect(params);
				for(Map<String,Object> map : selectResult){
				      if(map.containsKey("CREATION_TIMESTAMP")) {
				         if(map.get("CREATION_TIMESTAMP") != null) {					        	 
				        	 if("oracle.sql.TIMESTAMP".equals(map.get("CREATION_TIMESTAMP").getClass().getName())){
								 oracle.sql.TIMESTAMP ts = (oracle.sql.TIMESTAMP) map.get("CREATION_TIMESTAMP");
								 java.sql.Timestamp ts2 = ts.timestampValue();
								 String ts3 = new SimpleDateFormat("yyyy-MM-dd").format(ts2);
				               map.put("CREATION_TIMESTAMP", ts3); 
				            }
				         } 
				         if(map.get("LAST_UPDATE_TIMESTAMP") != null) {
				           if("oracle.sql.TIMESTAMP".equals(map.get("LAST_UPDATE_TIMESTAMP").getClass().getName())){
				            	oracle.sql.TIMESTAMP ts = (oracle.sql.TIMESTAMP) map.get("LAST_UPDATE_TIMESTAMP");
								 java.sql.Timestamp ts2 = ts.timestampValue();
								 String ts3 = new SimpleDateFormat("yyyy-MM-dd").format(ts2);
								 map.put("LAST_UPDATE_TIMESTAMP", ts3);
				            }
				         } 
			        }
				}
				result.put("draw", params.get("draw"));
				if(selectResult.size()>0) {
					result.put("recordsTotal", selectResult.get(0).get("TOTCNT"));
					result.put("recordsFiltered",selectResult.get(0).get("TOTCNT"));
				} else {
					result.put("recordsTotal", 0);
					result.put("recordsFiltered",0);
				}
				result.put("data", selectResult);
//				result.put("selectResult"	,selectResult.size()==0 ? "조회된 데이터가 없습니다." : selectResult);
			} catch (Exception e) {
				sErrMsg = e.getMessage();
				result.put("errMsg", sErrMsg );
			}
		} else if("DELETE".equals(queryType.toUpperCase())){
			try {
				int row = monitoringMapper.excuteSqlDelete(params);
				result.put("result" , "전체 "+ row+ "건 이 삭제 되었습니다.");
			} catch (Exception e) {
				sErrMsg = e.getMessage();
				result.put("errMsg", sErrMsg );
			}
		} else if("UPDATE".equals(queryType.toUpperCase())){
			try {
				int row = monitoringMapper.excuteSqlUpdate(params);
				result.put("result" , "전체 "+ row + "건 이 업데이트 되었습니다.");
			} catch (Exception e) {
				sErrMsg = e.getMessage();
				result.put("errMsg", sErrMsg );
			}
		} else { //INSERT
			try {
				int row = monitoringMapper.excuteSqlInsert(params);
				result.put("result" , "전체 "+ row+ "건 이 등록 되었습니다.");
			} catch (Exception e) {
				sErrMsg = e.getMessage();
				result.put("errMsg", sErrMsg );
			}
		}
		return result;
	}
	
	public Map<String, Object> callProvideIF(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		try {
			String chaincode = params.get("req_chain").toString().trim();
			String servicename = "";
			
			
			if(chaincode.contains("monitoring")) {
				servicename="/monitoring/dataProvider";					
			}else {
				if(params.get("req_site").toString().trim().contains("posapia")||params.get("req_site").toString().trim().contains("e-catalog")) {
					servicename="/U40A/directPAPIF.do";
				}else if(params.get("req_site").toString().trim().equals("patt")) {
					servicename="/U40A/directPATTIF.do";
				}else if(params.get("req_site").toString().trim().contains("hrsearch")) {
					servicename="/U40A/directHRIF.do";
				}else if(params.get("req_site").toString().trim().contains("P|")) {
					servicename="/U40A/provideAIF.do";
				}else if(params.get("req_site").toString().trim().contains("D|")) {
					servicename="/U40A/directAIF.do";
				}else{
					servicename="/U40A/dataProvider.do";
				}
			}
			
			String url="http://"+ params.get("req_was").toString().trim()+servicename;
			
			if(servicename.contains("provideAIF") || servicename.contains("directAIF")) {

				result = callU40AIF(params, url);
			}else {

				result = settingJson(params, url);	
			}
			
			
		
		}catch(Exception e) {
			e.getMessage();
			logger.error("[Activity-error] ProvideIfActivity.runActivity() 런엑티비티 시작도 못함 Exception : "+ e.getMessage() );
		}
		return result;
	}
	
	public Map<String, Object> callTextIF(Map<String, Object> params) {
		Map<String, Object> results = new HashMap<>();
		System.out.println("callTextIF on MonitoringService");
    try
    	{
    		
    		System.out.println(" params -> " + params);
    	
    		JSONObject jsonObj = analysisService.handleAnalysis(params);
    		
    		JSONArray jArr =jsonObj.getJSONArray("data");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		
    		String sentence = "";
    		String predict = "";
    		String probability = "";


    	for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			//sentence 존재 체크
    			if( jArr.getJSONObject(i).has("sentence")){
    				sentence = (String) jArr.getJSONObject(i).getString("sentence");
    			} else { //sentence 없을때는 초기화
    				sentence = "";
    			     }

    			//predict 존재 체크
    			if( jArr.getJSONObject(i).has("predict")){
    				predict = (String) jArr.getJSONObject(i).getString("predict");
    			} else { //predict 없을때는 초기화
    				predict = "";
    			     }

    			//probability 존재 체크
    			if( jArr.getJSONObject(i).has("probability")){
    				probability = (String) jArr.getJSONObject(i).getString("probability");
    			} else { //probability 없을때는 초기화
    				probability = "";
    			     }	

    			map.put("sentence", sentence);
    			map.put("predict", predict);
    			map.put("probability", probability);

    			list.add(map);
    			}
    		results.put("result_code", "success");    		
    		results.put("data", list);
    		System.out.println("was_return >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> settingJson(Map<String, Object> params, String url) {
		Map<String, Object> result = new HashMap<>();
		BufferedReader br = null;
		HttpURLConnection httpConn = null;
		
		StringBuffer sb = new StringBuffer();

		String value = params.get("jsonParam").toString();
		JSONObject paramvalue = JSONObject.fromObject(value);
		sb.append(value);
		
	
		
		try {
			// 암호화 키
			String enKey = "";
			// 복호화 키
 			String deKey = "";
 			
			if(paramvalue.has("es_sys_id") && paramvalue.get("es_sys_id").toString().contains("_hrsearch")){
				enKey = "aca54370bbe0445a6cc69f7fdcc52467";
				if(("01").equals(paramvalue.get("es_comp_cd"))) {
					deKey = "a543269431ff3288d11746f7ef45667";
				} else {
					deKey = "58d4760b9e683937ce9b0731e82fc66";
				}
			}
			
		
			Base64Utils base64 = new Base64Utils();
			String param = ( !((enKey == null) || (enKey.length() == 0)) )?base64.encrypt(sb.toString(), enKey):base64.base64Encoding("posmeta:"+sb.toString());
			
			param = URLEncoder.encode(param, "UTF-8");
			
			URL engineUrl01 = new URL(url);
			
			logger.info("#####url ::::: " + url);
			
 			httpConn = (HttpURLConnection) engineUrl01.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			String qparam = "q=" + param;
			
			logger.info("#######넘어온 파라미터 ::: " + qparam);
			
			OutputStream out = httpConn.getOutputStream();
			out.write(qparam.getBytes("UTF-8"));
			out.flush();
			out.close();
			out = null;
			
			br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			JSONObject searchSite = JSONObject.fromObject(br.readLine());
			logger.debug("#######검색결과 ::: " + searchSite.toString());
			
			String tmp="";
			String tmp_more="";
			String result_code_tmp="";
			String successFlag = searchSite.getString("result_code");
			result.put("result_code", successFlag);
			
			String metaChk="";
			String metaList="";
			
			
			
			if (("success").equals(successFlag)) {
				if (searchSite.has("gsa_data_key") && !("").equals(searchSite.getString("gsa_data_key"))) {
					
					tmp = searchSite.getString("gsa_data_key");
					result_code_tmp=searchSite.getString("result_code");
					tmp = URLDecoder.decode(tmp, "UTF-8");
					tmp = (!((deKey == null) || (deKey.length() == 0)))?base64.decrypt(tmp, deKey):base64.base64decoding(tmp);
					if(searchSite.has("metaChk")){
						metaChk=searchSite.getString("metaChk");
						result.put("metaChk",metaChk.replaceAll("\"", "&cute;"));
					}
					
					if(searchSite.has("metaList")){
						metaList=searchSite.getString("metaList");
						result.put("metaList",metaList);
					}
					
				
					result.put("result_code", result_code_tmp.replaceAll("\"", "&cute;"));
					result.put("gsa_data_key", tmp);
					result.put("param", sb.toString());
					
					if(params.get("req_site").toString().trim().contains("hrsearch")){
						result.put("metaList", getSystemQueryInfo(paramvalue.get("es_comp_cd").toString(), params.get("req_site").toString().trim()));	
					}else if(params.get("req_site").toString().trim().equals("patt")){
						result.put("metaList", getSystemQueryInfo("30", params.get("req_site").toString().trim()));	
					}else if(params.get("req_site").toString().trim().contains("dataprovider")){
						result.put("metaList", getSystemQueryInfo("30", params.get("req_site").toString().trim().substring(13)));	
					}
					

				}
				
				
			} else if(("failed").equals(successFlag)){
				tmp = searchSite.getString("gsa_error_key");
				result_code_tmp=searchSite.getString("result_code");
				
				result.put("result_code", result_code_tmp.replaceAll("\"", "&cute;"));
				result.put("gsa_error_key", tmp.replaceAll("\"", "&cute;"));
				if(searchSite.has("gsa_error_key_more_info")) {
					tmp_more = searchSite.getString("gsa_error_key_more_info");
					result.put("gsa_error_key_more_info", tmp_more.replaceAll("\"", "&cute;"));
				}
				
				logger.debug("#####fail####:::::검색결과 못가져옴 ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.settingJson() 커넥션 자체 문제 Exception : "+ e.getMessage() );
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("[Activity-error] ProvideIfActivity.settingJson() 커넥션 닫는부분  Exception : "+ e.getMessage() );
				}
			}
			if (httpConn != null) {
				try {
					httpConn.disconnect();
					httpConn = null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("[Activity-error] ProvideIfActivity.settingJson() 커넥션 닫는부분  Exception : "+ e.getMessage() );
				}
			}
		}
		return result;
	}
	
	
	public Map<String, Object> callU40AIF(Map<String, Object> params, String url) {
		Map<String, Object> result = new HashMap<>();
		BufferedReader br = null;
		HttpURLConnection httpConn = null;
		Base64Utils base64 = new Base64Utils();

		String jsonVal = params.get("jsonParam").toString();	
		String otherVal = params.get("otherParam").toString();
		String comCodeVal = (params.get("req_comcode")!=null)?params.get("req_comcode").toString():"";
		String param = "";
		
		try {
			if(params.get("req_site").toString().trim().contains("gih")) {
				param = ESUtil.isNotEmpty(jsonVal)?"u40ameta:"+jsonVal:"";
			}else {
				param = ESUtil.isNotEmpty(jsonVal)?"posmeta:"+jsonVal:"";	
			}
			
			if(url.contains("directAIF")) {
				if(otherVal.contains("chat_doc")) {
					String keyval = aesutil_keyval;
					String bsalt = aesutil_bsalt;
					AesUtil.getInstance(base64.base64decoding(keyval), base64.base64decoding(bsalt).getBytes());					
					param = AesUtil.encrypt(param.trim()); 
				}else {
					param = base64.base64Encoding(param);
				}
			}
			
			param = URLEncoder.encode(param, "UTF-8");	
			URL engineUrl01 = new URL(url);
			
			logger.info("#####url ::::: " + url);
			
 			httpConn = (HttpURLConnection) engineUrl01.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			
			String qparam = "q=" + param + otherVal + comCodeVal;
			
			logger.info("#######넘어온 파라미터 ::: " + qparam);
			
			OutputStream out = httpConn.getOutputStream();
			out.write(qparam.getBytes("UTF-8"));
			out.flush();
			out.close();
			out = null;
			
			br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String read = null;
			StringBuffer sb =  new StringBuffer();	   
			
			while ((read = br.readLine()) != null) {
				sb.append(read);			       
	     	}
			JSONObject searchSite = JSONObject.fromObject(sb.toString());
			logger.info("#######검색결과 ::: " +searchSite.toString());
			
			String tmp="";
						
			
			if (searchSite.has("gsa_data_key") && !("").equals(searchSite.getString("gsa_data_key"))) {
				
				tmp = searchSite.getString("gsa_data_key");				
			
				result.put("result_code", "success");
				result.put("gsa_data_key", tmp);
				result.put("param", jsonVal);					

			} else{
				tmp = searchSite.getString("gsa_error_key");
				
				result.put("result_code", "failed");
				result.put("gsa_error_key", tmp.replaceAll("\"", "&cute;"));
				
				logger.debug("#####fail####:::::검색결과 못가져옴 ");
			}
				
				
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.settingJson() 커넥션 자체 문제 Exception : "+ e.getMessage() );
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("[Activity-error] ProvideIfActivity.settingJson() 커넥션 닫는부분  Exception : "+ e.getMessage() );
				}
			}
			if (httpConn != null) {
				try {
					httpConn.disconnect();
					httpConn = null;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("[Activity-error] ProvideIfActivity.settingJson() 커넥션 닫는부분  Exception : "+ e.getMessage() );
				}
			}
		}
		return result;
	}

	
	/**
	 * elastic_query.xml에 작성 되어있는 쿼리 정보를 EngineQueryVO 객체에 담아 리턴한다,
	 * @return Node
	 * @throws Exception
	 */
/*	@SuppressWarnings("resource")
	protected synchronized String getSystemQueryInfo(String comCode) throws Exception {
		logger.info("##### GETSYSTEMQUERYNODE START #####");
		String rtn_metalist = null;
		StringBuffer sb =  new StringBuffer();
		BufferedReader br = null;
		HttpURLConnection httpConn = null;
		   
		final String queryPath = "http://uswpes.posco.net:7091/pic/system_query";
		String strURL = queryPath + "/" + "elastic_query.xml";
		URL url = new URL(strURL);
		
		try {
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			String elastic_query = "";
			
			
			OutputStream out = httpConn.getOutputStream();
			out.write(elastic_query.getBytes("UTF-8"));
			out.flush();
			out.close();
			out = null;
			
			br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() UnknownHostException : "+ uhe.getMessage() );
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() FileNotFoundException : "+ fnfe.getMessage() );
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() Exception : "+ e.getMessage() );
		}
		
		String read = "";
		
		while ((read = br.readLine()) != null) {
			sb.append(read);			       
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));
					
		NodeList sysList = doc.getElementsByTagName("system");
		Node sysNode = null;
		int nodeSize = sysList.getLength();
		for (int i=0; i<nodeSize; i++) {
			Element tmp = (Element) sysList.item(i);
			
			 * elastic_query.xml에 사용자의 회사 코드로 작성 되어진 쿼리 목록을 가져온다.
			 * <system id="POSCO" name="POSCO" companyCode="30">
			 *     <query></query>
			 *     ....
			 * </system>
			 
			if (tmp.getAttribute("companyCode").equals(comCode)) {
				sysNode = sysList.item(i);
				break;
			}
		}
		
		
		 * elastic_query.xml에 회사 코드로 쿼리를 만들어 줘야 함
		 * <system id="POSCO" name="POSCO" companyCode="30"></system>
		 
		if (sysNode != null) {
			Element element = (Element) sysNode;				
			NodeList metaList = element.getElementsByTagName("meta");
			StringBuffer sb1 = new StringBuffer();
			for (int i=0; i<metaList.getLength(); i++) {
				Element tmp = (Element) metaList.item(i);
				if(sb1.length()>0) sb1.append(",");
				sb1.append(tmp.getAttribute("id"));
			}
			if(sb1.length()>0) rtn_metalist = sb1.toString();
		}
		
		logger.info("rtn_metalist="+rtn_metalist);
		logger.info("##### GETSYSTEMQUERYNODE END #####");
		
		return rtn_metalist;
	}*/
	
	
	/**
	 * elastic_query_PATT.xml에 작성 되어있는 쿼리 정보를 EngineQueryVO 객체에 담아 리턴한다,
	 * @return Node
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	protected synchronized String getSystemQueryInfo(String comCode,String systemID) throws Exception {
		logger.info("##### GETSYSTEMQUERYNODE START #####");
		String rtn_metalist = null;
		StringBuffer sb =  new StringBuffer();
		BufferedReader br = null;
		HttpURLConnection httpConn = null;
		   
		final String queryPath = "http://uswpes.posco.net:7091/pic/system_query";
		String fileName = "";
		if(systemID.contains("hrsearch")) fileName = "elastic_query.xml";
		else fileName = "elastic_query_"+systemID.toUpperCase()+".xml";
		
		String strURL = queryPath + "/" + fileName;
		URL url = new URL(strURL);
		
		try {
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			String elastic_query = "";
			
			
			OutputStream out = httpConn.getOutputStream();
			out.write(elastic_query.getBytes("UTF-8"));
			out.flush();
			out.close();
			out = null;
			
			br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() UnknownHostException : "+ uhe.getMessage() );
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() FileNotFoundException : "+ fnfe.getMessage() );
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ProvideIfActivity.getSystemQueryInfo() Exception : "+ e.getMessage() );
		}
		
		String read = "";
		
		while ((read = br.readLine()) != null) {
			sb.append(read);			       
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));
					
		NodeList sysList = doc.getElementsByTagName("system");
		Node sysNode = null;
		int nodeSize = sysList.getLength();
		for (int i=0; i<nodeSize; i++) {
			Element tmp = (Element) sysList.item(i);
			/*
			 * elastic_query.xml에 사용자의 회사 코드로 작성 되어진 쿼리 목록을 가져온다.
			 * <system id="POSCO" name="POSCO" companyCode="30">
			 *     <query></query>
			 *     ....
			 * </system>
			 */
			if (tmp.hasAttribute("companyCode")) {
				if(tmp.getAttribute("companyCode").equals(comCode)) {
					sysNode = sysList.item(i);
					break;
				}
			}else {
				sysNode = sysList.item(i);
			}
		}
		
		/*
		 * elastic_query.xml에 회사 코드로 쿼리를 만들어 줘야 함
		 * <system id="POSCO" name="POSCO" companyCode="30"></system>
		 */
		if (sysNode != null) {
			Element element = (Element) sysNode;				
			NodeList metaList = null;
			if(systemID.contains("hrsearch")) metaList =element.getElementsByTagName("meta");
			else metaList = element.getElementsByTagName("field");
			StringBuffer sb1 = new StringBuffer();
			for (int i=0; i<metaList.getLength(); i++) {
				Element tmp = (Element) metaList.item(i);
				if(sb1.length()>0) sb1.append(",");
				sb1.append(tmp.getAttribute("id"));
			}
			if(sb1.length()>0) rtn_metalist = sb1.toString();
		}
		
		logger.info("rtn_metalist="+rtn_metalist);
		logger.info("##### GETSYSTEMQUERYNODE END #####");
		
		return rtn_metalist;
	}
	
	//Error Message 200(byteLength)byte 로 자르기
	public String subStrb(String str, int byteLength , String type){
		
		int retLength = 0;
		int tempSize = 0;
		int asc;
		if(str == null || "".equals(str) || "null".equals(str)){
			str = null;
			return str;
		}
		
		
		String tmp = str;
		if(type.equals("error")){
			tmp = str.substring(str.indexOf("ORA"));
		} else{
			tmp=str;
		}
		
		int length = tmp.length();
				
		for(int i = 0 ; i < length; i++){
			asc = (int)tmp.charAt(i);
			if(asc>127){
				if(byteLength >= tempSize + 3){
					tempSize += 3;
					retLength++;
				} else{
					return tmp.substring(0, retLength); 
				}
			} else{
				if(byteLength > tempSize){
					tempSize++;
					retLength++;
				}
			}
		}
		
		return tmp.substring(0, retLength);
	}
}

