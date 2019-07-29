package com.tophousekeeper.entity;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import com.tophousekeeper.system.security.I_Identifying;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * @author NiceBin
 * @description: 验证码类，前端需要生成验证码的信息
 * @date 2019/7/12 16:04
 */
public class ImgIdentifying implements I_Identifying<ImgIdentifying>,Serializable {
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
    //验证码生成的时间
    private Calendar calendar;
    //验证码结果，如果有结果说明已经被校验，防止因为网络延时的二次校验
    private String checkResult;

    public ImgIdentifying() throws NoSuchAlgorithmException {
        this.identifyingId = String.valueOf(Tool.getSecureRandom(0, 100));
    }

    public ImgIdentifying(String identifyingId){
        this.identifyingId = identifyingId;
    }

    @Override
    public ImgIdentifying getInstance(HttpServletRequest request) throws NoSuchAlgorithmException {
        String min_X = request.getParameter("min_X");
        String max_X = request.getParameter("max_X");
        String min_Y = request.getParameter("min_Y");
        String max_Y = request.getParameter("max_Y");

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
        identifying.setIdentifyingType(request.getParameter("identifyingType"));
        return identifying;
    }

    @Override
    public void checkIdentifying(HttpServletRequest request) {
        String identifyingId = request.getParameter("identifyingId");
        String moveEnd_X = request.getParameter("moveEnd_X");
        String identifyingType = request.getParameter("identifyingType");

        //检查验证参数
        if(identifyingId==null||identifyingType==null||!Tool.isInteger(moveEnd_X)){
            checkResult = PARAM_ERROR;
            throw new SystemException(EXCEPTION_CODE,FAILURE);
        }

        HttpSession session = request.getSession();
        ImgIdentifying identifying = (ImgIdentifying) session.getAttribute(I_Identifying.IDENTIFYING);

        //开始验证
        //看是否已经校验
        if(!Tool.isNull(identifying.getCheckResult())){
            checkResult = OVERDUE_ERROR;
            throw new SystemException(EXCEPTION_CODE,OVERDUE);
        }
        // 验证时间是否在有效期
        Calendar nowCalendar = Calendar.getInstance();
        if(nowCalendar.getTimeInMillis()-identifying.getCalendar().getTimeInMillis()
                >SystemStaticValue.IDENTIFYING_OVERDUE){
            checkResult = OVERDUE_ERROR;
            throw new SystemException(EXCEPTION_CODE,OVERDUE);
        }
        //验证类型
        if(!identifying.getIdentifyingType().equals(identifyingType)){
            checkResult = TYPE_ERROR;
            throw new SystemException(EXCEPTION_CODE,FAILURE);
        }
        //验证Id
        if(!identifying.getIdentifyingId().equals(identifyingId)){
            checkResult = ID_ERROR;
            throw new SystemException(EXCEPTION_CODE,FAILURE);
        }
        //跟前端判断保持一致
        int X = identifying.getX()-10;
        int move = Integer.parseInt(moveEnd_X);
        if((X+identifying.getX()) < move || move < (X-identifying.getX())){
            checkResult = CHECK_ERROR;
            throw new SystemException(EXCEPTION_CODE,FAILURE);
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

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }
}
