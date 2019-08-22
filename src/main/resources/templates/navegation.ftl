<#--导航栏模板-->
<script>
    var userName = '${userName!}';
</script>
<script src="/js/system/navegation.js"></script>
<#include "/welcome/access.ftl" encoding="UTF-8">
<div>
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/welcome">顶上管家</a>
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
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"
                        ><span class="glyphicon glyphicon-user"></span>${userName!}</a>
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