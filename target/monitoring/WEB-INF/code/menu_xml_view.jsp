<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="btns_bar fixed">
	<div class="left">
		<select id="sel_company"></select>
		<select id="sel_infratype"></select>
		<span class="last_info">※ 본 화면은 DB에서 실시간으로 조회되는 미리보기 화면 입니다.</span>
	</div>
		<div class="right">
  <form id="runJobCommand" name="runJobCommand" >
	<input type="hidden" id="RUN_COMMAND" name="RUN_COMMAND" value="/TOM/generateMenu/New_generateMenuBatch.sh >> /TOM/generateMenu/logs/menu.log"/>
	<input type="hidden" id="RUN_MODE" name="RUN_MODE" value="menu_xml_view"/>
 </form>
			<button class="btn1" onclick="runCommand();"><span>메뉴배치JOB실행</span></button>
		</div>
</div>

<!-- #content -->
<div id="content">
	<div class="rd_xmlviewer">
		<XMP>
		</XMP>
	</div>
</div>
<!-- //#content -->

<script>
$( document ).ready(function() {
	getMenuFullData('30','SMART');
	selectCommonCompanys('select');
	
	var infratypelist = {
			'SMART' : '가동계',
			'USMART' : '테스트계',
	}

	$( document ).ready(function() {
		drawSelect( $('#sel_infratype'), infratypelist, 'conf');
	});
	
	setTimeout(function() {
		$('#sel_company option:eq(0)').remove();
		$('#sel_infratype option:eq(0)').remove();
		}, 100);
	
	
	$('#sel_company').change(function(){
		getMenuFullData($('#sel_company').val(),$('#sel_infratype').val());
	});
	$('#sel_infratype').change(function(){
		getMenuFullData($('#sel_company').val(),$('#sel_infratype').val());
	});
	
	
});



//서버에 접속하여 job 실행(비동기)
function runCommand()
{
	
	var runConfirm = confirm("현재 연결된 DB의 메뉴배치 JOB을 지금 실행하시겠습니까?");

	if(runConfirm)
	{
		
	//서버정보 
	var paramData = $("#runJobCommand").serializeObject() ;

	console.log(paramData);

	$.ajax({
	    type: 'post'
	    , url: 'runJob'
	    , data: JSON.stringify(paramData)
	    , contentType: 'application/json'
	    , success: function(e) {
			if(  e!="success" )
			{
				alert(e);
			}
	    }
	    , error: function(jqXHR, textStatus, errorThrown) {
			console.log("오류?");
			console.log(jqXHR);
			console.log(textStatus);
	    	}
		});
	}
}
</script>