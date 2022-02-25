var jsonparameter="";
$(document).ready(function(){
	setDashboard();
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
	  
	$('#ask').click(function(){
		textChk(); 
	});
	
	$('#textParam').keydown(function (e) {
		
		  if (e.ctrlKey && e.keyCode == 13) {
			  textChk();
		  }

	});
		
});




function replaceAll(str,search, replace) {
    return str.split(search).join(replace);
} 

function textChk(){

	try{
		
		var data_text = document.getElementById('textParam').value;	
		var data_ip = document.getElementById('req_elaServer').value;	
		
		if(data_text===null || data_text.length==0 || data_ip===null || data_ip.length==0){
			alert("검출할 문장을 확인하세요.");
		}else{
			fnCallTextIF();	
		}
		
	
	}catch (e){
		 alert("error : "+e.message);
	}
		
	
}

function fnCallTextIF() {
	showProgress();
	
	var table = $('#center .grid_table').DataTable({
				ordering: false,
				destroy: true,
				scrollY : 0,
				searching : false,
				paging : false,
				autoWidth : false,
				language: {
				    info : '조회현황 : _TOTAL_ 건'
				 		},
				info:true,
        columns : [
				{data : 'predict'},
				{data : 'probability'},
				{data : 'sentence'}
						],
				dom: '<"top"i>t<"bottom"><"clear">',
				columnDefs:[
					{targets:[0], className:'dt-body-center'}
						]
			
			});
	table.clear();
	table.draw();
	
	 var params = {};
	 params['textParam'] = $('#textParam').val();
	 params['req_elaServer'] = $('#req_elaServer').val();
	 params['req_site'] = $('#req_site').val();
	 $.ajax({
		type : 'POST',
        url:'callTextIF',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(result){
        	if(result.result_code=='success'){ //draw meta table
        		if(result.data != undefined){
        			table.clear();
        			table.rows.add(result.data);
        			table.draw();
	        			}
	        		}

        		},
        complete : function(){
        	hideProgress();
        		},
        error : function(e){ 
        	console.log("callTextIF Error");
        	table.clear();
        	table.draw();
        		}
     });
} 

