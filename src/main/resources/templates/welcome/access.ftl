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
                <button type="button" class="btn btn-default" data-container="body"
                        data-toggle="popover" data-placement="top"
                        <#--                        data-html="true"-->
                        <#--                        data-title="验证码" id="identifyingBtn"-->
                        <#--                        data-content="Vivamus sagittis lacus vel augue laoreet rutrum faucibus."-->
                >
                    Popover on 顶部
                </button>
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
    $(function () {
        $("[data-toggle='popover']").popover(
            {
                trigger: 'click', //触发方式
                template: '<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title">标题</h3><div class="popover-content">内容</div></div>',
                html: true, // 为true的话，data-content里就能放html代码了
            }
        );
    });
    $('#identifyingBtn').on('show.bs.popover',
        function (e) {

        }
    );
    $('#identifyingBtn').on('shown.bs.popover',
        function (e) {
            e.currentTarget.dataset.content = "新生成的";
        }
    );
</script>