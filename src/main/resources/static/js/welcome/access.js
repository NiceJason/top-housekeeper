/**
 * 用户访问对象
 * @constructor
 */
var Access = function () {
    this._init();
}

Access.register = "register";
Access.login = "login";

var access;
/**
 * 单列模式
 * @returns 访问对象
 */
Access.getAccess = function () {
    if (!access) {
        access = new Access();
        return access;
    } else {
        return access;
    }
}

/**
 * 标志当前为注册行为
 */
Access.prototype.setRegisterType = function () {
    this.emailJQ = $('#register-emaill');
    this.passwordJQ = $('#register-password');
    this.emailErrorJQ = $('#register-emaill-error');
    this.passwordErrorJQ = $('#register-password-error');

    this.type = Access.register;
    //所有检测的结果都会存入Map，只有Map里的存在的值都为true，才生成验证码
    this.checkMap = new Map();
    //需要检查的数量，低于这数量，则检查不通过
    this.checkCount = 2;
}

/**
 * 标志当前为登录行为
 */
Access.prototype.setLoginType = function () {
    this.emailJQ = $('#login-emaill');
    this.passwordJQ = $('#login-password');
    this.emailErrorJQ = $('#login-emaill-error');
    this.passwordErrorJQ = $('#login-password-error');
    //自动登录勾选框
    this.autoLoginCheckJQ = $('#auto-login');

    this.type = Access.login;
    //所有检测的结果都会存入Map，只有Map里的存在的值都为true，才生成验证码
    this.checkMap = new Map();
    //需要检查的数量，低于这数量，则检查不通过
    this.checkCount = 2;
}

/**
 * 获取当前行为
 */
Access.prototype.getType = function () {
    return this.type;
}

/**
 * access对象的初始化
 * @private
 */
Access.prototype._init = function(){

    var self = this;

    this._bindInputListener($('#register-emaill'),$('#register-emaill-error'),this._checkEmailInput);
    this._bindInputListener($('#register-password'),$('#register-password-error'),this._checkPasswordInput);
    this._bindInputListener($('#login-emaill'),$('#login-emaill-error'),this._checkEmailInput);
    this._bindInputListener($('#login-password'),$('#login-password-error'),this._checkPasswordInput);

}

/**
 * 登录或注册的提交
 * 由于是给验证码的回调函数，所以this是指验证码
 */
Access.prototype._submit = function (result) {
    var self = this;
    var paramMap = new Map();
    var url;

    //检测输入的正确性
    if(!access._checkCheckMap())return;

    paramMap.set("identifyingId", result.identifyingId);
    paramMap.set("moveEnd_X", result.moveEnd_X);
    paramMap.set("identifyingType", result.identifyingType);

    if (access.getType() == Access.register) {
        url = "/access/registered";
    } else {
        url = "/access/login";
        //检测是否勾选了自动登录
        if(access.autoLoginCheckJQ.prop("checked")){
            paramMap.set("autoLogin", "true");
        }
    }

    paramMap.set("email", access.emailJQ.val());
    paramMap.set("password", access.passwordJQ.val());

    var query = Query.create(url, paramMap, function (data) {

        if (!query.checkEception()) {
            prompt(data.action_result, prompt.success);
            $('#accessModal').modal('hide');
        }else{
            //出现异常进行弹框提示
            prompt(data.msg, prompt.warning);
            //重新生成验证码
            access._initIdentifying(self.getContentDiv());
            //不进行错误页面跳转
            return true;
        }

    });
    query.sendMessage();
}

