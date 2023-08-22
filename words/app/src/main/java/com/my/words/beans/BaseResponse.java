package com.my.words.beans;

public class BaseResponse {
    private ResultDTO result;
    public ResultDTO getResult() {
        return result;
    }

    public static class ResultDTO {
        private String msg;
        private Integer code;

        public String getMsg() {
            return msg;
        }

        public Integer getCode() {
            return code;
        }
    }
}
