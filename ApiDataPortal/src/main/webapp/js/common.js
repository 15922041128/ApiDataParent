$(function () {
    $(".sideMenu ul.menuDrop").children('li').removeClass("current");
    var selectMenu = $(".sideMenu ul.menuDrop a[href$=\'"+ location.pathname +"\']");
    jQuery(selectMenu.parent()).css("background","#37a1e5");
	
	//捕获回车键
    $('#keyword').bind('keydown',function(e){
        if(e.keyCode==13){

            if($.trim($("#keyword").val()) == ""){
                return false;
            }

            $("#searchForm").submit();
        }
    });

    $(".search-btn").click(function(){
        if($("#keyword").val() == ""){
            return false;
        }
        $("#searchForm").submit();
        //window.open("/search/apis?keyword=" + htmlEscapedString($("#keyword").val()));
    });
	
	 //除了首页隐藏二级页面
	$(".sideMenu.action-menu-drop").hover(function(){
		$(this).addClass("hover");
	},
	function(){
		 $(this).removeClass("hover");
	})

    //用户信息
    $(".user-info").hover(function () {
        $(this).addClass("on");
    }, function () {
        $(this).removeClass("on");
    });

    //账号类型
    $("input:radio[name=type-radio]").change(function () {
        var type_val = $('input:radio[name="type-radio"]:checked').val();
        if (type_val == "2") {
            $(".qy-ipt").show();
        } else {
            $(".qy-ipt").hide();
        }
    });

    //输入框颜色
    $(".input01, .gn-input01, .textarea01").focus(function () {
        $(this).addClass("focus-border");
    }).blur(function () {
        $(this).removeClass("focus-border");
    });

    $(".input02").focus(function () {
        $(this).parent(".input-box").addClass("focus-border");
    }).blur(function () {
        $(this).parent(".input-box").removeClass("focus-border");
    });

    //公开 未公开
    $(".i-switch").live("click", function () {
        /*暂时写这儿，后面统一整理*/
        var aid = $(this).attr('aid');
        var tag = $(this).attr('tag');        //当前的状态码

        var status = switchStatus(tag);
        var data = {id:aid, status:status};
        ajax_changeStatus(data,null,function(){
            window.location.reload();
        });

    });


    $(".edit-dropdown .edit-btn").live("click", function (event) {
        $(this).parent(".edit-dropdown").toggleClass("on")
        event.stopPropagation();
    });
    $(document).click(function () {
        $(".edit-dropdown").removeClass("on");
    });

    //认证为企业用户 获取焦点失去焦点时文本框中文字颜色的改变
    $(".normal-input").focus(function(){
        $(this).css("color","#666");
    }).blur(function(){
        $(this).css("color","#a9a9a9");
    });

    //个人用户问号增减hover
    $("#help").hover(function () {
        $(this).parent().toggleClass("hover");
    });

    //个人用户中心左右高度自适应相等
    var $height_content_r = $(".user-content-r").outerHeight();
    $(".user-content-l").css("height", $height_content_r);

    //个人用户中心nav点击增减current
    $("div.user-content-l").find("li").click(function(){
        $(this).addClass("current").siblings().removeClass("current");
    });

});
/**
 * 改变是否公开按钮的状态
 * @param tag 当前的状态
 * @returns {*}  改变后的状态
 */
function switchStatus(tag){
    var status;        //click事件要改成的状态
    var $obj = $(".i-switch");
    switch (parseInt(tag)) {
        case 0: //私有  click:提交审核
            $obj.attr('class', 'i-switch');
            $('.textTip').attr('style','display:block;');
            $('.textTip').text('审核中，审过后对外展示');
            status = 2;
            break;
        case 1://公开 click： 变为私有
            $obj.attr('class', 'i-switch off');
            $('.textTip').attr('style','display:none;');
            status = 0;
            break;
        case 2: //审核中 click：变为私有
            $obj.attr('class', 'i-switch off');
            $('.textTip').attr('style','display:none;');
            status = 0;
            break;
        case 3: //未通过 click：提交审核
            $obj.attr('class', 'i-switch');
            $('.textTip').attr('style','display:block;');
            $('.textTip').text('审核中，审过后对外展示');
            status = 2;
            break;
    }
    $obj.attr('tag',status);
    return status;
}

