<@comm.page>
    <@comm.head>
    <link href="css/welcome.css" rel="stylesheet" type="text/css"/>

    </@comm.head>
    <@comm.body>
        <#include "/navegation.ftl" encoding="UTF-8">
<#--        <#include "/registered.ftl" encoding="UTF-8">-->
        <div id="myCarousel" class="carousel slide">
            <!-- 轮播（Carousel）指标 -->
<#--            <ol class="carousel-indicators">-->
<#--                <li data-target="#myCarousel" data-slide-to="0" class="active"></li>-->
<#--                <li data-target="#myCarousel" data-slide-to="1"></li>-->
<#--                <li data-target="#myCarousel" data-slide-to="2"></li>-->
<#--            </ol>-->
            <!-- 轮播（Carousel）项目 -->
            <div class="carousel-inner myCarouselCss">
                <div class="item active">
                    <img src="image/bootstrap.png" alt="First slide">
                </div>
                <div class="item">
                    <img src="image/jquery.png" alt="Second slide">
                </div>
                <div class="item">
                    <img src="image/springboot.png" alt="Third slide">
                </div>
            </div>
            <!-- 轮播（Carousel）导航 -->
            <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>

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

        <!-- Button trigger modal -->
        <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#registeredModal">
            Launch demo modal
        </button>


        <!-- 注册 modal -->
        <div class="modal fade" id="registeredModal" tabindex="-1" role="dialog" aria-labelledby="registeredModalLabel" draggable="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span></button>
                    <h4 class="modal-title" id="myModalLabel">注册</h4>
                </div>
                <div class="modal-body">
                    ...
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary">Save changes</button>
                </div>
            </div>
        </div>
        </div>

        <script type="text/javascript">
            $(function(){
                $(".modal-dialog").draggable();
                console.debug("函数被执行了");
            })
        </script>

        <#include "/footer.ftl" encoding="UTF-8">
    </@comm.body>
</@comm.page>