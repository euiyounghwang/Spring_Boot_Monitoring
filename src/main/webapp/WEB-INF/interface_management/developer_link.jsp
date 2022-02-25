<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../common/jsbn_rsa.jsp" %>

<!-- #content -->
<div id="content">
  <div id="data_insert" style="display: none;">
    <div class="btns_bar">
    	<div class="left">
    		<button class="btn1" onclick="validateEncryptedForm();"><span>저장</span></button>
    		<button class="btn2" onclick="closePasswdDiv()"><span>취소</span></button>
    	</div>
    </div>
    <div class="write_box" style="width:310px;">
    	<table>
		<colgroup>
			<col style="width:150px;">
			<col style="width:150px;">
		</colgroup>
		<thead>
			<tr>
				<th>새 비밀번호 </th>
				<th>새 비밀번호 확인</th>
			</tr>
    		</thead>
    		<tbody>
    			<tr>
    				<td class="tac"><input type="password" id="new_passwd" name ="system_id" style="width: 100%;"></td>
    				<td class="tac"><input type="password" id="new_passwd_re" name ="auth_id" style="width: 100%;"></td>
    			</tr>
    		</tbody>
    	</table>
  	</div>
  </div>
    <div class="btns_bar">
    	<div class="left">
			<button class="btn1" onclick="$('#data_insert').show();"><span>관리자화면 인증번호 관리</span></button>
	  	</div>
	</div>
