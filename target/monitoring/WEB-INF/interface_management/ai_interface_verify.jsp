<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- #content -->
<div id="content">	
	<!-- .ifmoniter -->
	<div class="ifmoniter col2">
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>검출할 문장&nbsp;&nbsp;&nbsp;</h2>
				<div  class="connter">
				<textarea name="textParam" id="textParam" rows="17" style="width: 99%;">
제 1 장   총  칙

 제 1조 [목 적]
① 본 계약은 포스코의 조업안정과 경쟁력을 확보하고 수탁운영사의 자율경영과 책임작업 수행체제를 정착하기 위하여 계약기간 동안 상호간에 준수 하여야 할 사항을 규정하기 위함이다.
② 본 계약은 포스코의 마그네슘제련공장 등 위탁설비(이하 “위탁설비”)의 운영, 가동 및 유지, 보전에 필요한 일상점검, 정기점검, 수리 등 위탁업무 전반에 대해 포스코와 수탁운영사간의 상호 역할과 책임, 사고발생시 대응책임과 손해배상, 생산성 향상을 위한 성과보상의 범위에 관하여 규정한다.

제 2조 [용어정의]
① 위탁운영 : 마그네슘제련공장 제품생산을 위한 공장설비의 주공정 운전작업 및 이에 수반되는  일체의 부대작업을 수탁운영사가 수행하는 것을 의미하며  그와 관련된 외주협력 또는 외주작업을 포함한다.
② 자본적지출 : 고정자산의 내용연수를 연장시키거나 자산의 가치를 증가시키는 수선비를 말한다.
③ 수익적지출 : 설비의 정상적인 사용 중에 발생하는 원상 회복 및 일상적인 정비비를 말한다.
				</textarea>
				</div>
				<div><strong>검색엔진 주소</strong><br/>
			<input type="text" id="req_elaServer" name="req_elaServer" value="http://10.132.17.69:5001/law_interface_results" style="width:100%"/><br/>
			<select name="req_site" id="req_site" style="width:70%">
			<option value='posco'>포스코약관공정화</option>
			<option value='enc'>포스코건설약관공정화</option>
			<option value='chemtech'>포스코케미컬약관공정화</option>
			</select>
			
			<button type="button" id="ask" class="btn1">검출 요청 >> (Ctrl+Enter)</button>
				</div>
			</div>
		</div>
		<!-- //.panel -->
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>검출결과</h2>
				<div  class="connter">
  				
	<table class="grid_table">
		<colgroup>
			 <col style="width: 90px;">
			 <col style="width: 90px;">
			 <col>
		</colgroup>
		<thead>
			<tr>
				<th>predict</th>
				<th>probability</th>
				<th>검출내용</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
				</div>
				</div>
			</div>
		<!-- //.panel -->
	</div>
</div>

<script src="/monitoring/js/interface_management/ai_interface_verify.js"></script>