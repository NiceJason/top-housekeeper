<@comm.page>
    <@comm.head>
        <link href="/css/welcome.css" rel="stylesheet" type="text/css"/>

    </@comm.head>
    <@comm.body>
        <div class="welcome-navegation">
            <#include "/navegation.ftl" encoding="UTF-8">
        </div>

        <div class="welcome-body">
            <div id="myCarousel" class="carousel slide">
                <!-- 轮播（Carousel）指标 -->
                <#--            <ol class="carousel-indicators">-->
                <#--                <li data-target="#myCarousel" data-slide-to="0" class="active"></li>-->
                <#--                <li data-target="#myCarousel" data-slide-to="1"></li>-->
                <#--                <li data-target="#myCarousel" data-slide-to="2"></li>-->
                <#--            </ol>-->
                <!-- 轮播（Carousel）项目 -->
                <div class="myCarouselCss carousel-inner ">
                    <div class="item active">
                        <img src="/image/bootstrap.png" alt="First slide">
                    </div>
                    <div class="item">
                        <img src="/image/jquery.jpg" alt="Second slide">
                    </div>
                    <div class="item">
                        <img src="/image/springboot.jpg" alt="Third slide">
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

            <div style="text-align: center">
                <h3>欢迎来到我的个人网站</h3>
                <p>搭建网站时的知识分享于：<a
                            href="https://www.cnblogs.com/top-housekeeper/">https://www.cnblogs.com/top-housekeeper/</a>
                </p>
                <p>本项目Github地址：<a href="https://github.com/NiceJason/top-housekeeper">https://github.com/NiceJason/top-housekeeper</a>
                </p>
                <p>本网站持续更新中...</p>
            </div>
        </div>
        <div class="welcome-footer">
            <#include "/footer.ftl" encoding="UTF-8">
        </div>
    </@comm.body>
</@comm.page>