// 获取url路径
function getLocation(){
	var curWwwPath = window.document.location.href;
	var pathName =  window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0,pos);
	var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return (localhostPaht + projectName);
}

// 获取url路径参数
function getUrlValue(name){
	 var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r != null) {
    	 return decodeURI(r[2]);
     } 
     return "-1";
}

// 以post方式打开新窗口
function openNewPageWithPostData(url, name, paramNames, paramValues) { 
	var newWindow = window.open(url, name);  
    if (!newWindow)  {
    	return false;  	
    }
    
    var html = "";  
    html += "<html><head></head><body><form id='formid' method='post' action='" + url + "'>";  
    
    for(var i=0 ; i<paramNames.length ; i++) { 
    	html += "<input type='hidden' name='" + paramNames[i] + "' value='" + paramValues[i] + "'/>";
    } 
      
    html += "</form><script type='text/javascript'>document.getElementById('formid').submit();";  
    html += "<\/script></body></html>".toString().replace(/^.+?\*|\\(?=\/)|\*.+?$/gi, "");   
    newWindow.document.write(html);  
      
    return newWindow;  
}

/** *********************页面提示信息显示方法************************* */
/**
 * 显示的div，提示信息，是否晃动，自动隐藏时间：-1为不隐藏，其它为隐藏时间（单位秒) message
 * 为false时表示不需要提示信息，仅需要显示div即可
 */
function showMessage(id,message,ishake,time){
	if(message!=""){
		if(message!="false"&&message!=false)
			$("#"+id).html(message);
		$("#"+id).fadeIn(300);
		if(ishake){
			shake(id);
		}
		if(time!=-1){
			if(isNaN(time))
				time=2000;
			else if(time>0)
				time = time * 1000;
			setTimeout(function(){
				if(time!=0){
				   $("#"+id).fadeOut(500);
				}
				else{
					$("#"+id).fadeOut(300);
				}
				$("#"+id).hide("fast");
			},time);
		}
	}
}
// 晃动div
function shake(o){
    var $panel = $("#"+o);
    var box_left =0;
    $panel.css({'left': box_left});
    for(var i=1; 4>=i; i++){
        $panel.animate({left:box_left-(8-2*i)},50);
        $panel.animate({left:box_left+2*(8-2*i)},50);
    }
}