$('#req_site').on('change', function() {
	var selectParam = '';
	var selectElaServer = '';
	var selectSite = document.getElementById("req_site").value;
	
	if(selectSite == 'posco'){
		selectParam = '제 1 장   총  칙\n'
			+'제 1조 [목 적]\n'
			+'① 본 계약은 포스코의 조업안정과 경쟁력을 확보하고 수탁운영사의 자율경영과 책임작업 수행체제를 정착하기 위하여 계약기간 동안 상호간에 준수 하여야 할 사항을 규정하기 위함이다.\n'
			+'② 본 계약은 포스코의 마그네슘제련공장 등 위탁설비(이하 “위탁설비”)의 운영, 가동 및 유지, 보전에 필요한 일상점검, 정기점검, 수리 등 위탁업무 전반에 대해 포스코와 수탁운영사간의 상호 역할과 책임, 사고발생시 대응책임과 손해배상, 생산성 향상을 위한 성과보상의 범위에 관하여 규정한다.\n\n'
			+'제 2조 [용어정의]\n'
			+'① 위탁운영 : 마그네슘제련공장 제품생산을 위한 공장설비의 주공정 운전작업 및 이에 수반되는  일체의 부대작업을 수탁운영사가 수행하는 것을 의미하며  그와 관련된 외주협력 또는 외주작업을 포함한다.\n'
			+'② 자본적지출 : 고정자산의 내용연수를 연장시키거나 자산의 가치를 증가시키는 수선비를 말한다.\n'
			+'③ 수익적지출 : 설비의 정상적인 사용 중에 발생하는 원상 회복 및 일상적인 정비비를 말한다.';
		selectElaServer = 'http://10.132.17.69:5001/law_interface_results';
	}
	else if(selectSite == 'enc'){
		selectParam = '0.0 0.0.0 제 1 장   총  칙\n'
			+'\n'
			+'1.1 [목 적]\n'
			+'1) 본 계약은 포스코의 조업안정과 경쟁력을 확보하고 수탁운영사의 자율경영과 책임작업 수행체제를 정착하기 위하여 계약기간 동안 상호간에 준수 하여야 할 사항을 규정하기 위함이다.\n'
			+'2 본 계약은 포스코의 마그네슘제련공장 등 위탁설비(이하 “위탁설비”)의 운영, 가동 및 유지, 보전에 필요한 일상점검, 정기점검, 수리 등 위탁업무 전반에 대해 포스코와 수탁운영사간의 상호 역할과 책임, 사고발생시 대응책임과 손해배상, 생산성 향상을 위한 성과보상의 범위에 관하여 규정한다.\n'
			+'\n'
			+'1.2 [용어정의]\n'
			+'1.2.1 위탁운영 : 마그네슘제련공장 제품생산을 위한 공장설비의 주공정 운전작업 및 이에 수반되는  일체의 부대작업을 수탁운영사가 수행하는 것을 의미하며  그와 관련된 외주협력 또는 외주작업을 포함한다.\n'
			+'가) 자본적지출 : 고정자산의 내용연수를 연장시키거나 자산의 가치를 증가시키는 수선비를 말한다.\n'
			+'1.2.3 수익적지출 : 설비의 정상적인 사용 중에 발생하는 원상 회복 및 일상적인 정비비를 말한다.\n'
			+'가) 테스트\n'
			+'나) 테스트\n'
			+'다) 테스트\n'
			+'라) 테스트\n'
			+'마) 테스트\n'
			+'바) 테스트\n'
			+'사) 테스트\n'
			+'아) 테스트\n'
			+'자) 테스트\n'
			+'차) 테스트\n'
			+'카) 테스트\n'
			+'타) 테스트\n'
			+'파) 테스트\n'
			+'하) 테스트';
//		selectElaServer = 'http://10.132.17.69:5001/classification/analysis_interface/';
		selectElaServer = 'http://10.132.17.69:5002/classification/analysis/';
	} 
	else if(selectSite == 'chemtech'){
		selectParam = '\n0.0 0.0.0 제 1 장   총  칙\n'
			+'\n'
			+'1.1 [목 적]]\n'
			+'1) 본 계약은 포스코의 조업안정과 경쟁력을 확보하고 수탁운영사의 자율경영과 책임작업 수행체제를 정착하기 위하여 계약기간 동안 상호간에 준수 하여야 할 사항을 규정하기 위함이다.\n'
			+'2) 본 계약은 포스코의 마그네슘제련공장 등 위탁설비(이하 “위탁설비”)의 운영, 가동 및 유지, 보전에 필요한 일상점검, 정기점검, 수리 등 위탁업무 전반에 대해 포스코와 수탁운영사간의 상호 역할과 책임, 사고발생시 대응책임과 손해배상, 생산성 향상을 위한 성과보상의 범위에 관하여 규정한다.\n'
			+'\n'
			+'1.2 [용어정의]\n'
			+'1.2.1 위탁운영 : 마그네슘제련공장 제품생산을 위한 공장설비의 주공정 운전작업 및 이에 수반되는  일체의 부대작업을 수탁운영사가 수행하는 것을 의미하며  그와 관련된 외주협력 또는 외주작업을 포함한다.\n'
			+'가) 자본적지출 : 고정자산의 내용연수를 연장시키거나 자산의 가치를 증가시키는 수선비를 말한다.\n'
			+'1.2.3 수익적지출 : 설비의 정상적인 사용 중에 발생하는 원상 회복 및 일상적인 정비비를 말한다.\n'
			+'가) 테스트\n'
			+'나) 테스트\n'
			+'다) 테스트\n'
			+'라) 테스트\n'
			+'마) 테스트\n'
			+'바) 테스트\n'
			+'사) 테스트\n'
			+'아) 테스트\n'
			+'자) 테스트\n'
			+'차) 테스트\n'
			+'카) 테스트\n'
			+'타) 테스트\n'
			+'파) 테스트\n'
			+'하) 테스트';
//		selectElaServer = 'http://10.132.17.69:5001/classification/analysis_interface/';
		selectElaServer = 'http://10.132.17.69:5002/classification/analysis/';
	}

	
	//요청 param 예제 표시하기
	document.getElementById("textParam").value = '';
	document.getElementById("textParam").value = selectParam;
	document.getElementById("req_elaServer").value = '';
	document.getElementById("req_elaServer").value = selectElaServer;
});

