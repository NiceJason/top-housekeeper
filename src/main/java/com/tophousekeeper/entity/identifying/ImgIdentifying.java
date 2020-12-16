package com.tophousekeeper.entity.identifying;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.security.I_Identifying;
import com.tophousekeeper.util.Tool;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @author NiceBin
 * @description: 验证码类，前端需要生成验证码的信息
 * @date 2019/7/12 16:04
 */
public class ImgIdentifying implements I_Identifying<ImgIdentifying,ImgIdentifyInfo>,Serializable {
    //此次验证码的id
    private String identifyingId;
    //此次验证码的业务类型
    private String identifyingType;
    //需要使用的图片
    private String imgSrc;
    //生成块的x坐标
    private int X;
    //生成块的y坐标
    private int Y;

    //允许的误差
    private int deviation = 2;
    @JSONField(serialize = false) //忽略这个属性转为JSON对象
    //验证码生成的时间
    private Calendar calendar;
    @JSONField(serialize = false)
    //验证码结果，如果有结果说明已经被校验，防止因为网络延时的二次校验
    private String checkResult;

    public ImgIdentifying() throws NoSuchAlgorithmException {
        this.identifyingId = String.valueOf(Tool.getSecureRandom(0, 100));
    }

    public ImgIdentifying(String identifyingId){
        this.identifyingId = identifyingId;
    }

    @Override
    public ImgIdentifying getInstance(ImgIdentifyInfo imgIdentifyInfo) throws NoSuchAlgorithmException {
        String min_X = imgIdentifyInfo.getMin_X();
        String max_X = imgIdentifyInfo.getMax_X();
        String min_Y = imgIdentifyInfo.getMin_Y();
        String max_Y = imgIdentifyInfo.getMax_Y();

        int imgPoolLength = SystemStaticValue.IDENTIFYING_IMG_POOL.length;
        String imgSrc = SystemStaticValue.IDENTIFYING_IMG_POOL[Tool.getSecureRandom(0, imgPoolLength - 1)];
        int X = Tool.getSecureRandom(min_X, max_X);
        int Y = Tool.getSecureRandom(min_Y, max_Y);

        ImgIdentifying identifying = new ImgIdentifying();
        identifying.setImgSrc(imgSrc);
        identifying.setX(X);
        identifying.setY(Y);
        Calendar calendar = Calendar.getInstance();
        identifying.setCalendar(calendar);
        identifying.setIdentifyingType(imgIdentifyInfo.getIdentifyingType());
        return identifying;
    }

    @Override
    public void checkIdentifying(HashMap<String, String> params,HttpSession session) throws SystemException{
        String identifyingId = params.get("identifyingId");
        String moveEnd_X = params.get("moveEnd_X");
        String identifyingType = params.get("identifyingType");

        //检查验证参数
        if(identifyingId==null||identifyingType==null||!Tool.isInteger(moveEnd_X)){
            checkResult = PARAM_ERROR;
            throw new SystemException(EXCEPTION_CODE,checkResult);
        }

        ImgIdentifying identifying = (ImgIdentifying) session.getAttribute(I_Identifying.IDENTIFYING);

        //开始验证
        //看是否已经校验
        if(!Tool.isNull(identifying.getCheckResult())){
            checkResult = OVERDUE_ERROR;
            throw new SystemException(EXCEPTION_CODE,checkResult);
        }
        // 验证时间是否在有效期
        Calendar nowCalendar = Calendar.getInstance();
        if(nowCalendar.getTimeInMillis()-identifying.getCalendar().getTimeInMillis()
                >SystemStaticValue.IDENTIFYING_OVERDUE){
            checkResult = OVERDUE_ERROR;
            throw new SystemException(EXCEPTION_CODE,checkResult);
        }
        //验证类型
        if(!identifying.getIdentifyingType().equals(identifyingType)){
            checkResult = TYPE_ERROR;
            throw new SystemException(EXCEPTION_CODE,checkResult);
        }
        //验证Id
        if(!identifying.getIdentifyingId().equals(identifyingId)){
            checkResult = ID_ERROR;
            throw new SystemException(EXCEPTION_CODE,checkResult);
        }
        //跟前端判断保持一致
        int X = identifying.getX()-10;
        int move = Integer.parseInt(moveEnd_X);
        if((X+identifying.getX()) < move || move < (X-identifying.getX())){
            checkResult = CHECK_ERROR;
            throw new SystemException(EXCEPTION_CODE,checkResult);
        }
        checkResult = SUCCESS;
    }

    //-----------------------------以下为set和get--------------------------
    public String getIdentifyingId() {
        return identifyingId;
    }

    public void setIdentifyingId(String identifyingId) {
        this.identifyingId = identifyingId;
    }

    @Override
    public String getIdentifyingType() {
        return identifyingType;
    }

    public void setIdentifyingType(String identifyingType) {
        this.identifyingType = identifyingType;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getDeviation() {
        return deviation;
    }

    public void setDeviation(int deviation) {
        this.deviation = deviation;
    }

    @JsonIgnore
    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @JsonIgnore
    @Override
    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }
}
