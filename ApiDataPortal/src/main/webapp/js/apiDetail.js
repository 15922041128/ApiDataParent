$(function () {

    //自定义title
    $(".side-list li a").each(function(b) {//这里是控制标签

        if (this.title) {

            var c = this.title; //把title的赋给自定义属性 myTilte ，屏蔽自带提示
            var a = 30; //设置提示框相对于偏移位置，防止遮挡鼠标
            console.log(c);
            $(this).mouseover(function(d) { //鼠标移上事件
                this.title = "";

                $("body").append('<div id="tooltip"><i></i>' + c + '</div>');
                $("#tooltip").css({
                    left: (d.pageX + a) + "px",
                    top: d.pageY + "px",
                    //opacity: "0.8"
                }).show(250) //设置提示框的坐标，并显示
            }).mouseout(function() { //鼠标移出事件
                this.title = c; //重新设置title
                $("#tooltip").remove() //移除弹出框
            }).mousemove(function(d) { //跟随鼠标移动事件
                $("#tooltip").css({
                    left: (d.pageX + a) + "px",
                    top: d.pageY + "px"
                })
            })
        }
    })

    //价格变动
    $('.price-times').live('click',function(){
        i = $(this).index()-1;
        $('.price-num').siblings(".price-num").each(function () {
            $(this).hide();
        });
        $('.price-num:eq('+i+')').show();
        $('.discountPrice').siblings(".discountPrice").each(function () {
            $(this).hide();
        });
        $('.discountPrice:eq('+i+')').show();
        $('.equivalent-price').siblings(".equivalent-price").each(function () {
            $(this).hide();
        });
        $('.equivalent-price:eq('+i+')').show();

    });

    //查看更多优惠价格
    $("#check-more-price").click(function(){
        var price = '<span class="tit">价格：</span>￥ {{each chargeList as val i}}<span class="price-num" {{if i>0}}style="display: none"{{/if}} ><span class="price-true">{{val.price}}</span></span>{{/each}}<span class="check-more-price-clicked">获取更多优惠价格</span>';
        var specs = '<span class="tit">规格：</span> {{each chargeList as val i}}<span class="price{{if i==0}} selected chargeSelected{{/if}} price-times" onclick="priceSelect(this,0)" tag="{{val.itemCode}}" chargeId="{{val.id}}">{{if val.chargeType == 0}}{{val.count}}次{{else}}{{if val.count == 30}}月{{/if}}{{if val.count == 90}}季{{/if}}{{if val.count == 180}}半年{{/if}}{{if val.count == 360}}年{{/if}}{{/if}}</span>{{/each}}';
        var apiId = $('#id').val();
        $.ajax({
            type: 'get',
            url: '/order/checkMorePrice',
            dataType: 'json',
            contentType: "application/json;charset=utf-8",
            cache: false,
            async: false,
            data: {apiId: apiId},
            success: function (data) {
                if(data.error == 'NotLogin'){
                    window.location.href = loginUrl + "/market/api/" + apiId;
                    return;
                }
                if(data.code == "0"){
                    layer.confirm("企业认证用户可查看更多优惠价格。",{
                        btn:['立即认证','关闭']
                    }, function(){
                        window.location.href = "/user/enterprise_auth_init";
                    }, function(){

                    });
                    return;
                }else{
                    if(data.count == 1){
                        layer.open({
                            content: '该数据未提供优惠价格<br/>请<strong><a href="/help/questionUI" class="org">联系我们</a></strong>获取更多优惠价格',
                            btn: ['关闭']
                        });
                    }else {
                        $("#apiCharge").html(createTable(data, price));
                        $("#specs").html(createTable(data, specs));
                    }
                }
            },
            error: function () {
                alert("系统错误，请刷新后重试！");
            }
        });
    });

    var createTable = function(data, tempTemplate) {
        var html = template.compile(tempTemplate)(data);
        return html;
    };




    $('#interface0').siblings("section").each(function () {
        $(this).attr('style', 'display:none;');
    });
    $('#index0').attr('class', 'cur');
    $('#index0').siblings().each(function () {
        $(this).attr('class', '');
    });
    $('pre[format=true]').each(function () {
        var str = $(this).text();
        //如果含有'{' 就认为返回的是json
        if(isJsonString(str)){
            str = JSON.stringify(eval('(' + str + ')'), null, 4);
            $(this).text(str);
        }
    });


    $('.purchaseBtn').click(function () {
        var para = {
            apiId: $('#id').val(),
            chargeId: $('#specs .chargeSelected').attr('chargeId'),
            count: $('#selectCount').val(),
            chargeType:$('#chargeType').val()
        };

        $.ajax({
            type: 'get',
            url: '/order/buy',
            dataType: 'json',
            contentType: "application/json;charset=utf-8",
            cache: false,
            async: false,
            data: {apiId: para.apiId, chargeId: para.chargeId, count: para.count, chargeType: para.chargeType},
            success: function (data) {
                if(data.error == 'NotLogin'){
                    window.location.href = loginUrl + "/market/api/" + para.apiId;
                    return;
                }
                if(0==data.code){
                    window.location.href = data.data;
                }else if(10==data.code||12==data.code){
                    $("#confirmEnt").fadeIn();
                    jQuery("#confirmBoot h3,.small").html(data.message);
                    jQuery("#confirmBoot").show();
                    jQuery("#confirmWindow").hide();
                }else{
                    jQuery("#confirmEnt").fadeIn();
                    jQuery("#confirmWindow h4,.small").html(data.message);
                    jQuery("#confirmWindow").show();
                    jQuery("#confirmBoot").hide();
                }
            },
            error: function (textStatus) {
                alert("系统错误，请刷新后重试！");
            }
        });
    });

    $(".collectBtn").click(function(){
        if($("#add-fav").children().hasClass('collectBtn')){
            var apiId = $('#id').val();
            $.ajax({
                type: 'get',
                url: '/collect/collect',
                dataType: 'json',
                contentType: "application/json;charset=utf-8",
                cache: false,
                async: false,
                data: {apiId: apiId},
                success: function (data) {
                    if(data.error == 'NotLogin'){
                        window.location.href = loginUrl + "/market/api/" + apiId;
                        return;
                    }
                    if(0==data.code){
                        window.location.href = data.data;
                    }else if(1==data.code||-1==data.code){
                        $("#add-fav").html('<i class="index-icon4"></i>已收藏<span class="collectNum"></span>');
                    }
                    var j = eval("("+data.data+")");
                    $(".collectNum").html('（'+ j.totalNum+'）');
                },
                error: function () {
                    alert("系统错误，请刷新后重试！");
                }
            });
        }
    })

    //购买数量的增减
    $(".plus").click(function (){
        var num = parseInt($(".numInput").val());
        $(".numInput").val(num+1);
    });

    $(".reduce").click(function () {
        var num = parseInt($(".numInput").val());
        if(num > 1){
            $(".numInput").val(num-1);
        }
    });

    /**控制标题下划线**/
    seletedTileStyle(0);

    /* 弹层 */
    $(".close").click(function(){
        $("#confirmEnt").fadeOut();
    });

    $(".blBtn").click(function(){
        $("#confirmEnt").fadeOut();
    });

    $(".appclose").click(function(){
        $("#confirmAppKey").fadeOut();
    });
    $(".appblBtn").click(function(){
        $("#confirmAppKey").fadeOut();
    });
    $('#makeSDK').click(function(){
        var apiId = $('#id').val();
        var lang = $(this).attr('lang');
        window.open("/makesdk?apiId="+apiId+'&lang='+lang);
    })

    jQuery(".homeDiv").slide({mainCell:".homeScroll ul",prevCell:".homeUp",nextCell:".homeDown",autoPage:true,effect:"left",vis:5,pnLoop:false});


    var $li = $('.sdk-box a');
    var $ul = $('.SDKcontent .pt10');
    $li.bind('click', function () {
        $('#sdkExample').removeClass('prettyprinted');
        var $this = $(this);
        // var $t = $this.index();
        $li.removeClass('on');
        $this.addClass('on');
        var lang = $this.attr('name');
        var tag = $this.attr('tag');
        $('#makeSDK').attr('clstag',"pageclick|keycount|wxlink_201608015|"+tag);
        $('#makeSDK').attr('lang',lang);
        // $ul.css('display','none');
        // $ul.eq($t).css('display','block')
        //渲染模板,展现sdk调用示例
        // sdkUtil.innerContent(,);
        new sdkUtil(lang + 'Template', JSON.parse($('#sdkSource').text()), lang).innerContent();

    });
    //初始化默认的java
    new sdkUtil('javaTemplate', JSON.parse($('#sdkSource').text()), 'java').innerContent()
});
var sdkUtil = function (tpl, data, lang) {
    this.tpl = tpl;
    this.data = data;
    this.lang = lang;
    this.innerContent = function () {
        if(!data.list)return null;
        switch (lang) {
            case 'java':
                langAdaptor.java(data);
                break;
            case 'android':
                langAdaptor.android(data);
                break;
            case 'php':
                langAdaptor.php(data);
                break;
            case 'python':
                langAdaptor.python(data);
                break;
            case 'csharp':
                langAdaptor.csharp(data);
                break;
            case 'objc':
                langAdaptor.objc(data);
                break;
            case 'swift':
                langAdaptor.swift(data);
                break;
        }
        var html = template(tpl, data);
        document.getElementById('sdkExample').innerHTML = html;
        PR.prettyPrint();
        $('#sdkExample').attr('style','border:1px solid #ddd');
    }
    var langAdaptor = {
        java: function (data) {
            var list = data.list;

            for (var i = 0; i < list.length; i++) {
                var type = list[i].type;
                list[i].type = type == 'Number' ? 'BigDecimal' : type;
            }
        },
        android : function (data) {
          this.java(data);
        },
        php: function (data) {
            var list = data.list;
            for (var i = 0; i < list.length; i++) {
                var type = list[i].type;
                list[i].type = type == 'String' ? 'string' : type;
            }
        },
        python: function (data) {
            var method = data.method;
            var plain = '';
            for (var i=0; i < method.length;i++){
                var s = method.charAt(i);
                if(s >= 'A' && s <= 'Z'){
                    s = '_' + s.toLowerCase();
                    while ((i+1 <method.length && method.charAt(i+1) >= 'A' && method.charAt(i+1) <= 'Z')){
                        i++;
                        s += method.charAt(i).toLowerCase();
                    }
                }
                plain += s;
            }
            data.method = plain;
        },
        csharp : function (data) {
            this.php(data);
        },
        objc : function (data) {
            data.apiName = 'SWGDefaultApi';
            var list = data.list;
            var suffix = '';
            var paramStr = '';
            for (var i = 0; i < list.length; i++) {
                var type = list[i].type;
                list[i].type = type == 'String' ? 'NSString' : (type == 'Number' ? 'NSNumber' : type);
                list[i].name = this.replaceUnderline(list[i].name);
                if(i == 0){
                    var temp = list[i].name;
                    suffix = temp.charAt(0).toUpperCase() + temp.substr(1);
                    paramStr = ':' + list[i].name + '\n\t';
                }else {
                    paramStr += list[i].name + ':' + list[i].name;
                    if(i != list.length -1){
                        paramStr += '\n\t';
                    }
                }
            }
            data.paramStr = paramStr;
            data.method = data.method + 'With' + this.replaceUnderline(suffix);   //baiduBaiKEWithBkKey
        },
        swift : function (data) {
            this.objc(data);
        },
        replaceUnderline : function (variable) {
            var loc = variable.indexOf('_');
            console.log(loc);
            if(loc > -1){ //含有下划线
                var tempVar = '';
                for (var i=0 ; i < variable.length ; i++){
                    if(variable.charAt(i) == '_'){
                        tempVar +=variable.charAt(++i).toUpperCase();
                    }else {
                        tempVar += variable.charAt(i);
                    }
                }
                variable = tempVar;
            }
            return variable;
        }
    };
}
/*获取appKey*/
function getKey(){
    var apiId = $('#id').val();
    $.ajax({
        type: 'get',
        url: '/gwtest/getAppKey',
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        cache: false,
        async: false,
        data: {apiId: apiId},
        success: function (data) {
            if(data.error == 'NotLogin'){
                window.location.href = loginUrl + "/market/api/" + apiId;
                return;
            }
            if(-1==data.code){
                window.location.href = data.data;
            }else if(0==data.code){
                $("#confirmAppKey").fadeIn();
                jQuery("#confirmApp div.small").html(data.message);
                jQuery("#confirmApp").show();
                jQuery("#confirmDL").hide();
            }
        },
        error: function (textStatus) {
            alert("系统错误，请刷新后重试！");
        }
    });
}
function priceSelect(obj,flag) {
    if(flag == 1){//促销
        $(obj).attr('class', 'free price selected2 chargeSelected price-times');
        $(obj).siblings(".price").each(function () {
            $(this).attr('class', 'free price price-times');
        });
    }else if(flag == 0){//正常价格
        $(obj).attr('class', 'price cur chargeSelected price-times');
        $(obj).siblings(".price").each(function () {
            $(this).attr('class', 'price price-times');
        });
    }


}

