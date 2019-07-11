<!--登录注册的集合模板，用标签页将两个页面整合到一个模态框中-->
<script src="/js/welcome/access.js"></script>
<script src="/js/welcome/img_ver.js"></script>
<!-- modal -->
<div class="modal fade" id="accessModal" tabindex="-1" role="dialog" draggable="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span></button>
                <h4 class="modal-title" id="myModalLabel">欢迎</h4>
            </div>
            <div class="modal-body">

                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" id="loginNav"><a href="#loginDiv" aria-controls="home" role="tab"
                                                             data-toggle="tab">登录</a></li>
                    <li role="presentation" id="registerNav"><a href="#registerDiv" aria-controls="profile" role="tab"
                                                                data-toggle="tab">注册</a></li>
                </ul>
                <!--显示的内容-->
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane" id="loginDiv"><#include "login.ftl" encoding="UTF-8"></div>
                    <div role="tabpanel" class="tab-pane"
                         id="registerDiv"><#include "register.ftl" encoding="UTF-8"></div>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="Access.getAccess().submit()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
<#--                <button type="button" class="btn btn-default" data-container="body"-->
<#--                        data-toggle="popover" data-placement="top"-->
<#--                        &lt;#&ndash;                        data-html="true"&ndash;&gt;-->
<#--                        &lt;#&ndash;                        data-title="验证码" id="identifyingBtn"&ndash;&gt;-->
<#--                        &lt;#&ndash;                        data-content="Vivamus sagittis lacus vel augue laoreet rutrum faucibus."&ndash;&gt;-->
<#--                >-->
<#--                    Popover on 顶部-->
<#--                </button>-->
<#--                <button id="custom" class="btn btn-default" data-toggle="popover" title="显示的标题" data-placement="bottom" data-content='显示的内容'>点我</button>-->
<#--                <button id="identifyingBtn" class="btn btn-default" data-toggle="popover" data-html="true" title="显示的标题" data-placement="bottom"-->
<#--                        data-content='<h3 id="identifyingContent" style="color:#F00;">显示的内容</h3><button type="submit">pop的按钮</button>'>点我</button>-->
                <button id="identifyingBtn" class="btn btn-default" onclick="test(123)" >点我</button>
                <button id="identifyingContent" data-toggle="popover"></button>
            </div>
        </div>
    </div>
</div>

<script>
    // 重点，以下JS是为了防导航栏在模态框出现的时候不会偏移
    // （因为那时候浏览器滚动条会被取消，导致多出的空间会使固定在顶端的导航栏鬼畜）
    $('#accessModal').on('show.bs.modal',
        function (e) {
            $('body').css("cssText", ";overflow-y:auto !important;")
        }
    );


    //验证码弹窗
    //要加下面这句进行弹窗初始化，不然弹窗出不来
    // $(function () {
    //     $("[data-toggle='popover']").popover({html : true }
    //     );
    // });
    $('#identifyingContent').on('show.bs.popover',
        function (e) {

        }
    );
    $('#identifyingContent').on('shown.bs.popover',
        function (e) {
            // e.currentTarget.dataset.content = "新生成的";
        }
    );

    function test(dom){
        $("#identifyingContent").popover({
            html:true,
            placement:'top',
            /*delay:{"hide":2000},*/
            title:'<span style="font-weight:700">详细url地址</span>',
            content:'<p style="width:auto;height:20px;">'+dom+'</p>'
        })
        $("#identifyingContent").popover('show');
    }
</script>