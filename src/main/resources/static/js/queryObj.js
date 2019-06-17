/*
 * 以下为程序错误码
 */
//通用的请求失败，包括未知原因
var EXPECTATION_FAILED=417;

/**
 * 访问后台的对象，为ajax封装
 * @constructor
 */
var Query = function (url, param, callback) {
    this.url=url;
    this.paramMap = this._convertParam(param);
    this.callback=callback;

    this._sendMessage();
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
        return JSON.stringify(param);
     }
}

/**
 * 对ajax回调函数的封装
 * @param callBack
 * @private
 */
Query.prototype._callback = function(query){

      if(query.status == EXPECTATION_FAILED){

          var error =query.responseJSON;
          this.queryException = true;
          window.location.href = "/system/error/"+error.code+"/"+error.msg;
      }

      if(this.callback instanceof  Function){
          this.callback(query);
      }
}

/**
 * 正式发送ajax
 * @private
 */
Query.prototype._sendMessage = function () {
    $.ajax(
        {
            url:this.url,
            dataType:"json",
            data:this.param,
            complete:this._callback
        }
    );
}