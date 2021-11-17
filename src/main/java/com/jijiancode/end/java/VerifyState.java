package com.jijiancode.end.java;

public class VerifyState {

    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public VerifyState setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public VerifyState setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public String toString() {
        return "VerifyState{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                '}';
    }
}
