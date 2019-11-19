package com.wbg.xigui.wxapi;


import com.wbg.xigui.bean.BaseBean;

/**
 * Created by admin on 2016/12/28.
 */
public class APIPayInfo extends BaseBean {
    private static final long serialVersionUID = 2732715004485340978L;
    public String partnerid;
    public String prepayid;
    public String noncestr ;
    public String timestamp;
    public String sign;
}
