/**
 * 系统信息类，加载记录一些信息
 * 加载时会自动绑定到window，使用时直接window.systemInfo即可
 * @param wnd
 * @constructor
 */
(function () {

    var SystemInfo = function (wnd) {
        this.wnd = wnd ;
        this._init();
    }

    SystemInfo.prototype._init = function(){

    }

    SystemInfo.prototype.setUserInfo = function (userInfo) {
        this.userInfo = userInfo;
    }

    SystemInfo.prototype.getUserInfo = function () {
        return this.userInfo;
    }

    window.systemInfo = new SystemInfo(window);
})();



