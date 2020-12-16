package com.tophousekeeper.entity.system;

/**
 * @auther: NiceBin
 * @description: 前后端交互的响应类，把前后端交互的数据格式统一起来：
 * {
 *    "meta": {
 *         "success": true,
 *         "message": "ok"
 *     },
 *     "data": ...
 * }
 * 为RESTful风格提供支持，这里要注意，只要属性有get方法，都会变成序列化JSON的参数
 * 所以不想传到前台的参数，不要有get方法
 * @date: 2020/4/21 18:32
 */
public class Response {
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private final Meta SUCCESS_META = new Meta(true,OK);
    private final Meta ERROR_META = new Meta(false,ERROR);
    //消息头
    private Meta meta;
    //消息主体
    private Object data ;
    //额外数据，由实现了ResponseBodyAdvice接口的类来为返回值添加新内容
    private Object extraData;

    public Response success(){
        this.meta = SUCCESS_META;
        return this;
    }

    public Response success(Object data){
        this.meta = SUCCESS_META;
        this.data = data;
        return this;
    }

    public Response failure(){
        this.meta = ERROR_META;
        return this;
    }

    public Response failure(String msg){
        this.meta = new Meta(false,msg);
        return this;
    }
    /**
     * 内部类，消息头
     */
    public class Meta{
        private boolean success;
        private String msg;

        public Meta(boolean success,String msg){
            this.success = success;
            this.msg = msg;
        }

        //-----------------------------以下为set和get
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

   //--------------以下为set和get

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
}
