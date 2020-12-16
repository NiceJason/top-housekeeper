<#--导航栏模板-->
<script src="/js/system/navegation.js"></script>
<script>
    //需要外面加层双引号，不然123@qq这种值就会报错，不知道是个字符串
    <#--var userName = "${userName!'null'}";-->
</script>
<#include "/welcome/access.ftl" encoding="UTF-8">
<div>
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/welcome">NiceBin小窝</a>
            </div>
            <div>

                <ul id="comm-nav-bar" class="nav nav-pills  navbar-left" style="margin-top:5px;">
                    <li><a href="/welcome">首页</a></li>
                </ul>

                <ul id="noLogin" class="nav navbar-nav navbar-right">
                    <li><a href="#" onclick="openLoginModal();return false;"><span
                                    class="glyphicon glyphicon-log-in"></span> 登录</a></li>
                    <li><a href="#" onclick="openRegisterModal();return false;"><span
                                    class="glyphicon glyphicon-user"></span> 注册</a></li>
                </ul>

                <ul id="alreadyLogin" class="nav navbar-nav navbar-right">
                    <#--可爱的头像&ndash;&gt;-->
                    <li><span style="margin-top: 17px;" class="glyphicon glyphicon-user"></span></li>
                    <li class="dropdown">
                        <a id="nav-userName" href="#" class="dropdown-toggle" data-toggle="dropdown"
                        ></a>
                        <ul class="dropdown-menu">
                            <li><a href="#" onclick="return false;">个人主页</a></li>
                            <li class="divider"></li>
                            <li><a href="/access/logout">注销</a></li>
                        </ul>
                    </li>
                </ul>

                <form class="navbar-form navbar-right" role="search">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">提交</button>
                </form>
            </div>
    </nav>
</div>