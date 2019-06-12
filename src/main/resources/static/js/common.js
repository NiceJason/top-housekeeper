/**
 * 公共方法类
 * 包括：1.提示窗
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
