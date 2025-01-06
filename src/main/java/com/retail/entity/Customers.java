package com.retail.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customers")
@Data
public class Customers {

    @Id
    String id;
    @NotNull
    String name;
    @NotNull
    String mobile;


}
