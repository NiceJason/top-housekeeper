/**
 * 访问后台的对象，为ajax封装
 * @param url 后台资源路径
 * @param param Map参数
 * @param contentType 传输类型
 * @param success   成功回调函数
 * @param error 失败回调函数
 * @param requestType 请求类型（get.post,put,delete）
 * @constructor
 */
var Query = function (url, param, contentType, successFunc, errorFunc, requestType) {
    this.url = url;

    //先确认参数存在
    if (param) {
        //如果是get请求类型，则将参数拼接到url后面
        if (requestType == Query.GET_TYPE) {
            this.param = this._concatParamToURL(param, url);
        } else {
            //其他请求类型，要根据不同的传输格式来确定传输的值的类型
            if (contentType == Query.NOMAL_TYPE) {
                this.param = JSON.parse(this._convertParamToJson(param));
            } else {
                this.param = this._convertParamToJson(param);
            }
        }
    } else {
        this.param = null;
    }


    this.contentType = contentType;
    this.successFunc = successFunc;
    this.errorFunc = errorFunc;
    //请求超时，默认10秒
    this.timeout = 10000;
    //是否异步请求，默认异步
    this.async = true;
    this.requestType = requestType;
}

Query.JSON_TYPE = 'application/json';
Query.NOMAL_TYPE = 'application/x-www-form-urlencoded';

/**
 * ajax请求的访问
 * 默认是post
 * @param url 要访问的地址
 * @param paramMap 传给后台的Map参数，key为字符串类型
 * @param callback 回调函数
 * @param contentType 传输数据的格式  默认传输application/x-www-form-urlencoded格式
 */
Query.create = function (url, paramMap, successFunc, errorFunc) {
    return new Query(url, paramMap, Query.NOMAL_TYPE, successFunc, errorFunc, Query.GET_TYPE);
}

//-----------------------以下为RESTFul方法---------------------------
//ajax请求类型
Query.GET_TYPE = "get";
Query.POST_TYPE = "post";
Query.PUT_TYPE = "put";
Query.DELETE_TYPE = "delete";

//get方法默认是Query.NOMAL_TYPE
Query.createGetType = function (url, paramMap, successFunc, errorFunc) {
    return new Query(url, paramMap, Query.NOMAL_TYPE, successFunc, errorFunc, Query.GET_TYPE);
}
Query.createPostType = function (url, paramMap, successFunc, errorFunc) {
    return new Query(url, paramMap, Query.JSON_TYPE, successFunc, errorFunc, Query.POST_TYPE);
}
Query.createPutType = function (url, paramMap, successFunc, errorFunc) {
    return new Query(url, paramMap, Query.JSON_TYPE, successFunc, errorFunc, Query.PUT_TYPE);
}
Query.createDeleteType = function (url, paramMap, successFunc, errorFunc) {
    return new Query(url, paramMap, Query.JSON_TYPE, successFunc, errorFunc, Query.DELETE_TYPE);
}

/**
 * 将paramMap参数转为json格式
 * @param paramMap
 * @private
 */
Query.prototype._convertParamToJson = function (paramMap) {

    return window.tool.strMap2Json(paramMap);

}

/**
 * 将参数拼接至URL尾部
 * @param paramMap
 * @param url
 * @private
 */
Query.prototype._concatParamToURL = function (paramMap, url) {
    let size = paramMap.size;

    if (size > 0) {
        let count = 0;
        url = url + "?";
        let urlParam = "";

        for (let [k, v] of paramMap) {
            urlParam = urlParam + encodeURIComponent(k) + "=" + encodeURIComponent(v);
            if (count < size-1) {
                urlParam = urlParam + " && ";
                count++;
            }
        }
        url = url + urlParam;
    }
    return url;
}

//ajax需要跳转的界面
Query.REDIRECT_URL = "REDIRECT_URL";

/**
 * ajax成功返回时调用的方法
 * 会根据ajax的ContentType类型，转换Response对象的data给回调的成功函数
 * 如application/json格式类型，data会转成json类型传递
 * @param queryResult 返回的值，通常为后台的Response对象
 * @private
 */
Query.prototype._successFunc = function (queryResult) {
    var data = this.__afterSuccessComplete(queryResult);
    if (this.successFunc) {
        this.successFunc(data);
    }

    //如果有需要跳转的页面，则自动跳转
    if (data && data.REDIRECT_URL != null) {
        window.location = data.REDIRECT_URL;
    }
}

/**
 * 会根据ajax的ContentType类型，转换Response对象的data给回调的失败函数
 * 如application/json格式类型，data会转成json类型传递
 * 如果对获得的参数不满意，可以用this.getMsg或this.getJsonMsg来进行获取（this指Query对象）
 *
 * 这里错误分3种
 * 1.是Web容器出错
 * 2.是Filter过滤器主动报错（如一些校验失败后主动抛出，会有错误提示）
 * 3.是Spring抛出，Spring异常会全局捕捉进行封装
 * @param queryResult 返回的值，通常为后台的Response对象
 * @private
 */
