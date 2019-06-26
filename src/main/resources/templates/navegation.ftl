<#--导航栏模板-->
<script>
    var userName = '${userName!}';
</script>
<script src="/js/navegation.js"></script>
<#include "/welcome/access.ftl" encoding="UTF-8">
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">菜鸟教程</a>
        </div>
        <div>

            <ul class="nav nav-pills  navbar-left" style="margin-top:5px;">
                <li class="active "><a data-toggle="tab" href="#home">首页</a></li>
                <li><a data-toggle="tab" href="#menu1">菜单 1</a></li>
                <li><a data-toggle="tab" href="#menu2">菜单 2</a></li>
                <li><a data-toggle="tab" href="#menu3">菜单 3</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        Java
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="#">jmeter</a></li>
                        <li><a href="#">EJB</a></li>
                        <li><a href="#">Jasper Report</a></li>
                        <li class="divider"></li>
                        <li><a href="#">分离的链接</a></li>
                        <li class="divider"></li>
                        <li><a href="#">另一个分离的链接</a></li>
                    </ul>
                </li>
            </ul>

            <ul id="noLogin" class="nav navbar-nav navbar-right">
                <li><a href="#" onclick="openRegisterModal();return false;"><span
                                class="glyphicon glyphicon-user"></span> 注册</a></li>
                <li><a href="#" onclick="openLoginModal();return false;"><span
                                class="glyphicon glyphicon-log-in"></span> 登录</a></li>
            </ul>

            <ul id="alreadyLogin" class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#"  class="dropdown-toggle" data-toggle="dropdown"
                    ><span class="glyphicon glyphicon-user"></span>${userName!}</a>
                    <ul class="dropdown-menu">
                        <li><a href="#" onclick="return false;">个人主页</a></li>
                        <li class="divider"></li>
                        <li><a href="/logout">注销</a></li>
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