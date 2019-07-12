package com.tophousekeeper.entity;

import java.io.Serializable;

/**
 * @author NiceBin
 * @description: 验证码类，前端需要生成验证码的信息
 * @date 2019/7/12 16:04
 */
public class Identifying implements Serializable {
    //需要使用的图片
    private String imgSrc;
    //生成块的x坐标
    private int X;
    //生成块的y坐标
    private int Y;

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
}
