// JavaScript Document
function addLoadEvent(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      oldonload();
      func();
    }
  }
}
/*====add 2014/10/29======*/
function displayTipYes(obj){
	var input = document.getElementById(obj.id);
	input.parentNode.getElementsByTagName("span")[0].style.display = "inline";
}

function displayTipNo(obj){
	var input = document.getElementById(obj.id);
	input.parentNode.getElementsByTagName("span")[0].style.display = "none";
}
/*=========*/

/*=========add 2014/11/11  =========*/
function displayYes(obj){
	var input = document.getElementById(obj.id);
	input.parentNode.getElementsByTagName("span")[0].style.display = "inline";
	input.parentNode.getElementsByTagName("span")[1].style.display = "none";
}


function displayNo(obj){
	var input = document.getElementById(obj.id);
	input.parentNode.getElementsByTagName("span")[0].style.display = "none";
	input.parentNode.getElementsByTagName("span")[1].style.display = "inline";
}
/*==================*/

$(function () {
       $("#s_notice1_t1").mouseenter(function () {
       $("#s_notice1_con1").stop().fadeIn(10);
    }).mouseleave(function () {
       $("#s_notice1_con1").fadeOut(10);
    })
	   $(".shopp").mouseenter(function () {
       $(".shopp_h").stop().fadeIn(10);
    }).mouseleave(function () {
       $(".shopp_h").fadeOut(10);
    })
})
$(function () {
       $("#s_notice1_t2").mouseenter(function () {
       $("#s_notice1_con2").stop().fadeIn(10);
    }).mouseleave(function () {
       $("#s_notice1_con2").fadeOut(10);
    })
	   $(".shopp").mouseenter(function () {
       $(".shopp_h").stop().fadeIn(10);
    }).mouseleave(function () {
       $(".shopp_h").fadeOut(10);
    })
})
$(function () {
       $("#s_notice1_t3").mouseenter(function () {
       $("#s_notice1_con3").stop().fadeIn(10);
    }).mouseleave(function () {
       $("#s_notice1_con3").fadeOut(10);
    })
	   $(".shopp").mouseenter(function () {
       $(".shopp_h").stop().fadeIn(10);
    }).mouseleave(function () {
       $(".shopp_h").fadeOut(10);
    })
})
$(function () {
       $("#s_notice1_t4").mouseenter(function () {
       $("#s_notice1_con4").stop().fadeIn(10);
    }).mouseleave(function () {
       $("#s_notice1_con4").fadeOut(10);
    })
	   $(".shopp").mouseenter(function () {
       $(".shopp_h").stop().fadeIn(10);
    }).mouseleave(function () {
       $(".shopp_h").fadeOut(10);
    })
})
$(function () {
       $("#s_notice1_t5").mouseenter(function () {
       $("#s_notice1_con5").stop().fadeIn(10);
    }).mouseleave(function () {
       $("#s_notice1_con5").fadeOut(10);
    })
	   $(".shopp").mouseenter(function () {
       $(".shopp_h").stop().fadeIn(10);
    }).mouseleave(function () {
       $(".shopp_h").fadeOut(10);
    })
})
$(function () {
       $("#s_notice1_t6").mouseenter(function () {
       $("#s_notice1_con6").stop().fadeIn(10);
    }).mouseleave(function () {
       $("#s_notice1_con6").fadeOut(10);
    })
	   $(".shopp").mouseenter(function () {
       $(".shopp_h").stop().fadeIn(10);
    }).mouseleave(function () {
       $(".shopp_h").fadeOut(10);
    })
})
$(function () {
       $("#s_notice1_t7").mouseenter(function () {
       $("#s_notice1_con7").stop().fadeIn(10);
    }).mouseleave(function () {
       $("#s_notice1_con7").fadeOut(10);
    })
	   $(".shopp").mouseenter(function () {
       $(".shopp_h").stop().fadeIn(10);
    }).mouseleave(function () {
       $(".shopp_h").fadeOut(10);
    })
})
$(function () {
       $("#s_notice1_t8").mouseenter(function () {
       $("#s_notice1_con8").stop().fadeIn(10);
    }).mouseleave(function () {
       $("#s_notice1_con8").fadeOut(10);
    })
	   $(".shopp").mouseenter(function () {
       $(".shopp_h").stop().fadeIn(10);
    }).mouseleave(function () {
       $(".shopp_h").fadeOut(10);
    })
})
;