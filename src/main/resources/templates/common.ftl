<#--
页面模板，使用方法
<@comm.page>

	<@comm.head title="我的标题">
	插入head里面出现的内容
	</@comm.head>

	<@comm.body>
	插入页面主体
	</@comm.body>

	可插入在</body>与</html>之间的内容
</@comm.page>


头标签定义了参数title, 有默认值，按需传递
relpath 是页面相对于“/”的路径,值为../或者../../或者空等等
该值定义在defaultVariable.ftl中，自动引入，
所有的ftl都可以直接使用relpath值
-->
<#macro head>
    <head>
        <#--meat标签的Keywords的的信息参数，代表说明网站的关键词是什么-->
        <meta name="Keywords"content="顶上管家，生活，理发，预约"/>
        <#--meta标签的Description的信息参数，代表说明网站的主要内容，概况是什么。 -->
        <meta name="Description"content="信息参数"/>
        <#--网页过期期限-->
        <meta http-equiv="Expires" content="0">
        <#--显示字符集的设定-->
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <#--在这里插入需要引用的样式或者脚本-->
        <#nested>
        <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    </head>
</#macro>

<#macro body>
    <body>
    <#--在这里插入需要引用的样式或者脚本-->
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <#nested>
    <#--在这里插入页面主体内容-->
    </body>
</#macro>


<#macro page>
    <!DOCTYPE html>
    <html xmlns="http://www.w3.org/1999/xhtml">
    <#--在这里插入需要引用的样式或者脚本-->
    <#nested>
    </html>
</#macro>