Access.prototype._initIdentifying = function (identifyingContent) {
       var self = this;

       //以防点击登录或注册，已经有信息在上面，用户不会再输出来触发失去焦点事件，从而导致验证码出不来
       //现在先判定checkMap的长度，如果不相等，检测下输入框的内容
       if(this.checkMap.size != this.checkCount){
          this._checkEmailInput(this.emailJQ.val(),this.emailJQ,this.emailErrorJQ);
          this._checkPasswordInput(this.passwordJQ.val(),this.passwordJQ,this.passwordErrorJQ);
       }
       if(!this._checkCheckMap())return;

       var paramMap = ImgIdentifying.getParamMap();
       var identifyingType = this.getType();
       paramMap.set("identifyingType",identifyingType);
       var url = "/access/identifying";

    //向后台发送验证码请求
    var query = Query.create(url,paramMap,function(data){

        var config = {
            el:identifyingContent,
            img:data.imgSrc,
            X:data.x,
            Y:data.y,
            //误差
            deviation:data.deviation,
            //此验证码的id
            identifyingId:data.identifyingId,
            //当前验证类型，以防验证码用到了别的验证上
            identifyingType:identifyingType
        }

        //创建验证码类
        self.identifying = new ImgIdentifying(config);
        //设置成功回调函数
        self.identifying.setSuccess(self._submit);
        // self.identifying.showIdentifying();
    },Query.NOMAL_TYPE);

    query.setBeforeSend(identifyingContent);
    query.sendMessage();
}
/**
 * 切换页面调用的函数
 * @param target 将要切换的目标标签页
 */
Access.prototype.changePage = function (target) {
      if(target == Access.register){
          this.setRegisterType();
      }else {
          this.setLoginType();
      }

      //如果验证码存在，隐藏验证码
      if(this.identifying){
          this.identifying.hiddenIdentifying();
      }
}

/**
 * 获得与之绑定的验证码类
 * @returns {ImgIdentifying}
 */
Access.prototype.getIdentifying = function () {
    return this.identifying;
}

/**
 * 摧毁绑定的验证码及其dom结构
 */
Access.prototype.destroyIdentifying = function () {
    if(this.identifying)this.identifying.destroy();
    this.identifying = null;
}

/**
 * 检测输入的密码格式
 * 密码为数字和字母，长度6-12位
 * @param password  输入的密码
 * @param passwordJQ 密码输入input框
 * @param promptJQ 文字提示的节点
 */
Access.prototype._checkPasswordInput = function (password,passwordJQ,promptJQ) {

    //密码格式是否无误
    var isPasswordOk = false;
    //错误信息
    var errorMsg;


    if(isLetterDigit(password)&& password.length <=10 && password.length>0){
        if(password.length<6||password.length>12){
            errorMsg = "密码长度为6-12位";
        }else {
            isPasswordOk = true;
        }
    }else{
        errorMsg = "密码只能为数字或字母";
    }

    if(!isPasswordOk){
        promptJQ.html(errorMsg);
        promptJQ.show();
    }else{
        promptJQ.hide();
    }

    this.checkMap.set("isPasswordOk",isPasswordOk);
    return isPasswordOk;
}

/**
 *
 * @param email 输入的email
 * @param emailJQ 邮箱输入input框
 * @param promptJQ 文字提示的节点
 */
Access.prototype._checkEmailInput = function (email,emailJQ,promptJQ) {
    //邮箱格式是否无误
    var isEmailOk = false;
    //错误信息
    var errorMsg;

    if(isEmail(email)){
        if(email.length >40){
            errorMsg = "邮箱长度最长40位";
        }
        else {
            isEmailOk = true;
        }
    }else {
        errorMsg = "邮箱格式不正确";
    }

    if(!isEmailOk){
        promptJQ.html(errorMsg);
        promptJQ.show();
    }else {
        promptJQ.hide();
    }

    this.checkMap.set("isEmailOk",isEmailOk);
    return isEmailOk;
}

/**
 * 对checkMap里的所有check进行检测
 * 当全部为true时才返回true，否则返回false
 */
Access.prototype._checkCheckMap = function(){
    var isAllOk = true;
    var count = 0;

    for(var [key,value] of this.checkMap){
        count++;
        if(value == false)isAllOk = false;
    }

    //检查数量是否匹对
    if(count != this.checkCount)isAllOk = false;
    return isAllOk;
}

/**
 * 绑定输入框监听失去焦点事件，为的就是输入完后立刻检测输入，进行文字提示
 * @param inputJQ 需要监听的input框
 * @param promptJQ 文字提示的节点
 * @param checkFunc 需要检测的函数
 */
Access.prototype._bindInputListener = function (inputJQ,promptJQ,checkFunc) {
    var self = this;

    inputJQ.blur(function (e) {
        if(checkFunc instanceof Function){
            checkFunc.call(self,inputJQ.val(),inputJQ,promptJQ);
        }
    });
}