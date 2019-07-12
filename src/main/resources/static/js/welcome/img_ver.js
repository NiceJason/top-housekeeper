function imgVer(Config) {
    var el = Config.el;
    var w = Config.width;
    var h = Config.height;
    var imgLibrary = Config.img;
    var PL_Size = 48;
    var padding = 20;
    var MinN_X = padding + PL_Size;
    var MaxN_X = w - padding - PL_Size - PL_Size / 6;
    var MaxN_Y = padding;
    var MinN_Y = h - padding - PL_Size - PL_Size / 6;

    //这个要转移到后台
    function RandomNum(Min, Max) {
        var Range = Max - Min;
        var Rand = Math.random();

        if (Math.round(Rand * Range) == 0) {
            return Min + 1;
        } else if (Math.round(Rand * Max) == Max) {
            return Max - 1;
        } else {
            var num = Min + Math.round(Rand * Range) - 1; return num;
        }
    }
    //图片随机数
    var imgRandom = RandomNum(1, imgLibrary.length);
    //确定图片
    var imgSrc = imgLibrary[imgRandom];
    var X = RandomNum(MinN_X, MaxN_X);
    var Y = RandomNum(MinN_Y, MaxN_Y);
    var left_Num = -X + 10;
    var html = '<div style="position:relative;padding:16px 16px 28px;border:1px solid #ddd;background:#f2ece1;border-radius:16px;">';
    html += '<div style="position:relative;overflow:hidden;width:' + w + 'px;">';
    html += '<div style="position:relative;width:' + w + 'px;height:' + h + 'px;">';
    html += '<img id="scream" src="' + imgSrc + '" style="width:' + w + 'px;height:' + h + 'px;">';
    html += '<canvas id="puzzleBox" width="' + w + '" height="' + h + '" style="position:absolute;left:0;top:0;z-index:2222;"></canvas>';
    html += '</div>';
    html += '<div class="puzzle-lost-box" style="position:absolute;width:' + w + 'px;height:' + h + 'px;top:0;left:' + left_Num + 'px;z-index:11111;">';
    html += '<canvas id="puzzleShadow" width="' + w + '" height="' + h + '" style="position:absolute;left:0;top:0;z-index:2222;"></canvas>';
    html += '<canvas id="puzzleLost" width="' + w + '" height="' + h + '" style="position:absolute;left:0;top:0;z-index:3333;"></canvas>';
    html += '</div>';
    html += '<p class="ver-tips"></p>';
    html += '</div>';
    html += '<div class="re-btn"><a></a></div>';
    html += '</div>';
    html += '<br>';
    html += '<div style="position:relative;width:' + w + 'px;margin:auto;">';
    html += '<div style="border:1px solid #c3c3c3;border-radius:24px;background:#ece4dd;box-shadow:0 1px 1px rgba(12,10,10,0.2) inset;">';//inset 为内阴影
    html += '<p style="font-size:12px;color: #486c80;line-height:28px;margin:0;text-align:right;padding-right:22px;">按住左边滑块，拖动完成上方拼图</p>';
    html += '</div>';
    html += '<div class="slider-btn"></div>';
    html += '</div>';

    el.html(html);

    var d = PL_Size / 3;
    var c = document.getElementById("puzzleBox");
    //getContext获取该dom节点的canvas画布元素
    //---------------------------------这一块是图片中央缺失的那一块--------------------------------------
    var ctx = c.getContext("2d");

    ctx.globalCompositeOperation = "xor";
    //设置阴影模糊级别
    ctx.shadowBlur = 10;
    //设置阴影的颜色
    ctx.shadowColor = "#fff";
    //设置阴影距离的水平距离
    ctx.shadowOffsetX = 3;
    //设置阴影距离的垂直距离
    ctx.shadowOffsetY = 3;
    //rgba第四个参数是透明度，前三个是三原色，跟rgb比就是多了第四个参数
    ctx.fillStyle = "rgba(0,0,0,0.8)";
    //beginPath() 方法开始一条路径，或重置当前的路径。
    //提示：请使用这些方法来创建路径：moveTo()、lineTo()、quadricCurveTo()、bezierCurveTo()、arcTo() 以及 arc()。
    ctx.beginPath();
    //指线条的宽度
    ctx.lineWidth = "1";
    //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式
    ctx.strokeStyle = "rgba(0,0,0,0)";
    //表示画笔移到(X,Y)位置，没画东西
    ctx.moveTo(X, Y);
    //画笔才开始移动到指定坐标，之间画一条直线
    ctx.lineTo(X + d, Y);
    //绘制一条贝塞尔曲线，一共四个点确定，开始点(没在参数里)，和两个控制点(1和2参数结合,3和4参数结合)，结束点(5和6参数结合)
    ctx.bezierCurveTo(X + d, Y - d, X + 2 * d, Y - d, X + 2 * d, Y);
    ctx.lineTo(X + 3 * d, Y);
    ctx.lineTo(X + 3 * d, Y + d);
    ctx.bezierCurveTo(X + 2 * d, Y + d, X + 2 * d, Y + 2 * d, X + 3 * d, Y + 2 * d);
    ctx.lineTo(X + 3 * d, Y + 3 * d);
    ctx.lineTo(X, Y + 3 * d);
    //必须和beginPath()成对出现
    ctx.closePath();
    //进行绘制
    ctx.stroke();
    //根据fillStyle进行填充
    ctx.fill();

    //---------------------------------这个为要移动的块------------------------------------------------
    var c_l = document.getElementById("puzzleLost");
    //---------------------------------这个为要移动的块增加阴影------------------------------------------------
    var c_s = document.getElementById("puzzleShadow");
    var ctx_l = c_l.getContext("2d");
    var ctx_s = c_s.getContext("2d");
    var img = new Image();
    img.src = imgSrc;

    img.onload = function () {
        //从原图片，进行设置处理再显示出来(其实就是设置你想显示图片的位置2和3参数，和框w高h)
        ctx_l.drawImage(img, 0, 0, w, h);
    }
    ctx_l.beginPath();
    ctx_l.strokeStyle = "rgba(0,0,0,0)";
    ctx_l.moveTo(X, Y);
    ctx_l.lineTo(X + d, Y);
    ctx_l.bezierCurveTo(X + d, Y - d, X + 2 * d, Y - d, X + 2 * d, Y);
    ctx_l.lineTo(X + 3 * d, Y);
    ctx_l.lineTo(X + 3 * d, Y + d);
    ctx_l.bezierCurveTo(X + 2 * d, Y + d, X + 2 * d, Y + 2 * d, X + 3 * d, Y + 2 * d);
    ctx_l.lineTo(X + 3 * d, Y + 3 * d);
    ctx_l.lineTo(X, Y + 3 * d);
    ctx_l.closePath();
    ctx_l.stroke();
    //带阴影，数字越高阴影越严重
    ctx_l.shadowBlur = 10;
    //阴影的颜色
    ctx_l.shadowColor = "black";

    // ctx_l.fill(); 其实加这句就能有阴影效果了，不知道为什么加多个图层

    //分割画布的块
    ctx_l.clip(); 

    ctx_s.beginPath();
    ctx_s.lineWidth = "1";
    ctx_s.strokeStyle = "rgba(0,0,0,0)";
    ctx_s.moveTo(X, Y);
    ctx_s.lineTo(X + d, Y);
    ctx_s.bezierCurveTo(X + d, Y - d, X + 2 * d, Y - d, X + 2 * d, Y);
    ctx_s.lineTo(X + 3 * d, Y);
    ctx_s.lineTo(X + 3 * d, Y + d);
    ctx_s.bezierCurveTo(X + 2 * d, Y + d, X + 2 * d, Y + 2 * d, X + 3 * d, Y + 2 * d);
    ctx_s.lineTo(X + 3 * d, Y + 3 * d);
    ctx_s.lineTo(X, Y + 3 * d);
    ctx_s.closePath();
    ctx_s.stroke();
    ctx_s.shadowBlur = 20;
    ctx_s.shadowColor = "black";
    ctx_s.fill();

    var moveStart = '';
    $(".slider-btn").mousedown(function (e) {
        $(this).css({ "background-position": "0 -216px" });
        moveStart = e.pageX;
    });

    onmousemove = function (e) {

        var moveX = e.pageX;
        var d = moveX - moveStart;
        if (moveStart == '') {

        } else {
            if (d < 0 || d > (w - padding - PL_Size)) {

            } else {
                $(".slider-btn").css({ "left": d + 'px', "transition": "inherit" });
                $("#puzzleLost").css({ "left": d + 'px', "transition": "inherit" });
                $("#puzzleShadow").css({ "left": d + 'px', "transition": "inherit" });
            }
        }
    };

    onmouseup = function (e) {

        var moveEnd_X = e.pageX - moveStart;
        var ver_Num = X - 10;
        var deviation = 4;
        var Min_left = ver_Num - deviation;
        var Max_left = ver_Num + deviation;
        if (moveStart == '') {

        } else {
            if (Max_left > moveEnd_X && moveEnd_X > Min_left) {
                $(".ver-tips").html('<i style="background-position:-4px -1207px;"></i><span style="color:#42ca6b;">验证通过</span><span></span>');
                $(".ver-tips").addClass("slider-tips"); $(".puzzle-lost-box").addClass("hidden"); $("#puzzleBox").addClass("hidden");
                setTimeout(function () {
                    $(".ver-tips").removeClass("slider-tips"); imgVer(Config);
                }, 2000);
                Config.success();
            } else {
                $(".ver-tips").html('<i style="background-position:-4px -1229px;"></i><span style="color:red;">验证失败:</span><span style="margin-left:4px;">拖动滑块将悬浮图像正确拼合</span>');
                $(".ver-tips").addClass("slider-tips");
                setTimeout(function () {
                    $(".ver-tips").removeClass("slider-tips");
                }, 2000);
                Config.error();
            }
        }
        //0.5指动画执行到结束一共经历的时间
        setTimeout(function () {
            $(".slider-btn").css({ "left": '0', "transition": "left 0.5s" });
            $("#puzzleLost").css({ "left": '0', "transition": "left 0.5s" });
            $("#puzzleShadow").css({ "left": '0', "transition": "left 0.5s" });
        }, 1000);
        $(".slider-btn").css({ "background-position": "0 -84px" });
        moveStart = '';
        $(".re-btn a").on("click", function () {
            imgVer(Config);
        })
    }
}

/**
 * 打开浏览器验证码
 * 在这之前应该进行输入校验，不合格则不弹出验证码了
 */
function openIdentifying() {

    var identifyingContent = $("#identifyingContent");
    //让验证框显示出来
    $(".verBox").css({
        "left":"0",
        "opacity":"1"
    })
    imgVer({
        el:identifyingContent,
        width:'260',
        height:'116',
        img:[
            'image/ver.png',
            'image/ver-1.png',
            'image/ver-2.png',
            'image/ver-3.png'
        ],
        success:function () {
            console.log("验证码正确，执行AJAX");
        },
        error:function () {
            console.log("验证码错误，此处可以进行一些记录，防止恶意刷或者错误多少次就换图之类的");
        }
    });
}