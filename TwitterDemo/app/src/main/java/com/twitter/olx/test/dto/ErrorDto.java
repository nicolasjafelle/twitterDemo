package com.twitter.olx.test.dto;

/**
 * Created by nicolas on 8/24/14.
 *
 */
public class ErrorDto {
    /*
    {
        "errors": [
        {
            "code": 195,
                "message": "Missing or invalid url parameter."
        }
        ]
    }
    */
    private int code;
    private String message;


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
}
