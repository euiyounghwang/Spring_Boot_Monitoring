package com.test.test.spring.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.test.test.spring.constants.WebConstants;
import com.test.test.spring.ssh.FileMgr;
import com.test.test.spring.ssh.ShellCommand;
import com.test.test.spring.util.ESUtil;
import com.test.test.spring.util.MntHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class ServermanageService {
	@Autowired
	ElasearchService elasearchService;
	
	@Autowired
	CodeService codeService;	
	
//	@Autowired
//	ESUtil esUtil;	
		
	@Value("${spring.task.name}")
	private String taskName;
	
//	@Value("${dev.search_engine}")
//	private String local_elasticsearch = "";
	
	@Value("${log.search_engine}")
	private String log_elasticsearch = "";
		
	@Value("${prd.search_engine}")
	private String prd_elasticsearch = "";
	
	private static Logger logger = Logger.getLogger(ServermanageService.class);

	
	public List<Map<String, String>> getCodeList(Map<String, Object> params){
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		params.put("length", 0);
		params.put("start", 0);

		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		params.put("elaIP", "http://"+this.log_elasticsearch+"/server-management/_search");
//		List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//		params.put("elaIP", "http://"+result.get(0).get("CD_TP_MEANING")+"/server-management/_search");
		params.put("indexId", "server-management");
		params.put("SERVER_TYPE", (String) params.get("P_SERVER_TYPE"));
		params.put("INFRA_TYPE", (String) params.get("P_INFRA_TYPE"));
		params.put("HOST_NAME", (String) params.get("P_HOST_NAME"));
		if(params.containsKey("P_SERVER_TYPE") && params.get("P_SERVER_TYPE").equals("WAS")) params.put("MUST_NOT", "StickyLB");//제외할 HOST_NAME 셋팅. 값이 여러개일 경우 , 로 구분하여 추가해준다.
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		String sel_code_type = (String) params.get("P_SEL_CODE_TYPE");
    		
    		JSONArray jArr =jsonObj.getJSONObject("aggregations").getJSONObject(sel_code_type).getJSONObject(sel_code_type).getJSONArray("buckets");
    		

    		String key="";

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, String> map = new HashMap<>();    			
    			key = jArr.getJSONObject(i).getString("key");
    			if(ESUtil.isNotEmpty(key)) {
    				map.put("KEY", key);
        			results.add(map);
    			}
    		}
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public Map<String, Object> findServerList(Map<String, Object> params){
		Map<String, Object> results = new HashMap<>();
		params.put("length", (Integer) params.get("length"));
		params.put("start", (Integer) params.get("start"));
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");

		String dir = (String) order.get(0).get("dir");
		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		String orderBy=columns.get(column).get("data");
		params.put("orderBy", orderBy);
		params.put("dir", dir);

		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
		
		
//		params.put("elaIP", "http://"+this.local_elasticsearch+"/server-management/_search");
		params.put("elaIP", "http://"+this.log_elasticsearch +"/server-management/_search");
//		List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//		params.put("elaIP", "http://"+result.get(0).get("CD_TP_MEANING")+"/server-management/_search");
		params.put("indexId", "server-management");
		params.put("SERVER_TYPE", (String) params.get("P_SERVER_TYPE"));
		params.put("INFRA_TYPE", (String) params.get("P_INFRA_TYPE"));
		params.put("HOST_NAME", (String) params.get("P_HOST_NAME"));
		params.put("SEARCH_TEXT", (String) params.get("P_SEARCH_TEXT"));
		
//		System.out.println("@@@@@@@@@@@@ findServerList @@@@@@@ " + params);
		
		//System.out.println("url : http://"+result.get(0).get("CD_TP_MEANING")+"/server-management/_search");
		
    	try
    	{
    		JSONObject  jsonObj = elasearchService.handleEla(params);
    		
    		JSONArray jArr =jsonObj.getJSONObject("hits").getJSONArray("hits");
    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    		String index=""; //인덱스
    		String type="";
    		String id="";
    		String severtype="";
    		String infratype="";
    		String hostname="";
    		String servername="";
    		String posconatip="";
    		String realip="";
    		String serviceip="";
    		String port="";
    		String userid="";
    		String userpw="";
    		String description="";

    		for(int i=0; i<jArr.size() ;i++)
    		{
    			Map<String, Object> map = new HashMap<>();

    			index = jArr.getJSONObject(i).getString("_index"); //인덱스
    			type = jArr.getJSONObject(i).getString("_type");
    			id = jArr.getJSONObject(i).getString("_id"); 			
    			severtype = (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SERVER_TYPE");
    			infratype = jArr.getJSONObject(i).getJSONObject("_source").has("INFRA_TYPE")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("INFRA_TYPE"):"";
    			hostname = jArr.getJSONObject(i).getJSONObject("_source").has("HOST_NAME")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("HOST_NAME"):"";
    			servername = jArr.getJSONObject(i).getJSONObject("_source").has("SERVER_NAME")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SERVER_NAME"):"";
    			posconatip = jArr.getJSONObject(i).getJSONObject("_source").has("POSCO_NAT_IP")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("POSCO_NAT_IP"):"";
    			realip = jArr.getJSONObject(i).getJSONObject("_source").has("REAL_IP")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("REAL_IP"):"";
    			serviceip = jArr.getJSONObject(i).getJSONObject("_source").has("SERVICE_IP")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("SERVICE_IP"):"";
    			port = jArr.getJSONObject(i).getJSONObject("_source").has("PORT")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("PORT"):"";
    			userid = jArr.getJSONObject(i).getJSONObject("_source").has("USER_ID")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("USER_ID"):"";
    			userpw = jArr.getJSONObject(i).getJSONObject("_source").has("USER_PW")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("USER_PW"):"" ;
    			description = jArr.getJSONObject(i).getJSONObject("_source").has("DESCRIPTION")? (String) jArr.getJSONObject(i).getJSONObject("_source").getString("DESCRIPTION"):"";

    		
    			severtype = severtype.replace("선택", "");
    			infratype = infratype.replace("선택", "");
    			hostname = hostname.replace("선택", "");
    			
    			map.put("INDEX", index);
    			map.put("TYPE", type);
    			map.put("ID", id);
    			map.put("SERVER_TYPE", severtype);
    			map.put("INFRA_TYPE", infratype);
    			map.put("HOST_NAME", hostname);
    			map.put("SERVER_NAME", servername);
    			map.put("POSCO_NAT_IP", posconatip);
    			map.put("REAL_IP", realip);
    			map.put("SERVICE_IP", serviceip);
    			map.put("PORT", port);
    			map.put("USER_ID", userid);
    			map.put("USER_PW", userpw);
    			map.put("DESCRIPTION", ESUtil.ConvertJSONBJECT_ARR(description));

    			list.add(map);
    		}
    		results.put("draw", params.get("draw"));
    		if(list.size()>0) {
//	    			if(jsonObj.getJSONObject("hits").getJSONObject("total") instanceof JSONObject){
//	   				 results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//	   				 results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
//	   			   }
//	   		 else {
//	   				 results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//	   				 results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//	   				}
//    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
//  				results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getString("total")));
    			results.put("recordsTotal", Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
				  results.put("recordsFiltered",Integer.parseInt(jsonObj.getJSONObject("hits").getJSONObject("total").getString("value")));
    		} else {
    			results.put("recordsTotal", 0);
    			results.put("recordsFiltered",0);
    		}
    		results.put("data", list);
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return results;
	}
	
	public String getConfigData(Map<String, Object> params){
		
		String url = params.get("url").toString();
		URL connect;
		OutputStream os = null;
		HttpURLConnection httpConn =null;
		BufferedReader reader = null;
		StringBuffer data = new StringBuffer();
    	try
    	{
			connect = new URL("http://"+url);
			httpConn = (HttpURLConnection)connect.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);    		
			//request write
			os = httpConn.getOutputStream();

			os.flush();
			os.close();
			os = null;

			reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
			
			String line;
			while (null != (line = reader.readLine())) {
			data.append(line);
			data.append('\n');
		   }
			httpConn.disconnect();
			httpConn = null;
    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return data.toString();
	}
	
	public String getConfigData(String filePath){
		
		BufferedReader reader = null;
		StringBuffer data = new StringBuffer();
    	try
    	{
    		//Feeder_DB_Connection_Info.xml을 열어 내용 받아오기
			File file = new File(filePath);
			if (file.exists()) {
				
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				
				String line;
				while (null != (line = reader.readLine())) {
				data.append(line);
				data.append('\n');
			   }
				
				
			}
    		
    		
    	} catch (FileNotFoundException fe) {  logger.error(fe.getMessage(), fe.getCause());
		} catch (IOException ie) {            logger.error(ie.getMessage(), ie.getCause());
		} catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	} finally {
            if (reader != null) {
                try {
                	reader.close();
                	reader = null;
                } catch (IOException e) {
                	logger.error(e.getMessage(), e.getCause());
                }
            }
        }
    	return data.toString();
	}

	public String insertServerInfo(Map<String, Object> params){
		
		String rtnmsg = null;
		String parent = null;
		
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
//		List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//		params.put("elaIP", "http://"+result.get(0).get("CD_TP_MEANING")+"/_bulk");
		params.put("elaIP", "http://"+log_elasticsearch+"/_bulk");
		//String url="http://10.132.17.121:9200/_bulk/";
		String url="http://"+log_elasticsearch+"/_bulk";
		
		System.out.println("params : "+params);
		
		//params.put("SERVER_TYPE", (String) params.get("P_SERVER_TYPE"));
		//params.put("INFRA_TYPE", (String) params.get("P_INFRA_TYPE"));
		//params.put("HOST_NAME", (String) params.get("P_HOST_NAME"));
		 	 
		try
		{
			
			HttpClient httpClient = new DefaultHttpClient();

    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();			    		 
				 
			String serverInfoId = (String)params.get("id");
			String serverType = (String)params.get("server_type");
			String infraType = (String)params.get("infra_type");
			String hostName = (String)params.get("host_name");
			String serverName = (String)params.get("server_name");
			String poscoNatIp = (String)params.get("posco_nat_ip");             
			String serverRealIp = (String)params.get("real_ip");
			String serviceIp = (String)params.get("service_ip");
			String port = (String)params.get("port");
			String userId = (String)params.get("user_id");
			String userPw = (String)params.get("user_pw");
			String description = (String)params.get("description");
			  
			if (serverInfoId == "") {
				parent = "{\"index\":{\"_index\":\"server-management\",\"_type\":\"_doc\"}}\n";
			}else {
				parent = "{\"update\":{\"_index\":\"server-management\",\"_type\":\"_doc\",\"_id\":\"" + serverInfoId + "\"}}\n";
				parent += "{\"doc\":";
			}
			  
			parent += "{\"SERVER_TYPE\": \"" + serverType + "\",\"INFRA_TYPE\": \"" + infraType + "\", \"HOST_NAME\": \"" + hostName + "\",\"SERVER_NAME\": \"" + serverName +"\",";
			parent += "\"POSCO_NAT_IP\": \"" + poscoNatIp +"\",\"REAL_IP\": \"" + serverRealIp + "\",\"SERVICE_IP\": \"" + serviceIp + "\",";
			parent += "\"PORT\": \"" + port +"\",\"USER_ID\": \"" + userId +"\",\"USER_PW\": \"" + userPw +"\",\"DESCRIPTION\": \"" + description +"\"";             
			 
			if (serverInfoId == "") {
				parent += "}\n";
			}else {
				parent += "}}\n";
			}

			try {
				HttpPost request = new HttpPost(url);

				System.out.println(url);
				System.out.println(parent);
				     
				HttpEntity entity = new ByteArrayEntity(parent.getBytes("UTF-8"));
				
				request.setEntity(entity);
				request.setHeader("Authorization", "Basic cG9zcm9lc2Q6Z3NhYWRtaW4=");
				request.setHeader("Content-Type", "application/x-ndjson");				
				
				HttpResponse response = httpClient.execute(request);
				entity =  response.getEntity();				
				
				if (entity != null) {
					InputStream instream = entity.getContent();					
					rtnmsg = convertStreamToString(instream);
					
					System.out.println("rtnmsg : "+rtnmsg);
					instream.close();
				}
				
				org.apache.http.Header[] headers = response.getAllHeaders();
				for (int i=0 ; i < headers.length; i++) {
					System.out.println(headers[i]);
				}
		
			// handle response here...
			} catch (Exception ex) {
			// handle exception here
			} finally {
				httpClient.getConnectionManager().shutdown();
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ServermanageService.insertServerInfo() Exception : " +e.getMessage(), e.getCause());
		} finally {
			try{
			}
			catch(Exception e)
			{
			logger.error(e.getMessage(),e);
			}
		}
		return rtnmsg;
	}
	
	public String deleteServerInfo(Map<String, Object> params){
		
		String rtnmsg = null;
		String parent = "";
				
		//GET ACTIVE ELASTIC IP 
		Map<String, Object> elaParam = new HashMap<>();
		elaParam.put("FK_CD_TP", "ELASTIC_IP");
		elaParam.put("ACTIVE_FLAG", "Y");
//		List<Map<String, Object>> result = codeService.selectTable102(elaParam);
//		params.put("elaIP", "http://"+result.get(0).get("CD_TP_MEANING")+"/_bulk");
		params.put("elaIP", "http://"+log_elasticsearch+"/_bulk");		
		
		//String url="http://10.132.17.121:9200/_bulk/";
		//String url="http://"+result.get(0).get("CD_TP_MEANING")+"/_bulk";
		String url="http://"+log_elasticsearch+"/_bulk";
		
		System.out.println("params : "+params);

		try
		{
			
			HttpClient httpClient = new DefaultHttpClient();
			String retrun = null;

    		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();			    		 
				 
			String serverInfoId = (String)params.get("id");
			
			String[] item = serverInfoId.split(",");
			
			for(int i=0; i<item.length; i++) {
				parent += "{\"delete\":{\"_index\":\"server-management\",\"_type\":\"_doc\",\"_id\":\"" + item[i] + "\"}}\n";
			}			

			System.out.println(parent);
			
			try {
				HttpPost request = new HttpPost(url);
				    
				System.out.println(parent);
				     
				HttpEntity entity = new ByteArrayEntity(parent.getBytes("UTF-8"));
				
				request.setEntity(entity);
				request.setHeader("Authorization", "Basic cG9zcm9lc2Q6Z3NhYWRtaW4=");
				request.setHeader("Content-Type", "application/x-ndjson");
				
				HttpResponse response = httpClient.execute(request);
				entity =  response.getEntity();				
				
				if (entity != null) {
					InputStream instream = entity.getContent();					
					rtnmsg = convertStreamToString(instream);
					
					System.out.println("rtnmsg : "+rtnmsg);
					instream.close();
				}
				
				org.apache.http.Header[] headers = response.getAllHeaders();
				for (int i=0 ; i < headers.length; i++) {
					System.out.println(headers[i]);
				}				
		
			// handle response here...
			} catch (Exception ex) {
			// handle exception here
			} finally {
				httpClient.getConnectionManager().shutdown();
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[Activity-error] ServermanageService.insertServerInfo() Exception : " +e.getMessage(), e.getCause());
		} finally {
			try{
			}
			catch(Exception e)
			{
			logger.error(e.getMessage(),e);
			}
		}
		return rtnmsg;
	}
	
	public String convertStreamToString(InputStream str) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(str));
		StringBuffer data = new StringBuffer();
		
		String line = null;
    	try
    	{
			while (null != (line = reader.readLine())) {
			data.append(line);
			data.append('\n');
		   }    		
    	}catch(Exception e)    	{
    		logger.error(e.getMessage(), e.getCause());
    	}
    	return data.toString();
	}


    /**
     * 서버별 색인 CronJob 관리 조회-config010
     * @param params
     * @return
     */
    public Map<String, Object> findCronJob(Map<String, Object> params){
    	
    	Map<String, Object> results = new HashMap<>();
        try
        {
            String hostName = (String)params.get("HOST_NAME");
            String serverRealIp = (String)params.get("REAL_IP");
            String userId = (String)params.get("USER_ID");
            String userPw = (String)params.get("USER_PW");
            ShellCommand sshAgent = new ShellCommand(hostName,serverRealIp, userId, userPw);
            if (sshAgent.connect())
            {
                String processInfo = sshAgent.executeCommand("crontab -l");

                List<MntHashMap<String, String>> resultList = new ArrayList<MntHashMap<String, String>>();
                if(processInfo != null)
                {
                    String[] tempRow = processInfo.split("\n");
                    //한줄씩 읽어서 배치시간(1~5) 프로세스에 넣기
                    //첫 번째	 분	 0~59
                    //두 번째	 시	 0~23
                    //세 번째	 일	 1~31
                    //네 번째	 월	 1~12
                    //다섯 번째	 요일	 0~7 (0,7 : 일요일, 1 : 월요일)
                    //여섯 번째	 명령어	 실행할 명령을 한줄로 쓴다.
                    String[] tempRow2;

                    for(String row : tempRow)
                    {
                        //빈줄은 자동 통과
                    	row = row.trim();
                    	if(row.length()==0) continue;
                    	
                    	//연속된 공백하나의 공백으로 변경하기
                        row = row.replaceAll(" +", " ");
                        tempRow2 = row.split(" ");
                        String[] cronJobRow = {"",""};
                        boolean joosukNotOnlyTextFlag = true;

                		//주석구분 #으로 시작 하면서 한글,영어,숫자만 구분함.
                		if(Pattern.matches("^#[ㄱ-ㅎ가-힣a-zA-Z0-9]*$",row))
                		{
                			logger.info("주석구분 #으로 시작 하면서 한글,영어,숫자만 구분함.");
                			cronJobRow[0] = row;
                			joosukNotOnlyTextFlag= false;
                		}

                		if(joosukNotOnlyTextFlag)
                		{
	                        int idx = 0;
	                        for(String col : tempRow2)
	                        {
	                            //읽은것이 숫자 또는 * 일때 한개 스트링으로 붙이기
	                            if(idx < 5)
	                            {
	                                //마지막에 공백 넣지 않음
	                                if(idx == 4)
	                                {
	                                    cronJobRow[0] = cronJobRow[0] + col;
	                                }
	                                else
	                                {
	                                    cronJobRow[0] = cronJobRow[0] + col + " ";
	                                }
	                            }
	                            else
	                            {
	                                //마지막에 공백 넣지 않음
	                                if(idx  ==  (tempRow2.length-1))
	                                {
	                                    cronJobRow[1] = cronJobRow[1] + col;
	                                }
	                                else
	                                {
	                                    cronJobRow[1] = cronJobRow[1] + col + " ";
	                                }
	                            }
	                            idx++;
	                        }

                		}

                        MntHashMap<String, String>  resultMap2 = new MntHashMap<String, String>();

                        //배치시간, 프로세스 둘 중 하나만 있어도 넣기(주석때문)
                        if( !"".equals(cronJobRow[0] )  || !"".equals(cronJobRow[1]))
                        {
//                        	logger.info("cronJobRow[0]:"+ cronJobRow[0]);

                            resultMap2.put("BATCH_TIME",cronJobRow[0] );
                            resultMap2.put("PROCESS",cronJobRow[1] );
                            resultList.add(resultMap2);

                        }
                    }
                }
            logger.info("결과:" + resultList.toString());

    		results.put("draw", params.get("draw"));
            results.put("data", resultList);
			results.put("recordsTotal", resultList.size());
			results.put("recordsFiltered",resultList.size());
			
            results.put("msg", "");
            

            //현재 디렉토리 경로 셋업:원격 색인 Job 실행 화면에서 필요함.
            String printWorkingDirectory = sshAgent.executeCommand("pwd");

            results.put("PRINT_WORKING_DIRECTORY", printWorkingDirectory);//현재디렉토리 경로

            // 로그아웃
            sshAgent.logout();
            }
            //연결에 실패했을때
            else
            {
            	results.put("msg", "호스트명 "+ hostName +"에 연결하지 못했습니다.");
                logger.info("호스트명 "+ hostName +"에 연결하지 못했습니다.");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("[Activity-error] ServermanageService.findCronJob() Exception : " + e.getMessage(), e.getCause());
        }
        return results;
    }

     /**
      * 서버별 색인 CronJob 관리 백업-config010
      * @param params
      * @return
      */
     public String backupCronJob(Map<String, Object> params){
    	 String rtnmsg = null;
    	 try
    	 {
             String hostName = (String)params.get("HOST_NAME");
             String serverRealIp = (String)params.get("REAL_IP");
             String userId = (String)params.get("USER_ID");
             String userPw = (String)params.get("USER_PW");
    		 String eventType = "backupCrontab";
    		 rtnmsg=FileMgr.backupCrontab(hostName,serverRealIp, userId, userPw,eventType,params);

    	 } catch (Exception e) {
    		 logger.error("[Activity-error] ServermanageService.backupCronJob() Exception : " +e.getMessage(), e.getCause());
    	 }
    	 return rtnmsg;
     }

     /**
      * 서버별 색인 CronJob 관리 저장-config010
      * @param params
      * @return
      */
     public String saveCronJob(Map<String, Object> params){
    	 String rtnmsg = null;
    	 ShellCommand sshAgent = null;
    	 try
    	 {

             String hostName = (String)params.get("HOST_NAME");
             String serverRealIp = (String)params.get("REAL_IP");
             String userId = (String)params.get("USER_ID");
             String userPw = (String)params.get("USER_PW");
    		 
    		 sshAgent = new ShellCommand(hostName,serverRealIp, userId, userPw);
             
    		 if (sshAgent.connect())
             {
				//crontab 읽어온 내용을 반복문으로 넣기
				String oneLine = "";
				
				//데이터 읽어서 Crontab 내용 만들기
				@SuppressWarnings("unchecked")
				ArrayList<String>  batchTimeArr = (ArrayList<String>)params.get("BATCH_TIME");
				@SuppressWarnings("unchecked")
				ArrayList<String>  processArr = (ArrayList<String>)params.get("PROCESS");

				int idx = 0 ;
				
				if(null != batchTimeArr)
				{
					for(String batchTime : batchTimeArr)
					{	
						oneLine = batchTime.trim() + " " + processArr.get(idx).trim();
						if(idx==0) sshAgent.executeCommand("echo -e \""+oneLine+"\" | crontab");
						else sshAgent.executeCommand("cat <(crontab -l) <(echo \""+oneLine+"\") | crontab -");
						idx++;
					}
				}
    			 
				rtnmsg = "success";
             }
             //연결에 실패했을때
             else
             {
            	 rtnmsg="호스트명 "+ hostName +"에 연결하지 못했습니다.";
                 logger.info("호스트명 "+ hostName +"에 연결하지 못했습니다.");
             }

    	 } catch (Exception e) {
//    		 e.printStackTrace();
    		 logger.error("[Activity-error] ServermanageService.saveCronJob() Exception : " +e.getMessage(), e.getCause());
    	 }finally{
			try{
				// 로그아웃
				sshAgent.logout();
			}
			catch(Exception e)
			{
				logger.error(e.getMessage(),e);
			}
		}
    	 return rtnmsg;
     }
     

     /**
      * 서버별 Job 실행
      * @param params
      * @return
      */
     public String runJob(Map<String, Object> params){
    	 String rtnmsg = null;
    	 ShellCommand sshAgent = null;
    	 try
    	 {
    		 

             String hostName = null;
             String serverRealIp =null;
             String userId = null;
             String userPw = null;
             String runCommand = (String)params.get("RUN_COMMAND");
             String runMode = (String)params.get("RUN_MODE");
             
    		 logger.info("runCommand="+runCommand);
    		 logger.info("runMode="+runMode);
    		 
             if(runMode.contains("menu_xml_view")) {
        		 logger.info("application.yml>task.Name="+taskName);
        		 if(taskName.equals("prdTask")) {
        			 hostName=WebConstants.MENU_BATCH_PRD_HOST_NAME;
        			 serverRealIp=WebConstants.MENU_BATCH_PRD_REAL_IP;
        			 userId=WebConstants.MENU_BATCH_PRD_USER_ID;
        			 userPw=WebConstants.MENU_BATCH_PRD_USER_PW;        			 
        		 }else {
        			 hostName=WebConstants.MENU_BATCH_DEV_HOST_NAME;
        			 serverRealIp=WebConstants.MENU_BATCH_DEV_REAL_IP;
        			 userId=WebConstants.MENU_BATCH_DEV_USER_ID;
        			 userPw=WebConstants.MENU_BATCH_DEV_USER_PW;
        		 }
             }else {
                 hostName = (String)params.get("HOST_NAME");
                 serverRealIp = (String)params.get("REAL_IP");
                 userId = (String)params.get("USER_ID");
                 userPw = (String)params.get("USER_PW");            	 
             }
    		 
    		 sshAgent = new ShellCommand(hostName,serverRealIp, userId, userPw);
             
    		 if (sshAgent.connect())
             {

                sshAgent.executeCommand(runCommand);  			 
				rtnmsg = "success";
             }
             //연결에 실패했을때
             else
             {
            	 rtnmsg="호스트명 "+ hostName +"에 연결하지 못했습니다.";
                 logger.info("호스트명 "+ hostName +"에 연결하지 못했습니다.");
             }

    	 } catch (Exception e) {
    		 logger.error("[Activity-error] ServermanageService.runJob() Exception : " +e.getMessage(), e.getCause());
    	 }finally{
			try{
				// 로그아웃
				sshAgent.logout();
			}
			catch(Exception e)
			{
				logger.error(e.getMessage(),e);
			}
		}
    	 return rtnmsg;
     }
     
     /*
 	 * 파일업로드
 	 */
 	public Map<String, Object> filexmlUpload(MultipartHttpServletRequest mReq, String feedServerPath, String feedBackupPath) {
 		Map<String, Object> resultMap = new HashMap<String, Object>();

 		Iterator<String> iter = mReq.getFileNames();
 		if (iter.hasNext() == false) {
 			resultMap.put("msg", "파일이 없습니다.");
 		} else {
 			while (iter.hasNext()) {
 				String uploadFileNm = iter.next();
 				MultipartFile mFile = mReq.getFile(uploadFileNm);
 				try {
 					// 기존파일 백업
 					File file = new File(feedServerPath);
 					File file_backup = new File(feedBackupPath);
 					if (file.exists()) {
 						FileCopyUtils.copy(file, file_backup);
 					}
 					mFile.transferTo(file);
 				} catch (Exception e) {
 					logger.error(e.getMessage(), e.getCause());
 				}
 			}
 			resultMap.put("msg", "success");
 		}

 		return resultMap;
 	}
 	
 	/*Feeder_DB_Connection_Info.xml 파일 읽어서 datatable에 뿌려주기위해 Map에 담아 리턴*/
 	public Map<String, Object> getXmlFileInfo(String filePath, Map<String, Object> params) {
 		
 		//System.out.println("start getXmlFileInfo()~!!");
 		int length = (Integer) params.get("length");
 		int start = (Integer) params.get("start");

		List<Map<String, String>> columns  = (List<Map<String, String>>) params.get("columns");
		List<Map<String, Object>> order  = (List<Map<String, Object>>) params.get("order");
		int column =  (Integer) order.get(0).get("column");
		String dir = (String) order.get(0).get("dir");		
		String orderBy=columns.get(column).get("data") + dir;
		
		Map<String, String> search  = (Map<String, String>) params.get("search");
		String where=search.get("value");
		
 		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list_part = new ArrayList<Map<String, Object>>();
		
 		try{
			//Feeder_DB_Connection_Info.xml을 열어 내용 받아오기
			JSONArray jArr = getXmlFilejarr(filePath);
									
			for(int i=0; i<jArr.size();i++){
		 		Map<String, Object> map = new HashMap<String, Object>();
		 		

		 		if(jArr.getJSONObject(i).has("id")) {
		 			//사용자가 검색한 값이 존재하면 비교한다.검색값과 일치하지 않으면 해당열은 continue처리
		 			if((where != null && !"".equals(where) && jArr.getJSONObject(i).getString("id").contains(where))
		 					|| where==null || where.isEmpty()) {
		 				map.put("id", jArr.getJSONObject(i).getString("id"));			 				
		 			}
		 			else {
		 				map.put("id", "");
		 				continue;
		 			}
		 							 				
		 		}			 			
		 		else
		 			map.put("id", "");
		 		
		 		for(Iterator<String> it=jArr.getJSONObject(i).keys(); it.hasNext();) {
		 			
		 			String strKey = it.next();
		 			if(strKey.equals("id"))
		 				continue;
		 			
		 			String listKey = strKey.replaceAll("\\.", "_");
		 			map.put(listKey, jArr.getJSONObject(i).getString(strKey));
		 			
		 		}

		 		//항목이 없을 경우 초기값셋팅
		 		if(!jArr.getJSONObject(i).has("feed.sub.sysid"))
	 				map.put("feed_sub_sysid", "");
		 		if(!jArr.getJSONObject(i).has("feed.db.type"))
	 				map.put("feed_db_type", "");
		 		if(!jArr.getJSONObject(i).has("feed.db.driver"))
	 				map.put("feed_db_driver", "");
		 		if(!jArr.getJSONObject(i).has("feed.db.url"))
	 				map.put("feed_db_url", "");
		 		if(!jArr.getJSONObject(i).has("feed.db.user"))
	 				map.put("feed_db_user", "");
		 		if(!jArr.getJSONObject(i).has("feed.db.pwd"))
	 				map.put("feed_db_pwd", "");
		 		if(!jArr.getJSONObject(i).has("feed.db.schema"))
	 				map.put("feed_db_schema", "");
		 		
		 		map.put("recordsTotalIdTypes", "");

    			list.add(map);
			}
			
			
			String tmpStr = null;
			

    		if(list.size()>0) {
    			resultMap.put("recordsTotal", list.size());
    			resultMap.put("recordsFiltered",list.size());
    			
    			StringBuffer tmpsb = new StringBuffer();
    			for(Map<String, Object> m : list) {
    				if(tmpsb.length()>0) tmpsb.append(",");
    				tmpsb.append(m.get("id"));
    				tmpsb.append("_");
    				tmpsb.append(m.get("type"));    				
    			}
    			if(tmpsb.length()>0) {
    				tmpStr=tmpsb.toString();
    			}	
    			
    		}else {
    			resultMap.put("recordsTotal", 0);
    			resultMap.put("recordsFiltered",0);
    			
    		}
			
			//순서 정렬하기
			if(!orderBy.isEmpty() && orderBy.equals("idasc"))
				Collections.sort(list, new IDAscCompare());
			else if(!orderBy.isEmpty() && orderBy.equals("iddesc"))
				Collections.sort(list, new IDDescCompare());

			//순서정렬된 list를 현재page에 해당하는 부분만 copy하기
			//System.out.println("list.size()="+list.size());
			if(length>0) {//length가 전체일 경우 -1로 넘어온다.
				for(int c=start; c<start+length; c++) {				
					if(c>=list.size()) break;
					//System.out.println("copy num="+c);
					list_part.add(list.get(c));	
				}
			}else {
				list_part=list;
			}
			
			if(ESUtil.isNotEmpty(tmpStr)) {
				list_part.get(0).replace("recordsTotalIdTypes", tmpStr);
			}

	 		resultMap.put("data", list_part);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
 		
 		
 		return resultMap;
 	}
 	

 	
 	/*
 	 * 내용수정은 추가는 <system></system> 1개씩만 가능하다
 	 * xml 파일의 update할 system id와 해당열의 map 정보를 받아와서
 	 * 기존 파일정보를 읽어온후
 	 * update정보와 비교
 	 * 일치할 경우 update정보로 교체
 	 * JSONArray를 다시 xml로 저장 
 	 * */
 	public Map<String, String> setXmlFileUpdate(String filePath, Map<String, Object> params) {
 		
 		Map<String, String> result = new HashMap<String, String>();
 		
 		String systemId = (String) params.get("id");
 		String systemType = (String) params.get("type");

 		try {

 	 		//Feeder_DB_Connection_Info.xml을 열어 내용 받아오기
 			JSONArray jArr = getXmlFilejarr(filePath);
 									
 			for(int i=0; i<jArr.size() ;i++){
 				
 				//<System id 와 type 항목이 동일하면 일치한다고 판단하고 해당 <system에 대한 수정을 시작한다.
 			 		if(jArr.getJSONObject(i).has("id") && jArr.getJSONObject(i).getString("id").equals(systemId)
 			 				&& jArr.getJSONObject(i).has("type") && jArr.getJSONObject(i).getString("type").equals(systemType)) {
 			 			
 			 			Object[] arr_keySet = params.keySet().toArray();
 						for(int j=0;j<arr_keySet.length;j++){
 							String key = arr_keySet[j].toString();
 							String xmlKey = key.replaceAll("_", "."); 
 							if(key.equals("id") || key.equals("type"))
 								continue;
 							jArr.getJSONObject(i).put(xmlKey, params.get(key));
 							
 						}
 						
 						System.out.println("setXmlFileUpdate() jArr.getJSONObject("+i+").toString()="+jArr.getJSONObject(i).toString());
 						
 			 		}
 				
 			}
 			
 			setXmlFileSave(filePath,jArr);
 			result.put("msg", "수정이 완료되었습니다.");
 			result.put("value", "true");
 		}catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
 		
 		return result;
 		
 	}

 	/*
 	 * 신규항목추가는 <system></system> 1개씩만 가능하다
 	 * 기존파일정보 읽어온 후
 	 * 가장 마지막에 신규항목 추가
 	 * 다시 저장
 	 * */
 	public Map<String, String> setXmlFileInsert(String filePath, Map<String, Object> params) {

 		Map<String, String> result = new HashMap<String, String>();

 		try {
 	 		
 	 		//Feeder_DB_Connection_Info.xml을 열어 내용 받아오기
 			JSONArray jArr = getXmlFilejarr(filePath);
 			JSONObject jObj = new JSONObject();
 			
 			Object[] arr_keySet = params.keySet().toArray();
			for(int j=0;j<arr_keySet.length;j++){
				String key = arr_keySet[j].toString();
				String xmlKey = key.replaceAll("_", "."); 
				jObj.put(xmlKey, params.get(key));
			}
			
			System.out.println("setXmlFileInsert() jObj="+jObj.toString());
			jArr.add(jObj);									
 	 		 	
 			setXmlFileSave(filePath,jArr);
 			result.put("msg", "등록이 완료되었습니다.");
 			result.put("value", "true");
 			
 			
 		}catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
 				
 		
 		return result;
 	}
 	
 	/*
 	 * XML 파일의 삭제할 System ID를 받아와서
 	 * 기존 파일정보 읽어온 후 
 	 * 삭제할 정보와 비교
 	 * 일치할 경우 해당 line삭제
 	 * JSONArray를 다시 xml로 저장 
 	 * */
 	public Map<String, String> setXmlFileDelete(String filePath, Map<String, Object> params) {

 		Map<String, String> result = new HashMap<String, String>();
 		Boolean boolremove = false;
 		
 		//여러개 받아서 반복문으로 처리할 방법 생각하기(20210726)
 		String systemIdTypes = (String) params.get("id_type"); 		
 		String[] arr_idtypes = systemIdTypes.isEmpty()?null:systemIdTypes.split(",");
 		
 		try {
 	 		
 			if(arr_idtypes!=null) {

 	 	 		//Feeder_DB_Connection_Info.xml을 열어 내용 받아오기
 	 			JSONArray jArr = getXmlFilejarr(filePath);
 	 									
 	 			for(int i=0; i<jArr.size() ;i++){
 	 				
 	 				for(int j=0; j<arr_idtypes.length; j++) {
 	 	 				//<System id 와 type 항목이 동일하면 일치한다고 판단하고 해당 <system 전체를 삭제한다.
 	 	 		 		if(jArr.getJSONObject(i).has("id") && jArr.getJSONObject(i).getString("id").equals(arr_idtypes[j].split("\\|")[0])
 	 	 		 				&& jArr.getJSONObject(i).has("type") && jArr.getJSONObject(i).getString("type").equals(arr_idtypes[j].split("\\|")[1])) {
 	 	 		 			System.out.println("setXmlFileDelete() remove="+jArr.get(i).toString());
 	 	 		 			jArr.remove(i);
 	 	 		 			boolremove = true;
 	 	 		 			
 	 	 		 		}
 	 				}
 	 			}
 	 	 		 	
 	 			if(boolremove) {
 	 				setXmlFileSave(filePath,jArr);
 	 	 			result.put("msg", "삭제가 완료되었습니다.");
 	 	 			result.put("value", "true");
 	 			}else {
 	 	 			result.put("msg", "삭제할 System ID를 선택해주세요.");
 	 	 			result.put("value", "false");
 	 				
 	 			}
 			}else {

 	 			result.put("msg", "삭제할 System ID를 선택해주세요.");
 	 			result.put("value", "false");
 			}
 			
 		}catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
 				
 		
 		return result;
 	}
 	
 	/*
 	 * 파일 경로에서 xml파일을 열어서 jsonArray로 변환한 후 리턴
 	 * */
 	private static JSONArray getXmlFilejarr(String filePath){
 		
 		JSONArray jArr = new JSONArray();
 		BufferedReader br = null;
 		StringBuffer sb =  new StringBuffer();
 		
 		//System.out.println("start getXmlFilejarr()~~!");
 		
 		try{
			//Feeder_DB_Connection_Info.xml을 열어 내용 받아오기
			File file = new File(filePath);
			if (file.exists()) {
				String read = "";
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				
				while ((read = br.readLine()) != null) {
					sb.append(read);			       
				}
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));	

				NodeList sysList = doc.getElementsByTagName("system");
				
				for (int i=0; i<sysList.getLength(); i++) {

					JSONObject jObj = new JSONObject();

					Node nodeSystem = sysList.item(i);
					Element tmp = (Element) sysList.item(i);
					
					jObj.put("id", tmp.getAttribute("id"));
					jObj.put("desc", tmp.getAttribute("desc"));
					jObj.put("type", tmp.getAttribute("type"));

					NodeList feedList = nodeSystem.getChildNodes();
					for( int j=0; j<feedList.getLength(); j++) {
						jObj.put(feedList.item(j).getNodeName(), feedList.item(j).getTextContent().trim());						
					}
					
					jArr.add(jObj);
				}
				
			}
 		} catch (FileNotFoundException fe) {  logger.error(fe.getMessage(), fe.getCause());
		} catch (IOException ie) {            logger.error(ie.getMessage(), ie.getCause());
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}	finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                	logger.error(e.getMessage(), e.getCause());
                }
            }
        }
 		
 		
 		return jArr;		
 		
 	}
 	
 	/*
 	 * JSONArray를 xml 변환한 후 파일경로에 저장
 	 * */
 	private static Boolean setXmlFileSave(String filePath, JSONArray jArr) {
 		
 		BufferedWriter out = null;

 		try {
			out = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8") );

	 		StringBuffer sb = new StringBuffer();
	 		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+System.lineSeparator());
	 		sb.append("<feed>"+System.lineSeparator());
			for(int i=0; i<jArr.size() ;i++){

		 		sb.append("<system ");
		 		if(jArr.getJSONObject(i).has("id")) sb.append("id=\"").append(jArr.getJSONObject(i).getString("id")).append("\" ");
		 		if(jArr.getJSONObject(i).has("desc")) sb.append("desc=\"").append(jArr.getJSONObject(i).getString("desc")).append("\" ");
		 		if(jArr.getJSONObject(i).has("type")) sb.append("type=\"").append(jArr.getJSONObject(i).getString("type")).append("\" ");
		 		sb.append(">"+System.lineSeparator());
		 		
		 		for(Iterator<String> it=jArr.getJSONObject(i).keys(); it.hasNext();) {
		 			String strKey = it.next();
		 			if(strKey.equals("id") || strKey.equals("desc") || strKey.equals("type"))
		 				continue;
		 			if(strKey.equals("feed.db.schema")) {
		 				if(!jArr.getJSONObject(i).getString(strKey).isEmpty()) {
		 					sb.append("<"+strKey+">").append(jArr.getJSONObject(i).getString(strKey)).append("</"+strKey+">"+System.lineSeparator());	
			 			}
		 			}else {
		 				sb.append("<"+strKey+">").append(jArr.getJSONObject(i).getString(strKey)).append("</"+strKey+">"+System.lineSeparator());	
		 			}
			 			
		 		}
		 		
		 		sb.append("</system> "+System.lineSeparator()+System.lineSeparator());		
			}
	 		sb.append("</feed>");
	 		out.write(sb.toString());
	 		out.newLine();		 
			
			out.close();
			
		} catch (FileNotFoundException fe) {  logger.error(fe.getMessage(), fe.getCause());
		} catch (IOException ie) {            logger.error(ie.getMessage(), ie.getCause());
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}	finally {
            if (out != null) {
                try {
                	out.close();
                	out = null;
                } catch (IOException e) {
                	logger.error(e.getMessage(), e.getCause());
                }
            }
        }
 		
 		return true;
 		
 	}
 	
 	
 	public class IDAscCompare implements Comparator <Map<String, Object>> {
		
		@Override
		public int compare (Map<String, Object> arg0, Map<String, Object> arg1){
			return ((String) arg0.get("id")).compareTo((String) arg1.get("id"));
		}
	}
 	
 	public class IDDescCompare implements Comparator <Map<String, Object>> {
		
		@Override
		public int compare (Map<String, Object> arg0, Map<String, Object> arg1){
			return ((String) arg1.get("id")).compareTo((String) arg0.get("id"));
		}
	}
 		
}










