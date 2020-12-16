package com.tophousekeeper.system;

/**
 * @auther: NiceBin
 * @description: 自定义异常类
 *                  注意：在过滤器Filter里抛出的错误这里捕捉不到的
 * @date: 2019/6/13 21:13
 */
public class SystemException extends RuntimeException{
    private String code;
    private String msg;

    public SystemException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString(){
        return "code="+this.code+" msg="+this.msg;
    }
}
