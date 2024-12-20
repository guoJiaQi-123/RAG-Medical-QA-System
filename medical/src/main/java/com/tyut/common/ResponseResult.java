package com.tyut.common;

/**
 * 统一结果集
 * @param <T>
 */
public class ResponseResult<T> {
    private int code;           // 状态码
    private String message;     // 消息
    private T data;             // 返回数据

    // 构造函数
    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 默认成功响应
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "操作成功", data);
    }

    // 默认失败响应
    public static <T> ResponseResult<T> fail(String message) {
        return new ResponseResult<>(500, message, null);
    }

    // 其他自定义返回状态码和消息
    public static <T> ResponseResult<T> of(int code, String message, T data) {
        return new ResponseResult<>(code, message, data);
    }

    // Getter 和 Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
