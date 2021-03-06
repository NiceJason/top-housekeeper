<!--登录注册的集合模板，用标签页将两个页面整合到一个模态框中-->
<link href="/css/imgIdentifying.css" type="text/css" rel="stylesheet">
<link href="/css/emailAutoComplete.css" type="text/css" rel="stylesheet">
<script src="/js/welcome/access.js"></script>
<script src="/js/welcome/imgIdentifying.js"></script>
<script src="/js/welcome/emailAutoComplete.js"></script>
<!-- modal -->
<div class="modal fade" id="accessModal" tabindex="-1" role="dialog">
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
                    <li role="presentation" id="loginNav"><a href="#loginDiv" onclick="Access.getAccess().changePage(Access.login)" aria-controls="home" role="tab"
                                                             data-toggle="tab">登录</a></li>
                    <li role="presentation" id="registerNav"><a href="#registerDiv" onclick="Access.getAccess().changePage(Access.register)" aria-controls="profile" role="tab"
                                                                data-toggle="tab">注册</a></li>
                </ul>
                <!--显示的内容-->
                <div id="contentDiv" class="tab-content">
                    <div role="tabpanel" class="tab-pane" id="loginDiv"><#include "login.ftl" encoding="UTF-8"></div>
                    <div role="tabpanel" class="tab-pane"
                         id="registerDiv"><#include "register.ftl" encoding="UTF-8"></div>
                </div>
                <!--验证码-->
                <div id="accessIdentifyingDiv" class="verBox">
                    <div id="acessIdentifyingContent" style="display:inline-block;"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="Access.getAccess()._initIdentifying($('#acessIdentifyingContent'))">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
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
    ).on('hidden.bs.modal',
        function (e) {
            //关闭模态框时，销毁验证码
           Access.getAccess().destroyIdentifying();
        }
    );

</script>