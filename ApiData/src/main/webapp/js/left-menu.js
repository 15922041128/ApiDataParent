

function initMenu() {
  $('#menu ul').hide();
 //$('#menu ul:first').show();
  $('#menu li:first a span').click( function() {
	   $('#menu li:first a span').css("color","#c11000");
	   $(this).parents('li').siblings('li').find('a').css("color","#666");
  });
  
  $('#menu li ul li').click( function() {
	  $(this).parents('#menu').find('a').css("color","#666");
	  $('#menu li a span').css("color","#333");
	  $(this).find('a').css("color","#c11000");
  });								
		
  $('#menu li a').click(
    function() {
      var checkElement = $(this).next();
      if((checkElement.is('ul')) && (checkElement.is(':visible'))) {
        return false;
        }
      if((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
        $('#menu li ul:visible').slideUp('normal');
        checkElement.slideDown('normal');
        return false;
        }
      }
    );
  }
$(document).ready(function() {
	initMenu();
	//getApplicationCount();

 });

function getApplicationCount(){
var result;
$.ajax({
  	cache:false,
    type:"post",
    async: false,
	url:"reportAction.do",
	data:{method:"newApplicationCount"},
	dataType:"text",
	success:function(result){
		// alert('result'+result);
		if(result!=0){
		$('#reportno1').addClass('new1');//.text(result);
		$('#reportno2').addClass('new1');//.text(result);
		}
	
	}
 	});
 	return result;
}