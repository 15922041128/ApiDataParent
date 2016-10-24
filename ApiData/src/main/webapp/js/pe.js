//(function(){
//	var doc=document,
//		ua = navigator.userAgent.toLowerCase(),
//	    check = function(r){return r.test(ua);},
//	    isOpera = check(/opera/),
//	    isChrome = check(/chrome/),
//	    isWebKit = check(/webkit/),
//	    isSafari = !isChrome && check(/safari/),
//	    isSafari2 = isSafari && check(/applewebkit\/4/),
//	    isSafari3 = isSafari && check(/version\/3/),
//	    isSafari4 = isSafari && check(/version\/4/),
//	    isIE = !isOpera && check(/msie/),
//	    isIE7 = isIE && check(/msie 7/),
//	    isIE8 = isIE && check(/msie 8/),
//	    isIE9 = isIE && check(/msie 9/),
//	    isIE10 = isIE && check(/msie 10/),
//	    isIE11 = isIE && check(/msie 11/),
//	    isIE6 = isIE && !isIE7 && !isIE8 && !isIE9 && !isIE10 && !isIE11,
//	    isGecko = !isWebKit && check(/gecko/),
//	    isGecko2 = isGecko && check(/rv:1\.8/),
//	    isGecko3 = isGecko && check(/rv:1\.9/);
//	function preventKey(e) {
//		var ev = e || window.event,//��ȡevent����  
//		obj = ev.target || ev.srcElement,//��ȡ�¼�Դ  
//		t = obj.type || obj.getAttribute('type'),readonly = obj.readOnly||obj.getAttribute('readonly'), code = ev.keyCode||ev.which||ev.charCode,charcode = String.fromCharCode(code).toLowerCase();//��ȡ�¼�Դ�ؼ�����,�ؼ�ֻ�����ԣ�����ֵ
//		if ( (code == 8 && t != "password" && t != "text" && t != "textarea")||(readonly&&(t == "password" || t == "text" || t == "textarea"))) {//����ֻ����������ֹ���˼�
//			return false;
//		}
//		if (((isOpera || isGecko)?ev.which==0:true)&&(code == 116 || code == 122|| code == 123 || (ev.shiftKey && code == 121))||(((isOpera || isGecko)?ev.which!=0:true)&&ev.ctrlKey && (charcode == 'a' || charcode == 's'))) {//���� F5,F11,F12,shift+F10,ctrl+a,ctrl+s
//			if (isIE)
//				ev.keyCode = 0;
//			ev.returnValue = false;
//			return false;
//		}
//	};
//	if (isOpera || isGecko)
//		doc.onkeypress = preventKey;
//	else if (isIE || isChrome || isSafari)
//		doc.onkeydown = preventKey;
//	if(isIE)document.onselectstart=function(){return false;};
//	doc.oncontextmenu = function(){
//		if(window.event){
//			window.event.cancelBubble = true;
//			window.event.returnValue=false;
//		}
//		return false;
//	};
//	try{window.history.forward(1);}catch(e){}
//})();
