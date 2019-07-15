/*
 * 以下为程序错误码
 */
//通用的请求失败，包括未知原因
var EXPECTATION_FAILED=417;
var EXPECTATION_QUERY=404;

/**
 * 访问后台的对象，为ajax封装
 * @constructor
 */
var Query = function (url, param, callback) {
    this.url=url;
    //注意，返回的是字符串
    this.param = JSON.parse(this._convertParam(param));
    this.callback=callback;

    this._sendMessage(this);
}

/**
 * ajax请求的访问
 * @param url 要访问的地址
 * @param paramMap 传给后台的Map参数，key为字符串类型
 * @param callback 回调函数
 */
Query.create = function (url, paramMap, callback) {
     return new Query(url, paramMap, callback);
}

/**
 * 将ParamMap转为json格式，目前只支持Map对象，以后会扩展
 * @param paramMap
 * @private
 */
Query.prototype._convertParam = function(param){

     if(param instanceof Map){
        return strMap2Json(param);
     }
}

/**
 * 对ajax回调函数的封装
 * @param callBack
 * @private
 */
Query.prototype._callback = function(queryResult){

      //Query对象
      var self = queryResult.queryObj;
      var data = $.parseJSON(queryResult.responseText);
      //记录请求是否有错误
      self.queryException = false;
      var handleError;

      if(queryResult.status == EXPECTATION_FAILED||queryResult.status == EXPECTATION_QUERY){
          var error =queryResult.responseText;
          self.queryException = true;
      }

      if(self.callback instanceof  Function){
          handleError=self.callback(data);
      }

      //如果出现了异常并且没有被处理，那么将进行默认错误处理
      if(self.queryException&&!handleError){
          window.location.href = "/system/error/"+error.code;
      }

      //如果需要跳转，则进行跳转
      if(data.redirect_url){
          window.location.href = data.redirect_url;
      }
}

/**
 * 正式发送ajax
 * @private
 */
Query.prototype._sendMessage = function (queryObj) {
    $.ajax(
        {
            type:"post",
            url:this.url,
            // contentType: 'application/json',
            data:this.param,
            complete:this._callback,
        }
    ).queryObj=queryObj;
}

/**
 * 检测是否有错误,返回ture有错误，或者false
 */
Query.prototype.checkEception = function () {
      return this.queryException;
}