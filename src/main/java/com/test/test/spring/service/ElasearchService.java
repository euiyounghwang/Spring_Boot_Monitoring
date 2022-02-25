package com.test.test.spring.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
//import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Service
public class ElasearchService {	
	@Autowired
	CodeService codeService;
	
	private static Logger logger = Logger.getLogger(MonitoringService.class);
	
	private static final String Exclude_User = "\"pd292816\",\"292816\",\"pd271610\",\"271610\",\"pd294562\",\"294562\",\"pd0a8762\",\"1\",\"2\",\"S13324\"";
	
	public JSONObject handleElaCommon(Map<String, Object> params){
		JSONObject json_result = null;
		String url = params.get("elaIP").toString();
		URL connect;
		OutputStream os = null;
		StringBuffer data = new StringBuffer();

		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpEntity resEntity = null;
		try {
//			System.out.println(url);
			HttpGet request = new HttpGet(url);
//			request.setHeader("Authorization", "Basic " + Base64Coder.encodeString("elastic:gsaadmin"));
			request.addHeader("Authorization", "Basic ZWxhc3RpYzpnc2FhZG1pbg==");
			request.addHeader("content-type", "application/json");
			request.addHeader("Accept", "application/json");

			// request.setEntity(params);
			HttpResponse response_elastic = httpClient.execute(request);
			resEntity = response_elastic.getEntity();
			result = EntityUtils.toString(resEntity);
			
			json_result = JSONObject.fromObject(result);
//			System.out.println(result);
			
			// handle response here...
		} catch (Exception ex) {
			// handle exception here
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return json_result;
	}
	
	
	public String handleElaCommonString(Map<String, Object> params){
		JSONObject json_result = null;
		String url = params.get("elaIP").toString();
		URL connect;
		OutputStream os = null;
		StringBuffer data = new StringBuffer();

		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpEntity resEntity = null;
		try {
//			System.out.println(url);
			HttpGet request = new HttpGet(url);
//			request.setHeader("Authorization", "Basic " + Base64Coder.encodeString("elastic:gsaadmin"));
			request.addHeader("Authorization", "Basic ZWxhc3RpYzpnc2FhZG1pbg==");
			request.addHeader("content-type", "application/json");
			request.addHeader("Accept", "application/json");

			// request.setEntity(params);
			HttpResponse response_elastic = httpClient.execute(request);
			resEntity = response_elastic.getEntity();
			result = EntityUtils.toString(resEntity);
			
//			json_result = JSONObject.fromObject(result);
//			System.out.println(result);
			
			// handle response here...
		} catch (Exception ex) {
			// handle exception here
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return result;
	}
	
	public JSONObject handleEla(Map<String, Object> params){
		JSONObject json_result = null;
		String url = params.get("elaIP").toString();
		URL connect;
		OutputStream os = null;
		StringBuffer data = new StringBuffer();

		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		
		try {
			StringBuilder temp_sb = new StringBuilder();
			 
			connect = new URL(url);
			httpConn = (HttpURLConnection)connect.openConnection();
			httpConn.setConnectTimeout(50000);
			httpConn.setRequestProperty("Authorization", "Basic ZWxhc3RpYzpnc2FhZG1pbg==");
//			httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			httpConn.setRequestProperty("Content-Type", "application/x-ndjson; charset=UTF-8");
			httpConn.setRequestProperty("size", params.get("length").toString());
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			
			//request write
			os = httpConn.getOutputStream();

			System.out.println("######################## 검색조건 Param ######################");
			
			String elajsonParam = "";
			
			if(params.get("EVENT_ID").toString().equals("findWasServerLog")) {
				elajsonParam = getWasServerLog(params); 
			} else if(params.get("EVENT_ID").toString().equals("findQueueServerLog")) {
				elajsonParam = getQueueServerLog(params); 
			} else if(params.get("EVENT_ID").toString().equals("findFeedServerLog")) {
				elajsonParam = getFeedServerLog(params); 
			} else if(params.get("EVENT_ID").toString().equals("findEcmServerLog")) {
				elajsonParam = getEcmServerLog(params); 
			} else if(params.get("EVENT_ID").toString().equals("findServerList")) {
				elajsonParam = getServerList(params);
			} else if(params.get("EVENT_ID").toString().equals("getCodeList")) {
				elajsonParam = getServerList(params); 
			}else if(params.get("EVENT_ID").toString().equals("get_realtime_transaction")) {
//				# elasticsearch 7.9
				elajsonParam = getRealTimeTransaction_Chart(params); 
			}
			else if(params.get("EVENT_ID").toString().equals("findAIServerLog")) {
//				# elasticsearch 7.9
				elajsonParam = getAIServerLog(params); 
			}
			else if(params.get("EVENT_ID").toString().equals("findFeederServerLog")) {
				elajsonParam = getFeederServerLog(params); 
			}
			else if(params.get("EVENT_ID").toString().equals("get_poscomply_realtime_transaction")) {
//				# elasticsearch 7.9
				elajsonParam = get_POSCOMPLY_RealTimeTransaction_Chart(params); 
			}
			else if(params.get("EVENT_ID").toString().equals("get_monthly_usage_transaction")) {
//				# elasticsearch 7.9
				elajsonParam = get_Monthly_Usage_Transaction_Chart(params); 
			}
			else if(params.get("EVENT_ID").toString().equals("get_daily_usage_transaction")) {
//				# elasticsearch 7.9
				elajsonParam = get_Daily_Usage_Transaction_Chart(params); 
			}
			
			
			System.out.println("elajsonParam -> "+elajsonParam.replace(" ", ""));
			os.write(elajsonParam.getBytes("UTF-8"));

			os.flush();
			os.close();
			os = null;

			reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
//			json_result = JSONObject.fromObject(reader.readLine());
			
			String line;
			while (null != (line = reader.readLine())) {
			data.append(line);
		   }
			json_result = JSONObject.fromObject(JSONSerializer.toJSON(data.toString()));
						
			httpConn.disconnect();
			httpConn = null;

			temp_sb.append("\n**>> URLs to Ela >>>>>>>>>>>>  " + url);
			temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			temp_sb.append("\n**>> getElajsonParam >>>>>>>>>>>>>>>  " + elajsonParam.replace(" ", ""));
			temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			temp_sb.append("\n**>> Json Result >>>>>>>>>>>>>>>  "+json_result);
			temp_sb.append("\n**>> data Result >>>>>>>>>>>>>>>  "+data);
			temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

			logger.info(temp_sb.toString());

//			if(params.get("EVENT_ID").toString().equals("get_realtime_transaction")
//					|| params.get("EVENT_ID").toString().equals("findAIServerLog")
//					|| params.get("EVENT_ID").toString().equals("get_poscomply_realtime_transaction")
//					|| params.get("EVENT_ID").toString().equals("get_monthly_usage_transaction")
//					|| params.get("EVENT_ID").toString().equals("get_daily_usage_transaction")
//					|| params.get("EVENT_ID").toString().equals("findServerList") 
//					|| params.get("EVENT_ID").toString().equals("findWasServerLog") 
//					)
//			{
//				System.out.println("전체개수[7.x]:"+Integer.parseInt(json_result.getJSONObject("hits").getJSONObject("total").getString("value")));
//			}
//			else {
//				System.out.println("전체개수:"+Integer.parseInt(json_result.getJSONObject("hits").getString("total")));
//			}
			
			System.out.println("전체개수[7.x]:"+Integer.parseInt(json_result.getJSONObject("hits").getJSONObject("total").getString("value")));
					
		   System.out.println(data);
		
		 } catch( Exception ee) {
	    	ee.printStackTrace();
	    	logger.error("HttpNetworkException : " + ee.getMessage());
	    } finally {
            if (os != null) {
                try {
                	os.close();
                	os = null;
                } catch (IOException e) {
                    logger.error(e.getMessage(), e.getCause());
                }
            }

			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					logger.error(e.getMessage(), e.getCause());
				}
			}

			if (httpConn != null) {
				try {
					httpConn.disconnect();
					httpConn = null;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
	    }
		
		return json_result;
		
	}
	

	/**
	 * [쿼리생성]getRealTimeTransaction_Chart 로그 조회
	 * [쿼리생성]TrainData 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getRealTimeTransaction_Chart(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);
		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";
			
			System.out.println("@@getRealTimeTransaction_Chart >> Query >> " + String.valueOf(message));
			System.out.println("@@getRealTimeTransaction_Chart >> Params >> " + params);
			
			st.append("{");
			st.append("\"track_total_hits\": true,");
			st.append("\"_source\": [\"DELAY_TIME\", \"AI_DELAY_TIME\", \"TIMESTAMP\"],");
			
			st.append("   \"query\": {");
			st.append("     \"bool\": {");
			st.append("       \"must\": [");
			st.append("         {");
			st.append("               \"range\": {");
			st.append("                 \"TIMESTAMP\": {");
			st.append("                   \"gte\": \"" + params.get("start_date") + "\",");
			st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
			st.append("                 }");
			st.append("               }");
			st.append("         }");
			st.append("       ]");
			st.append("       }");
			st.append(" },");


			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }]");
			st.append(" }");
			
			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getWasServerLog");
			logger.error("getWasServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	

	/**
	 * [쿼리생성]AI 서버 로그 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getAIServerLog(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";
			
			st.append("{");
			st.append("\"track_total_hits\": true,");
			st.append("   \"query\": {");
			st.append("     \"bool\": {");

			//검색어
			if( !"".equals(message) )
			{
				st.append("       \"must\": [");
				st.append("         {");
				st.append("           \"query_string\": {");
				st.append("             \"query\": \"" + message + "\", ");
				st.append("             \"lenient\": true,");
				st.append("             \"default_operator\": \"AND\",");
				st.append("             \"fields\": [");
				st.append("               \"*\"");
				st.append("             ]");
				st.append("           }");
				st.append("         }");
				st.append("       ],");
			}
			
			st.append("       \"filter\": {");
			st.append("         \"bool\": {");
			st.append("           \"must\": [");

			
			//로그레벨
			String pLogLevel = params.get("P_LOG_LEVEL").toString(); 
			if( !"".equals(pLogLevel) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"LOG_LEVEL\": [");
				st.append("                   \"" +pLogLevel + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}
			
			String type = params.get("system_id").toString(); 
			if( !"".equals(type) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"TYPE.keyword\": [");
				st.append("                   \"" +type + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}

		

			//시작일 종료일 추가
			String startDate = params.get("START_DATE").toString(); 
			String endDate = params.get("END_DATE").toString(); 

			//시작일
			if( !"".equals(startDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"gte\": \""+ startDate +"\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             },");
			}

			//종료일
			if( !"".equals(endDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"lte\": \""+endDate + "\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             }");
			}

			st.append("           ]");
			st.append("         }");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"highlight\": {");
			st.append("     \"require_field_match\": true,");
			st.append("     \"order\": \"score\",");
			st.append("     \"pre_tags\": [");
			st.append("       \"<b>\"");
			st.append("     ],");
			st.append("     \"post_tags\": [");
			st.append("       \"</b>\"");
			st.append("     ],");
			st.append("     \"fields\": {");
			st.append("       \"*\": {");
			st.append("         \"number_of_fragments\": 1,");
			st.append("         \"type\": \"plain\",");
			st.append("         \"fragment_size\": 150");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       },");
			st.append("       \"KEY.keyword\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }");
			st.append("   ]");
			st.append(" }");

			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getAIServerLog");
			logger.error("getAIServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	/**
	 * [쿼리생성]Feed서버 로그 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getFeederServerLog(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";

			st.append("{");
			st.append("   \"query\": {");
			st.append("     \"bool\": {");

			//검색어
			if( !"".equals(message) )
			{
				st.append("       \"must\": [");
				st.append("         {");
				st.append("           \"query_string\": {");
				st.append("             \"query\": \"" + message + "\", ");
				st.append("             \"lenient\": true,");
				st.append("             \"default_operator\": \"AND\",");
				st.append("             \"fields\": [");
				st.append("               \"*\"");
				st.append("             ]");
				st.append("           }");
				st.append("         }");
				st.append("       ],");
			}

			st.append("       \"filter\": {");
			st.append("         \"bool\": {");
			st.append("           \"must\": [");

			//로그레벨
			String pLogLevel = params.get("P_LOG_LEVEL").toString(); 
			if( !"".equals(pLogLevel) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"LOG_LEVEL\": [");
				st.append("                   \"" +pLogLevel + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}

			String type = params.get("system_id").toString(); 
			if( !"".equals(type) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"TYPE\": [");
				st.append("                   \"" +type + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}


			//시작일 종료일 추가
			String startDate = params.get("START_DATE").toString(); 
			String endDate = params.get("END_DATE").toString(); 

			
			//시작일
			if( !"".equals(startDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"gte\": \""+ startDate +"\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             },");
			}

			//종료일
			if( !"".equals(endDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"lte\": \""+endDate + "\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             }");
			}			
			
			if(st.toString().endsWith(","))
				st.delete(st.toString().length()-1, st.toString().length());

			st.append("           ]");
			st.append("         }");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"highlight\": {");
			st.append("     \"require_field_match\": true,");
			st.append("     \"order\": \"score\",");
			st.append("     \"pre_tags\": [");
			st.append("       \"<b>\"");
			st.append("     ],");
			st.append("     \"post_tags\": [");
			st.append("       \"</b>\"");
			st.append("     ],");
			st.append("     \"fields\": {");
			st.append("       \"*\": {");
			st.append("         \"number_of_fragments\": 1,");
			st.append("         \"type\": \"plain\",");
			st.append("         \"fragment_size\": 150");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }");
			st.append("   ]");
			st.append(" }");

			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getFeederServerLog");
			logger.error("getFeederServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	/**
	 * [쿼리생성]get_POSCOMPLY_RealTimeTransaction_Chart 로그 조회
	 * [쿼리생성]TrainData 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String get_POSCOMPLY_RealTimeTransaction_Chart(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);
		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";
			
			System.out.println("@@get_POSCOMPLY_RealTimeTransaction_Chart >> Query >> " + String.valueOf(message));
			System.out.println("@@get_POSCOMPLY_RealTimeTransaction_Chart >> Params >> " + params);
			
			st.append("{");
			st.append("\"_source\": [\"DELAY_TIME\", \"TIMESTAMP\"],");
			
			st.append("   \"query\": {");
			st.append("     \"bool\": {");
			st.append("       \"must\": [");
			st.append("         {");
			st.append("               \"terms\": {");
			st.append("                 \"COMPANY_CODE\": [");
			st.append("                   \"" + params.get("company_code") + "\"");
			st.append("                 ]");
			st.append("               }");
			st.append("         },");
			st.append("         {");
			st.append("               \"terms\": {");
			st.append("                 \"SYSTEM_ID\": [");
			st.append("                   \"LAW\"");
			st.append("                 ]");
			st.append("               }");
			st.append("         },");
			st.append("         {");
			st.append("               \"terms\": {");
			st.append("                 \"EXTRACT_UNIQUE_ID\": [");
			st.append("                   \"000001\"");
			st.append("                 ]");
			st.append("               }");
			st.append("         },");
			st.append("         {");
			st.append("               \"range\": {");
			st.append("                 \"TIMESTAMP\": {");
			st.append("                   \"gte\": \"" + params.get("start_date") + "\",");
			st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
			st.append("                 }");
			st.append("               }");
			st.append("         }");
			st.append("       ]");
			st.append("       }");
			st.append(" },");

//			//시작일 종료일 추가
//			String startDate = params.get("START_DATE").toString(); 
//			String endDate = params.get("END_DATE").toString(); 
//
//			//시작일
//			if( !"".equals(startDate) )
//			{
//				st.append("             {");
//				st.append("               \"range\": {");
//				st.append("                 \"TIMESTAMP\": {");
//				st.append("                   \"gte\": \""+ startDate +"\",");
//				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
//				st.append("                 }");
//				st.append("               }");
//				st.append("             },");
//			}
//
//			//종료일
//			if( !"".equals(endDate) )
//			{
//				st.append("             {");
//				st.append("               \"range\": {");
//				st.append("                 \"TIMESTAMP\": {");
//				st.append("                   \"lte\": \""+endDate + "\",");
//				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
//				st.append("                 }");
//				st.append("               }");
//				st.append("             }");
//			}

//			st.append("           ]");
//			st.append("         }");
//			st.append("       }");
//			st.append("     }");
//			st.append("   },");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"asc\"");
			st.append("       }");
			st.append("     }]");
			st.append(" }");
			
			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getWasServerLog");
			logger.error("getWasServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	/**
	 * [쿼리생성]get_Monthly_Usage_Transaction_Chart 로그 조회
	 * [쿼리생성]
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String get_Daily_Usage_Transaction_Chart(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);
		try
		{
			System.out.println("@query_type => " + params.get("query_type"));
			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date s_date = sdf.parse(String.valueOf(params.get("start_date")));
			long start_mileseconds = s_date.getTime() + 86400000;
//			Date e_date = sdf.parse(String.valueOf(params.get("end_date")));
//			long end_mileseconds = e_date.getTime();
			
			System.out.println("@@get_Daily_Usage_Transaction_Chart_MakeQuery " 
					+ String.valueOf(params.get("start_date")) + "->" + String.valueOf(start_mileseconds) + '\t'
//					+ String.valueOf(params.get("end_date")) + "->" + String.valueOf(end_mileseconds)
						);
			
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";
			
			System.out.println("@@get_Daily_Usage_Transaction_Chart >> Query >> " + String.valueOf(message));
			System.out.println("@@get_Daily_Usage_Transaction_Chart >> Params >> " + params);
			
			st.append("{");
			st.append("\"track_total_hits\": true,");
			st.append("   \"query\": {");
			st.append("     \"bool\": {");
			st.append("       \"must\": [");
			st.append("         {");
			st.append("               \"range\": {");
			st.append("                 \"TIMESTAMP\": {");
			st.append("                   \"gte\": \"" + params.get("start_date") + " 00:00:00\",");
			st.append("                   \"lte\": \"" + params.get("start_date") + " 23:59:59\",");
			st.append("                   \"format\": \"yyyy-MM-dd HH:mm:ss\"");
			st.append("                 }");
			st.append("               }");
			st.append("         },");
			st.append("         {");
			st.append("     				\"bool\": {");
			st.append("       					\"should\": [");
			st.append("         					{");
			st.append("         						\"wildcard\":");
			st.append("         							{");
			st.append("         								\"KEY\":");
			st.append("         									{");
			st.append("         											\"value\": \"*000001\"");
			st.append("         									}");
			st.append("         						  }");
			st.append("         					},");
			st.append("         					{");
			st.append("         						\"terms\":");
			st.append("         							{");
			st.append("         									\"EXTRACT_UNIQUE_ID\": [");
			st.append("         											\"000001\"]");
			st.append("         						  }");
			st.append("         					}");
			st.append("       				]");
			st.append("         		}");
			st.append("         }");
			st.append("       ],");
			st.append("       \"must_not\": [");
			st.append("         {");
			st.append("     				\"bool\": {");
			st.append("       					\"should\": [");
			st.append("         					{");
			st.append("         						\"terms\":");
			st.append("         							{");
			st.append("         									\"SSO_LOGIN_ID.keyword\": [");
			st.append(																	Exclude_User);
			st.append("         											]");
			st.append("         						  }");
			st.append("         					},");
			st.append("         					{");
			st.append("         						\"terms\":");
			st.append("         							{");
			st.append("         									\"LOGIN_ID\": [");
			st.append(																	Exclude_User);
			st.append("         											]");
			st.append("         						  }");
			st.append("         					}");
			st.append("       				]");
			st.append("         		}");
			st.append("         }");
			st.append("       ]");
			st.append("    }");
			st.append(" 	},");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       },");
			st.append("       \"EXTRACT_UNIQUE_ID\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }],");
			
			if (params.get("query_type").equals("aggs_data")) {
				
				st.append("   \"aggs\": {");
				st.append("   			\"metrics_by_data\": {");
				st.append("   					\"date_histogram\": {");
				st.append("   								\"field\": \"TIMESTAMP\",");
				st.append("   								\"interval\": \"hour\",");
				st.append("   								\"min_doc_count\": 0,");
				st.append("   								\"extended_bounds\": {");
//				st.append("   										\"min\": " + start_mileseconds + ",");
				st.append("   										\"min\": " + start_mileseconds);
//				st.append("   										\"max\": " + end_mileseconds);
				st.append("       					},");
				st.append("   								\"order\": {");
				st.append("   										\"_key\": \"asc\"");
				st.append("       						},");
//				st.append("   								\"format\": \"yyyy-MM\"");
//				st.append("   								\"format\": \"yyyy-MM-dd HH:mm\"");
//				st.append("   								\"format\": \"HH:mm\"");
				st.append("   								\"format\": \"HH\"");
				st.append("   					},");
				st.append("   					\"aggs\": {");
				st.append("   							\"SYSTEM_TYPE\": {");
				st.append("   									\"sampler\": {");
				st.append("   											\"shard_size\": 150000");
				st.append("   									},");
				st.append("   									\"aggs\": {");
				st.append("   											\"TYPE\": {");
				st.append("   													\"terms\": {");
				st.append("   																\"field\": \"TYPE.keyword\"");
				st.append("   													}");
				st.append("   											}");
				st.append("   								  }");
				st.append("   							}");
				st.append("   					}");
				st.append("   			}");
				st.append("   	}");
			}
			
			else if (params.get("query_type").equals("category")) {
				st.append("		\"aggs\": {");
				st.append("   				\"SYSTEM_TYPE\": {");
				st.append("   							\"sampler\": {");
				st.append("   											\"shard_size\": 150000");
				st.append("   									},");
				st.append("   				\"aggs\": {");
				st.append("   							\"TYPE\": {");
				st.append("   										\"terms\": {");
				st.append("   												\"field\": \"TYPE.keyword\"");
				st.append("   													}");
				st.append("   								}");
				st.append("   						}");
				st.append("   				}");
				st.append("   	 }");
			}
			
			st.append(" 	}");
			
			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  get_Daily_Usage_Transaction_Chart");
			logger.error("get_Daily_Usage_Transaction_Chart Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	
	/**
	 * [쿼리생성]get_Monthly_Usage_Transaction_Chart 로그 조회
	 * [쿼리생성]
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String get_Monthly_Usage_Transaction_Chart(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);
		try
		{
			System.out.println("@query_type => " + params.get("query_type"));
			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date s_date = sdf.parse(String.valueOf(params.get("start_date")));
			long start_mileseconds = s_date.getTime();
			Date e_date = sdf.parse(String.valueOf(params.get("end_date")));
			long end_mileseconds = e_date.getTime();
			
			System.out.println("@@get_Monthly_Usage_Transaction_Chart_MakeQuery " 
					+ String.valueOf(params.get("start_date")) + "->" + String.valueOf(start_mileseconds) + '\t'
					+ String.valueOf(params.get("end_date")) + "->" + String.valueOf(end_mileseconds)
						);
			
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";
			
			System.out.println("@@get_Monthly_Usage_Transaction_Chart >> Query >> " + String.valueOf(message));
			System.out.println("@@get_Monthly_Usage_Transaction_Chart >> Params >> " + params);
			
			st.append("{");
			st.append("\"track_total_hits\": true,");
			st.append("   \"query\": {");
			st.append("     \"bool\": {");
			st.append("       \"must\": [");
			st.append("         {");
			st.append("               \"range\": {");
			st.append("                 \"TIMESTAMP\": {");
			st.append("                   \"gte\": \"" + params.get("start_date") + "\",");
			st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
			st.append("                 }");
			st.append("               }");
			st.append("         },");
			st.append("         {");
			st.append("     				\"bool\": {");
			st.append("       					\"should\": [");
			st.append("         					{");
			st.append("         						\"wildcard\":");
			st.append("         							{");
			st.append("         								\"KEY\":");
			st.append("         									{");
			st.append("         											\"value\": \"*000001\"");
			st.append("         									}");
			st.append("         						  }");
			st.append("         					},");
			st.append("         					{");
			st.append("         						\"terms\":");
			st.append("         							{");
			st.append("         									\"EXTRACT_UNIQUE_ID\": [");
			st.append("         											\"000001\"]");
			st.append("         						  }");
			st.append("         					}");
			st.append("       				]");
			st.append("         		}");
			st.append("         }");
			st.append("       ],");
			st.append("       \"must_not\": [");
			st.append("         {");
			st.append("     				\"bool\": {");
			st.append("       					\"should\": [");
			st.append("         					{");
			st.append("         						\"terms\":");
			st.append("         							{");
			st.append("         									\"SSO_LOGIN_ID.keyword\": [");
			st.append(																	Exclude_User);
			st.append("         											]");
			st.append("         						  }");
			st.append("         					},");
			st.append("         					{");
			st.append("         						\"terms\":");
			st.append("         							{");
			st.append("         									\"LOGIN_ID\": [");
			st.append(																	Exclude_User);
			st.append("         											]");
			st.append("         						  }");
			st.append("         					}");
			st.append("       				]");
			st.append("         		}");
			st.append("         }");
			st.append("       ]");
			st.append("    }");
			st.append(" 	},");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       },");
			st.append("       \"EXTRACT_UNIQUE_ID\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }],");
			
			if (params.get("query_type").equals("aggs_data")) {
				
				st.append("   \"aggs\": {");
				st.append("   			\"metrics_by_data\": {");
				st.append("   					\"date_histogram\": {");
				st.append("   								\"field\": \"TIMESTAMP\",");
				st.append("   								\"interval\": \"month\",");
				st.append("   								\"min_doc_count\": 0,");
				st.append("   								\"extended_bounds\": {");
				st.append("   										\"min\": " + start_mileseconds + ",");
				st.append("   										\"max\": " + end_mileseconds);
				st.append("       					},");
				st.append("   								\"order\": {");
				st.append("   										\"_key\": \"asc\"");
				st.append("       						},");
				st.append("   								\"format\": \"yyyy-MM\"");
				st.append("   					},");
				st.append("   					\"aggs\": {");
				st.append("   							\"SYSTEM_TYPE\": {");
				st.append("   									\"sampler\": {");
				st.append("   											\"shard_size\": 150000");
				st.append("   									},");
				st.append("   									\"aggs\": {");
				st.append("   											\"TYPE\": {");
				st.append("   													\"terms\": {");
				st.append("   																\"field\": \"TYPE.keyword\"");
				st.append("   													}");
				st.append("   											}");
				st.append("   								  }");
				st.append("   							}");
				st.append("   					}");
				st.append("   			}");
				st.append("   	}");
			}
			
			else if (params.get("query_type").equals("category")) {
				st.append("		\"aggs\": {");
				st.append("   				\"SYSTEM_TYPE\": {");
				st.append("   							\"sampler\": {");
				st.append("   											\"shard_size\": 150000");
				st.append("   									},");
				st.append("   				\"aggs\": {");
				st.append("   							\"TYPE\": {");
				st.append("   										\"terms\": {");
				st.append("   												\"field\": \"TYPE.keyword\"");
				st.append("   													}");
				st.append("   								}");
				st.append("   						}");
				st.append("   				}");
				st.append("   	 }");
			}
			
			st.append(" 	}");
			
			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  get_Monthly_Usage_Transaction_Chart");
			logger.error("get_Monthly_Usage_Transaction_Chart Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	
	
	/**
	 * [쿼리생성]WAS 서버 로그 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getWasServerLog(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";

			st.append("{");
			st.append("\"track_total_hits\": true,");
			st.append("\"_source\": [\"TYPE\",\"PATH\",\"MESSAGE\",\"HANDLER\",\"MAIL_ID\",\"LOG_LEVEL\",\"SYSTEM_ID\",\"HOST\",\"CHAIN_CODE\",\"TIMESTAMP\"],");
		
			st.append("   \"query\": {");
			st.append("     \"bool\": {");

			//검색어
			if( !"".equals(message) )
			{
				st.append("       \"must\": [");
				st.append("         {");
				st.append("           \"query_string\": {");
				st.append("             \"query\": \"" + message + "\", ");
				st.append("             \"lenient\": true,");
				st.append("             \"default_operator\": \"AND\",");
				st.append("             \"fields\": [");
				st.append("               \"*\"");
				st.append("             ]");
				st.append("           }");
				st.append("         }");
				st.append("       ],");
			}

			st.append("       \"filter\": {");
			st.append("         \"bool\": {");
			st.append("           \"must\": [");

			//체인코드
			String pChainCode = params.get("P_CHAIN_CODE").toString();
			if( !"".equals(pChainCode) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"CHAIN_CODE\": [");
				st.append("                   \"" +pChainCode + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}


			//로그레벨
			String pLogLevel = params.get("P_LOG_LEVEL").toString(); 
			if( !"".equals(pLogLevel) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"LOG_LEVEL\": [");
				st.append("                   \"" +pLogLevel + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}

			//HOST
			String pHostName = params.get("P_HOST_NAME").toString(); 
			if( !"".equals(pHostName) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"HOST\": [");

				String[] pHostNameArr =pHostName.split("\\|");
				int pHostNameArrLen = pHostNameArr.length;

				System.out.println("pHostNameArrLen:" + pHostNameArrLen);
				for(int i=0; i < pHostNameArrLen ; i++)
				{
					//마지막 index
					if((pHostNameArrLen-1) == i)
					{
						st.append("                   \"" +pHostNameArr[i] +"\"                         ");
					}
					else
					{
						st.append("                   \"" +pHostNameArr[i] +"\",                         ");
					}
				}
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}


			//시작일 종료일 추가
			String startDate = params.get("START_DATE").toString(); 
			String endDate = params.get("END_DATE").toString(); 

			//시작일
			if( !"".equals(startDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"gte\": \""+ startDate +"\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             },");
			}

			//종료일
			if( !"".equals(endDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"lte\": \""+endDate + "\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             }");
			}

			st.append("           ]");
			st.append("         }");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"highlight\": {");
			st.append("     \"require_field_match\": true,");
			st.append("     \"order\": \"score\",");
			st.append("     \"pre_tags\": [");
			st.append("       \"<b>\"");
			st.append("     ],");
			st.append("     \"post_tags\": [");
			st.append("       \"</b>\"");
			st.append("     ],");
			st.append("     \"fields\": {");
			st.append("       \"*\": {");
			st.append("         \"number_of_fragments\": 1,");
			st.append("         \"type\": \"unified\",");
			st.append("         \"fragment_size\": 150");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }");
			st.append("   ]");
			st.append(" }");

			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getWasServerLog");
			logger.error("getWasServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	
	
	/**
	 * [쿼리생성]QUEUE 서버 로그 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getQueueServerLog(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";

			st.append("{");
			st.append("\"track_total_hits\": true,");
			st.append("   \"query\": {");
			st.append("     \"bool\": {");

			//검색어
			if( !"".equals(message) )
			{
				st.append("       \"must\": [");
				st.append("         {");
				st.append("           \"query_string\": {");
				st.append("             \"query\": \"" + message + "\", ");
				st.append("             \"lenient\": true,");
				st.append("             \"default_operator\": \"AND\",");
				st.append("             \"fields\": [");
				st.append("               \"*\"");
				st.append("             ]");
				st.append("           }");
				st.append("         }");
				st.append("       ],");
			}

			st.append("       \"filter\": {");
			st.append("         \"bool\": {");
			st.append("           \"must\": [");

			//결과코드
			String resultCode = params.get("RESULT_CODE").toString();
			if( !"".equals(resultCode) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"RESULT\": [");
				st.append("                   \"" +resultCode + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}

			//HOST
			String pHostName = params.get("P_HOST_NAME").toString(); 
			if( !"".equals(pHostName) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"HOST\": [");

				String[] pHostNameArr =pHostName.split("\\|");
				int pHostNameArrLen = pHostNameArr.length;

				System.out.println("pHostNameArrLen:" + pHostNameArrLen);
				for(int i=0; i < pHostNameArrLen ; i++)
				{
					//마지막 index
					if((pHostNameArrLen-1) == i)
					{
						st.append("                   \"" +pHostNameArr[i] +"\"                         ");
					}
					else
					{
						st.append("                   \"" +pHostNameArr[i] +"\",                         ");
					}
				}
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}


			//시작일 종료일 추가
			String startDate = params.get("START_DATE").toString(); 
			String endDate = params.get("END_DATE").toString(); 

			//시작일
			if( !"".equals(startDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"CREATE_DATE\": {");
				st.append("                   \"gte\": \""+ startDate +"\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             },");
			}

			//종료일
			if( !"".equals(endDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"CREATE_DATE\": {");
				st.append("                   \"lte\": \""+endDate + "\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             }");
			}

			st.append("           ]");
			st.append("         }");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"highlight\": {");
			st.append("     \"require_field_match\": true,");
			st.append("     \"order\": \"score\",");
			st.append("     \"pre_tags\": [");
			st.append("       \"<b>\"");
			st.append("     ],");
			st.append("     \"post_tags\": [");
			st.append("       \"</b>\"");
			st.append("     ],");
			st.append("     \"fields\": {");
			st.append("       \"*\": {");
			st.append("         \"number_of_fragments\": 1,");
			st.append("         \"type\": \"plain\",");
			st.append("         \"fragment_size\": 150");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"CREATE_DATE\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }");
			st.append("   ]");
			st.append(" }");

			logger.info( st.toString().replace(" ", "") );

		}catch( Exception e)
		{
			System.out.println("Error  getQueueServerLog");
			logger.error("getQueueServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	/**
	 * [쿼리생성]FEED 서버 로그 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getFeedServerLog(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";

			st.append("{");
			st.append("   \"query\": {");
			st.append("     \"bool\": {");
			
			

			//검색어
			if( !"".equals(message) && params.get("search_type").toString().equals("01"))
			{
				st.append("       \"must\": [");
				st.append("         {");
				st.append("           \"query_string\": {");
				st.append("             \"query\": \"" + message + "\", ");
				st.append("             \"lenient\": true,");
				st.append("             \"default_operator\": \"AND\",");
				st.append("             \"fields\": [");
				st.append("               \"*\"");
				st.append("             ]");
				st.append("           }");
				st.append("         }");
				st.append("       ],");
			}

			st.append("       \"filter\": {");
			st.append("         \"bool\": {");
			st.append("           \"must\": [");

			//HOST
			String pHostName = params.get("P_HOST_NAME").toString(); 
			if( !"".equals(pHostName) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"HOST\": [");

				String[] pHostNameArr =pHostName.split("\\|");
				int pHostNameArrLen = pHostNameArr.length;

				System.out.println("pHostNameArrLen:" + pHostNameArrLen);
				for(int i=0; i < pHostNameArrLen ; i++)
				{
					//마지막 index
					if((pHostNameArrLen-1) == i)
					{
						st.append("                   \"" +pHostNameArr[i] +"\"                         ");
					}
					else
					{
						st.append("                   \"" +pHostNameArr[i] +"\",                         ");
					}
				}
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}
			
			//시스템ID
			if( !"".equals(message) && params.get("search_type").toString().equals("02"))
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"SYSTEM_ID\": [");
				st.append("                   \"" +message + "\"");
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}


			//시작일 종료일 추가
			String startDate = params.get("START_DATE").toString(); 
			String endDate = params.get("END_DATE").toString(); 

			//시작일
			if( !"".equals(startDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"START_DATE\": {");
				st.append("                   \"gte\": \""+ startDate +"\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             },");
			}

			//종료일
			if( !"".equals(endDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"START_DATE\": {");
				st.append("                   \"lte\": \""+endDate + "\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             }");
			}

			st.append("           ]");
			st.append("         }");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"highlight\": {");
			st.append("     \"require_field_match\": true,");
			st.append("     \"order\": \"score\",");
			st.append("     \"pre_tags\": [");
			st.append("       \"<b>\"");
			st.append("     ],");
			st.append("     \"post_tags\": [");
			st.append("       \"</b>\"");
			st.append("     ],");
			st.append("     \"fields\": {");
			st.append("       \"*\": {");
			st.append("         \"number_of_fragments\": 1,");
			st.append("         \"type\": \"plain\",");
			st.append("         \"fragment_size\": 150");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }");
			st.append("   ]");
			st.append(" }");

			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getWasServerLog");
			logger.error("getWasServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	/**
	 * [쿼리생성]ECM 서버 로그 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getEcmServerLog(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String message = params.get("where")!=null? params.get("where").toString() : "";

			st.append("{");
			st.append("   \"query\": {");
			st.append("     \"bool\": {");

			//검색어
			if( !"".equals(message) )
			{
				st.append("       \"must\": [");
				st.append("         {");
				st.append("           \"query_string\": {");
				st.append("             \"query\": \"" + message + "\", ");
				st.append("             \"lenient\": true,");
				st.append("             \"default_operator\": \"AND\",");
				st.append("             \"fields\": [");
				st.append("               \"*\"");
				st.append("             ]");
				st.append("           }");
				st.append("         }");
				st.append("       ],");
			}

			st.append("       \"filter\": {");
			st.append("         \"bool\": {");
			st.append("           \"must\": [");

			//HOST
			String pHostName = params.get("P_HOST_NAME").toString(); 
			if( !"".equals(pHostName) )
			{
				st.append("             {");
				st.append("               \"terms\": {");
				st.append("                 \"HOST\": [");

				String[] pHostNameArr =pHostName.split("\\|");
				int pHostNameArrLen = pHostNameArr.length;

				System.out.println("pHostNameArrLen:" + pHostNameArrLen);
				for(int i=0; i < pHostNameArrLen ; i++)
				{
					//마지막 index
					if((pHostNameArrLen-1) == i)
					{
						st.append("                   \"" +pHostNameArr[i] +"\"                         ");
					}
					else
					{
						st.append("                   \"" +pHostNameArr[i] +"\",                         ");
					}
				}
				st.append("                 ]");
				st.append("               }");
				st.append("             },");
			}


			//시작일 종료일 추가
			String startDate = params.get("START_DATE").toString(); 
			String endDate = params.get("END_DATE").toString(); 

			//시작일
			if( !"".equals(startDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"gte\": \""+ startDate +"\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             },");
			}

			//종료일
			if( !"".equals(endDate) )
			{
				st.append("             {");
				st.append("               \"range\": {");
				st.append("                 \"TIMESTAMP\": {");
				st.append("                   \"lte\": \""+endDate + "\",");
				st.append("                   \"format\": \"yyyy-MM-dd||yyyy\"");
				st.append("                 }");
				st.append("               }");
				st.append("             }");
			}

			st.append("           ]");
			st.append("         }");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"highlight\": {");
			st.append("     \"require_field_match\": true,");
			st.append("     \"order\": \"score\",");
			st.append("     \"pre_tags\": [");
			st.append("       \"<b>\"");
			st.append("     ],");
			st.append("     \"post_tags\": [");
			st.append("       \"</b>\"");
			st.append("     ],");
			st.append("     \"fields\": {");
			st.append("       \"*\": {");
			st.append("         \"number_of_fragments\": 1,");
			st.append("         \"type\": \"plain\",");
			st.append("         \"fragment_size\": 150");
			st.append("       }");
			st.append("     }");
			st.append("   },");
			st.append("   \"size\":" + params.get("length").toString() +",");
			st.append("   \"sort\": [");
			st.append("     {");
			st.append("       \"TIMESTAMP\": {");
			st.append("         \"order\": \"desc\"");
			st.append("       }");
			st.append("     }");
			st.append("   ]");
			st.append(" }");

			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getWasServerLog");
			logger.error("getWasServerLog Exception : " + e.getMessage());
		}

		return st.toString();
	}
	
	/**
	 * [쿼리생성]서버 항목 조회
	 * was010
	 * @return
	 * @throws MonitorException
	 */
	private String getServerList(Map<String, Object> params) throws Exception {
		StringBuffer st = new StringBuffer(1000);

		try
		{
			//검색어
			String server_type = params.get("SERVER_TYPE")!=null? params.get("SERVER_TYPE").toString() : "";
			String infra_type = params.get("INFRA_TYPE")!=null? params.get("INFRA_TYPE").toString() : "";
			String host_name = params.get("HOST_NAME")!=null? params.get("HOST_NAME").toString() : "";
			String search_text = params.get("SEARCH_TEXT")!=null? params.get("SEARCH_TEXT").toString() : "";
			String must_not = params.get("MUST_NOT")!=null? params.get("MUST_NOT").toString() : "";//L/B서버 제외용
			//정렬
			String orderBy = params.get("orderBy")!=null? params.get("orderBy").toString() : "";
			String dir = params.get("dir")!=null? params.get("dir").toString() : "";
			
			server_type = server_type.replace("선택", "");
			infra_type = infra_type.replace("선택", "");
			host_name = host_name.replace("선택", "");
			
			st.append("{");
			st.append("   \"query\": {");
			if("".equals(server_type) && "".equals(infra_type) && "".equals(host_name) && "".equals(search_text)) {
				st.append("   	\"match_all\": {}");
			}else {
				st.append("    \"bool\": {                        ");
				st.append("        \"must\": [                    ");
				Boolean usecomma = false;
				if(!"".equals(server_type)) {
					st.append("            {                          ");
					st.append("                \"terms\": {           ");
					st.append("                    \"SERVER_TYPE\": [ ");
					st.append("                        \""+server_type+"\"        ");
					st.append("                    ]                  ");
					st.append("                }                      ");
					st.append("            }                         ");
					usecomma = true;
				}
				if(!"".equals(infra_type)) {
					if(usecomma) st.append("            ,                         ");
					st.append("            {                          ");
					st.append("                \"terms\": {           ");
					st.append("                    \"INFRA_TYPE\": [  ");
					st.append("                        \""+infra_type+"\"     ");
					st.append("                    ]                  ");
					st.append("                }                      ");
					st.append("            }                         ");
					usecomma = true;
				}
				if(!"".equals(host_name)) {
					if(usecomma) st.append("            ,                         ");
					st.append("            {                          ");
					st.append("                \"terms\": {           ");
					st.append("                    \"HOST_NAME\": [   ");
					st.append("                        \""+host_name+"\"   ");
					st.append("                    ]                  ");
					st.append("                }                      ");
					st.append("            }                          ");	
					usecomma = true;				
				}
				if(!"".equals(search_text)) {
					if(usecomma) st.append("            ,                         ");
					st.append("            {                          ");
					st.append("                \"query_string\": {           ");
					st.append("                    \"query\": \""+search_text+"\",   ");
					st.append("                    \"lenient\": true,   ");
					st.append("                    \"fields\": [   ");
					st.append("                        \"*\"   ");
					st.append("                    ]                  ");
					st.append("                }                      ");
					st.append("            }                          ");					
				}
				st.append("         ]                              ");

				if(!"".equals(must_not)) {
					st.append("      ,                         ");
					st.append("      \"must_not\": [             ");
					st.append("        {                       ");
					st.append("          \"terms\": {            ");
					st.append("            \"HOST_NAME\": [      ");
					int idx=0;
					for(String mustnotval : must_not.split(",")) {
						if(idx>0) 
							st.append("      ,                         ");
						st.append("              \""+mustnotval+"\"        ");
						idx++;
					}
					st.append("            ]                   ");
					st.append("          }                     ");
					st.append("        }                       ");
					st.append("      ]                         ");				
				}
				st.append("    }                                  ");
				 
			}
			st.append("         },");
			st.append("   \"from\": " + params.get("start").toString() + " ,");
			st.append("   \"size\": " + params.get("length").toString() + ",");
			if(!"".equals(orderBy)) {
				st.append("   \"sort\": [");
				st.append("    	  		{");
				st.append("    	           \""+orderBy+"\": {");
				st.append("    	              \"order\": \""+dir+"\"");
				st.append("    	            }");
				st.append("    	  		}");
				st.append("    	  ],");
				
			}
			st.append("    	  \"aggs\": {");
			st.append("    	    \"SERVER_TYPE\": {");
			st.append("    	      \"sampler\": {");
			st.append("    	        \"shard_size\": 150000");
			st.append("    	      },");
			st.append("    	      \"aggs\": {");
			st.append("    	        \"SERVER_TYPE\": {");
			st.append("    	          \"terms\": {");
			st.append("    	            \"field\": \"SERVER_TYPE\",");
			st.append("    	            \"size\": 150000");
			st.append("    	          },");
			st.append("    	          \"aggs\": {");
			st.append("    	            \"text\": {");
			st.append("    	              \"terms\": {");
			st.append("    	                \"field\": \"SERVER_TYPE\"");
			st.append("    	              }");
			st.append("    	            }");
			st.append("    	          }");
			st.append("    	        }");
			st.append("    	      }");
			st.append("    	    },");
			st.append("    	    \"INFRA_TYPE\": {");
			st.append("    	      \"sampler\": {");
			st.append("    	        \"shard_size\": 150000");
			st.append("    	      },");
			st.append("    	      \"aggs\": {");
			st.append("    	        \"INFRA_TYPE\": {");
			st.append("    	          \"terms\": {");
			st.append("    	            \"field\": \"INFRA_TYPE\",");
			st.append("    	            \"size\": 150000");
			st.append("    	          },");
			st.append("    	          \"aggs\": {");
			st.append("    	            \"text\": {");
			st.append("    	              \"terms\": {");
			st.append("    	                \"field\": \"INFRA_TYPE\"");
			st.append("    	              }");
			st.append("    	            }");
			st.append("    	          }");
			st.append("    	        }");
			st.append("    	      }");
			st.append("    	    },");
			st.append("    	    \"HOST_NAME\": {");
			st.append("    	      \"sampler\": {");
			st.append("    	        \"shard_size\": 150000");
			st.append("    	      },");
			st.append("    	      \"aggs\": {");
			st.append("    	        \"HOST_NAME\": {");
			st.append("    	          \"terms\": {");
			st.append("    	            \"field\": \"HOST_NAME\",");
			st.append("    	            \"size\": 150000");
			st.append("    	          },");
			st.append("    	          \"aggs\": {");
			st.append("    	            \"text\": {");
			st.append("    	              \"terms\": {");
			st.append("    	                \"field\": \"HOST_NAME\",");
			st.append("    	                \"order\": {");
			st.append("    	                  \"_term\": \"asc\"");
			st.append("    	                }");
			st.append("    	              }");
			st.append("    	            }");
			st.append("    	          }");
			st.append("    	        }");
			st.append("    	      }");
			st.append("    	    }");
			st.append("    	  }");
			st.append("    	}");

			logger.info( st.toString() );

		}catch( Exception e)
		{
			System.out.println("Error  getServerList");
			logger.error("getServerList Exception : " + e.getMessage());
		}

		return st.toString();
	}
}
