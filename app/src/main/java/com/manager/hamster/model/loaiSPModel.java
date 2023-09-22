package com.manager.hamster.model;

import java.util.List;

public class loaiSPModel {
    boolean success;
    String message;
    List<loaiSP> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<loaiSP> getResult() {
        return result;
    }

    public void setResult(List<loaiSP> result) {
        this.result = result;
    }
}
