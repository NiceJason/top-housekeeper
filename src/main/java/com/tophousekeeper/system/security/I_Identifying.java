package com.tophousekeeper.system.security;

import com.tophousekeeper.system.SystemStaticValue;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码类的接口，所有验证码必须继承此接口
 */
public interface I_Identifying<T> {

    String EXCEPTION_CODE = SystemStaticValue.IDENTIFYING_EXCEPTION_CODE;
    String IDENTIFYING = "Identifying";
    //--------------以下为验证码大体错误类型，抛出错误时候用，会传至前端---------------
    //验证成功
    String SUCCESS = "Success";
    //验证失败
    String FAILURE = "Failure";
    //验证码过期
    String OVERDUE = "Overdue";

    //-------以下为验证码具体错误类型，存放在checkResult-------------
    String PARAM_ERROR = "验证码参数错误";
    String OVERDUE_ERROR = "验证码过期";
    String TYPE_ERROR = "验证码业务类型错误";
    String ID_ERROR = "验证码id异常";
    String CHECK_ERROR = "验证码验证异常";


    /**
     * 获取生成好的验证码
     * @param request
     * @return
     */
    public T getInstance(HttpServletRequest request) throws Exception;

    /**
     * 进行验证，没抛异常说明验证无误
     * @return
     */
    public void checkIdentifying(HttpServletRequest request) throws Exception;

    /**
     * 获取验证结果，如果成功则为success，失败则为失败信息
     * @return
     */
    public String getCheckResult();

    /**
     * 获取验证码的业务类型
     * @return
     */
    public String getIdentifyingType();
}
