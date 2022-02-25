<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->

<div id="content">
  <div id="data_insert" style="display: none;">
    <div class="btns_bar">
    	<div class="left">
    		<button class="btn1" onclick="insertMenu();"><span>저장</span></button>
    		<button class="btn2" onclick="closeInsertDiv();"><span>취소</span></button>
    	</div>
    </div>
    <div class="write_box">
    	<table>
    		<colgroup>
    			<col style="width:80px;">
    			<col style="width:120px;">
    			<col style="width:140px;">
    			<col style="width:400px;">
    			<col style="width:400px;">
    			<col style="width:90px;">
    			<col style="width:160px;">
    			<col style="width:80px;">
    			<col style="width:120px;">
    			<col>
    		</colgroup>
    		<thead>
    			<tr>
    				<th>회사코드</th>
    				<th>CD_TP</th>
    				<th>*CD_TP1</th>
    				<th>CD_TP_MEANING</th>
    				<th>CD_V_EXPLAIN</th>
    				<th>FK_CD_TP</th>
    				<th>SEARCH_SORT_SEQ</th>
    				<th>DEPTH</th>
    				<th>ACTIVE_FLAG</th>
        	</thead>
    		<tbody>
    			<tr>
    				<td class="tac">00</td>
    				<td class="tac">ES_MENU_META</td>
    				<td class="tac"><input type="text" id="cd_tp1" name ="cd_tp1" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="cd_tp_meaning" name ="cd_tp_meaning" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="cd_v_explain" name ="cd_v_explain" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="fk_cd_tp" name ="fk_cd_tp" style="width: 100%;"></td>
    				<td class="tar"><input type="text" id="search_sort_seq" name ="search_sort_seq" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="depth" name ="depth" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="active_flag" name ="active_flag" style="width: 100%;"></td>
    			</tr>
    		</tbody>
    	</table>
  	</div>
  </div>	
  
  <div class="btns_bar">
  	<div class="left">
  		<button class="btn1" onclick="closeInsertDiv();openInsertDiv();"><span>등록</span></button>
  		<button class="btn2" id="btn_delete" disabled><span>삭제</span></button>
  		<span class="sep"></span>
  		<button class="btn2" id="btn_refresh"><span>새로고침</span></button>
  	</div>
  	<div class="right">
  	</div>
  </div>

	<table class="grid_table">
		<colgroup>
			<col style="width:60px;">
			<col style="width:100px;">
			<col style="width:150px;">
			<col style="width:150px;">
			<col style="width:350px;">
			<col style="width:350px;">
			<col style="width:120px;">
			<col style="width:180px;">
			<col style="width:100px;">
			<col style="width:175px;">
			<col style="width:160px;">
			<col style="width:140px;">
			<col style="width:140px;">
			<col style="width:120px;">
			<col style="width:120px;">
			<col style="width:120px;">
			<col style="width:120px;">
			<col style="width:200px;">
			<col style="width:230px;">
			<col>
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" id="check-all-menu"></th>
				<th>회사코드</th>
				<th>CD_TP</th>
				<th>CD_TP1</th>
				<th>CD_TP_MEANING</th>
				<th>CD_V_EXPLAIN</th>
				<th>FK_CD_TP</th>
				<th>SEARCH_SORT_SEQ</th>
				<th>DEPTH</th>
				<th>START_ACTIVE_DATE</th>
				<th>END_DATE_ACTIVE</th>
				<th>ACTIVE_FLAG</th>
				<th>ATTRIBUTE1</th>
				<th>ATTRIBUTE2</th>
				<th>ATTRIBUTE3</th>
				<th>ATTRIBUTE4</th>
				<th>ATTRIBUTE5</th>
				<th>CREATION_TIMESTAMP</th>
				<th>LAST_UPDATE_TIMESTAMP</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>

<script src="/monitoring/js/code/menu_attr_list.js"></script>