function ajax_changeStatus(data,success_callBack,fail_callBack){
    $.ajax({
        type: 'POST',
        url: '/api/updateApiStatus',
        data: 'id=' + data.id + '&status=' + data.status,
        dataType: 'json',
        success: function (data) {
            if(data.code == 0){
                if(success_callBack != null){
                    success_callBack();
                }
            }else{
                fail_callBack();
            }
        },
        error: function (data) {
            fail_callBack();
        }
    });
}

function updateApiAfterConfirm(success) {
    $.confirm({
        'title'    : '提示：',
        'message'	: '数据信息已修改成功！需要重新审核发布。 <br /><br />是否现在发布此数据？',
        'buttons'	: {
            '发布'	: {
                'class'	: '#1fb5ad',
                'action': success
            },
            '暂不发布'	: {
                'class'	: 'gray',
                'action': function(){}	// Nothing to do in this case. You can as well omit the action property.
            }
        }
    });
}

//关闭弹出层
function close_popup(obj) {
    $(obj).parent().parent().parent().find(':input').each(function () {
        $(this).val('');
        clearValidation($(this));
    });
    tobeDel = null;
    layer.closeAll();
}

function setTab(name, cursel, n) {
    for (i = 1; i <= n; i++) {
        var menu = document.getElementById(name + i);
        var con = document.getElementById("con-" + name + "-" + i);
        menu.className = i == cursel ? "on" : "";
        con.style.display = i == cursel ? "block" : "none";
    }
}

$(function(){
//返回顶部
    $(".gotop").click(function(){
        $('body,html').animate({scrollTop:0},1000);
        return false;
    });

    //浮层
    $(".index-lst li").hover(function(){
        $(this).addClass("hover");
    },function(){
        $(this).removeClass("hover");
    });

    $(".hot-lst li a").hover(function(){
        $(this).find(".nav-icon").css("background-position","0 -20px");
    },function(){
        $(this).find(".nav-icon").css("background-position","0 0");
    });
});


///* 返回顶部 */
//var scrolltotop = {
//    setting: {
//        startline: 0, //起始行
//        scrollto: 0, //滚动到指定位置
//        scrollduration: 400, //滚动过渡时间
//        fadeduration: [500, 100] //淡出淡现消失
//    },
//    controlHTML: '<span class="topBack"></span><br><a href="/help/questionUI?#maodian" target="_self"  onclick="doSomething(this,event)"><span class="message"></span></a>', //返回顶部按钮
//    controlattrs: {offsetx: 30, offsety: 50},//返回按钮固定位置
//    anchorkeyword: "#top",
//    state: {
//        isvisible: false,
//        shouldvisible: false
//    }, scrollup: function () {
//        if (!this.cssfixedsupport) {
//            this.$control.css({opacity: 0});
//        }
//        var dest = isNaN(this.setting.scrollto) ? this.setting.scrollto : parseInt(this.setting.scrollto);
//        if (typeof dest == "string" && jQuery("#" + dest).length == 1) {
//            dest = jQuery("#" + dest).offset().top;
//        } else {
//            dest = 0;
//        }
//        this.$body.animate({scrollTop: dest}, this.setting.scrollduration);
//    }, keepfixed: function () {
//        var $window = jQuery(window);
//        var controlx = $window.scrollLeft() + $window.width() - this.$control.width() - this.controlattrs.offsetx;
//        var controly = $window.scrollTop() + $window.height() - this.$control.height() - this.controlattrs.offsety;
//        this.$control.css({left: controlx + "px", top: controly + "px"});
//    }, togglecontrol: function () {
//        var scrolltop = jQuery(window).scrollTop();
//        if (!this.cssfixedsupport) {
//            this.keepfixed();
//        }
//        this.state.shouldvisible = (scrolltop >= this.setting.startline) ? true : false;
//        if (this.state.shouldvisible && !this.state.isvisible) {
//            this.$control.stop().animate({opacity: 1}, this.setting.fadeduration[0]);
//            this.state.isvisible = true;
//        } else {
//            if (this.state.shouldvisible == false && this.state.isvisible) {
//                this.$control.stop().animate({opacity: 0}, this.setting.fadeduration[1]);
//                this.state.isvisible = false;
//            }
//        }
//    }, init: function () {
//        jQuery(document).ready(function ($) {
//            var mainobj = scrolltotop;
//            var iebrws = document.all;
//            mainobj.cssfixedsupport = !iebrws || iebrws && document.compatMode == "CSS1Compat" && window.XMLHttpRequest;
//            mainobj.$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $("html") : $("body")) : $("html,body");
//            mainobj.$control = $('<div id="topcontro">' + mainobj.controlHTML + '</a>' + "</div>").css({
//                position: mainobj.cssfixedsupport ? "fixed" : "absolute",
//                bottom: mainobj.controlattrs.offsety,
//                right: mainobj.controlattrs.offsetx,
//                opacity: 0,
//                cursor: "pointer"
//            }).attr({title: "返回顶部"}).click(function () {
//                mainobj.scrollup();
//                return false;
//            }).appendTo("footer");
//            if (document.all && !window.XMLHttpRequest && mainobj.$control.text() != "") {
//                mainobj.$control.css({width: mainobj.$control.width()});
//            }
//            mainobj.togglecontrol();
//            $('a[href="' + mainobj.anchorkeyword + '"]').click(function () {
//                mainobj.scrollup();
//                return false;
//            });
//            $(window).bind("scroll resize", function (e) {
//                mainobj.togglecontrol();
//            });
//        });
//    }
//};
//scrolltotop.init();

