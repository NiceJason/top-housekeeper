/**
 * 打开登录注册窗口，确定标签页显示哪一页
 * @param type
 */
var openAccessModal = function(type){

    if(type == openAccessModal.register){
        $("#registerDiv").removeClass("fade").addClass("active");
        $("#loginDiv").removeClass("active").addClass("fade");
        $("#registerNav").addClass("active");
        $("#loginNav").removeClass("active");
    }else {
        $("#registerDiv").removeClass("active").addClass("fade");
        $("#loginDiv").removeClass("fade").addClass("active");
        $("#registerNav").removeClass("active");
        $("#loginNav").addClass("active");
    }
    $('#accessModal').modal('show');
}
openAccessModal.register = "register";
openAccessModal.login = "login";
