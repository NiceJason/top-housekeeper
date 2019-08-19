/**
 * 用户访问对象
 * @constructor
 */
var Access = function () {
    this.bindInputListener($('#register-emaill'),$('#register-emaill-error'));
    this.bindInputListener($('#register-password'),$('#register-password-error'));
    this.bindInputListener($('#login-emaill'),$('#login-emaill-error'));
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
    this.type = Access.register;
    //是否已经检测输入，合法才生成验证码
    this.isCheck = false;
}

/**
 * 标志当前为登录行为
 */
Access.prototype.setLoginType = function () {
    this.emailJQ = $('#login-emaill');
    this.passwordJQ = $('#login-password');
    this.type = Access.login;
    //是否已经检测输入，合法才生成验证码
    this.isCheck = false;
}

/**
 * 获取当前行为
 */
Access.prototype.getType = function () {
    return this.type;
}

/**
 * 登录或注册的提交
 * 由于是给验证码的回调函数，所以this是指验证码
 */
Access.prototype.submit = function (result) {
    var self = this;
    var paramMap = new Map();
    var url;

    //检测输入的正确性
    if(!access.checkEmailInput(access.emailJQ.val(),access.emailJQ))return;

    paramMap.set("identifyingId", result.identifyingId);
    paramMap.set("moveEnd_X", result.moveEnd_X);
    paramMap.set("identifyingType", result.identifyingType);

    if (access.getType() == Access.register) {
        url = "/access/registered";
        if(!access.checkPasswordInput(access.passwordJQ.val(),access.passwordJQ))return;
    } else {
        url = "/access/login";
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
            access.initIdentifying(self.getContentDiv());
            //不进行错误页面跳转
            return true;
        }

    });
    query.sendMessage();
}

Access.prototype.initIdentifying = function (identifyingContent) {
       var self = this;

       if(!this.isCheck){
          this.checkEmailInput(this.emailJQ,this.emailJQ.val());
          this.checkPasswordInput(this.passwordJQ,this.passwordJQ.val());
       }

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
        self.identifying.setSuccess(self.submit);
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
 * @param password
 * @param passwordJQ 密码输入input框
 * @param promptJQ 文字提示的节点
 */
Access.prototype.checkPasswordInput = function (password,passwordJQ,promptJQ) {

    //密码格式是否无误
    var isPasswordOk = false;
    //错误信息
    var errorMsg;


    if(isLetterDigit(password)&& password.length <=10 && password.length>0){
        if(password.length<6||password.length>12){
            errorMsg = "密码长度为6~12位";
        }else {
            isPasswordOk = true;
        }
    }else{
        errorMsg = "密码只能为数字或字母";
    }

    if(!isPasswordOk){
        prompt(errorMsg,prompt.warning);
        if(passwordJQ instanceof jQuery){
            passwordJQ.focus();
        }
    }

    return isPasswordOk;
}

/**
 *
 * @param email
 * @param emailJQ 邮箱输入input框
 * @param promptJQ 文字提示的节点
 */
Access.prototype.checkEmailInput = function (email,emailJQ,promptJQ) {
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
        prompt(errorMsg,prompt.warning);
        if(emailJQ instanceof jQuery){
            emailJQ.focus();
        }
    }

    return isEmailOk;
}

/**
 * 绑定输入框监听失去焦点事件，为的就是输入完后立刻检测输入，进行文字提示
 * @param inputJQ 需要监听的input框
 * @param checkFunc 需要检测的函数
 * @param promptJQ 文字提示的节点
 */
Access.prototype.bindInputListener = function (inputJQ,checkFunc,promptJQ) {
    inputJQ.blur(function (e) {

    });
}