function showDetail(index) {
    $('#index' + index).attr('class', 'cur');
    $('#index' + index).siblings().each(function () {
        $(this).attr('class', '');
    });
    $('#interface' + index).attr('style', 'display:block;');
    $('#interface' + index).siblings("section").each(function () {
        $(this).attr('style', 'display:none;');
    });
}
$('.sidebar li').click(function () {
    $('.sidebar li').removeClass('cur');
    $(this).addClass('cur');
    return false;
});



function testTools(apiId){
    $.ajax({
        type: 'get',
        url: '/gwtest/check',
        dataType: 'json',
        contentType: "application/json;charset=utf-8",
        async: false,
        data: {apiId: apiId},
        success: function (data) {
            if(data.error == 'NotLogin'){
                window.open(loginUrl + "/gwtest/init/" + apiId);
                return;
            }
            if(data.code == 2){
                //隐私数据进行弹框提示
                $("#confirmEnt").fadeIn();
                $('#confirmBoot').hide();
                $('#confirmWindow').show();
            }else if(data.code == 3){
                //需要企业认证
                $("#confirmEnt").fadeIn();
                $('#confirmBoot').show();
                $('#confirmWindow').hide();
            }else if(data.code == 5){
                //账号正在审核中
                $("#confirmEnt").fadeIn();
                $('#confirmBoot').show();
                $('#confirmWindow').hide();
                return;
            }else{
                window.open(data.url);
            }
            return;
        },
        error: function (textStatus) {
            alert("系统错误，请刷新后重试！");
        }
    });
}

function checkNum(obj){
    tempVal=obj.value.replace(/[^\d]/g,'');
    if(tempVal == "" || tempVal == 0) tempVal = 1;
    obj.value = tempVal;
}



//数据分享引用的js
with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?cdnversion='+~(-new Date()/36e5)];

