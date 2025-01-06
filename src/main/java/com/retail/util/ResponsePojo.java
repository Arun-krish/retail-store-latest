package com.retail.util;

public class ResponsePojo {

    public ResponsePojo(String status, String statusDescription) {
        this.status = status;
        this.statusDescription = statusDescription;
    }

    String status;
    String statusDescription;

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
