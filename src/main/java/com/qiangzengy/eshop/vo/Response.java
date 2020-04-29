package com.qiangzengy.eshop.vo;

import lombok.Data;

/**
 * 请求响应
 */

@Data
public class Response {
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    private String status;
    private String message;

    public Response() {

    }

    public Response(String status) {
        this.status = status;
    }

    public Response(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
