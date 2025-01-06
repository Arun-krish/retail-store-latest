package com.retail.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponsePojo {

    public ResponsePojo(String status, String statusDescription) {
        this.status = status;
        this.statusDescription = statusDescription;
    }

    String status;
    String statusDescription;
    Object data;


}
