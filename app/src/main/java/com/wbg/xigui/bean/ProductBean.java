package com.wbg.xigui.bean;

/**
 * @author tyk
 * @date :2019/8/29 17:09
 * @des :
 */
public class ProductBean extends BaseBean{
    //这里是用于订单支付成功传值给支付成功 显示  钱和债权
    public int type = 0;//0  立即支付    1  购物车支付
    public String orderId = "";
    public Float money = 0f;
    public Float zq = 0f;
    //这里是用于订单支付成功传值给支付成功 显示  钱和债权


    public ProductDetailBean productDetailBean;
    public IndentProductSkuView skuView;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ProductDetailBean getProductDetailBean() {
        return productDetailBean;
    }

    public void setProductDetailBean(ProductDetailBean productDetailBean) {
        this.productDetailBean = productDetailBean;
    }

    public IndentProductSkuView getSkuView() {
        return skuView;
    }

    public void setSkuView(IndentProductSkuView skuView) {
        this.skuView = skuView;
    }
}
