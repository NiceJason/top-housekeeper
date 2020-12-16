package com.tophousekeeper.entity.identifying;

/**
 * @auther: NiceBin
 * @description: 滑动验证的接收参数类，为了配合RESTful
 * @date: 2020/4/23 14:57
 */
public class ImgIdentifyInfo {
    //此次验证码的业务类型
    private String identifyingType;
    //图形x轴偏移最小值
    private String min_X ;
    //图形x轴偏移最大值
    private String max_X ;
    //图形y轴偏移最小值
    private String min_Y ;
    //图形y轴偏移最大值
    private String max_Y ;

    //--------------------------------以下为set和get----------------------
    public String getIdentifyingType() {
        return identifyingType;
    }

    public void setIdentifyingType(String identifyingType) {
        this.identifyingType = identifyingType;
    }

    public String getMin_X() {
        return min_X;
    }

    public void setMin_X(String min_X) {
        this.min_X = min_X;
    }

    public String getMax_X() {
        return max_X;
    }

    public void setMax_X(String max_X) {
        this.max_X = max_X;
    }

    public String getMin_Y() {
        return min_Y;
    }

    public void setMin_Y(String min_Y) {
        this.min_Y = min_Y;
    }

    public String getMax_Y() {
        return max_Y;
    }

    public void setMax_Y(String max_Y) {
        this.max_Y = max_Y;
    }
}
