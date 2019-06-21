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
    this.param = this._convertParam(param);
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
        return strMap2Json(param);
     }
}

/**
 * 对ajax回调函数的封装
 * @param callBack
 * @private
 */
Query.prototype._callback = function(query){

      //记录请求是否有错误
      var queryException = false;
      var handleError;

      if(query.status == EXPECTATION_FAILED){
          var error =query.responseJSON;
          queryException = true;
      }

      if(this.callback instanceof  Function){
          handleError=this.callback(query);
      }

      //如果出现了异常并且没有被处理，那么将进行默认错误处理
      if(queryException&&!handleError){
          window.location.href = "/system/error/"+error.code;
      }
}

/**
 * 正式发送ajax
 * @private
 */
Query.prototype._sendMessage = function () {
    $.ajax(
        {
            type:"post",
            url:this.url,
            contentType: 'application/json',
            data:this.param,
            // complete:this._callback,
            success:this.callback
        }
    );
}

/**
 * 获取后端返回的信息
 */
Query.prototype.getReponse = function(){

}

Query.prototype.getReponse2Json = function () {

}