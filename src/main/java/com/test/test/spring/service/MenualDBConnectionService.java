package com.test.test.spring.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class MenualDBConnectionService {
	public Map<String, Object> excuteSql(Map<String, Object> params){
		String[] host_info = params.get("host_info").toString().split(",");
		Map<String, Object> result = new HashMap<String, Object>();
		long begTime = System.currentTimeMillis();

        String driver = host_info[0];
        String url = host_info[1];
        String dbType = url.split(":")[1];
        String username = host_info[2];
        String password = host_info[3];
        String sSqlSource = params.get("QUERY").toString().trim()!=null ? params.get("QUERY").toString().trim() : "";
        String sErrMsg = null;
        String queryType = sSqlSource.substring(0, 6);
        Connection connection = null;
        Connection connection2 = null;
        try {
            connection = createConnection(driver, url, username, password);
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println(meta.getDatabaseProductName());
            

//            System.out.println("# rows inserted: " + numRowsUpdated);
//            System.out.println("after insert: " + query(connection, sqlQuery, Collections.EMPTY_LIST));

			System.out.println("~~ params.get(header)="+params.get("header"));
			System.out.println("~~ queryType.toUpperCase()="+queryType.toUpperCase());
            if("header".equals(params.get("header"))) {//테이블 header라인 그리기 위한 로직
            	if("oracle".equals(dbType)) {
	            	sSqlSource = "SELECT A.* FROM (" + sSqlSource;
	    			sSqlSource += ") A WHERE ROWNUM<2 ";
            	} else if("mysql".equals(dbType)) {
            		sSqlSource = "SELECT A.* FROM (" + sSqlSource;
	    			sSqlSource += " ) A LIMIT 2";
            	} else if("postgresql".equals(dbType)) {
            		sSqlSource = "SELECT A.* FROM (" + sSqlSource;
	    			sSqlSource += " ) A LIMIT 2";
            	}
    			System.out.println(sSqlSource);
            	try {
            		List<Map<String, Object>> selectResult =  query(connection, sSqlSource, Collections.EMPTY_LIST);

					System.out.println("selectResult="+selectResult.toString());    
					
					if(selectResult.size()!=0) {
    					for(Map<String,Object> map : selectResult){
	    					for (String key : map.keySet()) { 
	    						if(map.get(key)!=null && (map.get(key).getClass().getName().equals("oracle.sql.TIMESTAMP") || map.get(key).getClass().getName().equals("java.sql.Timestamp"))) {
 
	    				        	java.sql.Timestamp ts2 = null;
    								String ts3 = null;
	    				           if("oracle.sql.TIMESTAMP".equals(map.get(key).getClass().getName())){
	    				            	oracle.sql.TIMESTAMP ts = (oracle.sql.TIMESTAMP) map.get(key);
	    								ts2 = ts.timestampValue();
	    				           } else if("java.sql.Timestamp".equals(map.get(key).getClass().getName())){
	    				        	   ts2 = (java.sql.Timestamp) map.get(key);
	    				           }
	    				           if(ts2 !=null) {
	    								 ts3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts2);
	    								 map.put(key, ts3);		    				        	   
	    				           }

	    				         }  
	    					}
	    				}
    				}
            		
            		List<String> keys = new ArrayList<String>(selectResult.get(0).keySet());
            		List<String> clobKeys = new ArrayList<String>();
            		for(String key: keys){
            			if(selectResult.get(0).get(key)==null) {
            				selectResult.get(0).put(key, "");
            			}
            			if("oracle.sql.CLOB".equals(selectResult.get(0).get(key).getClass().getName())){
           				 	clobKeys.add(key);
           			 	}
            		}
            		
        			if(selectResult.size()!=0) {
	    				for(Map<String,Object> map : selectResult){
	    					for(String key: clobKeys){
	    				         if(map.get(key) != null) {
    								 String sb = convertToString((java.sql.Clob)map.get(key));
	    				             map.put(key, sb);
	    				         } 
	    					}
	    				}
        			}
            		
    				result.put("data", selectResult);
    			} catch (Exception e) {
    				sErrMsg = e.getMessage();
    				result.put("errMsg", sErrMsg );
    			}
            }else if("SELECT".equals(queryType.toUpperCase())){//내용 조회를 위해 한번 더 반복
            	String sSqlSource2 = ""; 
            	if("oracle".equals(dbType)) {
	            	sSqlSource = "SELECT * FROM (SELECT A.*, COUNT(*) OVER() AS TOTCNT FROM (" + sSqlSource;
	    			sSqlSource += ") A) B WHERE ROWNUM<=" + (Integer) params.get("length");
            	} else if("mysql".equals(dbType)) {
            		sSqlSource = "SELECT A.*, (SELECT COUNT(1) FROM ("+sSqlSource+") B) TOTCNT FROM (" + sSqlSource;
	    			sSqlSource += " ) A LIMIT " + (Integer) params.get("length");
            	} else if("postgresql".equals(dbType)) {
            		connection2 = createConnection(driver, url, username, password);
            		sSqlSource2 = "SELECT COUNT(*) TOTCNT FROM (" + sSqlSource +") AS A";
            		
            		sSqlSource = "SELECT A.* FROM (" + sSqlSource;
	    			sSqlSource += " ) AS A LIMIT "+ (Integer) params.get("length");
	    			
            	}
    			System.out.println("########################"+sSqlSource2);
    			try {
    				List<Map<String, Object>> selectResult =  query(connection, sSqlSource, Collections.EMPTY_LIST);
    				List<Map<String, Object>> selectResult2 = new ArrayList<Map<String, Object>>();
					if("postgresql".equals(dbType))
						selectResult2 = query(connection2, sSqlSource2, Collections.EMPTY_LIST);
    				if(selectResult.size()!=0) {
    					for(Map<String,Object> map : selectResult){
	    					for (String key : map.keySet()) {
	    						if(map.get(key)!=null && (map.get(key).getClass().getName().equals("oracle.sql.TIMESTAMP") || map.get(key).getClass().getName().equals("java.sql.Timestamp"))) {
	    				        	java.sql.Timestamp ts2 = null;
    								String ts3 = null;
	    				           if("oracle.sql.TIMESTAMP".equals(map.get(key).getClass().getName())){
	    				            	oracle.sql.TIMESTAMP ts = (oracle.sql.TIMESTAMP) map.get(key);
	    								ts2 = ts.timestampValue();
	    				           } else if("java.sql.Timestamp".equals(map.get(key).getClass().getName())){
	    				        	   ts2 = (java.sql.Timestamp) map.get(key);
	    				           }
	    				           if(ts2 !=null) {
	    								 ts3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts2);
	    								 map.put(key, ts3);		    				        	   
	    				           }
	    				         }  
	    					}
	    				}
    				}
    				List<String> keys = new ArrayList<String>(selectResult.get(0).keySet());
            		List<String> clobKeys = new ArrayList<String>();
            		for(String key: keys){
            			if(selectResult.get(0).get(key)==null) {
            				selectResult.get(0).put(key, "");
            			}
            			if("oracle.sql.CLOB".equals(selectResult.get(0).get(key).getClass().getName())){
           				 	clobKeys.add(key);
           			 	}
            		}
            		
        			if(selectResult.size()!=0) {
	    				for(Map<String,Object> map : selectResult){
	    					for(String key: clobKeys){
	    				         if(map.get(key) != null) {
    								 String sb = convertToString((java.sql.Clob)map.get(key));
	    				             map.put(key, sb);
	    				         } 
	    					}
	    				}
        			}
    				result.put("draw", params.get("draw"));
    				
    				if(selectResult.size()>0) {
    					if("postgresql".equals(dbType)) {
	    					result.put("recordsTotal", selectResult2.get(0).get("totcnt"));
	    					result.put("recordsFiltered",selectResult2.get(0).get("totcnt"));
    					} else {
    						result.put("recordsTotal", selectResult.get(0).get("TOTCNT"));
	    					result.put("recordsFiltered",selectResult.get(0).get("TOTCNT"));
    					}
    				} else {
    					result.put("recordsTotal", 0);
    					result.put("recordsFiltered",0);
    				}
    				result.put("data", selectResult);
    			} catch (Exception e) {
    				sErrMsg = e.getMessage();
    				result.put("errMsg", sErrMsg );
    			}
    		} else if("DELETE".equals(queryType.toUpperCase())){
    			System.out.println(sSqlSource);
    			try {
    				connection.setAutoCommit(false);
    	            int row = update(connection, sSqlSource, Collections.EMPTY_LIST);
    	            connection.commit();
    	            result.put("result" , "전체 "+ row+ "건 이 삭제 되었습니다.");
    			} catch (Exception e) {
    				sErrMsg = e.getMessage();
    				result.put("errMsg", sErrMsg );
    			}
    		} else if("UPDATE".equals(queryType.toUpperCase())){
    			System.out.println(sSqlSource);
    			try {
    				connection.setAutoCommit(false);
    	            int row = update(connection, sSqlSource, Collections.EMPTY_LIST);
    	            connection.commit();
    				result.put("result" , "전체 "+ row + "건 이 업데이트 되었습니다.");
    			} catch (Exception e) {
    				sErrMsg = e.getMessage();
    				result.put("errMsg", sErrMsg );
    			}
    		} else { //INSERT
    			System.out.println(sSqlSource);
    			try {
    				connection.setAutoCommit(false);
    	            int row = update(connection, sSqlSource, Collections.EMPTY_LIST);
    	            connection.commit();
    				result.put("result" , "전체 "+ row+ "건 이 등록 되었습니다.");
    			} catch (Exception e) {
    				sErrMsg = e.getMessage();
    				result.put("errMsg", sErrMsg );
    			}
    		}
        } catch (Exception e) {
            rollback(connection);
            rollback(connection2);
            e.printStackTrace();
        } finally {
            close(connection);
            close(connection2);
            long endTime = System.currentTimeMillis();
            System.out.println("wall time: " + (endTime - begTime) + " ms");
        }
        
        return result;
	}

	public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
	    Class.forName(driver);
	    if ((username == null) || (password == null) || (username.trim().length() == 0) || (password.trim().length() == 0)) {
	        return DriverManager.getConnection(url);
	    } else {
	        return DriverManager.getConnection(url, username, password);
	    }
	}
	
	public static void close(Connection connection) {
	    try {
	        if (connection != null) {
	            connection.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public static void close(Statement st) {
	    try {
	        if (st != null) {
	            st.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void close(ResultSet rs) {
	    try {
	        if (rs != null) {
	            rs.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void rollback(Connection connection) {
	    try {
	        if (connection != null) {
	            connection.rollback();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static List<Map<String, Object>> map(ResultSet rs) throws SQLException {
	    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
	    try {
	        if (rs != null) {
	            ResultSetMetaData meta = rs.getMetaData();
	            int numColumns = meta.getColumnCount();
	            while (rs.next()) {
	                Map<String, Object> row = new HashMap<String, Object>();
	                for (int i = 1; i <= numColumns; ++i) {
	                    String name = meta.getColumnName(i);
	                    Object value = rs.getObject(i);
	                    row.put(name, value);
	                }
	                results.add(row);
	            }
	        }
	    } finally {
	        close(rs);
	    }
	    return results;
	}
	
	public static List<Map<String, Object>> query(Connection connection, String sql, List<Object> parameters) throws SQLException {
	    List<Map<String, Object>> results = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        ps = connection.prepareStatement(sql);
	
	        int i = 0;
	        for (Object parameter : parameters) {
	            ps.setObject(++i, parameter);
	        }
	        rs = ps.executeQuery();
	        results = map(rs);
	    } finally {
	        close(rs);
	        close(ps);
	    }
	    return results;
	}
	
	public static int update(Connection connection, String sql, List<Object> parameters) throws SQLException {
	    int numRowsUpdated = 0;
	    PreparedStatement ps = null;
	    try {
	        ps = connection.prepareStatement(sql);
	
	        int i = 0;
	        for (Object parameter : parameters) {
	            ps.setObject(++i, parameter);
	        }
	        numRowsUpdated = ps.executeUpdate();
	    } finally {
	        close(ps);
	    }
	    return numRowsUpdated;
	}
	
	private String convertToString(java.sql.Clob data)
	{
	    final StringBuilder builder= new StringBuilder();

	    try
	    {
	        final Reader         reader = data.getCharacterStream();
	        final BufferedReader br     = new BufferedReader(reader);

	        int b;
	        while(-1 != (b = br.read()))
	        {
	            builder.append((char)b);
	        }

	        br.close();
	    }
	    catch (SQLException e)
	    {        
	        return e.toString();
	    }
	    catch (IOException e)
	    {
	        return e.toString();
	    }
	    return builder.toString();
	}
}










