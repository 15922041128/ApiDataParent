function iFrameHeight() {
	var ifm = document.getElementById("conFrame");
	/*var ifm2 = document.getElementById("leftFrame");*/
	var subWeb = document.frames ? document.frames["conFrame"].document : ifm.contentDocument;
	if (ifm != null && subWeb != null) {
		var iheight = subWeb.body.scrollHeight;
			
		ifm.height = iheight;
		ifm2.height = iheight;
			
	}
}