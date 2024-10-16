package org.petproject.entity;

import lombok.Data;

@Data
public class Currency {

    private int id;
    private String name;
    private String code;
    private String sign;

    public Currency(int id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.sign = sign;
        this.name = name;
    }
}
