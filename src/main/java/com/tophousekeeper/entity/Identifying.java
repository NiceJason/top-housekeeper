package com.tophousekeeper.entity;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author NiceBin
 * @description: 验证码类，前端需要生成验证码的信息
 * @date 2019/7/12 16:04
 */
public class Identifying implements Serializable {
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

    public Identifying(String id){
        this.identifyingId = id;
    }

    public String getIdentifyingId() {
        return identifyingId;
    }

    public void setIdentifyingId(String identifyingId) {
        this.identifyingId = identifyingId;
    }

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
}
