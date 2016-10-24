	var booleanFlag = true;
	$(function(){
		$("#btn_next").click(function(){
			if(booleanFlag){
				$('#authForm').submit();
				booleanFlag = false;
			}
		});
		// 只有android系统才显示
		if (navigator.userAgent.match(/android/i)) {
			$('#id_android').show();
			$('#id_computor').hide();
		}else{
			$('#id_android').hide();
			$('#id_computor').show();
		}
	});