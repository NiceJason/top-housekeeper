<@comm.page>
    <@comm.head>
        <title>错误页面</title>
        <style>
             p{
                font-size:30px;
                color:RED;
            }
        </style>
    </@comm.head>
    <@comm.body>
        <#include "/navegation.ftl" encoding="UTF-8">
        <div class="center-block" style="position: relative;margin-top:120px;width:720px;height:400px;background:url('/image/error.png') no-repeat">
            <div class="col-lg-6">
                <p class="text-left">${code!"404"}</p>
            </div>

            <div class="col-lg-6">
                <p class="text-right">${msg!"页面全靠想"}</p>
            </div>
        </div>

    </@comm.body>
</@comm.page>