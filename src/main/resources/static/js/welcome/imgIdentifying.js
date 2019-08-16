/**
 * 滑动验证类
 * complete传递的参数为identifyingId，identifyingType，moveEnd_X
 * @param config 各种配置
 */
var ImgIdentifying = function(config) {
    Identifying.call(this, config.identifyingId, config.identifyingType,config.el);
    this.config = config;
    this.init();
}

extendClass(Identifying, ImgIdentifying);

/**
 * 销毁函数
 */
ImgIdentifying.prototype.destroy = function () {
    Identifying.prototype.destroy.call(this);
}

var width = '260';
var height = '116';
var pl_size = 48;
var padding_ = 20;
ImgIdentifying.prototype.init = function () {

    this.clearDom();
    var el = this.getContentDiv();
    var w = width;
    var h = height;
    var PL_Size = pl_size;
    var padding = padding_;
    var self = this;

    //这个要转移到后台
    function RandomNum(Min, Max) {
        var Range = Max - Min;
        var Rand = Math.random();

        if (Math.round(Rand * Range) == 0) {
            return Min + 1;
        } else if (Math.round(Rand * Max) == Max) {
            return Max - 1;
        } else {
            var num = Min + Math.round(Rand * Range) - 1;
            return num;
        }
    }

    //确定图片
    var imgSrc = this.config.img;
    var X = this.config.X;
    var Y = this.config.Y;
    var left_Num = -X + 10;
    var html = '<div style="position:relative;padding:16px 16px 28px;border:1px solid #ddd;background:#f2ece1;border-radius:16px;">';
    html += '<div style="position:relative;overflow:hidden;width:' + w + 'px;">';
    html += '<div style="position:relative;width:' + w + 'px;height:' + h + 'px;">';
    html += '<img id="scream" src="' + imgSrc + '" style="width:' + w + 'px;height:' + h + 'px;">';
    html += '<canvas id="puzzleBox" width="' + w + '" height="' + h + '" style="position:absolute;left:0;top:0;z-index:222;"></canvas>';
    html += '</div>';
    html += '<div class="puzzle-lost-box" style="position:absolute;width:' + w + 'px;height:' + h + 'px;top:0;left:' + left_Num + 'px;z-index:11111;">';
    html += '<canvas id="puzzleShadow" width="' + w + '" height="' + h + '" style="position:absolute;left:0;top:0;z-index:222;"></canvas>';
    html += '<canvas id="puzzleLost" width="' + w + '" height="' + h + '" style="position:absolute;left:0;top:0;z-index:333;"></canvas>';
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

    //开始时间
    var beginTime;
    //结束时间
    var endTime;
    var moveStart = '';
    $(".slider-btn").mousedown(function (e) {
        $(this).css({"background-position": "0 -216px"});
        moveStart = e.pageX;
        beginTime = new Date().valueOf();
    });

    onmousemove = function (e) {
        var e = e || window.event;
        var moveX = e.pageX;
        var d = moveX - moveStart;
        if (moveStart == '') {

        } else {
            if (d < 0 || d > (w - padding - PL_Size)) {

            } else {
                $(".slider-btn").css({"left": d + 'px', "transition": "inherit"});
                $("#puzzleLost").css({"left": d + 'px', "transition": "inherit"});
                $("#puzzleShadow").css({"left": d + 'px', "transition": "inherit"});
            }
        }
    };

    onmouseup = function (e) {
        var e = e || window.event;
        var moveEnd_X = e.pageX - moveStart;
        var ver_Num = X - 10;
        var deviation = self.config.deviation;
        var Min_left = ver_Num - deviation;
        var Max_left = ver_Num + deviation;

        if (moveStart == '') {

        } else {
            endTime = new Date().valueOf();
            if (Max_left > moveEnd_X && moveEnd_X > Min_left) {
                $(".ver-tips").html('<i style="background-position:-4px -1207px;"></i><span style="color:#42ca6b;">验证通过</span><span></span>');
                $(".ver-tips").addClass("slider-tips");
                $(".puzzle-lost-box").addClass("hidden");
                $("#puzzleBox").addClass("hidden");
                setTimeout(function () {
                    $(".ver-tips").removeClass("slider-tips");
                }, 2000);
                self.success({
                    'identifyingId': self.config.identifyingId, 'identifyingType': self.config.identifyingType,
                    'moveEnd_X': moveEnd_X
                })
            } else {
                $(".ver-tips").html('<i style="background-position:-4px -1229px;"></i><span style="color:red;">验证失败:</span><span style="margin-left:4px;">拖动滑块将悬浮图像正确拼合</span>');
                $(".ver-tips").addClass("slider-tips");
                setTimeout(function () {
                    $(".ver-tips").removeClass("slider-tips");
                }, 2000);
                self.error();
            }
        }
        //0.5指动画执行到结束一共经历的时间
        setTimeout(function () {
            $(".slider-btn").css({"left": '0', "transition": "left 0.5s"});
            $("#puzzleLost").css({"left": '0', "transition": "left 0.5s"});
            $("#puzzleShadow").css({"left": '0', "transition": "left 0.5s"});
        }, 1000);
        $(".slider-btn").css({"background-position": "0 -84px"});
        moveStart = '';
        $(".re-btn a").on("click", function () {
            Access.getAccess().initIdentifying($('#acessIdentifyingContent'));
        })
    }
}

/**
 * 获取该类型验证码的一些参数
 */
ImgIdentifying.getParamMap = function () {

    var min_X = padding_ + pl_size;
    var max_X = width - padding_ - pl_size - pl_size / 6;
    var max_Y = padding_;
    var min_Y = height - padding_ - pl_size - pl_size / 6;

    var paramMap = new Map();
    paramMap.set("min_X", min_X);
    paramMap.set("max_X", max_X);
    paramMap.set("min_Y", min_Y);
    paramMap.set("max_Y", max_Y);

    return paramMap;
}

/**
 * 设置验证成功的回调函数
 * @param success
 */
ImgIdentifying.prototype.setSuccess = function (successFunc) {
    this.successFunc = successFunc;
}

/**
 * 设置验证失败的回调函数
 * @param success
 */
ImgIdentifying.prototype.setError = function (errorFunc) {
    this.errorFunc = errorFunc;
}
