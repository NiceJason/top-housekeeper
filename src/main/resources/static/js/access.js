/**
 * 用户注册对象
 * @constructor
 */
var Access = function () {

}

/**
 * 登录或注册的提交
 */
Access.prototype.submit = function () {

         var paramMap = new Map();

         if($("#registerNav").hasClass("active")){
             paramMap.set("email",$('#register-emaill').val());
             paramMap.set("password",$('#register-password').val());
         }else {
             paramMap.set("email",$('#login-emaill').val());
             paramMap.set("password",$('#login-password').val());
         }

         Query.create("/access/registered",paramMap,function (query) {

             prompt("注册成功",prompt.success);
             $('#registeredModal').modal('hide');

         })
}