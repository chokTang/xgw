package com.wbg.xigui.utils

enum class LoadingType {
    ERROR_NOT_EMPTY,//加载失败当前页面数据不为空
    ERROR_EMPTY, //加载失败并且当前页面数据为空
    SUCCESS, //加载成功
    EMPTY, //暂无数据
    NO_MORE//没有更多数据
}