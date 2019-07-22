/**
 * 用户访问对象
 * @constructor
 */
var Access = function () {
    this.imgIdentifying = new ImgIdentifying()
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
Access.prototype.submit = function (identifyingId, moveEnd_X,identifyingType) {

    var paramMap = new Map();
    var url;

    paramMap.set("identifyingId", identifyingId);
    paramMap.set("moveEnd_X", moveEnd_X);
    paramMap.set("identifyingType", identifyingType);

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