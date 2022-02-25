<%--
/* -----------------------------------------------------------------------------
 * PROJECT : M02(검색엔진 모니터링 시스템)
 * FileName : provideIf000.jsp
 *******************************************************************************
 * 시스템별 인터페이스 검증
 *******************************************************************************
 *
 * Program History
 * ------------------------------------------------------------------------------
 * Date             Changed In      Description
 * ------------------------------------------------------------------------------
 * 2017-12-08       박현진                          최초 작성
 * 2018-09-12		이용선			POSAPIA IF 추가
 * 2018-10-12		이용선			HR Search 파라메타 검증시스템 버그 수정
 * 2018-12-04     박성은			SPRING 프로젝트로 컨버팅 
 * ------------------------------------------------------------------------------
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <div class="search_box"  id="guide_view" style="display: none;">
	<table>
		<colgroup>
			<col span="4">
			<col style="width:13px;">
			<col span="5">
		</colgroup>
		<tbody>
			<tr>
				<td><b>HRSearch 필수파라미터</b></br>
				es_keyword : 검색키워드 / es_page_idx : 페이지인덱스  </br>
es_auth : 검색권한 / es_req_id : 키워드검색 or 상세검색 ex)[각각 1 or 3] </br>
es_sys_id : 시스템아이디 ex)[ict_hrsearch_idx or posco_hrsearch] </br>
es_comp_cd : 회사코드 ex)[각각 01 or 30] </br> es_num : 페이지당 보여질 검색 결과 수 </br>
es_dept_cd : 부서코드 / es_emp_no : 사원번호 / es_user_id : 사원id </br>
HR.STATUS : 재직 OR 퇴직 ex)[각각 1 or 3]
  </td>
				<td><b>POSAPIA 필수파라미터</b><br/>
				es_comp_cd : 회사코드 / es_emp_no :	사용자직번</br>
				es_user_id : 사용자ID(메일ID)  / es_sys_id : 시스템아이디 ex)[posco_posapia] </br>
				es_page_idx : 페이지번호 / es_num :	한 페이지당 요청 건수</br>
es_keyword : 통합검색 키워드 / es_req_id :	검색요청ID ex)[통합검색:1, 상세검색:2]</br>
es_search_type : 기본검색방법	ex)[E:정확, S:유사, A:정확+유사 혼용]</br>
es_exact_fields :	정확히검색 대상인 필드명(es_search_type:A 일때만 사용)</br>ex)["필드명01","필드명02",..]
</td>
				<td><b>PATT 필수파라미터</b><br/>
				es_comp_cd : 회사코드 / es_emp_no :	사용자직번</br>
				es_user_id :	사용자ID(메일ID) / es_dept_cd : 부서코드</br>
es_sys_id : 시스템아이디 ex)[patt] / es_page_idx : 페이지번호 </br>
es_num :	한 페이지당 요청 건수 / es_keyword : 통합검색 키워드 </br>
es_prefix_type : 메뉴코드 ex)[PATT_BASE, PATT_INFO_C, PATT_INFO_P]</br>
es_operator : [must, should] </br>

</td>
			</tr>
		</tbody>
	</table>
</div>
    <div class="btns_bar">
    	<div class="left">
    		<button class="btn1" onclick="$('#guide_view').show();"><span>필수파라메타 보기</span></button>
    		<button class="btn2" onclick="$('#guide_view').hide();"><span>필수파라메타 접기</span></button>
    	</div>
    </div>
<!-- #content -->
<div id="content">	
	<!-- .ifmoniter -->
	<div class="ifmoniter">
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>parameter 입력&nbsp;&nbsp;&nbsp;</h2>
				<button class="btn1" id="json_beautify">Data정렬</button>
				<button class="btn1"  id="json_compress">한줄로</button>
				<div  class="connter">
				<textarea name="jsonParam" id="jsonParam" rows="20" style="width: 99%;">{
    "es_keyword": "이현",
    "es_page_idx": "1",
    "es_auth": "1",
    "es_req_id": "1",
    "es_sys_id": "ict_hrsearch_idx",
    "es_comp_cd": "01",
    "es_num": "10",
    "es_dept_cd": "38_UA000",
    "es_emp_no": "946",
    "es_user_id": "285264",
    "HR.STATUS": "1"
}</textarea>
				</div>
				<div><strong>ex)172.31.247.249:8081, uswpes.posco.net:7091, uswpsso.posco.net/000P00009</strong><br/>
			<input type="text" id="req_was" name="req_was" value="uswpes.posco.net:7091" style="width:40%"/>
			
			<select name="req_chain" id="req_chain" style="width:14%">
			<option value='U40A'>U40A</option>
			<option value='monitoring'>monitoring</option>
			</select>
				
			<select name="req_site" id="req_site" style="width:28%">
			<option value='ict_hrsearch'>ict_hrsearch</option>
			<option value='posco_hrsearch'>posco_hrsearch</option>
			<option value='posco_posapia'>posco_posapia</option>
			<option value='posco_e-catalog'>posco_e-catalog</option>
			<option value='patt'>patt</option>
			<option value='dataprovider_citizen'>dataProvider CITIZEN</option>
			<option value='dataprovider_prop'>dataProvider PROP</option>
			</select>
			
			<button type="button" id="ask" class="btn1">요청</button><br/>
			<input type="text" id="otherParam" name="otherParam" value="" style="width:95%"/>
			
				</div>
			</div>
		</div>
		<!-- //.panel -->
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>검색결과</h2>
				<div class="connter" ><textarea id="resulttxtarea" rows="24" style="width: 99%;"></textarea>
				</div>
			</div>
		</div>
		<!-- //.panel -->
		<!-- .panel -->
		<div class="panel">
			<div class="section half" >
				<h2>elastic_query.xml에<br> 정의되지 않은 <br>상세 META</h2>
				<div  class="connter">
  				<ol class="q" id="nonMeatList">
  				</ol>
				</div>
			</div>
			
			<div class="section half">
				<h2>전송 요청 값</h2>				
				<div  class="connter">
  				<ol class="q" id="metaList">
  				</ol>
				</div>
			</div>
		</div>
		<!-- //.panel -->
	</div>
</div>


<script src="/monitoring/js/interface_management/interface_verify.js"></script>