<#--导航栏模板-->
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">菜鸟教程</a>
        </div>
        <div>

            <ul class="nav nav-pills center-block navbar-left">
                <li class="active "><a data-toggle="tab" href="#home">首页</a></li>
                <li ><a data-toggle="tab" href="#menu1">菜单 1</a></li>
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

            <ul class="nav navbar-nav navbar-right">
                <li><a href="#"><span class="glyphicon glyphicon-user"></span> 注册</a></li>
                <li><a href="#"><span class="glyphicon glyphicon-log-in"></span> 登录</a></li>
            </ul>

            <form class="navbar-form navbar-right" role="search">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Search">
                </div>
                <button type="submit" class="btn btn-default">提交</button>
            </form>

    </div>
</nav>

<div class="tab-content">
    <div id="home" class="tab-pane fade in active">
        <h3>首页</h3>
        <p>菜鸟教程 —— 学的不仅是技术，更是梦想！！！</p>
    </div>
    <div id="menu1" class="tab-pane fade">
        <h3>菜单 1</h3>
        <p>这是菜单 1 显示的内容。这是菜单 1 显示的内容。这是菜单 1 显示的内容。</p>
    </div>
    <div id="menu2" class="tab-pane fade">
        <h3>菜单 2</h3>
        <p>这是菜单 2 显示的内容。这是菜单 2 显示的内容。这是菜单 2 显示的内容。</p>
    </div>
    <div id="menu3" class="tab-pane fade">
        <h3>菜单 3</h3>
        <p>这是菜单 3 显示的内容。这是菜单 3 显示的内容。这是菜单 3 显示的内容。</p>
    </div>
</div>