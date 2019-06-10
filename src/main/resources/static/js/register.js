/**
 * 用户注册对象
 * @constructor
 */
var Register = function () {

}

Register.prototype.submit = function () {
         var paramMap = new Map();
         paramMap.put("email",document.getElementById("email"));
         paramMap.put("password",document.getElementById("password"));

         Query.create("/access/register",paramMap,function (query) {
             console.debug("发送成功");
         })
}