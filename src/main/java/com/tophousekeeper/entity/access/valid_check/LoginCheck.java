//package com.tophousekeeper.entity.access.valid_check;
//
//import com.tophousekeeper.system.SystemException;
//import com.tophousekeeper.system.SystemStaticValue;
//import com.tophousekeeper.util.Tool;
//
//import javax.validation.Constraint;
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//import javax.validation.Payload;
//import java.lang.annotation.Documented;
//
///**
// * Login对象的序列化检测
// * 用于注册时参数注入的检测
// */
//@Documented
//@Constraint(validatedBy = LoginCheck.PasswordCheckValidator.class)
//public @interface LoginCheck {
//    /*
//    以下3个属性是按规定添加的
//     */
//    //默认错误信息
//    String message() default "登录信息有误";
//    //分组，不同组可能采取不同的校验规则
//    Class<?>[] groups() default {};
//    //负载
//    Class<? extends Payload>[] payload() default {};
//
//    /**
//     *  Login的账号检测
//     * 父类使用了泛型，需要指定两个参数，第一个自定义注解类，第二个为需要校验的数据类型
//     */
//    class EmailCheckValidator implements ConstraintValidator<LoginCheck,String>{
//
//        @Override
//        public void initialize(LoginCheck constraintAnnotation) {
//
//        }
//
//        @Override
//        public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
//            if(Tool.isEmail(email)){
//                if(email.length()>40){
//                    throw new SystemException(SystemStaticValue.EMAIL_EXCEPTION_CODE,"邮箱长度最长40位");
//                }
//            }else{
//                throw new SystemException(SystemStaticValue.EMAIL_EXCEPTION_CODE,"邮箱格式不正确");
//            }
//            return true;
//        }
//    }
//
//    /**
//     *  Login的密码检测
//     * 父类使用了泛型，需要指定两个参数，第一个自定义注解类，第二个为需要校验的数据类型
//     */
//    class PasswordCheckValidator implements ConstraintValidator<LoginCheck,String> {
//
//        /**
//         * 初始方法，可以做一些初始化
//         * @param constraintAnnotation
//         */
//        @Override
//        public void initialize(LoginCheck constraintAnnotation) {
//
//        }
//
//        /**
//         * 最终的校验方法
//         * 规则：密码只能为数字或字母，长度为6-12位
//         * @param password 需要被校验的值
//         * @param constraintValidatorContext 用来操作提示信息的，这个上下文包含了认证中所有的信息，
//         * 我们可以利用这个上下文实现获取默认错误提示信息，禁用错误提示信息，改写错误提示信息等操作
//         * @return
//         */
//        @Override
//        public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
//            if(Tool.isLetterDigit(password)){
//                if(password.length()<6||password.length()>12){
//                    throw new SystemException(SystemStaticValue.PASSWORD_EXCEPTION_CODE,"密码长度需在6-12位");
//                }
//
//            }else {
//                throw new SystemException(SystemStaticValue.PASSWORD_EXCEPTION_CODE,"密码只能为数字或字母");
//            }
//            return true;
//        }
//    }
//}
