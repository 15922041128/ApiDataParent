/** 注册 */
function doRegister() {
	
	var userName = $('#userName').val();
	var password = $('#password').val();
	var password_confirm = $('#password2').val();
	var compName = $('#compName').val();
	var compTel= $('#compTel').val();
	var contactName = $('#contactName').val();
	var contactTel = $('#contactTel').val();
	var vCode = $('#vCode').val();
	var hiddenVCode = $('#hiddenVCode').val();
	
	if (userName=='') {
		showMessage('warnMessage', "用户名不能为空", true, 5);
		return false;
	}
	
	if (password=='') {
		showMessage('warnMessage', "密码不能为空", true, 5);
		return false;
	}
	
	if (password!=password_confirm) {
		showMessage('warnMessage', "两次密码必须相同", true, 5);
		return false;
	}
	
	if (compName=='') {
		showMessage('warnMessage', "公司名称不能为空", true, 5);
		return false;
	}
	
	if (compTel=='') {
		showMessage('warnMessage', "公司电话不能为空", true, 5);
		return false;
	}
	
	if (contactName=='') {
		showMessage('warnMessage', "联系人姓名不能为空", true, 5);
		return false;
	}
	
	if (contactTel=='') {
		showMessage('warnMessage', "联系人电话不能为空", true, 5);
		return false;
	}
	
	// 验证帐号是否存在
	
	var params = {"userName" : userName};
	
	var userNameExist = false;
	
	$.ajax({
           async:false,
           type: "GET",  
           url: "r/user/isExist",  
           data: params,
           success: function(msg){
               	if ("Y"==msg) {
               		userNameExist = true;
               	}
           }  
    });
	
	if (userNameExist == true) {
		showMessage('warnMessage', "该帐号已存在", true, 5);
		return false;
	}
	
	if (vCode == '') {
		showMessage('warnMessage', "请填写验证码", true, 5);
		return false;
	}
	
	if (vCode != hiddenVCode) {
		showMessage('warnMessage', "验证码错误,请重新获取", true, 5);
		return false;
	}
	
	var params = {
					"userName" : userName, 
					"password" : password, 
					"compName" : compName,
					"compTel" : compTel,
					"contactName" : contactName,
					"contactTel" : contactTel
				 };
	
	
	 $.ajax({
        async:false,
        type: "GET",  
        url: "r/user/register",  
        data: params,
        success: function(msg){
        	$('#userName').val('');
    		$('#password').val('');
    		$('#password2').val('');
    		$('#compName').val('');
    		$('#compTel').val('');
    		$('#contactName').val('');
    		$('#contactTel').val('');
    		$('#vCode').val('');
    		$('#hiddenVCode').val('');
        	swal("注册成功");
        	$("#clostButton").click();
        }  
    });
	
}

/** 发送验证码 */
function sendVCode(obj) {
	
	var contactTel = $('#contactTel').val();
	if (contactTel=='') {
		showMessage('warnMessage', "联系人电话不能为空", true, 5);
		return false;
	}
	var res = vaildPhoneNo(contactTel);
	if (!res) {
		showMessage('warnMessage', "联系人电话格式错误", true, 5);
		return false;	
	}
	
	var params = {"phoneNo" : contactTel};
	
	$.ajax({
        async:false,
        type: "GET",  
        url: "r/SMS/getVCode",
        data: params,
        success: function(vCode){
        	$('#hiddenVCode').val(vCode);
        }  
    });
	
	settime(obj);
}

/** 页面按钮事件 */
function signupLinkClick() {
	  //$('#login-modal').modal('toggle');
      //$('#signup-modal').modal();
	$('#login-modal').hide();
	$('#signup-modal').show();
}

function loginLinkClick() {
	//$('#signup-modal').modal('toggle');
    //$('#login-modal').modal();
	$('#login-modal').show();
	$('#signup-modal').hide();
}

function back2LoginLink() {
	// $('#resetpass-modal').modal('toggle');
    // $('#login-modal').modal();
}

function resetpassLink() {
	 // $('#login-modal').modal('hide');
}

function doClose() {
	$('#login-modal').hide();
	$('#signup-modal').hide();
}

var app = angular.module('apiData', ['ui.router', 'ngAnimate']);
app.config(function($stateProvider) {

    $stateProvider
	    .state('dataManager', {
			url: "^" + getLocation() + '/dataManager',
			templateUrl: 'dataManager.html'
		})
});
app.controller('loginController', function ($scope, $http, $state) {
	
	$scope.imageUrl = 'images/bigdata.png';
	
	
	/** 初始化 */
	$scope.init = function() {
		$scope.loginModel = {show: true};
		$scope.registModel = {show: false};
	}
	
	/** 登录 */
	$scope.login = function() {
		
		var userName = $("#login_userName").val();
		var password = $("#login_password").val();
		
		if (userName=='') {
			showMessage('warnMessage_login', "用户名不能为空", true, 5);
			return false;
		}
		
		if (password=='') {
			showMessage('warnMessage_login', "密码不能为空", true, 5);
			return false;
		}
		
		$http({
			method: 'GET',
//			url: 'r/user/login',
			url: 'login',
			params: {'userName': userName, 'password': password}
		}).success(function(data){
			var msg = data.isSuccess;
        	var user = data.loginUser;
        	if ('Y' == msg) {
        		$scope.loginModel = {show: false};
        		$scope.registModel = {show: true};
        		$scope.loginUserName = user.userName;
        		$('#login-modal').hide();
        	} else {
        		showMessage('warnMessage_login', "密码不正确,请重新输入!", true, 5);
        	}
		}).error(function(data){
			
		});
	}
	
	/** 注销 */
	$scope.loginOut = function() {
		
		$http({
			method: 'GET',
			url: 'r/user/loginOut'
		}).success(function(){
			$scope.loginModel = {show: true};
			$scope.registModel = {show: false};
			$scope.loginUserName = '';
		}).error(function(){
			
		});
	}
	
	/** 申请试用 */
	$scope.applyTrial = function(productID) {
		var loginUserName = $scope.loginUserName; 
		if (undefined == loginUserName || '' == loginUserName) {
			$('#login-modal').show();
		} else {
			window.location.href = 'dataManager.html';
//			$state.go('dataManager', {productID: productID});
//			window.location.href = 'dataManager.html';
//			$http({
//				method: 'GET',
//				url: 'dataManager',
//				params: {'productID': productID}
//			}).success(function(data){
//				alert(data);
//				$scope.productID = data.productID;
//				alert($scope.productID);
//			}).error(function(){
//				alert(1);
//			});
		}
	}
	
	$scope.personalCenter = function () {
		window.location.href = 'personalCenter/index.html';
	}
});

