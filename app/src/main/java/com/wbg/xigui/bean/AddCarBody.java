package com.wbg.xigui.bean;

/**
 * @author tyk
 * @date :2019/8/21 11:23
 * @des :
 */
public class AddCarBody {

    public String memberId;
    public String skuId;
    public int total;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
