<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- #content -->
<div class="search_box">
	<table>
		<tbody>
			<tr>
				<td><span class="label">시스템</span></td>
				<td>
				<select style="width:140px;" id="sel_system_id">
				</select>							
				</td>												
				<td><span class="label">권한</span></td>
				<td>
				<select style="width:140px;" id="sel_auth_id">
				</select>							
				</td>											
				<td><span class="label">회사코드</span></td>
				<td>
				<select style="width:140px;" id="req_comcode">
				</select>							
				</td>											
				<td><span class="label">사용여부</span></td>
				<td>
				<select style="width:140px;" id="sel_useyn">
				<option value="">전체</option><option value="Y">Y(사용)</option><option value="N">N(미사용)</option>
				</select>							
				</td>
				<td><span class="label">이름</span></td>
				<td>
				<input type="text" id="txt_full_name" name ="txt_full_name" style="width:140px;" autocomplete="off" onkeydown="var keyCode_main = event.keyCode; if(keyCode_main==13){ $(document.getElementById('btn_refresh')).click(); }">
				</td>
				<td><button class="btn1" id="btn_refresh">조회</button></td>				
			</tr>			
		</tbody>
	</table>
</div>	
<br>
<div id="content">
	<div id="data_search_insert"  style="display: none;">
				<!-- .dialog_item_box -->
				<div class="dialog_item_box">
					<div class="head">
						<table style="width:100%;">
							<colgroup>
								<col style="width:100px;">
								<col>
								<col style="width:100px;">
								<col>
								<col style="width:50px;">
								<col style="width:50px;">
							</colgroup>
							<tbody>
								<tr>
									<td><span class="label">이름</span></td>
									<td><input type="text" id = "input_nameVal" style="width:100%;" placeholder="검색할 이름을 입력해 주십시오."></td>
									<td><span class="label">직번</span></td>
									<td><input type="text" id = "input_empNo" style="width:100%;" placeholder="검색할 직번을 입력해 주십시오."></td>
									<td>
				                    <button class="btn2" onclick="selectOrgMember();"><span>검색</span></button>
				                    </td>
									<td>
			                    	<button class="btn2" onclick="closeInsertDiv();"><span>닫기</span></button>
			      					</td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- .body -->
					<div class="body">
						<div class="opt">
						※ 포스코 직영은 STANDING_CODE가 562(임시직(일급)), 563(시간제 임시직), 565(인턴(현장직)) 인 경우 검색권한이 자동부여 되지 않음.
  						<table>
    						<tr>
      						<td>
			                  	<table class="grid_table" id="grid_table_emp">
			                  		<colgroup>
			                  			<col style="width:10px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  		</colgroup>
			                  		<thead>
			                  			<tr>
			                  				<th><input type="checkbox" id="check-all-emp"></th>
			                  				<th>이름</th>
			                  				<th>직번</th>
			                  				<th>아이디</th>			                  				
			                  				<th>이메일</th>
			                  				<th>회사코드</th>
			                  				<th>회사명</th>
			                  				<th>부서</th>
			                  				<th>직급</th>
			                  				<th>직영/협력사</th>
			                  				<th>STANDING_CODE</th>
			                  			</tr>
			                  		</thead>
			                  		<tbody>
			                  		</tbody>
			                  	</table>
      						</td>
    						</tr>
  						</table>
						</div>
					  <div class="opt">
							<table style="width:100%;">
								<colgroup>
									<col style="width:1px;">
									<col>
								</colgroup>
								<tbody>
									<tr>
										<td><span class="label">추가될 사용자</br>(이름/직번/회사코드)</span></td>
										<td><span id="view_check_emp"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					  <div class="type">
							<table style="width:100%;">
								<colgroup>
									<col style="width:1px;">
									<col style="width:1px;">
									<col style="width:1px;">
									<col style="width:1px;">
									<col style="width:1px;">
									<col>
									<col style="width:1px;">
									<col style="width:1px;">
									<col style="width:1px;">
									<col style="width:1px;">
									<col style="width:50px;">
									<col style="width:50px;">
								</colgroup>
								<tbody>
									<tr>
										<td><span class="label">시스템</span></td>
										<td>
												<select id="sel_system" style="width:100px;"/>												
										</td>
										<td><span class="label">권한</span></td>
										<td>
												<select id="sel_auth" style="width:100px;"/>												
										</td>
										<td><span class="label">권한부여 사유</span></td>
										<td><input type="text" id = "input_commantVal" style="width:100%;" placeholder="권한부여 사유를 입력해 주십시오."></td>
										
										<td><span class="label">사용시작일</span></td>
										<td>
												<input type="text" class="dateonly" id="startDate" autocomplete="off" style="width: 121px;">												
										</td>
										<td><span class="label">사용종료일</span></td>
										<td>
												<input type="text" class="dateonly" id="endDate" autocomplete="off" style="width: 121px;">												
										</td>										
			      						<td>
			                    <button class="btn1" id="btn-emp-submit"><span>저장</span></button>
			      						</td>
										<td>
				                    	<button class="btn2" onclick="closeInsertDiv();"><span>취소</span></button>
				      					</td>
									</tr>
								</tbody>
							</table>
						</div>
					<!-- //.body -->
				</div>
				<!-- //.dialog_item_box -->
    
	</div>
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
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:150px;">
			<col style="width:150px;">
			<col style="width:150px;">
			<col style="width:100px;">
			<col style="width:200px;">
			<col style="width:121px;">
			<col style="width:121px;">
			<col style="width:100px;">
			<col>
		</colgroup>
		<thead>
			<tr>
				<th>*SYSTEM_ID</th>
				<th>*AUTH_ID</th>
				<th>*COMPANY_CODE</th>
				<th>*GROUP_ID</th>
				<th>*USER_EMP_NO<br/>(직번)</th>
				<th>FULL_NAME<br/>(이름)</th>
				<th>USER_LOGIN_ID<br/>(메일ID)</th>
				<th>META_YN<br/>(메타기능yn)</th>
				<th>USER_AUTH_EXPLAIN<br/>(권한부여사유)</th>
				<th>ATTRIBUTE2<br/>(사용시작일)</th>
				<th>ATTRIBUTE3<br/>(사용종료일)</th>
				<th>ATTRIBUTE1<br/>(사용여부YN)</th>
			</tr>
    		</thead>
    		<tbody>
    			<tr>
    				<td class="tac"><input type="text" id="system_id" name ="system_id" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="auth_id" name ="auth_id" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="company_code" name ="company_code" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="group_id" name ="group_id" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="user_emp_no" name ="user_emp_no" style="width: 100%;"></td>
    				<td class="tar"><input type="text" id="full_name" name ="full_name" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="user_login_id" name ="user_login_id" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="meta_yn" name ="meta_yn" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="user_auth_explain" name ="user_auth_explain" style="width: 100%;"></td>
    				<td class="tac"><input type="text" class="dateonly" id="start_date" name="start_date" autocomplete="off" style="width: 100%;"></td>
    				<td class="tac"><input type="text" class="dateonly" id="end_date" name="end_date" autocomplete="off" style="width: 100%;"></td>
    				<td class="tac"><input type="text" id="attribute1" name ="attribute1" style="width: 100%;"></td>
    			</tr>
    		</tbody>
    	</table>
  	</div>
  </div>	

  <div class="btns_bar">
  	<div class="left">
  		<button class="btn1" id="btn_searchemp_show" onclick="closeInsertDiv();$('#data_search_insert').show();"><span>검색 후 등록</span></button>
  		<button class="btn1" onclick="closeInsertDiv();openInsertDiv();"><span>직접입력 등록</span></button>
  		<button class="btn2" id="btn_delete" disabled><span>삭제</span></button>
  		<span class="sep"></span>
  		<button class="btn2" id="btn_refreshinit"><span>새로고침</span></button>
  	</div>
  	<div class="right">
  	</div>
  </div>

	<div class="searched_info" style="display:none;">
		<strong class="opt">전체</strong> 조건의 <strong class="key">검색어</strong> 검색을 완료했습니다. <strong class="ea">00</strong>건
		<button class="close" onclick="$(this).closest('.searched_info').hide();">닫기</button>	
	</div>

	<input type="hidden" id="cd_tp_params" class="test_class" name="cd_tp_params" value='<%=request.getParameter("cd_tp")%>' />
	<table class="grid_table" id="grid_table_list">
		<colgroup>
			<col style="width:60px;">
			<col style="width:70px;">
			<col style="width:100px;">
			<col style="width:70px;">
			<col style="width:90px;">
			<col style="width:70px;">
			<col style="width:80px;">
			<col style="width:100px;">
			<col style="width:100px;">
			<col style="width:120px;">
			<col style="width:120px;">
			<col style="width:200px;">
			<col style="width:80px;">
			<col style="width:250px;">
			<col style="width:120px;">
			<col style="width:120px;">
			<col style="width:90px;">
			<col style="width:200px;">
			<col style="width:200px;">
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" id="check-all-menu"></th>
				<th>SYSTEM_ID</th>
				<th>(시스템명)</th>
				<th>AUTH_ID</th>
				<th>(권한)</th>
				<th>회사코드</th>
				<th>(회사명)</th>
				<th>GROUP_ID</th>
				<th>FULL_NAME<br/>(이름)</th>
				<th>USER_EMP_NO<br/>(직번)</th>
				<th>USER_LOGIN_ID<br/>(메일ID)</th>
				<th>EMAIL_ADDRESS</th>
				<th>META_YN<br/>(메타기능)</th>
				<th>USER_AUTH_EXPLAIN<br/>(권한부여사유)</th>
				<th>ATTRIBUTE2<br/>(사용시작일)</th>
				<th>ATTRIBUTE3<br/>(사용종료일)</th>
				<th>ATTRIBUTE1<br/>(사용여부)</th>
				<th>CREATION_TIMESTAMP</th>
				<th>LAST_UPDATE_TIMESTAMP</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!-- //#content -->
<script src="/monitoring/js/configuration/esauthdb_list.js"></script>