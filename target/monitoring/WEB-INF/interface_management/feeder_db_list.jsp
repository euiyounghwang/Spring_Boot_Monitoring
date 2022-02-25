<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:eval expression="@environment.getProperty('feed_db_xml.server.path')" var="feedServerPath" />
<spring:eval expression="@environment.getProperty('feed_db_xml.edit.ip')" var="feedEditIp" />
<c:set var="feedEditIp" value="${feedEditIp }"/>
<%
String feedEditIp = (String) pageContext.getAttribute("feedEditIp");
StringBuffer requestURL = request.getRequestURL();
String requestURLsub =  requestURL.toString().substring(7, requestURL.indexOf(":888"));
%>

<input type="hidden" id="feededitYN" name ="feededitYN" value="<%=(feedEditIp.contains(requestURLsub)|| requestURL.toString().contains("localhost"))?"Y":"N"%>">
<!-- #content -->

<div id="content">
<%
if(feedEditIp.contains(requestURLsub) 
		|| requestURL.toString().contains("localhost")
		){	
%>
  <div id="data_insert" style="display: none;">
    <div class="btns_bar">
    	<div class="left">
    		<button class="btn1" onclick="insertSystem();"><span>저장</span></button>
    		<button class="btn2" onclick="closeInsertDiv();"><span>취소</span></button>
    	</div>
    </div>
    <div class="write_box">
    
	<table>
		<colgroup>
			<col style="width:200px;">
			<col style="width:200px;">
			<col style="width:150px;">
			<col style="width:200px;">
			<col style="width:180px;">
			<col>
		</colgroup>
		
		<tbody>
			<tr>
				<th class="tar">*System ID</th>
				<td class="tac"><input type="text" id="id" name ="id" style="width: 100%;"></td>
				<th class="tar">*desc(설명)</th>
				<td class="tac"><input type="text" id="desc" name ="desc" style="width: 100%;"></td>
				<th class="tar">*Type(prd/test/dev)</th>
				<td class="tac"><input type="text" id="type" name ="type" style="width: 100%;"></td>
			</tr>
			<tr>
				<th class="tar">*feed.sub.sysid(인덱스명|설명,)</th>
				<td class="tac" colspan="5"><input type="text" id="feed_sub_sysid" name ="feed_sub_sysid" style="width: 100%;"></td>
			</tr>
			<tr>
				<th class="tar">*feed.db.type</th>
				<td class="tar"><input type="text" id="feed_db_type" name ="feed_db_type" style="width: 100%;"></td>
				<th class="tar">*feed.db.driver</th>
				<td class="tac" colspan="3"><input type="text" id="feed_db_driver" name ="feed_db_driver" style="width: 100%;"></td>
			</tr>
			<tr>
				<th class="tar">*feed.db.url</th>
				<td class="tac" colspan="5"><input type="text" id="feed_db_url" name ="feed_db_url" style="width: 100%;"></td>
			</tr>
			<tr>
				<th class="tar">*feed.db.user</th>
				<td class="tac"><input type="text" id="feed_db_user" name ="feed_db_user" style="width: 100%;"></td>
				<th class="tar">*feed.db.pwd</th>
				<td class="tac"><input type="text" id="feed_db_pwd" name ="feed_db_pwd" style="width: 100%;"></td>
				<th class="tar">feed.db.schema</th>
				<td class="tac"><input type="text" id="feed_db_schema" name ="feed_db_schema" style="width: 100%;"></td>
			</tr>
		</tbody>
	</table>
    
  	</div>
  </div>
<%}%>
  <div class="btns_bar">
  	<div class="left">
<%
if(feedEditIp.contains(requestURLsub) 
		|| requestURL.toString().contains("localhost")
		){	
%>
  		<button class="btn1" onclick="closeInsertDiv();openInsertDiv();"><span>등록</span></button>
  		<button class="btn2" id="btn_delete" disabled><span>삭제</span></button>
  		<span class="sep"></span>
 <%}%>
  		<button class="btn2" id="btn_refresh"><span>새로고침</span></button>
  		  		<span class="sep"></span>
  		<button class="btn3" id="btn_link"><span>Feeder_DB_Connection_Info.xml 보기</span></button>
<%
if(feedEditIp.contains(requestURLsub) 
		|| requestURL.toString().contains("localhost")
		){	
%>
  		<button class="btn2" id="btn_filedownload"><span>xml파일 다운로드</span></button>
  		 <%}%>
	  	</div>
	  	
	  	<div class="right">
<%
if(feedEditIp.contains(requestURLsub) 
		|| requestURL.toString().contains("localhost")
		){	
%>
  		<form id ="fileForm" method ="post" enctype ="multipart/form-data" action="">
		    <div class="filebox bs3-primary">
				<input class="upload-name" value="" disabled="disabled">
		
				<label for="file">파일선택</label> 
			    <input type="file" name ="file" id="file" class="upload-hidden"> 
				<input type="hidden" id="server_cd_tp" name="server_cd_tp" value="${cd_tp}"/>
				<input type="hidden" id="server_company_code" value="${company_code}"/>
			    <label for="btn_upload">파일업로드</label> 
			    <input type="button" id="btn_upload" class="upload-hidden"/> 
			</div>
	    </form>
  		 <%}else{
	 %>
	※ xml 파일 수정은 ${feedEditIp } 에서만 할 수 있습니다.</br>
	&nbsp;&nbsp;&nbsp;89번 서버의 파일은 매분마다 134번 서버로 동기화 됩니다.
	 <%} %>
	    <!--파일 다운로드 처리를 위한 form과 hidden iframe-->
  		<form name ="fileDownForm" method ="post" action="">
				<input type="hidden" id="filename" name="filename" value="${feedServerPath }"/>
  		</form>
  		<iframe name="fileDown" style='visibility:hidden' src="" width="1" height="1"></iframe>
	
  	</div>
  	<div class="right">
  	</div>
  </div>
	<table class="grid_table">
		<colgroup>
			<col style="width:60px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:60px;">
			<col style="width:450px;">
			<col style="width:100px;">
			<col style="width:220px;">
			<col style="width:320px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:120px;">			
			<col style="width:1px;">
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" id="check-all-menu"></th>
				<th>System ID</th>
				<th>desc(설명)</th>
				<th>Type</th>
				<th>feed.sub.sysid(인덱스명|설명,)</th>
				<th>feed.db.type</th>
				<th>feed.db.driver</th>
				<th>feed.db.url</th>
				<th>feed.db.user</th>
				<th>feed.db.pwd</th>
				<th>feed.db.schema</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->
<script src="/monitoring/js/interface_management/feeder_db_list.js"></script>