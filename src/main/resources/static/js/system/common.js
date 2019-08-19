/**
 * 公共方法类
 * 包括：1.提示窗 prompt
 *      2.Map转Json Map2Json (还有其他格式和Json的相互转换)
 *      3.类的继承 extendClass
 *      4.判断输入是否只是数字或字母
 * @constructor
 */
var Common = function(){
    
}

/**
 * 弹出式提示框，默认1.2秒自动消失
 * @param message 提示信息
 * @param style 提示样式，有alert-success、alert-danger、alert-warning、alert-info
 * @param time 消失时间
 */
var prompt = function (message, style, time)
{
    style = (style === undefined) ? 'alert-success' : style;
    time = (time === undefined) ? 1200 : time;
    $('<div>')
        .appendTo('body')
        .addClass('alert ' + style)
        .html(message)
        .show()
        .delay(time)
        .fadeOut();
};

prompt.success="alert-success";
prompt.danger="alert-danger";
prompt.warning="alert-warning";
prompt.info="alert-info";

/**
 * Map对象转数组
 * @param map
 * @constructor
 */
var Map2Array =function (map) {
    return JSON.stringify([...map]);
}

/**
 * Map对象转对象，key为字符串
 * 如果有非字符串的键名，那么这个键名会被转成字符串，再作为对象的键名。
 * @param map
 * @constructor
 */
var strMap2Obj =function (map) {
    let obj = Object.create(null);
    for(let [k,v] of map){
        obj[k]=String(v);
    }
    return obj;
}

/**
 * Map对象转对象，key为字符串
 * 如果有非字符串的键名，那么这个键名会被转成字符串，再作为对象的键名。
 * @param map
 * @constructor
 */
var strMap2Json =function (map) {
    return JSON.stringify(strMap2Obj(map));
}

/**
 * 继承方法
 * @param parentClass 父类
 * @param childClass 子类
 */
var extendClass = function(parentClass,childClass){
    var Super = function(){};
    Super.prototype = parentClass.prototype;
    childClass.prototype = new Super();
    childClass.prototype.constructor = childClass;
}

/**
 * 判断字符串是否只是数字和字母
 * @param str
 */
var isLetterDigit = function(str){
    var reg = /^[0-9a-zA-Z]+$/;
    return reg.test(str);
}

/**
 * 判断是否是邮箱格式
 * @param str
 */
var isEmail = function (str) {
    var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
    return reg.test(str);
}