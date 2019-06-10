/**
 * 访问后台的对象，为ajax封装
 * @constructor
 */
var Query = function () {

}

/**
 * ajax请求的访问
 * @param url 要访问的地址
 * @param paramMap 参数，key为字符串类型
 * @param callback 回调函数
 */
Query.create = function (url, paramMap, callback) {
      //这里应该有对参数的检测等，需要做
    _sendMessage(url,paramMap,callback);
}

/**
 * 正式发送ajax
 * @private
 */
Query._sendMessage = function (url,paramMap,callback) {
    $.ajax(
        {
            url:url,
            dataType:"json",
            data:paramMap,
            complete:callback
        }
    );
}