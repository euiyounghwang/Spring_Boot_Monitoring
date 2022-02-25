<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="dialog_tree">
	<div class="btns_bar">
		<div class="right">
			<button class="btn1" onclick="loadCenterPage('code/menu_xml_view', 'N');"><span>XML 미리보기</span></button>
		</div>
	</div>

	<div class="content">
		<ul class="tree">
		</ul>
	</div>

</div>

<script src="/monitoring/js/code/menu_xml_manage.js"></script>
<script>
(function(){
	$(document).off('click','#dialog_tree .tree .item').on('click','#dialog_tree .tree .item',function(){
		var menu = $(this).text();
		var cd_tp1= $(this).closest('div').attr('cd_tp1');
		var comCode = $(this).parents('li:eq(2)').attr('id');
		
		if(menu == 'ElaConfig' || menu == 'provideIF'){ //no metas menu.
			$(this).toggleClass('opened');
		} else if (menu.indexOf('(')>-1){ //click company code.
			if(!$(this).next().children().length){
				drawTreeLevel2(menu.split('(')[0]);
			}
			$(this).toggleClass('opened');
		} else { //menu_test_data show
			$('#dialog_tree .tree .item').removeClass('active');
			$(this).addClass('active');
			loadCenterPage('code/menu_xml_page', 'N');
		}
	});
	
	$(document).off('click','#dialog_tree .tree .arr').on('click','#dialog_tree .tree .arr',function(e){
		e.stopPropagation();
		
		var comCode = $(this).closest('li').attr('id');
		var children = $(this).parent('div').next().children().length > 0 ? false : true;
		
		if(children){
			drawTreeLevel2(comCode);
		}
		
		$(this).parent('.item').toggleClass('opened');
	});
})();

selectCommonCompanys('tree');
</script>