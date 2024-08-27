package org.petproject.entity;

import lombok.Value;

@Value
public class Currency {

    Integer id;
    String name;
    String code;
    String sign;

}