//替换脚本标签
function htmlEscapedString(str) {
    var badChars = /&(?!\w+;)|[<>"']/g;
    var map = {"&": "&amp;", "<": "&lt;", ">": "&gt;", "\"": "&quot;"};
    var fn = function (s) {
        return map[s] || s;
    };
    return str.replace(badChars, fn);
};

function htmlEscape(str) {
    var badChars = /&(?!\w+;)|[<>"']/g;
    var map = {"<": "&lt;", ">": "&gt;"};
    var fn = function (s) {
        return map[s] || s;
    };
    return str.replace(badChars, fn);
};

function isJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}

function addUrlQuery(url, name, sample) {
    var signal = '&';
    if (url.indexOf('?') == -1) {
        signal = '?';
    }
    return url.trim() + signal + name + '=' + sample;
}

function removeUrlQuery(url, name) {
    var inx = url.indexOf('?' + name + '=');
    if (inx >= 0) {
        var rUrl = url.substr(0, inx);
        var indx = url.indexOf('&', inx + 1);
        if (indx >= 0) {
            rUrl = rUrl + "?" + url.substr(indx + 1);
        }
        return rUrl;
    }
    inx = url.indexOf('&' + name + '=');
    if (inx >= 0) {
        var rUrl = url.substr(0, inx);
        var indx = url.indexOf('&', inx + 1);
        if (indx >= 0) {
            rUrl = rUrl + url.substr(indx);
        }
        return rUrl;
    }
    return url;
}

function doSomething(obj, evt) {
    var e = (evt) ? evt : window.event;
    if (window.event) {
        e.cancelBubble = true;// ie下阻止冒泡
    } else {
        e.stopPropagation();// 其它浏览器下阻止冒泡
    }
}

var alertWindow = function alertWindow(){
    $("#dataReport").show();
    var timer = window.setTimeout(hiddenMsg,3000);

    $(".close").click(function(){
        $("#dataReport").fadeOut();
        clearTimeout(timer);
    });

    $(".buttons a").click(function(){
        $("#dataReport").fadeOut();
        clearTimeout(timer);
    });

    function hiddenMsg(){
        $("#dataReport").fadeOut();
    }
}

function seletedTileStyle(index){
    if(index>=0){
        jQuery(".lnb-lst  li a")
            .parent("li")
            .removeClass()
            .eq(index).children("a")
            .addClass("cur");
    }else{
        jQuery(".lnb-lst  li a")
            .parent("li")
            .removeClass();
    }

}

//去掉前后空格
function replaceSpace(obj){
    obj.value = $.trim(obj.value);
}
