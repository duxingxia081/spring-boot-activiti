package com.jf.springbootactiviti.api;
/**
 * @Description: 通用返回对象
 * @Author: weizh
 * @date: 2021/2/13 9:12 下午
 */
public class CommonResult<T> {
    private long code;
    private String message;
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @Description: 成功返回结果
     * @param: data(返回的数据)
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:26 下午
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * @description: 成功返回结果
     * @param: data(返回的数据)
     * @param: message(返回的消息)
     * @return: com.jf.common.api.CommonResult<T>
     * @author: weizh
     * @date: 2021/2/23 9:27 下午
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * @Description: 失败返回结果
     * @param: errorCode(错误码)
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:28 下午
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * @Description: 失败返回结果
     * @param: errorCode(错误码)
     * @param: message(错误信息)
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:28 下午
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode,String message) {
        return new CommonResult<>(errorCode.getCode(), message, null);
    }

    /**
     * @Description: 失败返回结果
     * @param: message(提示消息)
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:29 下午
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * @Description: 失败返回结果
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:33 下午
     */
    public static <T> CommonResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * @Description: 参数验证失败返回结果
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:34 下午
     */
    public static <T> CommonResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * @Description: 参数验证失败返回结果
     * @param: message(提示信息)
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:34 下午
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * @Description: 未登录返回结果
     * @param: data(数据)
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:34 下午
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * @Description: 未授权返回结果
     * @param: data(数据)
     * @return: com.jf.common.api.CommonResult<T>
     * @Author: weizh
     * @date: 2021/2/13 9:35 下午
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
