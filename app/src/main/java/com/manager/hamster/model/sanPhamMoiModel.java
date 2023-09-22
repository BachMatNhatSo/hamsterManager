package com.manager.hamster.model;

import java.util.List;

public class sanPhamMoiModel {
    boolean success;
    String message;
    List<sanPhamMoi> result;

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

    public List<sanPhamMoi> getResult() {
        return result;
    }

    public void setResult(List<sanPhamMoi> result) {
        this.result = result;
    }
}
