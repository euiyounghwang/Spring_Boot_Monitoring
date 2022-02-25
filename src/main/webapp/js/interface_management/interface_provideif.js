var jsonparameter="";
$(document).ready(function(){
	setDashboard();
	resetResult();
	selectCommonCompanys()
	$(window).unbind('resize.setDashboard').bind('resize.setDashboard',setDashboard);
	$('#right').unbind('show.setDashboard hide.setDashboard').bind('show.setDashboard hide.setDashboard',setDashboard);
	function setDashboard(){
		if($('#content .ifmoniter')[0]){
			var dashboard = $('#content .ifmoniter');
/*			if(dashboard.width() < 1300){
				dashboard.addClass('col2');
			}else{
				dashboard.removeClass('col2');
			}*/
		}
	}
	
	setTimeout(function() {
		$('#req_comcode option:eq(0)').remove();
		}, 100);
	  
	$('#hex_ask').click(function(){
		fnStrToHex();	
	});
	$('#str_ask').click(function(){
		fnHexToStr();	
	});
	
	$('#hex_clear').click(function(){
		$('#req_strtohex').val('');
		$('#rlt_hex').val('');
	});
	
	
	  
	$('#ask').click(function(){
		jsonChk();	
	});	
	
	$('#jsonParam').keydown(function (e) {
		
		  if (e.ctrlKey && e.keyCode == 13) {
			  jsonChk();
		  }

	});
	
	$('#json_beautify').click(function(){
		json_beautify('jsonParam'); 
	});
	
	$('#json_compress').click(function(){
		json_compress();
	});
		
	$('#req_site').on('change', function() {
		var selectParam = '';		
		var selectSite = document.getElementById("req_site").value;
		var selectOtherParam = '';		
		
		if(selectSite.indexOf('ecm_gnb')>-1){
			selectParam = '{"reqsysid":"ecm","subject":"","allintext":"본문키워드","owner":"","format":"","security":"","folder":"edms,cab0000bf4b95636763,","sort":"D"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=doc&sysId=ecm&backComcode=';
		}
		else if(selectSite.indexOf('ecm_basic')>-1){
			selectParam = '{"reqsysid":"ecm","subject":"컨텐츠명","subject_operator":"OR","ecmtaglist":"태그","ecmtaglist_operator":"AND","owner":"소유자","creator":"등록자","security":"1A,2A,2B,3A,","udaterange":"2017-10-11..2018-04-11","format":"ppt,xls,doc,etc,","folderbox":"","subfolder_yn":"Y"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=basic_doc&sysId=ecm&backComcode=';
		}
		else if(selectSite.indexOf('ecm_detail')>-1){
			selectParam = '{"reqsysid":"ecm","subject":"컨텐츠명","allintext":"본문키워드","owner":"소유자","udaterange":"2017-10-11..2018-04-11","format":"ppt,xls,doc,etc,","security":"1A,2A,2B,3A,","folder":"edms,cab0000bf4b95636763,","sort":"D","ecmtaglist":"태그","approval":"Y", "personalize":"N"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=doc&sysId=ecm&backComcode=';
		}
		else if(selectSite.indexOf('posco_pride')>-1){
			selectParam = '{"reqsysid":"pride","subject":"","intext":"","allintext":"연구","srch_con_cont":"키1 키2 키3","operator":"OR","owner":"","cdaterange":"2018-01-11..2018-04-11","dept_name":"전사","menu_id":13,"sortmeta":"meta:INPUTDATE:D::ED"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=posco_pride&sysId=pride&backComcode=';
		}
		else if(selectSite.indexOf('KM_HUB')>-1){
			selectParam = '{"reqsysid":"KM_HUB","subject":"","intext":"","allintext":"","author":"","cdaterange":""}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=ksi&sysId=KM_HUB&backComcode=';
		}
		else if(selectSite.indexOf('new_sop')>-1){
			selectParam = '{"reqsysid":"sop","subject":"휴가","content":"휴가","file_name":"휴가","user_name":"휴가","menu_id":"1","operator":"AND","fields_operator":"OR"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=new_sop&sysId=sop&backComcode=';
		}
		else if(selectSite.indexOf('gih')>-1){
			selectParam = '{"basickeyword":"EIC"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=gih&sysId=ict_gl&backComcode=';
		}
		else if(selectSite.indexOf('ecm_ep')>-1){
			selectParam = '';
			selectOtherParam = '&page_idx=1&num=15&backCategory=doc&sysId=ecm&backComcode=';
		}
		else if(selectSite.indexOf('ep')>-1){
			selectParam = '';
			selectOtherParam = '&page_idx=1&num=15&backCategory=doc&sysId=ep&backComcode=';
		}
		else if(selectSite.indexOf('posco_today')>-1){
			selectParam = '{"reqsysid":"posco_today","keyword":"남기고싶은이야기","menu_code":"001001|MN004001","menu_sub_code":"","usrid":"PC912346","sortmeta":"meta:INPUTDATE:D::ED","search_type":"E","subject":"","intext":"","allintext":""}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=posco_today&sysId=posco_today&backComcode=';
		}
		else if(selectSite.indexOf('plm')>-1){
			selectParam = '{"subject_code":"KBE20035","reqsysid":"PLM","usrid":"euiyoung.hwang"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=plm&sysId=PLM&backComcode=';
		}
		else if(selectSite.indexOf('ecm_chat')>-1){
			selectParam = '{"reqsysid":"ecm","subject":"","allintext":"테스트","owner":"황의영","cdaterange":"","format":"","security":"","folder":"p2008039,edms,cab0000bf4b95961317,cab0000bf4b95a3b717,CP999,","sort":"D","comcode":"30","usrid":"pd292816","spempno":"pd292816","inoutside":"I","deptno":"ZZZZZ","ismydoc":""}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=chat_doc&sysId=ecm&backComcode=';
		}
		else if(selectSite.indexOf('ecm_drive')>-1){
			selectParam = '{"reqsysid":"drive","subject":"","allintext":"","ecmtaglist":"","owner":"","udaterange":"2020-07-25..2020-08-25","format":"","approval":"","security":"","folder":"IAB20,IAB10,FAI00,","sort":"D","comcode":"01","usrid":"jason_kim","spempno":"277335","inoutside":"I","deptno":"IAB20"}';
			selectOtherParam = '&page_idx=1&num=15&backCategory=chat_doc&sysId=drive&backComcode=';
		}else{
			selectParam = '';
			selectOtherParam = '';
		}

		
		//요청 param 예제 표시하기
		document.getElementById("jsonParam").value = '';
		document.getElementById("jsonParam").value = selectParam;
		document.getElementById("otherParam").value = '';
		document.getElementById("otherParam").value = selectOtherParam;
	});
	
	window.onload = function(e){ 
	    companycheck();
	}
	
	$('select').on('change', function() {
		resetResult();
	});
});

