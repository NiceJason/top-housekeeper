/**
 * 访问后台的类，构造form表单来进行post请求
 * @param url
 * @param paramMap  参数map
 * @constructor
 */
var QueryForm = function (url,paramMap) {
    //form表单的JQ对象
    this.form = $("<form></form>");
    this.form.attr("action",url);
    this.form.attr("method","post");
    //遍历Map
    for(var [key,value] of paramMap){
       var inputDom = $("<input/>") ;
       inputDom.attr("name",key);
       inputDom.attr("value",value);
       this.form.append(inputDom);
    }
    //必须要放入body里面，不然请求发不出去
    var bodyDom = $("body");
    bodyDom.append(this.form);
    this.sendMessage();

    //发送完后销毁
    this.destroy();
}

QueryForm.create = function (url, paramMap) {
    return new QueryForm(url,paramMap);
}

QueryForm.prototype.sendMessage = function () {
     this.form.submit();
}

QueryForm.prototype.destroy =  function () {
    this.form.remove();
}