/**
 * 验证码的父类，所有验证码都要继承这个类
 * @param id 验证码的唯一标识
 * @param type 验证码的类型
 * @constructor
 */
var Identifying = function (id,type){
    this.id = id;
    this.type = type;
}

/**
 * 回调函数
 * 验证成功后进行调用
 * this需要指具体验证类
 * @param result 对象，有对应验证类的传递的参数，具体要看验证类
 */
Identifying.prototype.success = function (result) {
    if(this.success instanceof Function){
        this.success(result);
    }
}

/**
 * 验证失败发生错误调用的函数
 * @param result
 */
Identifying.prototype.error = function (result) {
    if(this.error instanceof Function){
        this.error(result);
    }else{
        //统一处理错误
    }
}