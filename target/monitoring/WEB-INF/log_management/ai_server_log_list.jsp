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
							
							<td><span class="label">시스템명</span></td>
							<td>
							<select style="width:180px;" id="sel_system_id">
 							<option value="" selected>전체</option>
 							<option value="AI_JEAN_LOG">포스코 AI중복문서(제안)</option>
 							<option value="ICT_CONTRACT_LOG">감사포털(ICT)</option>
 							<option value="POSCOMPLY_LOG_17">약관공정화(건설)</option>
 							<option value="POSCOMPLY_LOG_02">약관공정화(케미칼)</option>
    				  </select>
							
							</td>
							<td><span class="label">로그레벨</span></td>
							<td>
							<select style="width:140px;" id="sel_log_level">
							<option value="" selected>전체</option>
 							<option value="INFO">INFO</option>
 							<option value="ERROR">ERROR</option>
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
			 <col style="width: 150px;">
			 <col style="width: 120px;">
			 <col style="width: 90px;">
			 <col style="width: 90px;">
			 <col style="width: 90px;">
			 <col style="width: 100px;">
			 <col style="width: 100px;">
			 <col style="width: 100px;">
			 <col style="width: 60px;">
		</colgroup>
		<thead>
			<tr>
				<th>인덱스</th>
				<th>ID</th>
				<th>LOG PATH</th>
				<th>EMP_NO</th>
				<th>LOGIN_ID</th>
				<th>HOST</th>
				<th>HANDLER</th>
				<th>TIMESTAMP</th>
				<th>LOG LEVEL</th> 
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->

<script src="/monitoring/js/log_management/ai_server_log_list.js"></script>

<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>