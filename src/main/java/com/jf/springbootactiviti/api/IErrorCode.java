package com.jf.springbootactiviti.api;

/**
 * @Description: 封装API的错误码
 * @Author: weizh
 * @date: 2021/2/13 9:36 下午
 */
public interface IErrorCode {
    /**
     * @description: 获取错误编码
     * @return: long
     * @author weizh
     * @date: 2021/2/24 9:35
     */
    long getCode();

    /**
     * @description: 获取错误描述
     * @return: java.lang.String
     * @author: weizh
     * @date: 2021/2/24 9:35
     */
    String getMessage();
}