<div class="left">
	<div class="btns_bar">
 	<h2 style="display:inline">통합검색(U40A)</h2> [Glue 3.4.9, Java 1.6]
	</div>
	
	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<tr>
				<th>패밀리망</th>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.32%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #1 (http://10.132.16.32:7091/U40A/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.33%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #2 (http://10.132.16.33:7091/U40A/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.34%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #3 (http://10.132.16.34:7091/U40A/common/devADM.jsp)</a>
			</td>
			<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.35%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #4 (http://10.132.16.35:7091/U40A/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.160%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #5 (http://10.132.16.160:7091/U40A/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.161%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #6 (http://10.132.16.161:7091/U40A/common/devADM.jsp)</a>
			</td>
			</tr>
			<tr>
				<th>포스코망</th>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.32%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #1 (http://172.31.141.32:7091/U40A/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.33%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #2 (http://172.31.141.33:7091/U40A/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.34%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #3 (http://172.31.141.34:7091/U40A/common/devADM.jsp)</a></br></td>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.35%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #4 (http://172.31.141.35:7091/U40A/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.160%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #5 (http://172.31.141.160:7091/U40A/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.161%3A7091%2FU40A%2Fcommon%2FdevADM.jsp" target="_blank">WAS #6 (http://172.31.141.161:7091/U40A/common/devADM.jsp)</a></td>
			</tr>
		</tbody>
	</table>
	
	<div class="btns_bar">
 	<h2 style="display:inline">직원프로필(U71)</h2> [Glue 3.4.9, Java 1.6]
 	</div>
	
	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<tr>
				<th>패밀리망</th>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.32%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #1 (http://10.132.16.32:7091/U71/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.33%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #2 (http://10.132.16.33:7091/U71/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.34%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #3 (http://10.132.16.34:7091/U71/common/devADM.jsp)</a>
			</td>
			<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.35%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #4 (http://10.132.16.35:7091/U71/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.160%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #5 (http://10.132.16.160:7091/U71/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.161%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #6 (http://10.132.16.161:7091/U71/common/devADM.jsp)</a>
			</td>
			</tr>
			<tr>
				<th>포스코망</th>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.32%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #1 (http://172.31.141.32:7091/U71/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.33%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #2 (http://172.31.141.33:7091/U71/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.34%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #3 (http://172.31.141.34:7091/U71/common/devADM.jsp)</a></br></td>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.35%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #4 (http://172.31.141.35:7091/U71/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.160%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #5 (http://172.31.141.160:7091/U71/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.161%3A7091%2FU71%2Fcommon%2FdevADM.jsp" target="_blank">WAS #6 (http://172.31.141.161:7091/U71/common/devADM.jsp)</a></td>
			</tr>
		</tbody>
	</table>
	
	<div class="btns_bar">
 	<h2 style="display:inline">세무관리시스템(U40B)</h2> [Glue 4.2.13, Java 1.6]
 	</div>
	
	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<tr>
				<th>단축키</th>
				<td>ctrl+q(3번) = 모델관리</br>
				shift+d(3번) = 사전관리</br>
				shift+m(3번) = 수동검출</td>
				<td></td>
			</tr>
			<tr>
				<th>개발/테스트계</th>
				<td><a href="http://uswpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2Fd-terms.posco.net%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">개발계(usmart sso연결) (http://d-terms.posco.net:7191/U40B/common/devADM.jsp)</a></br>
				<a href="http://uswpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2Ft-terms.posco.net%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">테스트계(usmart sso연결) (http://t-terms.posco.net:7191/U40B/common/devADM.jsp)</a></br>
				</td>
				<td><a href="http://10.105.113.72:7191/U40B/common/devADM.jsp" target="_blank">개발계(직접연결) (http://10.105.113.72:7191/U40B/common/devADM.jsp)</a></br>
				<a href="http://10.105.113.71:7191/U40B/common/devADM.jsp" target="_blank">테스트계(직접연결) (http://10.105.113.71:7191/U40B/common/devADM.jsp)</a></td>
			</tr>
			<tr>
				<th>클라우드서버[가동계 WAS]</th>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2Fp-terms.posco.net%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">가동계 (http://p-terms.posco.net:7191/U40B/common/devADM.jsp)</a></br>
				<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.81%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">WAS #1 (http://10.105.112.81:7191/U40B/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.82%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">WAS #2 (http://10.105.112.82:7191/U40B/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.83%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">WAS #3 (http://10.105.112.83:7191/U40B/common/devADM.jsp)</a>
			</td>
			<td>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.84%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">WAS #4 (http://10.105.112.84:7191/U40B/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.85%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">WAS #5 (http://10.105.112.85:7191/U40B/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.86%3A7191%2FU40B%2Fcommon%2FdevADM.jsp" target="_blank">WAS #6 (http://10.105.112.86:7191/U40B/common/devADM.jsp)</a>
			</td>
			</tr>
		</tbody>
	</table>
	
	<div class="btns_bar">
 	<h2 style="display:inline">약관 공정화 시스템(U40C)</h2> [Glue 4.2.13, Java 1.6]
 	</div>
	
	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<tr>
				<th>개발/테스트계</th>
				<td><a href="http://uswpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2Fd-compli.posco.net%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">개발계(usmart sso연결) (http://d-compli.posco.net:7191/U40C/common/devADM.jsp)</a></br>
				<a href="http://uswpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2Ft-compli.posco.net%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">테스트계(usmart sso연결) (http://t-compli.posco.net:7191/U40C/common/devADM.jsp)</a></br>
				</td>
				<td><a href="http://10.105.113.72:7191/U40C/common/devADM.jsp" target="_blank">개발계(직접연결) (http://10.105.113.72:7191/U40C/common/devADM.jsp)</a></br>
				<a href="http://10.105.113.71:7191/U40C/common/devADM.jsp" target="_blank">테스트계(직접연결) (http://10.105.113.71:7191/U40C/common/devADM.jsp)</a></td>
			</tr>
			<tr>
				<th>클라우드서버[가동계 WAS]</th>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2Fp-compli.posco.net%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">가동계 (http://p-compli.posco.net:7191/U40C/common/devADM.jsp)</a></br>
				
				<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.81%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">WAS #1 (http://10.105.112.81:7191/U40C/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.82%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">WAS #2 (http://10.105.112.82:7191/U40C/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.83%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">WAS #3 (http://10.105.112.83:7191/U40C/common/devADM.jsp)</a>
			</td>
			<td>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.84%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">WAS #4 (http://10.105.112.84:7191/U40C/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.85%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">WAS #5 (http://10.105.112.85:7191/U40C/common/devADM.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.105.112.86%3A7191%2FU40C%2Fcommon%2FdevADM.jsp" target="_blank">WAS #6 (http://10.105.112.86:7191/U40C/common/devADM.jsp)</a>
			</td>
			</tr>
		</tbody>
	</table>
	
	<div class="btns_bar">
 	<h2 style="display:inline">약관 공정화 시스템(LAW)-건설</h2> [Glue 4.2.13, Java 1.8]
 	</div>
	
	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<tr>
				<th>건설 망 [가동계 테스트용]</th>
				<td><a href="http://203.245.160.154:8191/LAW/common/devADM.jsp" target="_blank">건설AI_WAS #1 (http://203.245.160.154:8191/LAW/common/devADM.jsp)</a></td>
				<td></td>
			</tr>
			<tr>
				<th>건설 망 [고객 실제 사용]</th>
				<td><a href="http://203.245.160.154:7191/LAW/common/devADM.jsp" target="_blank">건설AI_WAS #1 (http://203.245.160.154:7191/LAW/common/devADM.jsp)</a></td>
				<td><a href="http://203.245.160.155:7191/LAW/common/devADM.jsp" target="_blank">건설AI_WAS #2 (http://203.245.160.155:7191/LAW/common/devADM.jsp)</a></td>
			</tr>
		</tbody>
	</table>
	
	<div class="btns_bar">
 	<h2 style="display:inline">인물 통합모니터링 서비스(U7A)</h2> [Glue 3.4.9, Java 1.6]
 	</div>
	
	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<tr>
				<th>패밀리망</th>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.32%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #1 (http://10.132.16.32:7091/U7A/U7A010/index.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.33%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #2 (http://10.132.16.33:7091/U7A/U7A010/index.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.34%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #3 (http://10.132.16.34:7091/U7A/U7A010/index.jsp)</a>
			</td>
			<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.35%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #4 (http://10.132.16.35:7091/U7A/U7A010/index.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.160%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #5 (http://10.132.16.160:7091/U7A/U7A010/index.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F10.132.16.161%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #6 (http://10.132.16.161:7091/U7A/U7A010/index.jsp)</a>
			</td>
			</tr>
			<tr>
				<th>포스코망</th>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.32%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #1 (http://172.31.141.32:7091/U7A/U7A010/index.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.33%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #2 (http://172.31.141.33:7091/U7A/U7A010/index.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.34%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #3 (http://172.31.141.34:7091/U7A/U7A010/index.jsp)</a></br></td>
				<td><a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.35%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #4 (http://172.31.141.35:7091/U7A/U7A010/index.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.160%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #5 (http://172.31.141.160:7091/U7A/U7A010/index.jsp)</a></br>
			<a href="http://swpsso.posco.net/idms/U61/jsp/redirectSMSP.jsp?redir_url=http%3A%2F%2F172.31.141.161%3A7091%2FU7A%2FU7A010%2Findex.jsp" target="_blank">WAS #6 (http://172.31.141.161:7091/U7A/U7A010/index.jsp)</a></td>
			</tr>
		</tbody>
	</table>

	
	<div class="btns_bar">
 	<h2 style="display:inline">산학연(S23C)</h2> [Spring 4.4, Java 1.8, JBoss]
 	</div>
	
	<table class="rd_info">
		<colgroup>
			<col style="width:15%;">
			<col style="width:40%;">
			<col style="width:45%;">
		</colgroup>
		<tbody>
			<tr>
				<th>산학연 사외망[개발계 WAS]</th>
				<td><a href="http://192.168.79.69:8101/S23/S23C10/" target="_blank">충주산학연사외사용자개발AP (http://192.168.79.69:8101/S23/S23C10/)</a></td>
				<td></td>
			</tr>
			<tr>
				<th>산학연 사외망[가동계 WAS]</th>
				<td><a href="http://192.168.79.105:8101/S23/S23C10/" target="_blank">충주산학연사외사용자 가동계 #1 (http://192.168.79.105:8101/S23/S23C10/)</a></br>
				<a href="http://192.168.79.106:8101/S23/S23C10/" target="_blank">충주산학연사외사용자 가동계 #2 (http://192.168.79.106:8101/S23/S23C10/)</a></td>
				<td><a href="http://192.168.79.104:8101/S23/S23C10/" target="_blank">가동계 LB (http://192.168.79.104:8101/S23/S23C10/)</a></td>
			</tr>
		</tbody>
	</table>
</div>
<!-- //#content -->
</br>
</br>
</br>

<script src="/monitoring/js/interface_management/developer_link.js"></script>