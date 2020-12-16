/**
 * 用户信息，单例模式
 * 因为页面加载的时候，js会重新加载，数据清空
 * @param username
 * @constructor
 */
var UserInfo = function () {

}

//单例
var userInfo;
/**
 * 单例模式，因为一次会话中只创建一次这个类，保持信息一致
 */
UserInfo.getUserInfo = function(userName){
    if(userInfo){
        return userInfo;
    }else {
        userInfo = new UserInfo();
        userInfo.userName = userName;
        return userInfo;
    }
}

UserInfo.prototype.setUserName = function(userName){
    this.userName = userName;
}

UserInfo.prototype.getUserName = function () {
    return this.userName;
}