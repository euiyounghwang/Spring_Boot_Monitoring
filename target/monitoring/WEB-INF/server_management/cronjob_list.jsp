<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->
<div class="search_box">
	<table>
		<tbody>
			<tr>
				<td><span class="label">서버환경</span></td>
				<td>
				<select style="width:140px;" id="sel_infra_type">
				</select>							
				</td>
				<td><span class="label">호스트</span></td>
				<td>
				<select style="width:140px;" id="sel_host">
				</select>
				</td>
				<td><button class="btn1" id="btn_refresh">조회</button></td>
				</tr>
		</tbody>
	</table>
</div>	
<br>
  
<div id="content">

	<table class="grid_table">
		<colgroup>
			<col style="width:120px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:130px;">
			<col style="width:120px;">
			<col style="width:120px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col>
			<col style="width:150px;">
		</colgroup>
		<thead>
			<tr>
				<th>SERVER_TYPE</th>
				<th>INFRA_TYPE</th>
				<th>HOST_NAME</th>
				<th>SERVER_NAME</th>
				<th>POSCO_NAT_IP</th>
				<th>REAL_IP</th>
				<th>SERVICE_IP</th>
				<th>USER ID</th>
				<th>USER PW</th>
				<th>DESCRIPTION</th>
				<th>CronJob 상세 조회</th>
			</tr>
		</thead>
		<tbody>		
		</tbody>
	</table>
</div>

<script src="/monitoring/js/server_management/cronjob_list.js"></script>