package org.petproject.model;

import lombok.Value;

@Value
public class Currency {

    Integer id;
    String name;
    String code;
    String sign;

}
