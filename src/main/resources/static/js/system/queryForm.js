/**
 * 访问后台的类，构造form表单来进行post请求
 * @param url
 * @constructor
 */
var QueryForm = function (url,paramMap) {
    //form表单的JQ对象
    this.form = $("<form></form>");
    this.form.attr("action",url);
    for(var key in paramMap){
        this.form.attr(key,paramMap.get(key));
    }
    this.sendMessage();
}

QueryForm.create = function (url, paramMap) {
    return new QueryForm(url,paramMap);
}

QueryForm.prototype.sendMessage = function () {

}