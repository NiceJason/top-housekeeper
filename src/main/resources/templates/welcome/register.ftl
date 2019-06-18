<script src="/js/registered.js"></script>

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
                <form id="registerInfo">
                    <div class="form-group">
                        <label for="exampleInputEmail1">Email address</label>
                        <input type="email" class="form-control" id="emaill" placeholder="Email">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">Password</label>
                        <input type="password" class="form-control" id="password" placeholder="Password">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="new Registered().submit()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<script>
    // 重点，以下JS是为了防导航栏在模态框出现的时候不会偏移
    // （因为那时候浏览器滚动条会被取消，导致多出的空间会使固定在顶端的导航栏鬼畜）
    $('#registeredModal').on('show.bs.modal',
        function(e){
            $('body').css("cssText",";overflow-y:auto !important;")
        }
    );
</script>