Query.prototype._errorFunc = function (queryResult) {

    //返回的信息
    var data = this.__afterErrorComplete(queryResult);
    //如果data里面没东西
    if (!data) {
        data = queryResult.statusText;
    }

    //是否调用者自身已解决了错误
    var handleError = false;

    //调用回调函数，如果返回结果为true，则不会默认错误处理
    if (this.errorFunc instanceof Function) {
        handleError = this.errorFunc(data);
    }

    //错误编号
    var code;
    //错误信息
    var msg;

    //没有取消对错误的后续处理，那么进行跳转
    if (!handleError) {

        //如果data成功转为Json对象
        if (data) {
            //Filter过滤器主动报错（如一些校验失败后主动抛出，会有错误提示）
            if (data.status) {
                code = data.status;
            }
            if (data.message) {
                msg = data.message;
            }
        }

        //最终跳转至错误页面
        var path = "/system/error";
        if (code && msg) {
            path = path + "/" + error.code + "/" + error.msg;
        }
        window.location.href = path;
    }
}

Query.SUCCESS_TYPE = "SUCCESS_TYPE";
Query.ERROR_TYPE = "ERROR_TYPE";
/**
 * 当一个请求完成时，无论成功或失败，都要调用此函数做一些处理
 * @param queryResult 服务端返回的数据
 * @returns {*}
 * @private
 */
Query.prototype._afterComplete = function (queryResult) {
    this._cancleLoadDom();
}

/**
 * 成功的返回处理，会将data部分转为对象
 * 默认application/json会进行单引号转双引号
 * @param queryResult 服务端返回的数据
 * @param queryResult
 * @returns {*}
 * @private
 */
Query.prototype.__afterSuccessComplete = function (queryResult) {
    this._afterComplete();
    this.response = queryResult;

    var data = queryResult.data;
    //data必须要有内容，且不是对象才有转换的意义
    if (data && !(data instanceof Object)) {
            data = this.getJsonMsg();
    }
    return data;
}

/**
 * 失败的返回处理
 * 最终会根据ajax的contentType来进行data相应类型转换
 * 默认application/json会进行单引号转双引号
 * @param queryResult 服务端返回的数据
 * @private
 */
Query.prototype.__afterErrorComplete = function (queryResult) {
    this._afterComplete();
    this.response = queryResult;
    var data = queryResult.responseJSON;
    if (!data) {
        data = queryResult.responseText;
    }

    return data;
}

/**
 * 取消请求时的等待框
 * @private
 */
Query.prototype._cancleLoadDom = function () {
    //取消加载框
    if (this.loadDom) {
        $(this.loadDom).remove("#loadingDiv");
    }
}

/**
 * 正式发送ajax
 * @private
 */
Query.prototype.sendMessage = function () {
    var self = this;
    var xhr = $.ajax(
        {
            url: this.url,
            type: this.requestType,
            contentType: this.contentType,
            data: this.param,
            // ajax发送前调用的方法，初始化等待动画
            // @param XHR  XMLHttpRequest对象
            beforeSend: function (XHR) {
                //试图从Cookie中获得token放入http头部
                var token = window.tool.getCookieValue(window.commonStaticValue.TOKEN);
                if(token){
                    XHR.setRequestHeader(window.commonStaticValue.TOKEN,token);
                }

                //绑定本次请求的queryObj
                XHR.queryObj = self;
                if (self.beforeSendFunc instanceof Function) {
                    self.beforeSendFunc(XHR);
                }

                if (self.loadDom instanceof HTMLElement) {
                    self.loadDom.innerText = "";
                    $(self.loadDom).append("<div id='loadingDiv' class='loading'><img src='/image/loading.gif'/></div>");
                } else if (self.loadDom instanceof jQuery) {
                    self.loadDom.empty();
                    self.loadDom.append("<div id='loadingDiv' class='loading'><img src='/image/loading.gif'/></div>");
                }
            },
            //将QueryObj设置为上下文
            context: self,
            success: this._successFunc,
            error: this._errorFunc,
            complete:function(){
              console.log("ajax完成");
            },
            timeout: this.timeout,
            async: this.async
        }
    );
}

//-----------------------------------下面提供了获取后台返回信息方法（帮忙封装了）
/**
 * 获取返回信息Response的Meta头
 */
Query.prototype.getMeta = function () {
    return this.response.meta;
}

/**
 * 获得返回值里的data部分
 * @returns {*}
 */
Query.prototype.getMsg = function () {
    return this.response.data;
}

/**
 * 获得返回值里的data部分，尝试将其转为Json对象
 */
Query.prototype.getJsonMsg = function () {
    var data = this.response.data;
    if (data) {
            //先将字符串里的&quot;转为双引号
            var data = window.tool.replaceAll(data, "&quot;", "\"");
            try{
                var jsonData = JSON.parse(data);
                return jsonData;
            }catch (e) {
                return data;
            }
    }
}

//------------------------以下为对Query的参数设置---------------------------
/**
 * 在ajax发送前设置参数，可以有加载的动画，并且请求完成后会自动取消
 * @param loadDom 需要显示动画的dom节点
 * @param beforeSendFunc ajax发送前的自定义函数
 */
Query.prototype.setBeforeSend = function (loadDom, beforeSendFunc) {
    this.loadDom = loadDom;
    this.beforeSendFunc = beforeSendFunc;
}

/**
 * 设置超时时间
 * @param timeout
 */
Query.prototype.setTimeOut = function (timeout) {
    this.timeout = timeout;
}

Query.prototype.setAsync = function (async) {
    this.async = async;
}
