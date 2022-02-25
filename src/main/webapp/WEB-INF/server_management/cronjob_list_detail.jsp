<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->  
<div id="content">
  <div class="btns_bar">
  	<div class="left">
  	<span class="last_info">실행할 프로세스</span>
  	<input type="text" value="" id="RUN_COMMAND" name="RUN_COMMAND" style="width:600px">
	<input type="hidden" id="RUN_MODE" name="RUN_MODE" value="cronjob_list_detail"/>
  	<button class="btn2" onclick="runCommand();"><span>실행</span></button>
  	</div>
  	<div class="right">
  		<button class="btn2" onclick="loadCenterPage('server_management/cronjob_list', 'Y');"><span>목록으로</span></button>
  		<span class="sep"></span>
  		<button class="btn1" onclick="addRow('','')"><span>행추가</span></button>
  		<button class="btn2" onclick="deleteRow()"><span>행삭제</span></button>
  		<span class="sep"></span>
  		<button class="btn2" onclick="saveCronJob()"><span>저장</span></button>
  	</div>
  </div>
  <h2>CronJob 상세 조회</h2>
  <form id="searchFormCronJob" name="searchFormCronJob" class="forBackupCrontabFormClass">
	<input type="hidden" id="HOST_NAME" name="HOST_NAME" value="${host_name}"/>
	<input type="hidden" id="REAL_IP" name="REAL_IP" value="${real_ip}"/>
	<input type="hidden" id="USER_ID" name="USER_ID" value="${user_id}"/>
	<input type="hidden" id="USER_PW" name="USER_PW" value="${user_pw}"/>
	<table class="grid_table">
		<colgroup>
			<col style="width:60px;">
			<col style="width:300px;">
			<col>
		</colgroup>
		<thead>
			<tr>
				<th></th>
				<th>배치시간</th>
				<th>프로세스</th>
			</tr>
		</thead>
		<tbody  id="my-tbody">		
		</tbody>
	</table>
  </form>
</div>

<script src="/monitoring/js/server_management/cronjob_list_detail.js"></script>