package com.retail.util;

public class ResponsePojo {

    public ResponsePojo(String status, String statusDescription, Object data) {
        this.status = status;
        this.statusDescription = statusDescription;
        this.data = data;
    }

    public ResponsePojo(String status, String statusDescription) {
        this.status = status;
        this.statusDescription = statusDescription;
    }

    String status;
    String statusDescription;
    Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
