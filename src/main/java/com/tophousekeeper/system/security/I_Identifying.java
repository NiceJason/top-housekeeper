package com.tophousekeeper.system.security;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 验证码类的接口，所有验证码必须继承此接口
 * T为验证码类，K为接收验证码的消息类
 */
public interface I_Identifying<T,K> {

    //错误编号
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

    //------------------------以下为checkIdentifying方法用到的key值
    //无法解析的Key值
    String UNABLE_TO_RESOLVE = "UnableToResolve";

    /**
     * 获取生成好的验证码
     * @param identifyInfo 验证码消息类
     * @return
     */
     T getInstance(K identifyInfo) throws Exception;

    /**
     * 进行验证，没抛异常说明验证无误
     * 由于是通过拦截器来实现检测的，所以不同的传输类型会要不同的解析
     * 拦截器会尽量将参数解析出来
     * 如果实在无法解析，则值放入KEY = "UnableToResolve"
     * 需要自行判空
     * @return
     */
     void checkIdentifying(HashMap<String, String> params, HttpSession session) throws SystemException;

    /**
     * 获取验证结果，如果成功则为success，失败则为失败信息
     * @return
     */
     String getCheckResult();

    /**
     * 获取验证码的业务类型
     * @return
     */
     String getIdentifyingType();
}
