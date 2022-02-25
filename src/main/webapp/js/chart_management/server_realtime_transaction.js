/**
 * 서버 실시간 조회
 */

 

$( document ).ready(function() {
	
	Show_AI_Delay_Time_Graph();

	$(document.getElementById("butt_stop")).trigger('click');
});



function Show_AI_Delay_Time_Graph() {
//	var data = [1.74, 1.12, 0.92, 0.98, 1.12, 0.97, 1.17, 1.17, 1.13, 0.9, 0.97, 0.95, 1.69, 0.92, 0.97, 0.94, 0.95, 1.27, 1.79, 1.26, 0.98, 0.99, 0.96, 0.98, 0.98, 1.41, 0.97, 1.48, 0.98, 0.98, 1.11, 0.98, 0.92, 0.93, 1.1, 0.92, 1.84, 0.92, 0.99, 1.27, 0.95, 0.96, 1.5, 1.23, 0.98, 0.97, 0.97, 0.95, 1.37, 1.22, 0.98, 1.77, 1.34, 1.38, 1.24, 1.16, 0.95, 0.93, 1.55, 1.37, 1.54, 1.54, 0.99, 1.8, 1.13, 0.99, 1.56, 1.58, 0.89, 0.89, 0.99];
//	var labels =  ["06-18 09:48:33", "06-18 11:14:55", "06-18 11:15:03", "06-18 11:25:00", "06-18 12:08:55", "06-18 12:09:22", "06-18 12:11:55", "06-18 12:12:10", "06-18 12:12:50", "06-18 12:14:00", "06-18 12:14:56", "06-18 12:17:29", "06-18 12:18:15", "06-18 12:24:23", "06-18 12:26:50", "06-18 12:27:27", "06-18 12:27:53", "06-18 12:28:47", "06-18 12:31:10", "06-18 12:34:10", "06-18 12:44:45", "06-18 12:45:51", "06-18 12:47:06", "06-18 12:47:10", "06-18 12:48:04", "06-18 12:49:54", "06-18 12:52:35", "06-18 13:21:51", "06-18 13:22:22", "06-18 13:23:56", "06-18 13:24:34", "06-18 15:23:52", "06-18 15:24:14", "06-18 15:25:19", "06-18 15:26:21", "06-18 15:32:47", "06-18 15:32:59", "06-18 15:34:42", "06-18 15:35:19", "06-18 15:35:27", "06-18 15:36:59", "06-18 15:37:04", "06-18 15:37:09", "06-18 15:37:31", "06-18 23:06:25", "06-18 23:06:28", "06-18 23:06:46", "06-18 23:07:03", "06-18 23:08:10", "06-18 23:08:16", "06-18 23:08:24", "06-18 23:08:28", "06-22 18:59:09", "06-22 18:59:11", "06-23 10:40:19", "06-23 13:35:59", "06-23 13:36:59", "06-23 13:42:13", "06-23 15:34:23", "06-23 15:37:08", "06-23 15:38:54", "06-23 15:48:45", "06-24 11:07:24", "06-24 13:59:48", "06-24 14:00:06", "06-24 14:00:09", "06-24 14:01:51", "06-24 14:01:55", "06-26 16:47:20", "06-26 17:39:37", "06-29 14:53:03"];
	var delay_time = [];
	var ai_delay_time = [];
	var lables = [];
	var params = {};
	params['version_params'] = $('#version_params').val();
	params['EVENT_ID'] = 'get_realtime_transaction';
	params['start'] = '0';
	params['length'] = '1000';
	//params['start_date'] = '2020-06-01';
	params['start_date'] = dateAddDel(getTimeStamp(), -60, 'd');
//	params['start_date'] = dateAddDel(getTimeStamp(), -6, 'm');
	$.ajax({
		type : 'POST',
    url:'get_realtime_transaction',
    data : JSON.stringify(params),
    dataType:'json',
    contentType: 'application/json',
    beforeSend: function(){
    	 $("#LoadingImage").show();
//    		showProgress();
        },
    success:function(datas){
//    	{datas=[{pos_accuracy=, pos_precision=, pos_f1=, pos_recall=, neg_accuracy=, label=, neg_recall=, neg_f1=, neg_precision=}]}
    	$.each(datas,function(key,value) {
	    	$.each(value,function(keys,values) {
	    		console.log('native', keys, values.data, values.label);
	    		delay_time = values.delay_time;
	      	ai_delay_time = values.ai_delay_time;
	    		labels = values.label;
	    		console.log('split', labels, delay_time.split(","),  ai_delay_time.split(","), labels.split(","), typeof(labels),
	    				typeof(delay_time.split(",")), typeof(labels.split(",")));
	    		});

			var config = {
					type: 'line',
					data: {
			//			labels: ['2020-07-02 23:32:16','2020-07-02 23:31:56','2020-07-02 23:31:33','2020-07-01 13:45:46'],
						labels: labels.split(","),
						datasets: [
							{
								label: 'AI REQUEST TOTAL Delay TIME (전체요청시간)',
								fill: false,
			//					backgroundColor: window.chartColors.red,
								"backgroundColor" : "rgb(102, 204, 255)",
			//					borderColor: window.chartColors.red,
								"borderColor" : "rgb(102, 204, 255)",
								"lineTension" : 0.5,
								borderWidth: 2,
								data : delay_time.split(",")
			//					data: ['0.79','0.52','0.79','0.29']
							},
							/*
							{
								label: 'AI Highlight DELAY TIME (AI중복검출 하이라이트 시간)',
								fill: true,
//								"backgroundColor" : "rgb(100, 225, 15)",
//								"borderColor" : "rgb(100, 225, 15)",
//								"lineTension" : 0.1,
//									"backgroundColor" : "rgb(102, 204, 255)",
//									"borderColor" : "rgb(102, 204, 255)",
//								 backgroundColor: [
//									    				'rgba(255, 99, 132, 0.2)',
//					                     'rgba(54, 162, 235, 0.2)',
//					                     'rgba(255, 206, 86, 0.2)',
//					                     'rgba(75, 192, 192, 0.2)',
//					                     'rgba(153, 102, 255, 0.2)',
//					                     'rgba(255, 159, 64, 0.2)'
//				                  ],
//				         borderColor: [
//				                      'rgba(255, 99, 132, 1)',
//				                      'rgba(54, 162, 235, 1)',
//				                      'rgba(255, 206, 86, 1)',
//				                      'rgba(75, 192, 192, 1)',
//				                      'rgba(153, 102, 255, 1)',
//				                      'rgba(255, 159, 64, 1)'
//				                  ],
				        "backgroundColor" : 'rgba(255, 99, 132, 1)',
				        "borderColor" :  'rgba(255, 99, 132, 1)',
				        borderWidth: 2,
								data : ai_delay_time.split(",")
							}
							*/
					 ]
					},
					options: {
						responsive: true,
						scales: {
				            xAxes: {
				                type: 'timeseries',
				            },
							yAxes: [{
								scaleLabel: {
									display: true,
									labelString: 'Detect Delay Time (Seconds)'
							      }
							    }]
						} ,
						title: {
							display: true,
							text: 'AI Detect Delay RealTime Chart (제안문서AI중복검출)'
						},
					}
					
				};
			
			var ctx = document.getElementById('line-chart').getContext('2d');
			new Chart(ctx, config);
    	
	    	});
        },
    error : function(e){ 
        },
    complete : function() {
//        	hideProgress();   
//        	$("#loading").empty();
        	$("#LoadingImage").hide();
//    	alert(labels);
        }
     });
}

