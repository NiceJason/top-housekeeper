var EmailAutoComplete = function(){

}

var mailBox = [
    "@qq.com",
    "@sina.com",
    "@163.com",
    "@126.com",
    "@yahoo.com.cn",
    "@gmail.com",
    "@sohu.com"
];

/**
 * 绑定要进行提示的输入框
 * @param inputJQ 需要提示的input节点
 * @param listJQ 提示列表节点
 */
EmailAutoComplete.inputList=function(inputJQ,listJQ){

    inputJQ.bind('input propertychange', function() {
        var key = input.val();
        if(key.indexOf("@") != -1){
            key = key.slice(0,key.indexOf("@"));
        }
        var mailBoxLen = mailBox.length;
        var html = "";
        for(var i=0; i<mailBoxLen; i++){
            html += '<option value="'+ key + mailBox[i] +'"></option>';
        }
        listJQ.html(html);
    });
}

EmailAutoComplete.getEmailBox = function () {
    return mailBox;
}