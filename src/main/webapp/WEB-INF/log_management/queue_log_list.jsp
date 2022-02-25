<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->
<div class="search_box">

				<table>
					<tbody>
						<tr>
							<td><span class="label">생성날짜</span></td>
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
							
							<td><span class="label">상태결과</span></td>
							<td>
							<select style="width:140px;" id="result_code">
							</select>
							</td>
						</tr>
						<tr>
							<td><span class="label">검색어</span></td>
							<td colspan="11">
							<table>
							<tr>
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
  
	<table class="grid_table">
		<colgroup>
			 <col style="width: 10%;">
			 <col style="width: 15%;">
			 <col style="width: 5%;">
			 <col style="width: 10%;">
			 <col style="width: 5%;">
			 <col style="width: 5%;">
			 <col style="width: 5%;">
			 <col style="width: 10%;">
			 <col style="width: 10%;">
			 <col style="width: 10%;">
			 <col style="width: 15%;">
		</colgroup>
		<thead>
			<tr>
				<th>회사코드</th>
				<th>인덱스</th>
				<th>시스템ID</th>
				<th>검색색인ID</th>
				<th>큐 KEY</th>
				<th>ACTION</th>
				<th>S_CODE</th>
				<th>생성날짜</th>
				<th>업데이트날짜</th>
				<th>상태값</th>
				<th>HIGHLIGHT</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->
<script src="/monitoring/js/log_management/queue_log_list.js"></script>

<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>