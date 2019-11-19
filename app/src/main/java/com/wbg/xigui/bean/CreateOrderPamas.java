package com.wbg.xigui.bean;

import java.util.List;

/**
 * @author tyk
 * @date :2019/8/20 11:24
 * @des :
 */
public class CreateOrderPamas {
    public String addressId = "";
    public int isFromShoppingBag;
    public String memberId;
    public int payment;
    public List<CreateSkuOrder> list;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getIsFromShoppingBag() {
        return isFromShoppingBag;
    }

    public void setIsFromShoppingBag(int isFromShoppingBag) {
        this.isFromShoppingBag = isFromShoppingBag;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public List<CreateSkuOrder> getList() {
        return list;
    }

    public void setList(List<CreateSkuOrder> list) {
        this.list = list;
    }

    public static class CreateSkuOrder {
        public String merchantId;
        public String productId;
        public String skuId;
        public int total;

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
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
}
