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
})

/**
 * 导航类，记录着导航信息
 * @constructor
 */
var Navegation = function () {

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
