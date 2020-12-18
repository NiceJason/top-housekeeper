//前端系统初始化要做的事
(function () {
   window.systemStart = {

        /**
         * 初始化
         * @private
         */
        _init : function(){
            this.getAlive();
        },

        /**
         * 询问登录状态
         */
        getAlive : function () {
            var query = Query.createGetType("/access/getAliveUser",null,function (data)
            {
                var userInfo = UserInfo.getUserInfo(data);
                window.systemInfo.setUserInfo(userInfo);
            });
            query.setAsync(false);
            query.sendMessage();
        }
    }

    window.systemStart._init();
})();