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

//以post方式打开新窗口
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