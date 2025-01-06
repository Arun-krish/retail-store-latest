package com.retail.util;

public class ResponsePojo {

    public ResponsePojo(String status, String statusDescription,Object data) {
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



}
