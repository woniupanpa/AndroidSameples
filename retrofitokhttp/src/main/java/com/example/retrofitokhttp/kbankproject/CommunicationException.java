package com.example.retrofitokhttp.kbankproject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yanjim
 * @Date 2022/1/9 16:06
 */
public class CommunicationException extends Exception{
    public static final int CONNECT_FAILED = 32;
    public static final int SEND_FAILED = 33;
    public static final int RECEIVE_FAILED = 34;
    private int responseCode;
    private int type;

    public CommunicationException() {
    }

    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommunicationException(String message, Throwable cause, int type) {
        super(message, cause);
        this.type = type;
    }

    public CommunicationException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public int getType() {
        return this.type;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public CommunicationException setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
}
