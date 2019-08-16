<br/>
<span>没有账号，注册一个！</span>
<br/>
<!-- 注册表单-->
<form id="registerInfo" action="/access/registered" style="margin-top: 5px">
    <div class="form-group">
        <label for="register-emaill">Email address</label>
        <!--list为了绑定数据源；spellcheck取消拼写检查，不然会有红色波浪线-->
        <input name="email" class="form-control" id="register-emaill" list="email_list"
               spellcheck ="false">
        <datalist id="email_list"></datalist>
    </div>
    <div class="form-group">
        <label for="register-password">Password</label>
        <input name="password" class="form-control" id="register-password" >
    </div>
</form>

<script>
    inputList($("#register-emaill"),$("#email_list"));
</script>