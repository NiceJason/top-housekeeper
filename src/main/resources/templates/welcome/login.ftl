<br/>
<span>有账号就赶紧登录吧~</span>
<br/>
<!-- 登录表单-->
<form id="loginInfo" action="/access/login" style="margin-top: 5px">
    <div class="form-group">
        <label for="login-emaill">Email address</label>
        <!--list为了绑定数据源；spellcheck取消拼写检查，不然会有红色波浪线-->
        <input name="email" class="form-control" id="login-emaill" list="email_list"
               spellcheck ="false">
        <datalist id="email_list"></datalist>
    </div>
    <div class="form-group">
        <label for="login-password">Password</label>
        <input name="password" class="form-control" id="login-password" >
    </div>
</form>
<script>
    inputList($("#login-emaill"),$("#email_list"));
</script>