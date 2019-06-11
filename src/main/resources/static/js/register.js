/**
 * 用户注册对象
 * @constructor
 */
var Register = function () {

}

Register.prototype.submit = function () {
         var paramMap = new Map();
         paramMap.set("email",document.getElementById("email"));
         paramMap.set("password",document.getElementById("password"));

         Query.create("/access/register",paramMap,function (query) {
             console.debug("发送成功");
         })
}