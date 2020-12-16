/**
 * 导航类，记录着导航信息
 * @constructor
 */
var Navegation = function () {
    //是否初始化了，即是否已经获取到导航的信息
    this.isPrepared = false;
}

var navegation;

Navegation.getNavegation = function () {
    if (!navegation) {
        navegation = new Navegation();
        return navegation;
    } else {
        return navegation;
    }
}

/**
 * 获取目录信息，生成的目录信息如
 * <ul id="comm-nav-bar" class="nav nav-pills  navbar-left" style="margin-top:5px;">
 <li class="active "><a data-toggle="tab" href="#home">首页</a></li>
 <li class="dropdown">
 <a href="#" class="dropdown-toggle" data-toggle="dropdown">
 前后端感悟
 <b class="caret"></b>
 </a>
 <ul class="dropdown-menu">
 <li><a href="#menu1">JS</a></li>
 <li class="divider"></li>
 <li><a href="#">Java</a></li>
 </ul>
 </li>
 */
Navegation.prototype.getURLs = function () {

    var query = Query.createGetType("/resources/getNavegationURLs", null, function (data) {

            //当前插入位置
            var index = 3;
            //获取导航栏
            var navBar = $("#comm-nav-bar");
            //获取导航栏下的li数组
            var navBar_li = navBar.children("li");

            //生成目录
            for (var i = 0, lenI = data.length; i < lenI; i++) {
                var catalog_li = $("<li class='dropdown'>");
                var catalog = data[i];

                catalog_li.append(
                    "<a href='#' class='dropdown-toggle' data-toggle='dropdown'>" +
                    catalog.catalogName +
                    "<b class='caret'></b>" +
                    "</a>");

                var items = catalog.items;
                var item_ul = $("<ul class='dropdown-menu'>");

                //生成项目
                for(var j=0,lenJ = items.length;j<lenJ;j++){
                   var item = items[j];
                   //项目HTML
                   var item_li = $("<li><a url ='"+item.itemHref
                       +"' onclick='jumpURL(this);'>"+
                       item.itemName+
                       "</a></li>");

                   //如果连接是#，添加不可访问样式
                    if(item.itemHref == "#"){
                        item_li.addClass("disabled");
                        item_li.attr("data-toggle","tooltip");
                        item_li.attr("data-placement","right");
                        item_li.attr("title","该模块敬请期待");
                    }

                   //插入分隔符
                   if(item.itemSplit == 1){
                       //分隔符HTML
                       var item_split = $("<li class='divider'></li>");
                       item_ul.append(item_split);
                   }
                    item_ul.append(item_li);
                }
                catalog_li.append(item_ul);
                // catalog_li.insertAfter(navBar_li[index]);
                catalog_li.appendTo(navBar);
            }
    },function (data) {
        window.tool.prompt("获取目录信息失败", prompt.danger);
        return true;
    });
    query.setAsync(false);
    query.sendMessage();
}

/**
 * 初始化目录
 * data为传入的目录数据
 */
Navegation.prototype.init = function (data) {
    if (navegation.isPrepared == false) {

    }
}

/**
 * URL跳转
 * @param e 触发跳转的元素
 * @returns {boolean}
 */
var jumpURL = function (e) {
    //获取跳转属性
    var src = e.getAttribute("url");
    if(src == "#"){
       window.tool.prompt("该模块敬请期待",windw.tool.prompt_warning,1);
       return;
    }
    var paramMap = new Map();
    paramMap.set("src",src);
    var query = QueryForm.create("/Navegation/jumpURL",paramMap);
    //防止元素默认的跳转
    return false;
}

/**
 * 打开注册窗口
 */
var openRegisterModal = function () {
    $("#registerDiv").removeClass("fade").addClass("active");
    $("#loginDiv").removeClass("active").addClass("fade");
    $("#registerNav").addClass("active");
    $("#loginNav").removeClass("active");
    Access.getAccess().setRegisterType();

    $('#accessModal').modal('show');
}

/**
 * 打开登录窗口
 */
var openLoginModal = function () {

    $("#registerDiv").removeClass("active").addClass("fade");
    $("#loginDiv").removeClass("fade").addClass("active");
    $("#registerNav").removeClass("active");
    $("#loginNav").addClass("active");
    Access.getAccess().setLoginType();

    $('#accessModal').modal('show');
}



/*
 * 判断导航栏是否已经登录，显示登录信息
 */
$(document).ready(function () {

    //判断是否已经登录
    var userInfo = window.systemInfo.getUserInfo();
    var userName = null;
    if(userInfo){
        userName = userInfo.getUserName();
    }
    if (userName && userName != "null" && userName.length > 0) {
        $("#nav-userName").html(userName);
        $("#noLogin").hide();
        $("#alreadyLogin").show();
    } else {
        $("#alreadyLogin").hide();
        $("#noLogin").show();
    }

    navegation = Navegation.getNavegation();
    if (navegation.isPrepared == false) {
        navegation.getURLs();
    }
    navegation.init();
})