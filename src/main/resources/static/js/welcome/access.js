/**
 * 用户访问对象
 * @constructor
 */
var Access = function () {

}

Access.register = "register";
Access.login = "login";

var access;
/**
 * 单列模式
 * @returns {Access|Data.access|access|Data.access}
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
    this.type = Access.register;
}

/**
 * 标志当前为登录行为
 */
Access.prototype.setLoginType = function () {
    this.type = Access.login;
}

/**
 * 获取当前行为
 */
Access.prototype.getType = function () {
    return this.type;
}

/**
 * 登录或注册的提交
 */
Access.prototype.submit = function (result) {

    var paramMap = new Map();
    var url;

    paramMap.set("identifyingId", result.identifyingId);
    paramMap.set("moveEnd_X", result.moveEnd_X);
    paramMap.set("identifyingType", result.identifyingType);

    if (access.getType() == Access.register) {
        paramMap.set("email", $('#register-emaill').val());
        paramMap.set("password", $('#register-password').val());
        url = "/access/registered";
    } else {
        paramMap.set("email", $('#login-emaill').val());
        paramMap.set("password", $('#login-password').val());
        url = "/access/login";
    }

    var query = Query.create(url, paramMap, function (data) {

        if (!query.checkEception()) {
            prompt(data.action_result, prompt.success);
            $('#accessModal').modal('hide');
        }

    })
}

Access.prototype.initIdentifying = function (identifyingContent) {
       var self = this;

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
        self.identifying.showIdentifying();
    },Query.NOMAL_TYPE)

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