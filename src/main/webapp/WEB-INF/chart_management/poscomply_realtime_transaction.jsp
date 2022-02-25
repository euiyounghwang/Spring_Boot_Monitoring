<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<br/><br/>
<div style="position: absolute; padding: 3px; border: 0px solid #484848; border-radius:3px; left: 0px; top: 230px; width:97%;">
     <p align='center'>
            <div id="LoadingImage" align="center" style="display: none">
                <!--  <img src="img/InternetSlowdown_Day.gif" />-->
                <img src="img/loading_small.gif" />
            </div>
     </p>
</div>
<table width="100%" height="20px;" border="0">
	<tr>
		<td width="100%" align="left">
			<!--    <h1>{{topic}}</h1>-->
			<div style="width:85%;">
				<!--<canvas style="height:30vh; width:50vw" id="line-chart" class="chartsjs"></canvas>-->
				<canvas id="line-chart" class="chartsjs"  width="3" height="1" style="display:block; width:100, height:150"></canvas>
				<!--<canvas id="line-chart" height="350" width="800"></canvas>-->
			</div>
		</td>
	</tr>
	<tr>
		<td width="100%" height="30px" align="left">
		</td>
	</tr>
	<tr>
		<td width="100%" align="left">
			<!--    <h1>{{topic}}</h1>-->
			<div style="width:85%;">
				<!--<canvas id="line-error-chart" class="chartsjs"  width="3" height="1" style="display:block; width:100, height:150"></canvas>-->
				<!--  <lable id='line-error' />-->
				   <lable id='line-error' />
			</div>
		</td>
	</tr>
</table>

<!-- //#content -->

<script src="/monitoring/js/chart_management/poscomply_realtime_transaction.js"></script>


<style type="text/css">
    .dataTables_processing {
        top: 110px !important;
        z-index: 11000 !important;
    }
    
    td>table{
    	width:100% !important; 
    }
</style>
