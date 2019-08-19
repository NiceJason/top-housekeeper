/**
 * 验证码的父类，所有验证码都要继承这个类
 * @param id 验证码的唯一标识
 * @param type 验证码的类型
 * @param contentDiv 包含着验证码的DIV
 * @constructor
 */
var Identifying = function (id,type,contentDiv){
    this.id = id;
    this.type = type;
    this.contentDiv=contentDiv;
}

/**
 * 销毁函数
 */
Identifying.prototype.destroy = function(){
    this.successFunc = null;
    this.errorFunc = null;
    this.clearDom();
    this.contentDiv = null;
}

/**
 * 清除节点内容
 */
Identifying.prototype.clearDom = function(){
    if(this.contentDiv instanceof jQuery){
        this.contentDiv.empty();
    }else if(this.contentDiv instanceof HTMLElement){
        this.contentDiv.innerText = "";
    }
}

/**
 * 回调函数
 * 验证成功后进行调用
 * this需要指具体验证类
 * @param result 对象，有对应验证类的传递的参数，具体要看验证类
 */
Identifying.prototype.success = function (result) {
    if(this.successFunc instanceof Function){
        this.successFunc(result);
    }
}

/**
 * 验证失败发生错误调用的函数
 * @param result
 */
Identifying.prototype.error = function (result) {
    if(this.errorFunc instanceof Function){
        this.errorFunc(result);
    }else{
        //统一处理错误
    }
}

/**
 * 获取验证码id
 */
Identifying.prototype.getId = function () {
    return this.id;
}

/**
 * 获取验证码类型
 * @returns {*}
 */
Identifying.prototype.getType = function () {
    return this.type;
}

/**
 *  显示验证框
 */
Identifying.prototype.showIdentifying = function(callback){
    this.contentDiv.show(null,callback);
}

/**
 * 隐藏验证框
 */
Identifying.prototype.hiddenIdentifying = function(callback){
    this.contentDiv.hide(null,callback);
}

/**
 * 获得验证码显示的dom元素
 */
Identifying.prototype.getContentDiv = function () {
    return this.contentDiv;
}