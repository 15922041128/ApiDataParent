$(document).ready(function(){
    mouseenterClass('user-div','active');
    mouseenterClass('top-li4','code-active');

    $("input[name=q]").focus(function(){
        document.onkeydown = function(e){
            var ev = document.all ? window.event : e;
            if(ev.keyCode==13) {
                $("#searchBtn_v4").click();
            }
        }
    })
    $("#searchBtn_v4").click(function(){
        var q = $("input[name=q]").val();
        q=q.replace(/\//g,'');

        //$("#juheapi_search").css({background:"#FFFFFF"});
        if(q.length==''){
            //$("#juheapi_search").css({background:"#fae1dc"}).focus();
            //$("#searchBtn_v4").focus();
            //return false;
            q='天气预报';
        }

        //var searchUrl = '/docs/s/q/%40q%40';
        //var searchUrl = '<{R action="docs" method="s" params="q=@q@"}>';
        searchUrl=searchUrl.replace(/%40q%40/,encodeURIComponent(q));
        window.location.href=searchUrl;
    })
    $("#loginout").click(function(){
        $.getJSON(loginOutUrl,function(obj){
            //window.location.href=juheIndex;
            window.location.reload();
        })
    });
    
    
    //返回顶部联系我们qq
    $('#juhegoTop li a').mouseover(function(){
		$(this).next('span').fadeIn(300);
	}).mouseout(function(){
		$(this).next('span').hide();
	});

	$(window).scroll(function(){
		if ($(window).scrollTop() > 100 ){
			$("#tools_goTop").fadeIn(300);
			$('#tools_goTop span').hide();
		} else {
			$("#tools_goTop").fadeOut(300);
		};
	});

	$("#tools_goTop a").click(function(){
		$(window).scrollTop(0);
	});
    
})
function mouseenterClass(did,dclass){
    $('.'+did).mouseenter(function(){
        $(this).addClass(dclass);
    }).mouseleave(function(){
        $(this).removeClass(dclass);
    });
}