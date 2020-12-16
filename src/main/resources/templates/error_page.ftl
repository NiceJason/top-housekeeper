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
                <p id="errorCode" style="padding-left: 60px" class="text-left">${code!"500"}</p>
            </div>

            <div class="col-lg-6">
                <p id="errorMsg" class="text-right">${msg!"出错啦"}</p>
            </div>
        </div>
    </@comm.body>
</@comm.page>