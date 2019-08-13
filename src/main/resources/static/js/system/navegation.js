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
    if(!navegation){
        navegation = new Navegation();
        return navegation;
    }else {
        return navegation;
    }
}

/**
 * 获取目录信息
 */
Navegation.prototype.getURLs = function () {

    var query = Query.create("/resources/getNavegationURLs",null,function (data) {
        if(!query.checkEception()){
            data.
        }
    });
}

/**
 * 初始化目录
 */
Navegation.prototype.init = function () {
    if(navegation.isPrepared == false){
        prompt("获取目录信息失败",prompt.danger);
    }
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
    if(userName.length != 0){
        $("#noLogin").hide();
        $("#alreadyLogin").show();
    }else {
        $("#alreadyLogin").hide();
        $("#noLogin").show();
    }
    navegation = Navegation.getNavegation();
    if(navegation.isPrepared == false){
        navegation.getURLs();
    }
    navegation.init();
})