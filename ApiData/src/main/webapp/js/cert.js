function onloadCertList() {
	//OnLoad();
	var DNs = getCerts();
	if(DNs!==false){
		var option=document.getElementById("certlist").options;
		option.length = 0;
		for (var i = 0; i < DNs.length; i++) {
			option.add(new Option(DNs[i], DNs[i]));
		}
	}
};

function signCert(){
	var strMessage = String(new Date().getTime()),strDN=document.getElementById("certlist").value
	,c = document.getElementById("CryptoAgency1");
	try {
		c.AddCertBySubjectDN(strDN);
        var cert = c.GetCertContent();
        var strRet = c.SignData(strMessage);
		//签名结果
		document.getElementById("strRet").value = strRet;
        document.getElementById("certificate").value = cert;
        //document.getElementsByTagName("form")[0].submit();
	}
	catch(e){
		alert(c?c.GetLastErrorDesc():e.message);
		return false;
	}
};

function getCerts(){
	try {
	    var c=document.getElementById("CryptoAgency1");
	    if(c&&(typeof c.GetCertSubjectDNs != 'undefined')){
	    	var DNs=c.GetCertSubjectDNs("CFCA");
	    	return DNs?DNs.split("||"):false;
	    }
	    return false;
    }catch(e){
    	alert((c?c.GetLastErrorDesc():"")+" "+e.message);
    	return false;
    }
};

// 显示下载提示 added by cfj
function displayAddr(){	
	var span_1=document.getElementById("span_1");  
	//var span_2=document.getElementById("span_submit");
	if(checkCab()==false){
		//span_2.style.display='none';
		//span_1.innerHTML='<a href="#" class="p4" onclick="downLoad()">点击链接安装控件,安装完毕后请刷新当前页面</a>';
		span_1.innerHTML='<input type="button" value="点击链接安装控件,安装完毕后请重启浏览器" onclick="downLoad()"/>';
		//span_2.innerHTML='<a href="#" class="p4" onclick="onLoad()">刷新页面</a>';
	}else{
		span_1.innerHTML='请插入数字证书，并点击"下一步"';		
	}
}

// 检查是否安装了控件  added by cfj
function checkCab() {
    try 
    {
		var c=document.getElementById("CryptoAgency1");
		
		var LastErrorDesc = c.GetLastErrorDesc();  //调用控件的一个可用函数
		if(LastErrorDesc!="undefined"){				//如果该函数调用成功
			return true;
		}
		else{										//如果该函数调用失败
			return false;
		}
		
    }
    catch (e)
    {    	 
    	 return false;
    }
}

function downLoad() {
    try {
    	if (navigator.appName.indexOf("Internet") >= 0 || navigator.appVersion.indexOf("Trident") >= 0) {
            if (window.navigator.cpuClass == "x86") {
            	location.href=_BASEPATH_+'download/CryptoKit.PBC.CRC.x86.exe';
            }
        }
        else {
            location.href=_BASEPATH_+'download/npCryptoKit.PBC.CRC.x86.exe';
        }    	
    }
    catch (e) {
        alert("控件下载失败");
        return;
    }
}

var OnLoad = (function(){
	var str,ua = navigator.userAgent.toLowerCase(),
	    check = function(r){return r.test(ua);},
	    isOpera = check(/opera/),isIE = !isOpera && check(/msie/);
	if(isIE){
		str = '<object id="CryptoAgency1" codeBase="'+_BASEPATH_+'download/CryptoKit.PBC.CRC.x86.cab#version=3,4,0,2" classid="clsid:F7AACEAA-BE9D-4ECD-8A19-067765E4387C"></object>';
	}else{
		str = '<embed id="CryptoAgency1" type="application/npCryptoKit.PBC.CRC.x86" style="height: 0px; width:0px"></embed>';
	}
	return function(){
		return str;
	};
})();