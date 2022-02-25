<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="btns_bar fixed">
	<div class="right">
		<button class="btn1" onclick="save_configuration();"><span>저장</span></button>
	</div>
</div>

<!-- #content -->
<div id="content">
<!-- 	<h2>설정</h2> -->
	
	<table class="rd_info">
		<colgroup>
			<col style="width:1px;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>검색엔진 서버 IP 설정</th>
				<td><span></span><select id="sel_ela_ip" style="width: 400px;"></select></td>
			</tr>
			<tr>
				<th></th>
				<td></td>
			</tr>
		</tbody>
	</table>

</div>
<!-- //#content -->

<script src="/monitoring/js/configuration/configure_upsert.js"></script>