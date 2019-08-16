<@comm.page>
    <@comm.head>
    </@comm.head>
    <@comm.body>
        <#include "/navegation.ftl" encoding="UTF-8">
        <div id="blog-content" style="position: relative;width: 100%;height: 90%;top: 51px;"></div>

        <script>
            $(document).ready(function () {
                var blogDom = $("#blog-content");
                var iframDom = $("<iframe width='100%' height='100%'/>");
                iframDom.attr("src","${blogSrc!}");
                blogDom.append(iframDom);

            })
        </script>
    </@comm.body>
</@comm.page>