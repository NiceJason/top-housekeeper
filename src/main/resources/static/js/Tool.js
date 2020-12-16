/**
 * 工具类，以下为拥有的方法，js加载时会自动执行，给window绑定tool对象
 * 直接window.tool.方法使用即可
 * 包括：1.提示窗 prompt
 *      2.Map转Json Map2Json (还有其他格式和Json的相互转换)
 *      3.类的继承 extendClass
 *      4.判断输入是否只是数字或字母
 *      5.智能判断错误信息弹出
 *      6.增强js的字符串replace方法，可以替换所有的字符
 *      7.将cookie转换成map
 * @constructor
 */
(function () {
    //js加载时绑定到window
    window.tool = {
        /**
         * 弹出式提示框，默认1.2秒自动消失
         * @param message 提示信息
         * @param style 提示样式，有alert-success、alert-danger、alert-warning、alert-info
         * @param time 消失时间
         */
        prompt: function (message, style, time) {
            style = (style === undefined) ? 'alert-success' : style;
            time = (time === undefined) ? 1200 : time;
            $('<div>')
                .appendTo('body')
                //alter样式和其他样式
                .addClass('alert').addClass(style)
                .html(message)
                .show()
                .delay(time)
                .fadeOut();
        },
        prompt_success: "alert-success",
        prompt_warning: "alert-warning",
        prompt_info: "alert-info",

        /**
         * Map对象转数组
         * @param map
         * @constructor
         */
        Map2Array: function (map) {
            return JSON.stringify([...map]);
        },

        /**
         * Map对象转对象，key为字符串
         * 如果有非字符串的键名，那么这个键名会被转成字符串，再作为对象的键名。
         * @param map
         * @constructor
         */
        strMap2Obj: function (map) {
            if (map instanceof Map) {
                let obj = Object.create(null);
                for (let [k, v] of map) {
                    obj[k] = String(v);
                }
                return obj;
            } else {
                throw "非map类型无法转为Json格式";
            }
        },

        /**
         * Map对象转对象，key为字符串
         * 如果有非字符串的键名，那么这个键名会被转成字符串，再作为对象的键名。
         * @param map
         * @constructor
         */
        strMap2Json: function (map) {
            return JSON.stringify(this.strMap2Obj(map));
        },

        /**
         * 继承方法
         * @param parentClass 父类
         * @param childClass 子类
         */
        extendClass: function (parentClass, childClass) {
            var Super = function () {
            };
            Super.prototype = parentClass.prototype;
            childClass.prototype = new Super();
            childClass.prototype.constructor = childClass;
        },

        /**
         * 判断字符串是否只是数字和字母
         * @param str
         */
        isLetterDigit: function (str) {
            var reg = /^[0-9a-zA-Z]+$/;
            return reg.test(str);
        },

        /**
         * 判断是否是邮箱格式
         * @param str
         */
        isEmail: function (str) {
            var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
            return reg.test(str);
        },

        /**
         * 智能判断错误信息弹出
         * @param data 将要弹出的信息
         */
        smratErrorPrompt: function (data) {
            //如果错误data是个对象，那大概率是后端Response对象
            if (data instanceof Object) {
                //这里有两种情况，一种是Response错误对象，一种是Web容器报错（如Filter验证失败的摆错）
                var msg;
                if (data.meta) { //有meta，意味着是Response对象报错
                    msg = data.meta.msg;
                } else {  //这里是Web容器报错
                    msg = data.message;
                }
                //出现异常进行弹框提示
                this.prompt(msg, this.prompt_warning);
                return;
            } else {
                //出现异常进行弹框提示
                this.prompt(data, this.prompt_warning);
            }
        },

        /**
         * 由于js字符串只有replace方法（替换一个），所以不方便
         * 封装此函数达到全部替换的目的
         * @param str 目标字符串
         * @param searchValue 要替换的字符
         * @param replaceValue 替换后的字符
         */
        replaceAll: function (str, searchValue, replaceValue) {
            var reg = new RegExp(searchValue, "g");
            return str.replace(reg, replaceValue);
        },

        /**
         * 将cookie转换成map
         * cookie初始化格式： name1=value1; name2=value2; name3=value3
         * 注意 name前面可能会有空格的
         */
        getCookieMap: function () {
            var cookieString = document.cookie;
            var cookieMap = new Map();
            if (cookieString.length > 0) {
                var cookies = cookieString.split(';');
                for (let i = 0; i < cookies.length; i++) {
                    var cookie = cookies[i];
                    var name = $.trim(cookie.split('=')[0]);
                    var value = $.trim(cookie.split('=')[1]);
                    cookieMap.set(name, value);
                }
            }
            return cookieMap;
        }
    }
})();

