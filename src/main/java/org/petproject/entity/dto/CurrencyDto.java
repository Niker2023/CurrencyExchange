package org.petproject.entity.dto;

import lombok.Value;

@Value
public class CurrencyDto {

    Integer id;
    String name;
    String code;
    String sign;

}