function json_beautify(id){
	var jsonDataVal = document.getElementById(id).value; // textarea의 데이터를 가져온다.
	var jsonData = JSON.parse(jsonDataVal); // json object로 만든다.
	var jsonBeauty = JSON.stringify(jsonData, null, 4);//정렬
	document.getElementById(id).value = '';
	document.getElementById(id).value = jsonBeauty;
}

function json_compress(){
    var value = document.getElementById("jsonParam").value;// textarea의 데이터를 가져온다.
//    value = value.replace(/\s+/gi, "");// 공백제거 
    value = value.replace(/\s+/gi, "");// 공백제거
    value = value.replace("AND", " AND ");// 공백제거
    value = value.replace("OR", " OR ");// 공백제거
	document.getElementById("jsonParam").value = '';
	document.getElementById("jsonParam").value = value;
}

function companycheck(){
	 var data = document.getElementById('jsonParam').value;
	 if(data.indexOf("ict_hrsearch")>0){
		
			document.getElementById('POSCOmore').style.display="none";
			document.getElementById('POSCO').style.display="none";

		 	document.getElementById('ICTmore').innerText="전송요청값 펼쳐보기";
			document.getElementById('ICTmore').style.display="";
			
		}else if(data.indexOf("posco_hrsearch")>0){
			
			document.getElementById('ICTmore').style.display="none"
			document.getElementById('ICT').innerHTML="";
			
			document.getElementById('POSCOmore').innerText="전송요청값 펼쳐보기";
			document.getElementById('POSCOmore').style.display="";
			
			
		}
}

