 <%--
/* -----------------------------------------------------------------------------
 * PROJECT : monitoring(검색엔진 모니터링 시스템)
 * FileName : interface_provideif.jsp
 *******************************************************************************
 * provideAIF 시스템별 인터페이스 URL생성
 *******************************************************************************
 *
 * Program History
 * ------------------------------------------------------------------------------
 * Date             Changed In      Description
 * ------------------------------------------------------------------------------
 * 2021-01-08       이용선                          최초 작성 
 * ------------------------------------------------------------------------------
 */
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="search_box">
<div class="left">
	  		<div class="last_info">folderbox Text=>Hex / Hex=>Text  </div>
	  		Text : <input type="text" id="req_strtohex" name="req_strtohex" value="/인재창조원(15.2이전)/글로벌리더십센터/★리더십교육파트/신임-패밀리 신임임원(PEVC)/2009년/강의교안" style="width:80%"/> <button class="btn1" id="hex_ask"><span>암호화(Text=>Hex)</span></button></br>
			Hex : <input type="text" id="rlt_hex" name="rlt_hex"  style="width:80%"/>
	  		<button class="btn1" id="str_ask"><span>복호화(Hex=>Text)</span></button>
	  		<button class="btn1" id="hex_clear"><span>clear</span></button>		
	  	</div>
</div>
<!-- #content -->
<div id="content">

	<!-- .ifmoniter -->
	<div class="ifmoniter col2">
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>parameter 입력&nbsp;&nbsp;&nbsp;</h2>
				<button class="btn1" id="json_beautify">Data정렬</button>
				<button class="btn1"  id="json_compress">한줄로</button>
				<div  class="connter">
				<textarea name="jsonParam" id="jsonParam" rows="19" style="width: 99%;">{
    "reqsysid": "ecm",
    "subject": "",
    "allintext": "본문키워드",
    "owner": "",
    "format": "",
    "security": "",
    "folder": "edms,cab0000bf4b95636763,",
    "sort": "D"
}</textarea>
				</div>
				<div><strong>ex)172.31.247.249:8081, uswpes.posco.net:7091</strong><br/>
			<input type="text" id="req_was" name="req_was" value="uswpes.posco.net:7091" style="width:40%"/>			
			<input type="hidden" id="req_chain" name="req_chain" value="U40A"/>				
			<select name="req_site" id="req_site" style="width:28%">
			<option value='P|ecm_gnb'>[P]ECM GNB</option>
			<option value='P|ecm_basic'>[P]ECM 기본검색</option>
			<option value='P|ecm_detail'>[P]ECM 본문검색</option>
			<option value='P|posco_pride'>[P]포스코 PRIDE</option>
			<option value='P|ecm_ep'>[P]ECM 추천컨텐츠</option>
			<option value='P|ep'>[P]EP 추천컨텐츠</option>
			<option value='P|KM_HUB'>[P]성공씨앗</option>
			<option value='P|new_sop'>[P]ICT SOP</option>
			<option value='P|gih'>[P]ICT GIH</option>
			<option value='D|posco_today'>[D]포스코 투데이</option>
			<option value='D|plm'>[D]PLM</option>
			<option value='D|ecm_chat'>[D]챗봇 ECM검색</option>
			<option value='D|ecm_drive'>[D]ECM drive</option>
			</select>			
			<select name="req_comcode" id="req_comcode" style="width:14%">
			</select>
			
			<button type="button" id="ask" class="btn1">요청</button><br/>
			<input type="text" id="otherParam" name="otherParam" value="&page_idx=1&num=15&backCategory=doc&sysId=ecm&backComcode=" style="width:95%"/>
			<br/>※ chat_doc 호출시 <strong>가동계(swpes)</strong>는 <strong>:8889</strong>에서 <strong>테스트계(uswpes)</strong>는 <strong>:8888</strong>에서만 호출할 수 있음(암호키분리) 
				</div>
			</div>
		</div>
		<!-- //.panel -->
		
		<!-- .panel -->
		<div class="panel">
			<div class="section">
				<h2>provideAIF/directAIF 호출 URL</h2>
				<div class="connter" ><textarea id="resulttxtarea" rows="24" style="width: 99%;"></textarea>
				</div>
			</div>
		</div>
		<!-- //.panel -->
	</div>
</div>


<script src="/monitoring/js/interface_management/interface_provideif.js"></script>