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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//import org.apache.log4j.Logger;
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


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat; 
import java.util.Date;



@Service
public class ChartService {
//	@Autowired
//	MonitoringMapper monitoringMapper;
	@Autowired
	ElasearchService elasearchService;
	
	@Autowired
	CodeService codeService;
	

	@Value("${server.running.flag}")
	private String server_running_flag = "";
	
//	@Value("${dev.search_engine}")
//	private String local_elasticsearch = "";
	
	@Value("${log.search_engine}")
	private String log_elasticsearch = "";
		
	@Value("${prd.search_engine}")
	private String prd_elasticsearch = "";
	
	private static Logger logger = Logger.getLogger(ChartService.class);
	
	
	/*
	 * 제안시스템 AI 성능
	 */
	public Map<String, Object> getRealTimeTransaction_Chart(Map<String, Object> params)
	{
		
		try {
//			System.out.println("@@@UTC@@ " + ESUtil.utcConvertToLocal("2020-07-17T08:55:44.053Z"));
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
		
//		{EVENT_ID=get_realtime_transaction, elaIP=http://10.132.57.72:9201/unfair-slave-manual-*/_search, indexId=unfair-slave-manual-*}
		System.out.println("this.elasticsearch >> " + this.log_elasticsearch);
		params.put("elaIP", "http://"+ this.log_elasticsearch +"/jean-ai-master-*/_search");
		params.put("indexId", "jean_ai_master-*");
//		params.put("elaIP", "http://"+ GlobalValues.getSearchEngineInfo() +"/unfair-slave-manual1-*/_search");
//		params.put("indexId", "unfair-slave-manual1-*");
		System.out.println("getRealTimeTransaction_Chart_params >> " +  params);
		
   try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
       
    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
				String s_delay_time = "";
				String a_delay_time = "";
				String timestamp = "";
	
				String session_delay_time = "";
				String ai_delay_time = "";
				String labels = "";
    		

	    	for(int i=0; i<jArr.size() ;i++)
	    		  {
	    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
	    			type = jArr.getJSONObject(i).getString("_type");
	    			id = jArr.getJSONObject(i).getString("_id"); //ID
	
	    			//DELAY_TIME 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("DELAY_TIME")){
	    				s_delay_time = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DELAY_TIME");
	    				session_delay_time += s_delay_time + ",";
	    			} else { 
	    				session_delay_time = "";
	    			     }
	    			
	    			//DELAY_TIME 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("AI_DELAY_TIME")){
	    				a_delay_time = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("AI_DELAY_TIME");
	    				ai_delay_time += a_delay_time + ",";
	    			} else { 
	    				ai_delay_time = "";
	    			     }
	    			
	    			//DELAY_TIME 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("TIMESTAMP")){
	    				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");
	    				labels += timestamp + ",";
	    			} else { //LAW_NAME 없을때는 초기화
	    				timestamp = "";
	    			     }
	       	  }


	    	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		Map<String, Object> map = new HashMap<>();
    		
    		if (jArr.size() >0) {
    				map.put("delay_time", session_delay_time.substring(0, session_delay_time.length()-1));
			    	map.put("ai_delay_time", ai_delay_time.substring(0, ai_delay_time.length()-1));
			  		map.put("label", labels.substring(0, labels.length()-1));
    			}  else {
	    			map.put("delay_time", "");
	    			map.put("ai_delay_time", "");
						map.put("label", "");
    			}

    		list.add(map);
    		results.put("datas", list);
    		System.out.println("getRealTimeTransaction_Chart_delay_time >> " +  results);
  		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	
	/*
	 * 제안시스템 AI 성능 (일별Report)
	 */
	public Map<String, Object> get_Daily_Usage_Transaction_Chart(Map<String, Object> params)
	{
		
		try {
//			System.out.println("@@@UTC@@ " + ESUtil.utcConvertToLocal("2020-07-17T08:55:44.053Z"));
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
		
//		{EVENT_ID=get_realtime_transaction, elaIP=http://10.132.57.72:9201/unfair-slave-manual-*/_search, indexId=unfair-slave-manual-*}
		System.out.println("\n\nthis.elasticsearch >> " + this.log_elasticsearch);
		params.put("elaIP", "http://"+ this.log_elasticsearch +"/ai-jean-server-log-*,unfair-slave-manual-*/_search");
		params.put("indexId", "ai-jean-server-log-*,unfair-slave-manual-*");
		System.out.println("get_Daily_Usage_Transaction_Chart >> " +  params);
		
		try
    	{
				String index=""; //인덱스
    		String type="";
    		String id=""; //ID
    		String system_groups = "";
    		String system_groups_code = "";
    		String timestamp = "";
				String labels = "";
				
				// *******************************************************************
				// *******************************************************************
				// *******************************************************************
				// group by system_ids
				params.put("query_type", "category");
				JSONObject  jsonObj_ids = elasearchService.handleEla(params);
				JSONArray jArr_ids =jsonObj_ids.getJSONObject("aggregations").getJSONObject("SYSTEM_TYPE").getJSONObject("TYPE").getJSONArray("buckets");
			
				Map<String, Object> system_id_map = new HashMap<>();
				Map<String, String[]> system_id_map_data = new HashMap<>();
				
		   ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	     Map<String, Object> map = new HashMap<>();
				
				if (jArr_ids.size() > 0) 
				{
					for(int i=0; i<jArr_ids.size() ;i++)
					{
	//						System.out.println("@@sys_id_length => " + jArr_ids.size());
	//						System.out.println("@@ids@@ => " + jArr_ids.getJSONObject(i).getString("key"));
							system_groups += jArr_ids.getJSONObject(i).getString("key")
									.replace("POSCOMPLY_LOG","약관공정화시스템")
									.replace("AI_JEAN_LOG","제안AI검출시스템(포스코)")
									.replace("30","(포스코)")
									.replace("17","(포스코건설)")
									.replace("02","(포스코케미컬)")
									 +  "!";
							
							system_groups_code += jArr_ids.getJSONObject(i).getString("key") + "!";
							
							system_id_map.put(jArr_ids.getJSONObject(i).getString("key"), "");
							String[] initial = new String[24];
							for(int zero=0; zero < initial.length; zero++)
								initial[zero] = "0,";
							system_id_map_data.put(jArr_ids.getJSONObject(i).getString("key"), initial);
						}
					}
					else {
						System.out.println("NO");
					}

//					System.out.println("system_id_map_data" + system_id_map_data);
				
				// # X축
				for(int i=0; i<24 ;i++) 
				{
						timestamp = String.format("%02d:00", i);
						labels += timestamp + ",";
				 }
					    		
    		
				if (jArr_ids.size() > 0) 
    			{
			//				@system_id_map@ => {POSCOMPLY_LOG_17=, POSCOMPLY_LOG_30=, AI_JEAN_LOG=}
//							System.out.println("@1 system_id_map@ => " + system_id_map);
							
							// *******************************************************************
							// *******************************************************************
							// *******************************************************************
							// group by system_each_datas
							params.put("query_type", "aggs_data");
			    		JSONObject  jsonObj = elasearchService.handleEla(params);
			    		
			//    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
			    		JSONArray jArr =jsonObj.getJSONObject("aggregations").getJSONObject("metrics_by_data").getJSONArray("buckets");
			       
//			    			System.out.println("@1 system_id_map_data@ => " + system_id_map_data.get("AI_JEAN_LOG")[1]);

			    	 for(int i=0; i<jArr.size() ;i++)
			    		  {
			
		//	    		 		if ( jArr.getJSONObject(i).has("key_as_string")) {
		//	    		 			  timestamp = jArr.getJSONObject(i).getString("key_as_string");
		//	    		 			  labels += timestamp + ",";
		//	    		 			} else {
		//	    		 				timestamp = ",";
		//	    		 			}
			    		 
		//	    		 		System.out.println("key_as_string " + jArr.getJSONObject(i).getString("key_as_string"));
			    				
			    				JSONArray jArr_Monty_Aggs = jArr.getJSONObject(i).getJSONObject("SYSTEM_TYPE")
			    						.getJSONObject("TYPE").getJSONArray("buckets");
			    				
			    				if (jArr_Monty_Aggs.size() > 0)
			    			 		{
				    					Iterator<String> keys = system_id_map.keySet().iterator();
			    						while (keys.hasNext())
			    							{
			    									  String key = keys.next();
			    										for(int j=0; j<jArr_Monty_Aggs.size();j++) {
			    											if (key.equals(jArr_Monty_Aggs.getJSONObject(j).getString("key"))) {
			    													system_id_map_data.get(key)[Integer.parseInt(jArr.getJSONObject(i).getString("key_as_string"))] = jArr_Monty_Aggs.getJSONObject(j).getString("doc_count") + ",";
			    													system_id_map_data.put(key, system_id_map_data.get(key));
			    												}
			    										}
			    						}
			    			 		}	 
		 	       	  	}
			    	 
//			    	for(int test=0; test < system_id_map_data.get("AI_JEAN_LOG").length; test++)
//			    		System.out.println("@2 system_id_map_data@ => " + test +"=> " + system_id_map_data.get("AI_JEAN_LOG")[test]);
		
					
		    		String Key_Data = "";
		    		for (int h=0; h < system_groups_code.split("!").length; h++) {
		//    				Key_Data += system_id_map.get(system_groups_code.split("!")[h]) + "!";
		    				for (int h1=0; h1 < system_id_map_data.get(system_groups_code.split("!")[h]).length; h1++) {
		    					Key_Data += system_id_map_data.get(system_groups_code.split("!")[h])[h1];
		    					}
		    					
		    				Key_Data += "!";
//		    				System.out.println("KEY_DATA " + Key_Data);
		    			}
		    		
		    					
		    		if (jArr.size() >0) {
		//    					map.put("system_groups", "A!B!C!");
		    					map.put("system_groups", system_groups);
		    					map.put("system_groups_code", system_groups_code);
		    					map.put("system_groups_data", Key_Data);
		//    					map.put("system_groups_data", "1.79,1.52,0.79,0.29,0.8,0.4!0.79,0.52,0.79,0.29,3.8,0.4!0.79,0.52,0.79,0.29,0.8,0.4!0.79,0.52,0.79,0.29,0.8,0.4!0.79,0.52,0.79,0.29,0.8,0.4!0.79,0.52,0.79,0.29,0.8,0.4");
		    					map.put("label", labels.substring(0, labels.length()-1));
		    			}  
		    		else {
			    			map.put("system_groups", "");
			    			map.put("system_groups_code", "");
			    			map.put("system_groups_data", "");
			    			map.put("label", labels.substring(0, labels.length()-1));
		    			}
    			}
		    else {
			    	map.put("system_groups", "");
	    			map.put("system_groups_code", "");
	    			map.put("system_groups_data", "");
	    			map.put("label", labels.substring(0, labels.length()-1));
		    		}
    		

    		list.add(map);
    		results.put("datas", list);
    		System.out.println("get_Daily_Usage_Transaction_Chart >> " +  results);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	/*
	 * 제안시스템 AI 성능 (월별Report)
	 */
	public Map<String, Object> get_Monthly_Usage_Transaction_Chart(Map<String, Object> params)
	{
		
		try {
//			System.out.println("@@@UTC@@ " + ESUtil.utcConvertToLocal("2020-07-17T08:55:44.053Z"));
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
		
//		{EVENT_ID=get_realtime_transaction, elaIP=http://10.132.57.72:9201/unfair-slave-manual-*/_search, indexId=unfair-slave-manual-*}
		System.out.println("this.elasticsearch >> " + this.log_elasticsearch);
		params.put("elaIP", "http://"+ this.log_elasticsearch +"/ai-jean-server-log-*,unfair-slave-manual-*/_search");
		params.put("indexId", "ai-jean-server-log-*,unfair-slave-manual-*");
		System.out.println("get_Monthly_Usage_Transaction_Chart >> " +  params);
		
		try
    	{
				String index=""; //인덱스
    		String type="";
    		String id=""; //ID
    		String system_groups = "";
    		String system_groups_code = "";
    		String timestamp = "";
				String labels = "";
				
				// *******************************************************************
				// *******************************************************************
				// *******************************************************************
				// group by system_ids
				params.put("query_type", "category");
				JSONObject  jsonObj_ids = elasearchService.handleEla(params);
				JSONArray jArr_ids =jsonObj_ids.getJSONObject("aggregations").getJSONObject("SYSTEM_TYPE").getJSONObject("TYPE").getJSONArray("buckets");
			
				Map<String, Object> system_id_map = new HashMap<>();
				
				for(int i=0; i<jArr_ids.size() ;i++)
				{
//						System.out.println("@@sys_id_length => " + jArr_ids.size());
//						System.out.println("@@ids@@ => " + jArr_ids.getJSONObject(i).getString("key"));
						system_groups += jArr_ids.getJSONObject(i).getString("key")
								.replace("POSCOMPLY_LOG","약관공정화시스템")
								.replace("AI_JEAN_LOG","제안AI검출시스템(포스코)")
								.replace("30","(포스코)")
								.replace("17","(포스코건설)")
								.replace("02","(포스코케미컬)")
								 +  "!";
						
						system_groups_code += jArr_ids.getJSONObject(i).getString("key") + "!";
						
						system_id_map.put(jArr_ids.getJSONObject(i).getString("key"), "");
				}
//				@system_id_map@ => {POSCOMPLY_LOG_17=, POSCOMPLY_LOG_30=, AI_JEAN_LOG=}
//				System.out.println("@1 system_id_map@ => " + system_id_map);
				
				// *******************************************************************
				// *******************************************************************
				// *******************************************************************
				// group by system_each_datas
				params.put("query_type", "aggs_data");
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
//    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		JSONArray jArr =jsonObj.getJSONObject("aggregations").getJSONObject("metrics_by_data").getJSONArray("buckets");
       
//    		Iterator<String> keys = system_id_map.keySet().iterator();
//             while (keys.hasNext()){
//                 String key = keys.next();
//                 System.out.println("KEY : " + key); // Key2 , Key1, Key4, Key3, Key5
//             	}

	    	 for(int i=0; i<jArr.size() ;i++)
	    		  {
//	    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
//	    			type = jArr.getJSONObject(i).getString("_type");
//	    			id = jArr.getJSONObject(i).getString("_id"); //ID
	
	    		 		if ( jArr.getJSONObject(i).has("key_as_string")) {
	    		 			  timestamp = jArr.getJSONObject(i).getString("key_as_string");
//	    		 			  System.out.println("###" +  params.get("start_date") + "--"+ timestamp);
	    		 			  labels += timestamp + ",";
	    		 			} else {
	    		 				timestamp = ",";
	    		 			}
	    				
	    				JSONArray jArr_Monty_Aggs = jArr.getJSONObject(i).getJSONObject("SYSTEM_TYPE")
	    						.getJSONObject("TYPE").getJSONArray("buckets");
	    				
	    				if (jArr_Monty_Aggs.size() > 0)
	    			 		{
		    					Iterator<String> keys = system_id_map.keySet().iterator();
	    						while (keys.hasNext())
	    							{
	    									String key = keys.next();
	    									String this_value = system_id_map.get(key) + "0,";
	    										for(int j=0; j<jArr_Monty_Aggs.size();j++) {
	    											if (key.equals(jArr_Monty_Aggs.getJSONObject(j).getString("key"))) {
	    												this_value = system_id_map.get(key) + 
	    			    										 jArr_Monty_Aggs.getJSONObject(j).getString("doc_count") + ",";
	    												system_id_map.put(key, this_value);
	    												}
	    										}
	    									system_id_map.put(key, this_value);
	    							}
	    			 		}	 
	    				else 
	    				   {
	    					  Iterator<String> keys = system_id_map.keySet().iterator();
	    						while (keys.hasNext())
	    							{
	    									String key = keys.next();
	    		             system_id_map.put(key, system_id_map.get(key) + "0,");
	    							}
	    					}
	    				
//	    				System.out.println("@2 system_id_map@ => " + system_id_map);
 	       	  	}

	    	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		Map<String, Object> map = new HashMap<>();
    		
    		String Key_Data = "";
    		for (int h=0; h < system_groups_code.split("!").length; h++)
    				Key_Data += system_id_map.get(system_groups_code.split("!")[h]) + "!";
    		
    		if (jArr.size() >0) {
//    					map.put("system_groups", "A!B!C!");
    					map.put("system_groups", system_groups);
    					map.put("system_groups_code", system_groups_code);
    					map.put("system_groups_data", Key_Data);
//    					map.put("system_groups_data", "1.79,1.52,0.79,0.29,0.8,0.4!0.79,0.52,0.79,0.29,3.8,0.4!0.79,0.52,0.79,0.29,0.8,0.4!0.79,0.52,0.79,0.29,0.8,0.4!0.79,0.52,0.79,0.29,0.8,0.4!0.79,0.52,0.79,0.29,0.8,0.4");
    					map.put("label", labels.substring(0, labels.length()-1));
    			}  
    		else {
	    			map.put("system_groups", "");
	    			map.put("system_groups_code", "");
	    			map.put("system_groups_data", "");
	    			map.put("label", "");
    			}

    		list.add(map);
    		results.put("datas", list);
    		System.out.println("getRealTimeTransaction_Chart_delay_time >> " +  results);
  		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
	
	/*
	 * 그룹사 약관공정화 시스템 AI 성능
	 */
	public Map<String, Object> get_POSCOMPLY_RealTimeTransaction_Chart(Map<String, Object> params)
	{
		
		try {
//			System.out.println("@@@UTC@@ " + ESUtil.utcConvertToLocal("2020-07-17T08:55:44.053Z"));
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, Object> results = new HashMap<>();
//		params.put("length", (Integer) params.get("length"));
//		params.put("start", (Integer) params.get("start"));
		
		
		System.out.println("this.local_elasticsearch >> " + this.log_elasticsearch);
		params.put("elaIP", "http://"+ this.log_elasticsearch +"/unfair-slave-manual-*/_search");
		params.put("indexId", "unfair-slave-manual-*");
		params.put("company_code", "17");
		System.out.println("get_POSCOMPLY_RealTimeTransaction_Chart >> " +  params);
		
   try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
       
    		String index=""; //인덱스
    		String type="";
    		String id=""; //ID
				String delay_time = "";
				String timestamp = "";
	
				String data = "";
				String labels = "";
    		

	    	for(int i=0; i<jArr.size() ;i++)
	    		  {
	    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
	    			type = jArr.getJSONObject(i).getString("_type");
	    			id = jArr.getJSONObject(i).getString("_id"); //ID
	
	    			//DELAY_TIME 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("DELAY_TIME")){
	    				delay_time = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DELAY_TIME");
	    				data += delay_time + ",";
	    			} else { //LAW_NAME 없을때는 초기화
	    				delay_time = "";
	    			     }
	    			
	    			//DELAY_TIME 존재 체크
	    			if( jArr.getJSONObject(i).getJSONObject("_source").has("TIMESTAMP")){
	    				timestamp = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("TIMESTAMP");
	    				labels += timestamp + ",";
	    			} else { //LAW_NAME 없을때는 초기화
	    				timestamp = "";
	    			     }
	       	  }
	    	
	    	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    		Map<String, Object> map = new HashMap<>();
    		
    		if (jArr.size() >0) {
    			map.put("data_enc", data.substring(0, data.length()-1));
	    		map.put("label", labels.substring(0, labels.length()-1));
    			}
    		else {
    			map.put("data_enc", "");
    			map.put("label", "");
    		}

    		System.out.println("get_POSCOMPLY_RealTimeTransaction_Chart_delay_time >> " +  delay_time);

    		list.add(map);
    		results.put("datas", list);
    		System.out.println("get_POSCOMPLY_RealTimeTransaction_Chart_return >> " +  results);
//    		getRealTimeTransaction_Chart_return >> {datas=[{data=2.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99, label=06-18 09:48:33,06-18 11:14:55,06-18 11:15:03,06-18 11:25:00,06-18 12:08:55,06-18 12:09:22,06-18 12:11:55,06-18 12:12:10,06-18 12:12:50,06-18 12:14:00,06-18 12:14:56,06-18 12:17:29,06-18 12:18:15,06-18 12:24:23,06-18 12:26:50,06-18 12:27:27,06-18 12:27:53,06-18 12:28:47,06-18 12:31:10,06-18 12:34:10,06-18 12:44:45,06-18 12:45:51,06-18 12:47:06,06-18 12:47:10,06-18 12:48:04,06-18 12:49:54,06-18 12:52:35,06-18 13:21:51,06-18 13:22:22,06-18 13:23:56,06-18 13:24:34,06-18 15:23:52,06-18 15:24:14,06-18 15:25:19,06-18 15:26:21,06-18 15:32:47,06-18 15:32:59,06-18 15:34:42,06-18 15:35:19,06-18 15:35:27,06-18 15:36:59,06-18 15:37:04,06-18 15:37:09,06-18 15:37:31,06-18 23:06:25,06-18 23:06:28,06-18 23:06:46,06-18 23:07:03,06-18 23:08:10,06-18 23:08:16,06-18 23:08:24,06-18 23:08:28,06-22 18:59:09,06-22 18:59:11,06-23 10:40:19,06-23 13:35:59,06-23 13:36:59,06-23 13:42:13,06-23 15:34:23,06-23 15:37:08,06-23 15:38:54,06-23 15:48:45,06-24 11:07:24,06-24 13:59:48,06-24 14:00:06,06-24 14:00:09,06-24 14:01:51,06-24 14:01:55,06-26 16:47:20,06-26 17:39:37,06-29 14:53:03}]}
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	
}

