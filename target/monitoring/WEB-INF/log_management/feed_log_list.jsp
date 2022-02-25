<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->
<div class="search_box">
				<table>
					<tbody>
						<tr>
							<td><span class="label">날짜</span></td>
							<td><input type="text" class="datetime" id="startDate"></td>
							<td>-</td>
							<td><input type="text" class="datetime" id="endDate"></td>
							
							<td><span class="label">서버환경</span></td>
							<td>
							<select style="width:140px;" id="sel_server_conf">
							</select>
							</td>
							
							<td><span class="label">호스트</span></td>
							<td>
							<select style="width:140px;" id="sel_host">
							</select>
							</td>
							
							<!-- <td><span class="label">성공여부</span></td>
							<td>
							<select style="width:140px;" id="success">
							</select>
							</td> -->
						</tr>
						<tr>
							<td><span class="label">검색어</span></td>
							<td colspan="5">
								<table>
									<tr>
										<td>
											<select style="width:140px;" id="search_type"></select>
										</td>
										<td  style="width:350px;"><input type="text" style="width:350px;" id="input_search"/></td>
										<td><button class="btn1" id="btn_refresh">조회</button></td>
									</tr>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
</div>	
<br>
  
<div id="content">
  <!-- 생성일시(START_DATE), 업데이트일시(END_DATE), 서버환경, 시스템구분, 호스트(HOST), REAL IP(HOST_IP), JOB명(제외),  ACTION(ACTION),  상태값, 컨텐츠ID(_id), LOG LEVEL, LOG MESSAGE(MESSAGE), ,HIGHLIGHT -->
	<table class="grid_table">
		<colgroup>
			 <col style="width: 150px;">
			 <col style="width: 120px;">
			 <col style="width: 60px;">
			 <col style="width: 60px;">
			 <col style="width: 120px;">
			 <col style="width: 120px;">
			 <col style="width: 40px;">
			 <col style="width: 40px;">
			 <col style="width: 40px;">
			 <col style="width: 100px;">
			 <col style="width: 40px;">
			 <col style="width: 120px;">
		</colgroup>
		<thead>
			<tr>
				<th>생성일시</th>
				<th>업데이트일시</th>
				<th>서버환경</th>
				<th>시스템구분</th>
				<th>호스트</th>
				<th>REAL IP</th>
				<th>JOB명</th>
				<th>ACTION</th> 
				<th>상태값</th>
				<th>컨텐츠ID</th>
				<th>LOG LEVEL</th>
				<th>HIGHLIGHT</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->

<script src="/monitoring/js/log_management/feed_log_list.js"></script>

<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>