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
                        <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Email">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">Password</label>
                        <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<script>
    // $('#registeredModal').on('show.bs.modal', centerModals('#registeredModal'));
        //$('body').modal('hide');
        $('#registeredModal').on('show.bs.modal', adjustBody_beforeShow);
        $('#registeredModal').on('hidden.bs.modal', adjustBody_afterShow);

</script>