function replaceAll(str,search, replace) {
    return str.split(search).join(replace);
} 

function jsonChk(){
	var flag = false;

	try{
		var data = document.getElementById('jsonParam').value;
		
		if(data!=null && data.length>0){
			jsonparameter=JSON.parse(data+'');
			if(data.indexOf("reqsysid")>0){
				flag=true;			
			}else if(data.indexOf("basickeyword")>0){
				flag=true;			
			}
		}else{
			flag=true;	
		}
		 
		$(document.getElementById("metaChk")).html(''); 
		$(document.getElementById("POSCO")).html('');
		$(document.getElementById("ICT")).html('');
		document.getElementById("resulttxtarea").value = '';//기존 검색결과 초기화
		
		if(flag){
			fnCallProvideIF('xml');
		}else{
			fnCallProvideIF('json');
		}
	
	}catch (e){
		 alert("error : "+e.message);
	}
		
	
}

function fnStrToHex() {
	$.ajax({
		type : 'POST',
        url:'getStringToHex',
        data : 'folder='+$('#req_strtohex').val(),
        success:function(result){
        		$("#rlt_hex").val(result);
        },
        error : function(e){ 
        	console.log("StrToHex Error");
        }
     });
} 

function fnHexToStr() {
	$.ajax({
		type : 'POST',
        url:'getHexToString',
        data : 'folder='+$('#rlt_hex').val(),
        success:function(result){
        		$("#req_strtohex").val(result);
        },
        error : function(e){ 
        	console.log("HexToStr Error");
        }
     });
} 

function fnCallProvideIF(rltType) {
	showProgress();
	resetResult();
	 var params = {};
	 params['req_site'] = $('#req_site').val();
	 params['req_chain'] = $('#req_chain').val();
	 params['req_was'] = $('#req_was').val();
	 params['req_comcode'] = $('#req_comcode').val();
	 params['otherParam'] = $('#otherParam').val();
	 params['jsonParam'] = $('#jsonParam').val();
	$.ajax({
		type : 'POST',
        url:'callProvideIF',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(result){
        	if(result.result_code=='success'){ 
        		$("#resulttxtarea").val(result.gsa_data_key);
        		if(rltType=='json') json_beautify('resulttxtarea');
        	} else {
        		var errorMsg;
        		errorMsg = result.gsa_error_key;
        		if(result.gsa_error_key_more_info != undefined){
        			errorMsg += "\n"+result.gsa_error_key_more_info;
        		}
        		$("#resulttxtarea").val(errorMsg);
        	}
        },
        complete : function(){
        	hideProgress();
        },
        error : function(e){ 
        	console.log("callProvideIF Error");
        }
     });
} 

function resetResult(){
	$('#metaList').empty();
	$('#nonMeatList').empty();
	$('#resulttxtarea').val('');
}

function fnOther(){		
	alert("HRSEARCH 외 타 시스템 적용 준비중");
}

function selectCommonCompanys(){
	$.ajax({
		type : 'POST',
        url:'selectCompanys',
        dataType:'json',
        success:function(data){
	        	var obj = $('#req_comcode');
	        	drawSelect(obj, data, 'company');
       },
       error : function(e){ 
       }
    });
}
