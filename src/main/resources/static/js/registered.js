/**
 * 用户注册对象
 * @constructor
 */
var Registered = function () {

}

Registered.prototype.submit = function () {
         var paramMap = new Map();
         paramMap.set("email",$('#emaill').val());
         paramMap.set("password",$('#password').val());

         Query.create("/access/registered",paramMap,function (query) {
             console.debug("发送成功");
             prompt("注册成功",prompt.success);
             $('#registeredModal').modal('hide');

         })
}